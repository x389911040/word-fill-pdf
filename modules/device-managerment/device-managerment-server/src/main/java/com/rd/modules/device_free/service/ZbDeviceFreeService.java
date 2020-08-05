/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_free.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.utils.UserUtils;
import com.rd.modules.common.exception.BusinessException;
import com.rd.modules.common.utils.GenerateBillCodeUtils;
import com.rd.modules.device.common.DeviceUtils;
import com.rd.modules.device.entity.ZbDeviceAccounts;
import com.rd.modules.device.entity.ZbDeviceFree;
import com.rd.modules.device.entity.ZbDeviceFreeItem;
import com.rd.modules.device.entity.ZbDeviceMove;
import com.rd.modules.device.entity.ZbDeviceSeal;
import com.rd.modules.device_accounts.service.ZbDeviceAccountsService;
import com.rd.modules.device_free.dao.ZbDeviceFreeDao;
import com.rd.modules.device_free.dao.ZbDeviceFreeItemDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 设备闲置申请Service
 * @author xuejh
 * @version 2020-05-06
 */
@Service
@Transactional(readOnly=true)
public class ZbDeviceFreeService extends CrudService<ZbDeviceFreeDao, ZbDeviceFree> {

	@Autowired
	private ZbDeviceAccountsService accountsService;

	@Autowired
	private ZbDeviceFreeItemDao zbDeviceFreeItemDao;

	@Autowired
	private ZbDeviceFreeDao freeDao;
	
	/**
	 * 获取单条数据
	 * @param zbDeviceFree
	 * @return
	 */
	@Override
	public ZbDeviceFree get(ZbDeviceFree zbDeviceFree) {
		ZbDeviceFree entity = super.get(zbDeviceFree);
		if (entity != null){
			ZbDeviceFreeItem zbDeviceFreeItem = new ZbDeviceFreeItem(entity);
			zbDeviceFreeItem.setStatus(ZbDeviceFreeItem.STATUS_NORMAL);
			entity.setZbDeviceFreeItemList(zbDeviceFreeItemDao.findList(zbDeviceFreeItem));
		}
		return entity;
	}
	
