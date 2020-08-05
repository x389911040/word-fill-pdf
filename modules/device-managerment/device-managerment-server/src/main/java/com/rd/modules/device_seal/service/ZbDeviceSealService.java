/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_seal.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.utils.UserUtils;
import com.rd.modules.common.exception.BusinessException;
import com.rd.modules.common.utils.GenerateBillCodeUtils;
import com.rd.modules.device.common.DeviceUtils;
import com.rd.modules.device.entity.ZbDeviceAccounts;
import com.rd.modules.device.entity.ZbDeviceMove;
import com.rd.modules.device.entity.ZbDeviceSeal;
import com.rd.modules.device.entity.ZbDeviceSealItem;
import com.rd.modules.device_accounts.service.ZbDeviceAccountsService;
import com.rd.modules.device_seal.dao.ZbDeviceSealDao;
import com.rd.modules.device_seal.dao.ZbDeviceSealItemDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 设备封存表Service
 * @author xuejh
 * @version 2020-04-29
 */
@Service
@Transactional(readOnly=true)
public class ZbDeviceSealService extends CrudService<ZbDeviceSealDao, ZbDeviceSeal> {
	
	@Autowired
	private ZbDeviceSealItemDao zbDeviceSealItemDao;

	@Autowired
	private ZbDeviceSealDao sealDao;

	@Autowired
	private ZbDeviceAccountsService accountsService;
	
	/**
	 * 获取单条数据
	 * @param zbDeviceSeal
	 * @return
	 */
	@Override
	public ZbDeviceSeal get(ZbDeviceSeal zbDeviceSeal) {
		ZbDeviceSeal entity = super.get(zbDeviceSeal);
		if (entity != null){
			ZbDeviceSealItem zbDeviceSealItem = new ZbDeviceSealItem(entity);
			zbDeviceSealItem.setStatus(ZbDeviceSealItem.STATUS_NORMAL);
			entity.setZbDeviceSealItemList(zbDeviceSealItemDao.findList(zbDeviceSealItem));
		}
		return entity;
	}
	
