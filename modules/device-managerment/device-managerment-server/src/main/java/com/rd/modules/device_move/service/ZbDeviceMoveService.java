/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_move.service;

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
import com.rd.modules.device.entity.ZbDeviceMoveItem;
import com.rd.modules.device.entity.ZbDeviceMoveRecord;
import com.rd.modules.device_accounts.service.ZbDeviceAccountsService;
import com.rd.modules.device_move.dao.ZbDeviceMoveDao;
import com.rd.modules.device_move.dao.ZbDeviceMoveItemDao;
import com.rd.modules.device_move.dao.ZbDeviceMoveRecordDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 设备移交Service
 * @author xuejh
 * @version 2020-04-15
 */
@Service
@Transactional(readOnly=true)
public class ZbDeviceMoveService extends CrudService<ZbDeviceMoveDao, ZbDeviceMove> {

	@Autowired
	private ZbDeviceAccountsService accountsService;
	
	@Autowired
	private ZbDeviceMoveItemDao zbDeviceMoveItemDao;

	@Autowired
	private ZbDeviceMoveDao zbDeviceMoveDao;

	@Autowired
	private ZbDeviceMoveRecordDao deviceMoveRecordDao;

	/**
	 * 获取单条数据
	 * @param zbDeviceMove
	 * @return
	 */
	@Override
	public ZbDeviceMove get(ZbDeviceMove zbDeviceMove) {
		ZbDeviceMove entity = super.get(zbDeviceMove);
		if (entity != null){
			ZbDeviceMoveItem zbDeviceMoveItem = new ZbDeviceMoveItem(entity);
			zbDeviceMoveItem.setStatus(ZbDeviceMoveItem.STATUS_NORMAL);
			entity.setZbDeviceMoveItemList(zbDeviceMoveItemDao.findList(zbDeviceMoveItem));
		}
		return entity;
	}
	