	/**
	 * 查询分页数据
	 * @param zbDeviceFree 查询条件
	 * @return
	 */
	@Override
	public Page<ZbDeviceFree> findPage(ZbDeviceFree zbDeviceFree) {
		List<ZbDeviceFree> freeList = freeDao.findPage(zbDeviceFree);
		if (CollectionUtils.isNotEmpty(freeList)) {
			return new Page<>(zbDeviceFree.getPageNo(), zbDeviceFree.getPageSize(), freeDao.findCount(zbDeviceFree), freeList);
		}

		return new Page<>();
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param zbDeviceFree
	 */
	@Transactional(readOnly=false)
	public void saveModel(ZbDeviceFree zbDeviceFree) throws Exception {
		// 新增单据设置单据默认状态
		if (StringUtils.isNotBlank(zbDeviceFree.getSubmitType())){
			// 待提交
			zbDeviceFree.setState(zbDeviceFree.AUDIT_STATE_WAIT);
		} else {
			// 未审核
			zbDeviceFree.setState(zbDeviceFree.STATE_AUDIT_N);
		}

		checkFreeItems(zbDeviceFree);

		super.save(zbDeviceFree);
		// 保存 ZbDeviceFree子表
		for (ZbDeviceFreeItem zbDeviceFreeItem : zbDeviceFree.getZbDeviceFreeItemList()){
			if (!ZbDeviceFreeItem.STATUS_DELETE.equals(zbDeviceFreeItem.getStatus())){
				zbDeviceFreeItem.setFreeId(zbDeviceFree);
				if (zbDeviceFreeItem.getIsNewRecord()){
					zbDeviceFreeItemDao.insert(zbDeviceFreeItem);
				}else{
					zbDeviceFreeItemDao.update(zbDeviceFreeItem);
				}
			}else{
				zbDeviceFreeItemDao.delete(zbDeviceFreeItem);
			}
		}
	}

	/**
	 * 单据明细校验
	 * @author xuejh
	 * @create 2020/5/6 16:15
	 * @param zbDeviceFree
	 * @return void
	 */
	private void checkFreeItems(ZbDeviceFree zbDeviceFree) throws Exception {
		List<ZbDeviceFreeItem> itemList = zbDeviceFree.getZbDeviceFreeItemList();
		if (CollectionUtils.isEmpty(itemList)) {
			throw new BusinessException("单据明细为空");
		}

		for (ZbDeviceFreeItem item : itemList) {
			ZbDeviceAccounts deviceAccounts = accountsService.get(item.getDeviceAccountsId());
			if (deviceAccounts == null) {
				throw new BusinessException("根据台账Id["+ item.getDeviceAccountsId() +"]查询台账未找到");
			}
		}
	}

	/**
	 * 更新状态
	 * @param zbDeviceFree
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ZbDeviceFree zbDeviceFree) {
		super.updateStatus(zbDeviceFree);
	}
	
	/**
	 * 删除数据
	 * @param zbDeviceFree
	 */
	@Transactional(readOnly=false)
	public void deleteModel(ZbDeviceFree zbDeviceFree) throws Exception {
		if (!zbDeviceFree.getState().equals(ZbDeviceFree.STATE_AUDIT_N)) {
			throw new BusinessException("只能删除未审核单据");
		}

		super.delete(zbDeviceFree);
		ZbDeviceFreeItem zbDeviceFreeItem = new ZbDeviceFreeItem();
		zbDeviceFreeItem.setFreeId(zbDeviceFree);
		zbDeviceFreeItemDao.deleteByEntity(zbDeviceFreeItem);
	}

	/**
	 * 获取页面初始化数据
	 * @author xuejh
	 * @create 2020/5/6 15:49
	 * @param zbDeviceFree 
	 * @return com.rd.modules.device.entity.ZbDeviceFree
	 */
	public ZbDeviceFree getPageShowData(ZbDeviceFree zbDeviceFree) {
		if (zbDeviceFree.getIsNewRecord()) {
			// 新增封存申请,生成单据号
			// 生成不重复的单据编号
			while (true) {
				String billCode = GenerateBillCodeUtils.getDeviceFreeBillCode(new Date());
				ZbDeviceFree deviceFree = new ZbDeviceFree();
				deviceFree.setBillCode(billCode);

				ZbDeviceFree existModel = super.get(deviceFree);
				if (existModel == null) {
					zbDeviceFree.setBillCode(billCode);
					break;
				}
			}

			User loginUser = UserUtils.getUser();
			if (loginUser != null) {
				// 初始化申请人
				zbDeviceFree.setCreateBy(loginUser.getUserCode());
				zbDeviceFree.setCreateByName(loginUser.getUserName());

				String createDeptName = DeviceUtils.getCreateDeptName(loginUser);
				if (StringUtils.isNotBlank(createDeptName)) {
					zbDeviceFree.setCreateDeptName(createDeptName);
				}
			}

			zbDeviceFree.setCreateDate(new Date());
		} else {
			User creatUser = UserUtils.get(zbDeviceFree.getCreateBy());
			if (creatUser != null) {
				zbDeviceFree.setCreateByName(creatUser.getUserName());
				String createDeptName = DeviceUtils.getCreateDeptName(creatUser);
				if (StringUtils.isNotBlank(createDeptName)) {
					zbDeviceFree.setCreateDeptName(createDeptName);
				}
			}

			// 查询单据明细
			List<ZbDeviceFreeItem> itemList = zbDeviceFreeItemDao.findListById(zbDeviceFree);
			if (CollectionUtils.isNotEmpty(itemList)) {
				zbDeviceFree.setZbDeviceFreeItemList(itemList);
			}
		}

		return zbDeviceFree;
	}

	/**
	 * 审核闲置申请单据
	 * @author xuejh
	 * @create 2020/5/6 16:18
	 * @param deviceFree
	 * @return void
	 */
	public void audit(ZbDeviceFree deviceFree) throws Exception {
		checkFreeItems(deviceFree);

		deviceFree.setState(ZbDeviceSeal.STATE_AUDIT_Y);

		User loginUser = UserUtils.getUser();
		if (loginUser == null) {
			throw new BusinessException("当前登录用户为空");
		}
		deviceFree.setAuditBy(loginUser.getUserCode());
		deviceFree.setAuditDate(new Date());

		super.update(deviceFree);

		// 修改台账状态为闲置
		List<ZbDeviceFreeItem> items = deviceFree.getZbDeviceFreeItemList();
		for (ZbDeviceFreeItem item : items) {
			ZbDeviceAccounts deviceAccounts = accountsService.get(item.getDeviceAccountsId());
			deviceAccounts.setState(ZbDeviceAccounts.DEVICE_STATE_FREE);
			accountsService.update(deviceAccounts);
		}
	}
}