	/**
	 * 查询分页数据
	 * @param zbDeviceSeal 查询条件
	 * @return
	 */
	@Override
	public Page<ZbDeviceSeal> findPage(ZbDeviceSeal zbDeviceSeal) {
		List<ZbDeviceSeal> sealList = sealDao.findPage(zbDeviceSeal);
		if (CollectionUtils.isNotEmpty(sealList)) {
			return new Page<>(zbDeviceSeal.getPageNo(), zbDeviceSeal.getPageSize(), sealDao.findCount(zbDeviceSeal), sealList);
		}

		return new Page<>();
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param zbDeviceSeal
	 */
	@Transactional(readOnly=false)
	public void saveModel(ZbDeviceSeal zbDeviceSeal) throws Exception {
		// 新增单据设置单据默认状态
		if (StringUtils.isNotBlank(zbDeviceSeal.getSubmitType())){
			// 待提交
			zbDeviceSeal.setState(ZbDeviceSeal.AUDIT_STATE_WAIT);
		} else {
			// 未审核
			zbDeviceSeal.setState(ZbDeviceMove.AUDIT_STATE_N);
		}

		checkSealItems(zbDeviceSeal);

		super.save(zbDeviceSeal);
		// 保存 ZbDeviceSeal子表
		for (ZbDeviceSealItem zbDeviceSealItem : zbDeviceSeal.getZbDeviceSealItemList()){
			if (!ZbDeviceSealItem.STATUS_DELETE.equals(zbDeviceSealItem.getStatus())){
				zbDeviceSealItem.setSealId(zbDeviceSeal);
				if (zbDeviceSealItem.getIsNewRecord()){
					zbDeviceSealItemDao.insert(zbDeviceSealItem);
				}else{
					zbDeviceSealItemDao.update(zbDeviceSealItem);
				}
			}else{
				zbDeviceSealItemDao.delete(zbDeviceSealItem);
			}
		}
	}

	/**
	 * 校验单据明细
	 * @author xuejh
	 * @create 2020/4/30 10:22
	 * @param zbDeviceSeal
	 * @return void
	 */
	private void checkSealItems(ZbDeviceSeal zbDeviceSeal) throws Exception {
		List<ZbDeviceSealItem> itemList = zbDeviceSeal.getZbDeviceSealItemList();
		if (CollectionUtils.isEmpty(itemList)) {
			throw new BusinessException("单据明细为空");
		}

		for (ZbDeviceSealItem item : itemList) {
			ZbDeviceAccounts deviceAccounts = accountsService.get(item.getDeviceAccountsId());
			if (deviceAccounts == null) {
				throw new BusinessException("根据台账Id["+ item.getDeviceAccountsId() +"]查询台账未找到");
			}
		}
	}

	/**
	 * 更新状态
	 * @param zbDeviceSeal
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ZbDeviceSeal zbDeviceSeal) {
		super.updateStatus(zbDeviceSeal);
	}
	
	/**
	 * 删除数据
	 * @param zbDeviceSeal
	 */
	@Transactional(readOnly=false)
	public void deleteModel(ZbDeviceSeal zbDeviceSeal) throws Exception {
		if (!zbDeviceSeal.getState().equals(ZbDeviceSeal.STATE_AUDIT_N)) {
			throw new BusinessException("只能删除未审核单据");
		}

		super.delete(zbDeviceSeal);

		ZbDeviceSealItem zbDeviceSealItem = new ZbDeviceSealItem();
		zbDeviceSealItem.setSealId(zbDeviceSeal);
		zbDeviceSealItemDao.deleteByEntity(zbDeviceSealItem);
	}

	/**
	 * 页面初始化数据
	 * @author xuejh
	 * @create 2020/4/30 9:16
	 * @param zbDeviceSeal
	 * @return com.rd.modules.device.entity.ZbDeviceSeal
	 */
    public ZbDeviceSeal getPageShowData(ZbDeviceSeal zbDeviceSeal) {
    	if (zbDeviceSeal.getIsNewRecord()) {
    		// 新增封存申请,生成单据号
			// 生成不重复的单据编号
			while (true) {
				String billCode = GenerateBillCodeUtils.getDeviceSealBillCode(new Date());
				ZbDeviceSeal deviceSeal = new ZbDeviceSeal();
				deviceSeal.setBillCode(billCode);

				ZbDeviceSeal existModel = super.get(deviceSeal);
				if (existModel == null) {
					zbDeviceSeal.setBillCode(billCode);
					break;
				}
			}

			User loginUser = UserUtils.getUser();
			if (loginUser != null) {
				// 初始化申请人
				zbDeviceSeal.setCreateBy(loginUser.getUserCode());
				zbDeviceSeal.setCreateByName(loginUser.getUserName());
				String createDeptName = DeviceUtils.getCreateDeptName(loginUser);
				if (StringUtils.isNotBlank(createDeptName)) {
					zbDeviceSeal.setCreateDeptName(createDeptName);
				}
			}

			zbDeviceSeal.setCreateDate(new Date());
		} else {
			User creatUser = UserUtils.get(zbDeviceSeal.getCreateBy());
			if (creatUser != null) {
				zbDeviceSeal.setCreateByName(creatUser.getUserName());
				String createDeptName = DeviceUtils.getCreateDeptName(creatUser);
				if (StringUtils.isNotBlank(createDeptName)) {
					zbDeviceSeal.setCreateDeptName(createDeptName);
				}
			}

			if (zbDeviceSeal.getState().equals(ZbDeviceSeal.STATE_AUDIT_Y)) {
				User auditUser = UserUtils.get(zbDeviceSeal.getAuditBy());
				if (auditUser != null) {
					zbDeviceSeal.setAuditByName(auditUser.getUserName());
				}
			}

			// 查询单据明细
			List<ZbDeviceSealItem> itemList = zbDeviceSealItemDao.findListById(zbDeviceSeal);
			if (CollectionUtils.isNotEmpty(itemList)) {
				zbDeviceSeal.setZbDeviceSealItemList(itemList);
			}
		}

    	return zbDeviceSeal;
    }

    /**
     * 审核封存单据
     * @author xuejh
     * @create 2020/4/30 14:02
     * @param deviceSeal
     * @return void
     */
	public void audit(ZbDeviceSeal deviceSeal) throws Exception {
		checkSealItems(deviceSeal);

		deviceSeal.setState(ZbDeviceSeal.STATE_AUDIT_Y);

		User loginUser = UserUtils.getUser();
		if (loginUser == null) {
			throw new BusinessException("当前登录用户为空");
		}
		deviceSeal.setAuditBy(loginUser.getUserCode());
		deviceSeal.setAuditDate(new Date());

		super.update(deviceSeal);

		// 修改台账状态为封存
		List<ZbDeviceSealItem> items = deviceSeal.getZbDeviceSealItemList();
		for (ZbDeviceSealItem item : items) {
			ZbDeviceAccounts deviceAccounts = accountsService.get(item.getDeviceAccountsId());
			if (deviceSeal.getSealType().equals(ZbDeviceSeal.SEAL_TYPE_CLOSE)) {
				// 状态变更为封存
				deviceAccounts.setState(ZbDeviceAccounts.DEVICE_STATE_SEAL);
			} else {
				// 状态变更为正常
				deviceAccounts.setState(ZbDeviceAccounts.DEVICE_STATE_NORMAL);
			}
			accountsService.update(deviceAccounts);
		}
	}
}