	/**
	 * 查询分页数据
	 * @param zbDeviceMove 查询条件
	 * @return
	 */
	@Override
	public Page<ZbDeviceMove> findPage(ZbDeviceMove zbDeviceMove) {
		List<ZbDeviceMove> deviceMoveList = zbDeviceMoveDao.findPage(zbDeviceMove);
		if (CollectionUtils.isNotEmpty(deviceMoveList)) {
			return new Page<>(zbDeviceMove.getPageNo(), zbDeviceMove.getPageSize(), zbDeviceMoveDao.findCount(zbDeviceMove), deviceMoveList);
		}

		return new Page<>();
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param zbDeviceMove
	 */
	@Transactional(readOnly=false)
	public void saveModel(ZbDeviceMove zbDeviceMove) throws Exception {
		// 新增单据设置单据默认状态
		if (StringUtils.isBlank(zbDeviceMove.getSubmitType())) {
			// 未审核
			zbDeviceMove.setState(ZbDeviceMove.AUDIT_STATE_N);
		} else {
			// 待提交
			zbDeviceMove.setState(ZbDeviceMove.AUDIT_STATE_WAIT);
		}

		// 校验单据明细
		checkBillItems(zbDeviceMove);

		super.save(zbDeviceMove);

		// 保存 ZbDeviceMove子表
		for (ZbDeviceMoveItem zbDeviceMoveItem : zbDeviceMove.getZbDeviceMoveItemList()){
			ZbDeviceAccounts byEntity = accountsService.get(zbDeviceMoveItem.getDeviceAccountsId());
			if (byEntity == null) {
				throw new BusinessException("id为["+ zbDeviceMoveItem.getDeviceAccountsId() +"]的设备台账未找到");
			}

			if (!ZbDeviceMoveItem.STATUS_DELETE.equals(zbDeviceMoveItem.getStatus())){
				zbDeviceMoveItem.setDeviceMoveId(zbDeviceMove);
				if (zbDeviceMoveItem.getIsNewRecord()){
					// 修改设备状态为移交中
					byEntity.setState(ZbDeviceAccounts.DEVICE_STATE_MOVE);
					zbDeviceMoveItemDao.insert(zbDeviceMoveItem);
				}else{
					zbDeviceMoveItemDao.update(zbDeviceMoveItem);
				}
			}else{
				zbDeviceMoveItemDao.delete(zbDeviceMoveItem);
				// 修改设备状态为正常
				byEntity.setState(ZbDeviceAccounts.DEVICE_STATE_NORMAL);
			}
			accountsService.update(byEntity);
		}
	}

	/**
	 * 校验单据明细
	 * @author xuejh
	 * @create 2020/4/17 9:46
	 * @param zbDeviceMove
	 * @return void
	 */
	private void checkBillItems(ZbDeviceMove zbDeviceMove) throws BusinessException {
		// 校验单据状态
//		if (!zbDeviceMove.getState().equals(ZbDeviceMove.AUDIT_STATE_WAIT)) {
//			throw new BusinessException("单据必须是待提交");
//		}

		// 获取单据明细
		List<ZbDeviceMoveItem> itemList = zbDeviceMove.getZbDeviceMoveItemList();
		if (CollectionUtils.isEmpty(itemList)) {
			throw new BusinessException("移交单明细为空");
		}

		// 单据明细校验
		for (ZbDeviceMoveItem moveItem : itemList) {
			if (StringUtils.isBlank(moveItem.getDeviceAccountsId())) {
				throw new BusinessException("设备台账Id不能为空");
			}

			ZbDeviceAccounts zbDeviceAccounts = accountsService.get(moveItem.getDeviceAccountsId());
			if (zbDeviceAccounts == null) {
				throw new BusinessException("根据设备台账Id["+ moveItem.getDeviceAccountsId() +"]查询台账未找到");
			}

//			if (!ZbDeviceMoveItem.STATUS_DELETE.equals(moveItem.getStatus()) && !zbDeviceAccounts.getOwnerType().equals(ZbDeviceAccounts.OWNER_TYPE_EMP)) {
//				throw new BusinessException("移交设备台账编号["+ zbDeviceAccounts.getAccountsCode() +"]非个人所有");
//			}
		}
	}

	/**
	 * 更新状态
	 * @param zbDeviceMove
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ZbDeviceMove zbDeviceMove) {
		super.updateStatus(zbDeviceMove);
	}
	
	/**
	 * 删除数据
	 * @param zbDeviceMove
	 */
	@Transactional(readOnly=false)
	public void deleteMoveOrder(ZbDeviceMove zbDeviceMove) throws Exception {
		// 获得移交明细
		List<ZbDeviceMoveItem> moveList = zbDeviceMoveItemDao.findListById(zbDeviceMove);
		if (CollectionUtils.isEmpty(moveList)) {
			throw new BusinessException("移交单明细未找到");
		}

		for (ZbDeviceMoveItem item : moveList) {
			// 修改台账状态为正常
			ZbDeviceAccounts deviceAccounts = accountsService.get(item.getDeviceAccountsId());
			if (deviceAccounts == null) {
				throw new BusinessException("根据台账Id["+ item.getDeviceAccountsId() +"]查询台账未找到");
			}

			deviceAccounts.setState(ZbDeviceAccounts.DEVICE_STATE_NORMAL);
			accountsService.update(deviceAccounts);

			zbDeviceMoveItemDao.delete(item);
		}

		super.delete(zbDeviceMove);
	}

	/**
	 * 获取新增页面所需数据
	 * @author xuejh
	 * @create 2020/4/16 17:51
	 * @param zbDeviceMove
	 * @return com.rd.modules.device.entity.ZbDeviceMove
	 */
    public ZbDeviceMove getPageShowData(ZbDeviceMove zbDeviceMove) throws Exception {
		// 新增单据获取页面初始化数据
		if (zbDeviceMove.getIsNewRecord()) {
			// 生成不重复的单据编号
			while (true) {
				String billCode = GenerateBillCodeUtils.getDeviceMoveBillCode(new Date());
				ZbDeviceMove deviceMove = new ZbDeviceMove();
				deviceMove.setBillCode(billCode);

				ZbDeviceMove existModel = super.get(deviceMove);
				if (existModel == null) {
					zbDeviceMove.setBillCode(billCode);
					break;
				}
			}

			User loginUser = UserUtils.getUser();
			if (loginUser != null) {
				// 初始化移交人信息
				zbDeviceMove.setMoveCode(loginUser.getUserCode());
				zbDeviceMove.setMoveName(loginUser.getUserName());

				// 移交单位名称
				String createDeptName = DeviceUtils.getCreateDeptName(loginUser);
				if (StringUtils.isNotBlank(createDeptName)) {
					zbDeviceMove.setMoveDeptName(createDeptName);
				}
			}

			// 移交单位编码
			Office office = EmpUtils.getOffice();
			if (office != null) {
				zbDeviceMove.setMoveDeptCode(office.getOfficeCode());
			}

			zbDeviceMove.setCreateDate(new Date());
		} else {
			// 移交人
			User moveUser = UserUtils.get(zbDeviceMove.getMoveCode());
			if (moveUser != null) {
				zbDeviceMove.setMoveName(moveUser.getUserName());
			}

			String moveDeptName = DeviceUtils.getCreateDeptName(moveUser);
			if (StringUtils.isNotBlank(moveDeptName)) {
				zbDeviceMove.setMoveDeptName(moveDeptName);
			}

			// 接收人
			User receiveUser = UserUtils.get(zbDeviceMove.getReceiveCode());
			if (receiveUser != null) {
				zbDeviceMove.setReceiveName(receiveUser.getUserName());
			}

			String receiveDeptName = DeviceUtils.getCreateDeptName(receiveUser);
			if (StringUtils.isNotBlank(receiveDeptName)) {
				zbDeviceMove.setReceiveDeptName(receiveDeptName);
			}

			// 修改获取页面数据
			List<ZbDeviceMoveItem> moveItems = zbDeviceMoveItemDao.findListById(zbDeviceMove);
			if (CollectionUtils.isNotEmpty(moveItems)) {
				zbDeviceMove.setZbDeviceMoveItemList(moveItems);
			}
		}

		return zbDeviceMove;
	}

	/**
	 * 移交单据审核
	 * @author xuejh
	 * @create 2020/4/17 9:19
	 * @param zbDeviceMove
	 * @return void
	 */
	@Transactional
	public void audit(ZbDeviceMove zbDeviceMove) throws Exception {
		// 校验单据明细
		checkBillItems(zbDeviceMove);
		// 修改单据状态为已审核
		zbDeviceMove.setState(ZbDeviceMove.AUDIT_STATE_Y);
		// 设置审核人
		zbDeviceMove.setAuditCode(UserUtils.getUser().getUserCode());
		// 审核时间
		zbDeviceMove.setAuditDate(new Date());

		zbDeviceMoveDao.update(zbDeviceMove);

		// 设备资产变更，获取移交设备明细
		List<ZbDeviceMoveItem> moveItems = zbDeviceMove.getZbDeviceMoveItemList();
		for (ZbDeviceMoveItem moveItem : moveItems) {
			// 获取设备台账
			ZbDeviceAccounts oldAccounts = accountsService.get(moveItem.getDeviceAccountsId());
			if (oldAccounts == null) {
				throw new BusinessException("台账Id["+ moveItem.getDeviceAccountsId() +"]原台帐信息未找到");
			}

			// 资产变更
			oldAccounts.setOwnerOfficeCode(zbDeviceMove.getReceiveCode());
			oldAccounts.setOwnerCode(zbDeviceMove.getReceiveCode());
			// 台账状态
			oldAccounts.setState(ZbDeviceAccounts.DEVICE_STATE_NORMAL);
			accountsService.update(oldAccounts);

			// 写入资产变更记录
			ZbDeviceMoveRecord moveRecord = new ZbDeviceMoveRecord();
			moveRecord.setDeviceMoveId(zbDeviceMove.getId());
			moveRecord.setDeviceAccountsId(moveItem.getDeviceAccountsId());
			deviceMoveRecordDao.insert(moveRecord);
		}
	}

	/**
	 * 查询设备变更记录
	 * @author xuejh
	 * @create 2020/4/29 15:12
	 * @param deviceAccounts
	 * @return com.jeesite.common.entity.Page<com.rd.modules.device.entity.ZbDeviceMoveRecord>
	 */
    public Page<ZbDeviceMoveRecord> findMoveRecordPage(ZbDeviceAccounts deviceAccounts) {
		List<ZbDeviceMoveRecord> moveRecordList = deviceMoveRecordDao.findListByAccountsId(deviceAccounts);
		ZbDeviceMoveRecord searchModel = new ZbDeviceMoveRecord();
		searchModel.setDeviceAccountsId(deviceAccounts.getId());
		if (CollectionUtils.isNotEmpty(moveRecordList)) {
			return new Page<>(deviceAccounts.getPageNo(), deviceAccounts.getPageSize(), deviceMoveRecordDao.findCount(searchModel), moveRecordList);
		}

		return new Page<>();
	}
}