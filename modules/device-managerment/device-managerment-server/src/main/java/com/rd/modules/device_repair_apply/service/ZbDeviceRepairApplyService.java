/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_repair_apply.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.sys.entity.Company;
import com.jeesite.modules.sys.entity.Employee;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.utils.EmpUtils;
import com.jeesite.modules.sys.utils.UserUtils;
import com.rd.modules.common.exception.BusinessException;
import com.rd.modules.common.utils.GenerateBillCodeUtils;
import com.rd.modules.device.entity.ZbDeviceAccounts;
import com.rd.modules.device.entity.ZbDeviceRepair;
import com.rd.modules.device.entity.ZbDeviceRepairApply;
import com.rd.modules.device.entity.ZbDeviceRepairApplyItem;
import com.rd.modules.device.entity.ZbDeviceRepairHandover;
import com.rd.modules.device_accounts.service.ZbDeviceAccountsService;
import com.rd.modules.device_repair.service.ZbDeviceRepairService;
import com.rd.modules.device_repair_apply.dao.ZbDeviceRepairApplyDao;
import com.rd.modules.device_repair_apply.dao.ZbDeviceRepairApplyItemDao;
import com.rd.modules.device_repair_handover.service.ZbDeviceRepairHandoverService;
import com.rd.modules.device_repair_record.service.ZbDeviceRepairRecordService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 设备维修申请单Service
 * @author xuejh
 * @version 2020-04-20
 */
@Service
@Transactional(readOnly=true)
public class ZbDeviceRepairApplyService extends CrudService<ZbDeviceRepairApplyDao, ZbDeviceRepairApply> {
	
	@Autowired
	private ZbDeviceRepairApplyItemDao zbDeviceRepairApplyItemDao;

	@Autowired
	private ZbDeviceRepairApplyDao deviceRepairApplyDao;

	@Autowired
	private ZbDeviceAccountsService accountsService;

	@Autowired
	private ZbDeviceRepairService repairService;

	/**
	 * 维修记录服务
	 */
	@Autowired
	private ZbDeviceRepairRecordService repairRecordService;

	/**
	 * 引入交接单服务
	 */
	@Autowired
	private ZbDeviceRepairHandoverService repairHandoverService;
	
	/**
	 * 获取单条数据
	 * @param zbDeviceRepairApply
	 * @return
	 */
	@Override
	public ZbDeviceRepairApply get(ZbDeviceRepairApply zbDeviceRepairApply) {
		ZbDeviceRepairApply entity = super.get(zbDeviceRepairApply);
		if (entity != null){
			ZbDeviceRepairApplyItem zbDeviceRepairApplyItem = new ZbDeviceRepairApplyItem(entity);
			zbDeviceRepairApplyItem.setStatus(ZbDeviceRepairApplyItem.STATUS_NORMAL);
			entity.setZbDeviceRepairApplyItemList(zbDeviceRepairApplyItemDao.findList(zbDeviceRepairApplyItem));
		}
		return entity;
	}
	
