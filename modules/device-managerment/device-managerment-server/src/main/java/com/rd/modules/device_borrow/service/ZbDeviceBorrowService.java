/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_borrow.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.utils.EmpUtils;
import com.jeesite.modules.sys.utils.UserUtils;
import com.rd.modules.common.exception.BusinessException;
import com.rd.modules.common.utils.GenerateBillCodeUtils;
import com.rd.modules.device.common.DeviceUtils;
import com.rd.modules.device.entity.ZbDeviceAccounts;
import com.rd.modules.device.entity.ZbDeviceMove;
import com.rd.modules.device_accounts.service.ZbDeviceAccountsService;
import com.rd.modules.device_borrow.dao.ZbDeviceBorrowDao;
import com.rd.modules.device_borrow.dao.ZbDeviceBorrowItemDao;
import com.rd.modules.device_borrow.entity.ZbDeviceBorrow;
import com.rd.modules.device_borrow.entity.ZbDeviceBorrowItem;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 设备借用单Service
 * @author xuejh
 * @version 2020-05-15
 */
@Service
@Transactional(readOnly=true)
public class ZbDeviceBorrowService extends CrudService<ZbDeviceBorrowDao, ZbDeviceBorrow> {

	@Autowired
	private ZbDeviceAccountsService accountsService;

	@Autowired
	private ZbDeviceBorrowItemDao zbDeviceBorrowItemDao;

	@Autowired
	private ZbDeviceBorrowDao deviceBorrowDao;
	
	/**
	 * 获取单条数据
	 * @param zbDeviceBorrow
	 * @return
	 */
	@Override
	public ZbDeviceBorrow get(ZbDeviceBorrow zbDeviceBorrow) {
		ZbDeviceBorrow entity = super.get(zbDeviceBorrow);
		if (entity != null){
			ZbDeviceBorrowItem zbDeviceBorrowItem = new ZbDeviceBorrowItem(entity);
			zbDeviceBorrowItem.setStatus(ZbDeviceBorrowItem.STATUS_NORMAL);
			entity.setZbDeviceBorrowItemList(zbDeviceBorrowItemDao.findList(zbDeviceBorrowItem));
		}
		return entity;
	}
	
	/**
	 * 查询分页数据
	 * @param zbDeviceBorrow 查询条件
	 * @return
	 */
	@Override
	public Page<ZbDeviceBorrow> findPage(ZbDeviceBorrow zbDeviceBorrow) {
		List<ZbDeviceBorrow> borrowList = deviceBorrowDao.findPage(zbDeviceBorrow);
		if (CollectionUtils.isNotEmpty(borrowList)) {
			return new Page<>(zbDeviceBorrow.getPageNo(), zbDeviceBorrow.getPageSize(), deviceBorrowDao.findCount(zbDeviceBorrow), borrowList);
		}

		return new Page<>();
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param zbDeviceBorrow
	 */
	@Transactional(readOnly=false)
	public void saveBorrow(ZbDeviceBorrow zbDeviceBorrow) throws Exception {
		// 新增单据设置单据默认状态
		if (StringUtils.isNotBlank(zbDeviceBorrow.getSubmitType())){
			// 待提交
			zbDeviceBorrow.setState(ZbDeviceMove.AUDIT_STATE_WAIT);
		}

		// 校验单据状态
//		if (!zbDeviceBorrow.getState().equals(ZbDeviceBorrow.AUDIT_STATE_WAIT)) {
//			throw new BusinessException("单据必须是待提交");
//		}

		// 校验单据明细
		checkBillItems(zbDeviceBorrow);

		if (StringUtils.isBlank(zbDeviceBorrow.getSubmitType())) {
			// 未审核
			zbDeviceBorrow.setState(ZbDeviceMove.AUDIT_STATE_N);
		}

		// 保存主表信息
		super.save(zbDeviceBorrow);

		// 保存 ZbDeviceBorrow子表
		for (ZbDeviceBorrowItem zbDeviceBorrowItem : zbDeviceBorrow.getZbDeviceBorrowItemList()){
			if (!ZbDeviceBorrowItem.STATUS_DELETE.equals(zbDeviceBorrowItem.getStatus())){
				zbDeviceBorrowItem.setDeviceBorrowId(zbDeviceBorrow);
				if (zbDeviceBorrowItem.getIsNewRecord()){
					zbDeviceBorrowItemDao.insert(zbDeviceBorrowItem);
				}else{
					zbDeviceBorrowItemDao.update(zbDeviceBorrowItem);
				}
			}else{
				zbDeviceBorrowItemDao.delete(zbDeviceBorrowItem);
			}
		}
	}

	/**
	 * 保存校验主表信息
	 * @author xuejh
	 * @create 2020/5/15 14:23
	 * @param zbDeviceBorrow
	 * @return void
	 */
	private void checkBillItems(ZbDeviceBorrow zbDeviceBorrow) throws Exception {
		if (StringUtils.isBlank(zbDeviceBorrow.getApplyCode())) {
			throw new BusinessException("申请人不能为空");
		}

		if (StringUtils.isBlank(zbDeviceBorrow.getApplyDeptCode())) {
			throw new BusinessException("申请部门不能为空");
		}

		if (StringUtils.isBlank(zbDeviceBorrow.getBorrowCode())) {
			throw new BusinessException("借出人不能为空");
		}

		if (StringUtils.isBlank(zbDeviceBorrow.getBorrowDeptCode())) {
			throw new BusinessException("借出部门不能为空");
		}

		// 获取单据明细
		List<ZbDeviceBorrowItem> itemList = zbDeviceBorrow.getZbDeviceBorrowItemList();
		if (CollectionUtils.isEmpty(itemList)) {
			throw new BusinessException("借用单明细为空");
		}

		// 单据明细校验
		for (ZbDeviceBorrowItem item : itemList) {
			if (StringUtils.isBlank(item.getDeviceAccountsId())) {
				throw new BusinessException("设备台账Id不能为空");
			}

			ZbDeviceAccounts zbDeviceAccounts = accountsService.get(item.getDeviceAccountsId());
			if (zbDeviceAccounts == null) {
				throw new BusinessException("根据设备台账Id["+ item.getDeviceAccountsId() +"]查询台账未找到");
			}
		}
	}

	/**
	 * 更新状态
	 * @param zbDeviceBorrow
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ZbDeviceBorrow zbDeviceBorrow) {
		super.updateStatus(zbDeviceBorrow);
	}
	
	/**
	 * 删除数据
	 * @param zbDeviceBorrow
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(ZbDeviceBorrow zbDeviceBorrow) {
		super.delete(zbDeviceBorrow);
		ZbDeviceBorrowItem zbDeviceBorrowItem = new ZbDeviceBorrowItem();
		zbDeviceBorrowItem.setDeviceBorrowId(zbDeviceBorrow);
		zbDeviceBorrowItemDao.deleteByEntity(zbDeviceBorrowItem);
	}

	/**
	 * 新增/修改表单获取初始化数据
	 * @author xuejh
	 * @create 2020/5/15 14:04
	 * @param zbDeviceBorrow
	 * @return com.rd.modules.device_borrow.entity.ZbDeviceBorrow
	 */
    public ZbDeviceBorrow getPageShowData(ZbDeviceBorrow zbDeviceBorrow) {
		// 新增单据获取页面初始化数据
		if (zbDeviceBorrow.getIsNewRecord()) {
			// 生成不重复的单据编号
			while (true) {
				String billCode = GenerateBillCodeUtils.getDeviceBorrowBillCode(new Date());
				ZbDeviceBorrow deviceBorrow = new ZbDeviceBorrow();
				deviceBorrow.setBillCode(billCode);

				ZbDeviceBorrow existModel = super.get(deviceBorrow);
				if (existModel == null) {
					zbDeviceBorrow.setBillCode(billCode);
					break;
				}
			}

			User loginUser = UserUtils.getUser();
			if (loginUser != null) {
				// 初始化移交人信息
				zbDeviceBorrow.setApplyCode(loginUser.getUserCode());
				zbDeviceBorrow.setApplyName(loginUser.getUserName());

				// 移交单位名称
				String createDeptName = DeviceUtils.getCreateDeptName(loginUser);
				if (StringUtils.isNotBlank(createDeptName)) {
					zbDeviceBorrow.setApplyDeptName(createDeptName);
				}
			}

			// 移交单位编码
			Office office = EmpUtils.getOffice();
			if (office != null) {
				zbDeviceBorrow.setApplyDeptCode(office.getOfficeCode());
			}

			zbDeviceBorrow.setCreateDate(new Date());
		} else {
			// 申请人
			User applyUser = UserUtils.get(zbDeviceBorrow.getApplyCode());
			if (applyUser != null) {
				zbDeviceBorrow.setApplyName(applyUser.getUserName());

				String deptName = DeviceUtils.getCreateDeptName(applyUser);
				if (StringUtils.isNotBlank(deptName)) {
					zbDeviceBorrow.setApplyDeptName(deptName);
				}
			}

			// 借出人
			User borrowUser = UserUtils.get(zbDeviceBorrow.getBorrowCode());
			if (borrowUser != null) {
				zbDeviceBorrow.setBorrowName(borrowUser.getUserName());

				String deptName = DeviceUtils.getCreateDeptName(borrowUser);
				if (StringUtils.isNotBlank(deptName)) {
					zbDeviceBorrow.setBorrowDeptName(deptName);
				}
			}

			// 修改获取页面数据
			List<ZbDeviceBorrowItem> borrowItems = zbDeviceBorrowItemDao.findListById(zbDeviceBorrow);
			if (CollectionUtils.isNotEmpty(borrowItems)) {
				zbDeviceBorrow.setZbDeviceBorrowItemList(borrowItems);
			}
		}

		return zbDeviceBorrow;
	}

	/**
	 * 审核借用单
	 * @author xuejh
	 * @create 2020/5/15 15:12
	 * @param zbDeviceBorrow
	 * @return void
	 */
	@Transactional(readOnly=false)
	public void audit(ZbDeviceBorrow zbDeviceBorrow) throws Exception {
		// 校验单据信息
		checkBillItems(zbDeviceBorrow);

		// 修改单据状态为已审核
		zbDeviceBorrow.setState(ZbDeviceBorrow.AUDIT_STATE_Y);

		super.update(zbDeviceBorrow);

		// 修改明细台账状态为借用中
		List<ZbDeviceBorrowItem> itemList = zbDeviceBorrow.getZbDeviceBorrowItemList();
		for (ZbDeviceBorrowItem item : itemList) {
			ZbDeviceAccounts deviceAccounts = accountsService.get(item.getDeviceAccountsId());
			deviceAccounts.setState(ZbDeviceAccounts.DEVICE_STATE_BORROW);
			accountsService.update(deviceAccounts);

			// 修改借用明细状态为待归还
			item.setState(ZbDeviceBorrowItem.STATE_RETURN_WAIT);
			zbDeviceBorrowItemDao.update(item);
		}
	}

	/**
	 * 归还借用设备
	 * @author xuejh
	 * @create 2020/5/26 14:44
	 * @param accounts
	 * @return void
	 */
	@Transactional(readOnly=false)
    public void returnBack(ZbDeviceAccounts accounts) throws Exception {
		String[] strArr = DeviceUtils.getIdsArrayByStr(accounts.getAccountsIds());
		if (ArrayUtils.isEmpty(strArr)) {
			throw new BusinessException("设备Id不能为空");
		}

		for (String id : strArr) {
			ZbDeviceAccounts deviceAccounts = accountsService.get(id);
			if (deviceAccounts == null) {
				throw new BusinessException("根据台账Id["+ id +"]查询设备未找到");
			}

			if (!deviceAccounts.getState().equals(ZbDeviceAccounts.DEVICE_STATE_BORROW)) {
				throw new BusinessException("设备台账["+ id +"]状态不是借用中");
			}

			// 已归还修改设备台账为闲置
			deviceAccounts.setState(ZbDeviceAccounts.DEVICE_STATE_FREE);
			accountsService.update(deviceAccounts);

			// 设置借用明细归还时间
			ZbDeviceBorrowItem borrowItem = new ZbDeviceBorrowItem();
			borrowItem.setDeviceAccountsId(id);
			borrowItem.setState(ZbDeviceBorrowItem.STATE_RETURN_WAIT);
			ZbDeviceBorrowItem byEntity = zbDeviceBorrowItemDao.getByEntity(borrowItem);
			if (byEntity == null) {
				throw new BusinessException("借用明细未找到");
			}

			if (!byEntity.getState().equals(ZbDeviceBorrowItem.STATE_RETURN_WAIT)) {
				throw new BusinessException("明细状态必须为待归还");
			}

			byEntity.setState(ZbDeviceBorrowItem.STATE_RETURN);
			byEntity.setReturnDate(new Date());
			zbDeviceBorrowItemDao.update(byEntity);
		}
    }
}