	/**
	 * 查询分页数据
	 * @param zbDeviceRepairApply 查询条件
	 * @return
	 */
	@Override
	public Page<ZbDeviceRepairApply> findPage(ZbDeviceRepairApply zbDeviceRepairApply) {
		List<ZbDeviceRepairApply> deviceRepairApplyList = deviceRepairApplyDao.findPage(zbDeviceRepairApply);
		if (CollectionUtils.isNotEmpty(deviceRepairApplyList)) {
			return new Page<>(zbDeviceRepairApply.getPageNo(), zbDeviceRepairApply.getPageSize(), deviceRepairApplyDao.findCount(zbDeviceRepairApply), deviceRepairApplyList);
		}

		return new Page<>();
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param zbDeviceRepairApply
	 */
	@Transactional(readOnly=false)
	public void saveModel(ZbDeviceRepairApply zbDeviceRepairApply) throws Exception {
		// 新增单据设置单据默认状态
		if (StringUtils.isBlank(zbDeviceRepairApply.getSubmitType())) {
			// 未审核
			zbDeviceRepairApply.setState(ZbDeviceRepairApply.AUDIT_STATE_N);
		} else {
			// 待提交
			zbDeviceRepairApply.setState(ZbDeviceRepairApply.AUDIT_STATE_WAIT);
		}

		// 校验单据明细
		checkBillItems(zbDeviceRepairApply);

		super.save(zbDeviceRepairApply);
		// 保存 ZbDeviceRepairApply子表
		for (ZbDeviceRepairApplyItem zbDeviceRepairApplyItem : zbDeviceRepairApply.getZbDeviceRepairApplyItemList()){
			ZbDeviceAccounts deviceAccounts = accountsService.get(zbDeviceRepairApplyItem.getDeviceAccountsId());
			if (deviceAccounts == null) {
				throw new BusinessException("根据Id["+ zbDeviceRepairApplyItem.getDeviceAccountsId() +"]查询设备台账未找到");
			}

			if (!ZbDeviceRepairApplyItem.STATUS_DELETE.equals(zbDeviceRepairApplyItem.getStatus())){
				zbDeviceRepairApplyItem.setRepairApplyId(zbDeviceRepairApply);
				if (zbDeviceRepairApplyItem.getIsNewRecord()){
//					// 修改设备台账状态为维修待审核
//					deviceAccounts.setState(ZbDeviceAccounts.DEVICE_STATE_REPAIR_AUDIT_N);
//					accountsService.update(deviceAccounts);
					zbDeviceRepairApplyItemDao.insert(zbDeviceRepairApplyItem);
				}else{
					zbDeviceRepairApplyItemDao.update(zbDeviceRepairApplyItem);
				}
			}else{
				// 修改设备台账状态为正常
//				deviceAccounts.setState(ZbDeviceAccounts.DEVICE_STATE_NORMAL);
//				accountsService.update(deviceAccounts);
				zbDeviceRepairApplyItemDao.delete(zbDeviceRepairApplyItem);
			}
		}
	}

	/**
	 * 单据明细校验
	 * @author xuejh
	 * @create 2020/4/20 17:25
	 * @param zbDeviceRepairApply
	 * @return void
	 */
	private void checkBillItems(ZbDeviceRepairApply zbDeviceRepairApply) throws Exception {
		String billCode = zbDeviceRepairApply.getBillCode();
		if (StringUtils.isBlank(billCode)) {
			throw new BusinessException("单据编号不能为空");
		}

		List<ZbDeviceRepairApplyItem> items = zbDeviceRepairApply.getZbDeviceRepairApplyItemList();
		if (CollectionUtils.isEmpty(items)) {
			throw new BusinessException("单据明细为空");
		}

		// 明细校验
		for (ZbDeviceRepairApplyItem item : items) {
			ZbDeviceAccounts byEntity = accountsService.get(item.getDeviceAccountsId());
			if (byEntity == null) {
				throw new BusinessException("根据Id["+ item.getDeviceAccountsId() +"]查询设备台账未找到");
			}
		}
	}

	/**
	 * 更新状态
	 * @param zbDeviceRepairApply
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ZbDeviceRepairApply zbDeviceRepairApply) {
		super.updateStatus(zbDeviceRepairApply);
	}
	
	/**
	 * 删除数据
	 * @param zbDeviceRepairApply
	 */
	@Transactional(readOnly=false)
	public void deleteModel(ZbDeviceRepairApply zbDeviceRepairApply) throws Exception {
		// 获取维修申请单明细
		List<ZbDeviceRepairApplyItem> repairApplyItems = zbDeviceRepairApplyItemDao.findListById(zbDeviceRepairApply);
		if (CollectionUtils.isEmpty(repairApplyItems)) {
			throw new BusinessException("根据维修申请单Id["+ zbDeviceRepairApply.getId() +"]查询明细未找到");
		}

		for (ZbDeviceRepairApplyItem item : repairApplyItems) {
			// 修改设备台账为正常
			ZbDeviceAccounts deviceAccounts = accountsService.get(item.getDeviceAccountsId());
			if (deviceAccounts == null) {
				throw new BusinessException("根据台账Id["+ item.getDeviceAccountsId() +"]查询台账未找到");
			}

			deviceAccounts.setState(ZbDeviceAccounts.DEVICE_STATE_NORMAL);
			accountsService.update(deviceAccounts);

			// 删除明细
			zbDeviceRepairApplyItemDao.deleteByEntity(item);
		}
//		ZbDeviceRepairApplyItem zbDeviceRepairApplyItem = new ZbDeviceRepairApplyItem();
//		zbDeviceRepairApplyItem.setRepairApplyId(zbDeviceRepairApply);
//		zbDeviceRepairApplyItemDao.deleteByEntity(zbDeviceRepairApplyItem);
		super.delete(zbDeviceRepairApply);
	}

	/**
	 * 新增页面初始化数据
	 * @author xuejh
	 * @create 2020/4/20 15:09
	 * @param zbDeviceRepairApply
	 * @return com.rd.modules.device.entity.ZbDeviceRepairApply
	 */
    public ZbDeviceRepairApply getPageShowData(ZbDeviceRepairApply zbDeviceRepairApply) {
		// 新增单据获取页面初始化数据
		if (zbDeviceRepairApply.getIsNewRecord()) {
			// 生成不重复的单据编号
			while (true) {
				String billCode = GenerateBillCodeUtils.getDeviceRepairApplyBillCode(new Date());
				ZbDeviceRepairApply repairApply = new ZbDeviceRepairApply();
				repairApply.setBillCode(billCode);

				ZbDeviceRepairApply existModel = super.get(repairApply);
				if (existModel == null) {
					zbDeviceRepairApply.setBillCode(billCode);
					break;
				}
			}

			User loginUser = UserUtils.getUser();

			if (loginUser != null) {
				// 初始化申请人信息
				zbDeviceRepairApply.setCreateBy(loginUser.getUserCode());
				zbDeviceRepairApply.setCreateByName(loginUser.getUserName());
			}

			String createDeptName = getCreateDeptName(loginUser);
			if (StringUtils.isNotBlank(createDeptName)) {
				zbDeviceRepairApply.setCreateDeptName(createDeptName);
			}

			zbDeviceRepairApply.setCreateDate(new Date());
		} else {
			// 获取申请单明细
			List<ZbDeviceRepairApplyItem> applyItemList = zbDeviceRepairApplyItemDao.findListById(zbDeviceRepairApply);
			if (CollectionUtils.isNotEmpty(applyItemList)) {
				zbDeviceRepairApply.setZbDeviceRepairApplyItemList(applyItemList);
			}

			// 初始化申请人信息
			User createUser = UserUtils.get(zbDeviceRepairApply.getCreateBy());
			if (createUser != null) {
				zbDeviceRepairApply.setCreateByName(createUser.getUserName());
			}

			String createDeptName = getCreateDeptName(createUser);
			if (StringUtils.isNotBlank(createDeptName)) {
				zbDeviceRepairApply.setCreateDeptName(createDeptName);
			}
		}

		return zbDeviceRepairApply;
    }

    /**
     * 审核维修申请单
     * @author xuejh
     * @create 2020/4/21 9:41
     * @param zbDeviceRepairApply
     * @return void
     */
	@Transactional(readOnly=false)
	public void audit(ZbDeviceRepairApply zbDeviceRepairApply) throws Exception {
		checkBillItems(zbDeviceRepairApply);
		if (!zbDeviceRepairApply.getState().equals(ZbDeviceRepairApply.AUDIT_STATE_N)) {
			throw new BusinessException("单据必须是未审核");
		}

		zbDeviceRepairApply.setState(ZbDeviceRepairApply.AUDIT_STATE_Y);
		this.update(zbDeviceRepairApply);

		List<ZbDeviceRepairApplyItem> applyItems = zbDeviceRepairApply.getZbDeviceRepairApplyItemList();
		for (ZbDeviceRepairApplyItem applyItem : applyItems) {
			zbDeviceRepairApplyItemDao.update(applyItem);

			// 修改台账状态为维修中
			ZbDeviceAccounts deviceAccounts = accountsService.get(applyItem.getDeviceAccountsId());
			deviceAccounts.setState(ZbDeviceAccounts.DEVICE_STATE_REPAIR);
			accountsService.update(deviceAccounts);

			// 生成维修单
			ZbDeviceRepair deviceRepair = new ZbDeviceRepair();
			deviceRepair.setRepairApplyItemId(applyItem.getId());
			deviceRepair.setDeviceAccountsId(applyItem.getDeviceAccountsId());
			deviceRepair.setCreateBy(zbDeviceRepairApply.getCreateBy());
			deviceRepair.setErrorText(applyItem.getRemarks());

			if (applyItem.getRepairType().equals(ZbDeviceRepairApply.REPAIR_TYPE_INNER)) {
				// 院内维修
				deviceRepair.setRepairType(ZbDeviceRepairApply.REPAIR_TYPE_INNER);
				repairService.saveModel(deviceRepair);

				// 生成交接单
				ZbDeviceRepairHandover repairHandover = new ZbDeviceRepairHandover();
				repairHandover.setDeviceRepairId(deviceRepair.getId());
				repairHandover.setCreateBy(zbDeviceRepairApply.getCreateBy());

				repairHandoverService.saveModel(repairHandover);
			} else {
				// 外送维修，维修单状态为待外送维修确认
				deviceRepair.setRepairType(ZbDeviceRepairApply.REPAIR_TYPE_OUTER);
				repairService.saveModel(deviceRepair);
			}
		}

//		if (CollectionUtils.isNotEmpty(handoverItems)) {
//			// 有院内维修设备,生成交接单
//			ZbDeviceRepairHandover repairHandover = new ZbDeviceRepairHandover();
//
//			repairHandover.setDeviceRepairApplyId(zbDeviceRepairApply.getId());
//			if (StringUtils.isNotBlank(zbDeviceRepairApply.getRemarks())) {
//				repairHandover.setRemarks(zbDeviceRepairApply.getRemarks());
//			}
//			repairHandover.setZbDeviceRepairHandoverItemList(handoverItems);
//			repairHandoverService.saveModel(repairHandover);
//		}
	}

	/**
	 * 根据用户获取用户归属单位信息
	 * @author xuejh
	 * @create 2020/5/7 14:37
	 * @param user
	 * @return java.lang.String
	 */
	private String getCreateDeptName(User user) {
		Employee employee = EmpUtils.get(user);
		if (employee != null) {
			Office office = employee.getOffice();
			Company company = employee.getCompany();
			if (office != null && company != null) {
				return company.getCompanyName() + "-" + office.getOfficeName();
			}
		}

		return null;
	}
}