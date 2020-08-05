/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_repair_handover.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.rd.modules.common.exception.BusinessException;
import com.rd.modules.common.utils.GenerateBillCodeUtils;
import com.rd.modules.device.entity.ZbDeviceRepairHandover;
import com.rd.modules.device_repair_handover.dao.ZbDeviceRepairHandoverDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 设备维修交接单Service
 * @author xuejh
 * @version 2020-04-20
 */
@Service
@Transactional(readOnly=true)
public class ZbDeviceRepairHandoverService extends CrudService<ZbDeviceRepairHandoverDao, ZbDeviceRepairHandover> {
	
	@Autowired
	private ZbDeviceRepairHandoverDao repairHandoverDao;

	/**
	 * 查询分页数据
	 * @param zbDeviceRepairHandover 查询条件
	 * @return
	 */
	@Override
	public Page<ZbDeviceRepairHandover> findPage(ZbDeviceRepairHandover zbDeviceRepairHandover) {
		List<ZbDeviceRepairHandover> repairHandoverList = repairHandoverDao.findPage(zbDeviceRepairHandover);
		if (CollectionUtils.isNotEmpty(repairHandoverList)) {
			return new Page<>(zbDeviceRepairHandover.getPageNo(), zbDeviceRepairHandover.getPageSize(), repairHandoverDao.findCount(zbDeviceRepairHandover), repairHandoverList);
		}

		return new Page<>();
	}
	
	/**
	 * 更新状态
	 * @param zbDeviceRepairHandover
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ZbDeviceRepairHandover zbDeviceRepairHandover) {
		super.updateStatus(zbDeviceRepairHandover);
	}
	
	/**
	 * 生成维修交接单接口
	 * @author xuejh
	 * @create 2020/4/21 10:17
	 * @param zbDeviceRepairHandover
	 * 		deviceRepairApplyId: 维修申请单Id
	 * 		zbDeviceRepairHandoverItemList: 交接单明细 对应维修申请单明细
	 * @return void
	 */
	@Transactional
	public void saveModel(ZbDeviceRepairHandover zbDeviceRepairHandover) throws Exception {
		if (zbDeviceRepairHandover == null) {
			throw new BusinessException("生成交接单参数为空");
		}

		// 生成不重复的单据编号
		while (true) {
			String billCode = GenerateBillCodeUtils.getDeviceRepairHandoverBillCode(new Date());
			ZbDeviceRepairHandover repairHandover = new ZbDeviceRepairHandover();
			repairHandover.setBillCode(billCode);

			ZbDeviceRepairHandover existModel = super.get(repairHandover);
			if (existModel == null) {
				zbDeviceRepairHandover.setBillCode(billCode);
				break;
			}
		}

		// 保存主表信息
		super.save(zbDeviceRepairHandover);

		// 审核申请单自动生成交接单
//		if (StringUtils.isNotBlank(zbDeviceRepairHandover.getDeviceRepairApplyId())) {
//			for (ZbDeviceRepairHandoverItem handoverItem : zbDeviceRepairHandover.getZbDeviceRepairHandoverItemList()) {
//				// 生成设备维修记录
//				ZbDeviceRepairRecord repairRecord = new ZbDeviceRepairRecord();
//				repairRecord.setRepairApplyId(zbDeviceRepairHandover.getDeviceRepairApplyId());
//				repairRecord.setDeviceAccountsId(handoverItem.getDeviceAccountsId());
//				repairRecord.setRepairType(ZbDeviceRepairApply.REPAIR_TYPE_INNER);
//				repairRecord.setRepairOrganName("维修中心");
//				repairRecordService.saveModel(repairRecord);
//			}
//		}
	}

	/**
	 * 获取页面交接单明细
	 * @author xuejh
	 * @create 2020/4/27 15:57
	 * @param repairHandover
	 * @return com.rd.modules.device.entity.ZbDeviceRepairHandover
	 */
	public ZbDeviceRepairHandover getPageShowData(ZbDeviceRepairHandover repairHandover) {

		return repairHandover;
	}

	/**
	 * 审核设备交接单
	 * @author xuejh
	 * @create 2020/4/28 9:58
	 * @param repairHandover
	 * @return void
	 */
	@Transactional
	public void audit(ZbDeviceRepairHandover repairHandover) throws Exception {
//		checkBillItems(repairHandover);
//		if (!repairHandover.getState().equals(ZbDeviceRepairHandover.STATE_CONFIRM_N)) {
//			throw new BusinessException("单据状态必须是待确认");
//		}
//
//		// 确认人
//		User loginUser = UserUtils.getUser();
//		if (loginUser != null) {
//			repairHandover.setConfirmCode(loginUser.getUserCode());
//		}
//		// 确认时间
//		repairHandover.setConfirmDate(new Date());
//		// 状态变更为已确认
//		repairHandover.setState(ZbDeviceRepairHandover.STATE_CONFIRM_Y);
//		super.update(repairHandover);
//
//		// 更新明细
//		List<ZbDeviceRepairHandoverItem> itemList = repairHandover.getZbDeviceRepairHandoverItemList();
//		for (ZbDeviceRepairHandoverItem item : itemList) {
//			// 更新设备台账状态
//			ZbDeviceAccounts deviceAccounts = accountsService.get(item.getDeviceAccountsId());
//			if (item.getRepairResultType().equals(ZbDeviceRepairHandoverItem.RESULT_TYPE_COMPLETE)) {
//				// 维修完毕，台账正常
//				deviceAccounts.setState(ZbDeviceAccounts.DEVICE_STATE_NORMAL);
//			} else if (item.getRepairResultType().equals(ZbDeviceRepairHandoverItem.RESULT_TYPE_SCRAP)) {
//				// 已报废，台账：待报废申请
//				deviceAccounts.setState(ZbDeviceAccounts.DEVICE_STATE_SCRAP_APPLY);
//			} else {
//				// 待采购维修器材  台账：维修待采购
//				deviceAccounts.setState(ZbDeviceAccounts.DEVICE_STATE_REPAIR_PUR);
//			}
//			zbDeviceRepairHandoverItemDao.update(item);
//			accountsService.update(deviceAccounts);
//
//			// 生成设备维修记录
//			ZbDeviceRepairRecord repairRecord = new ZbDeviceRepairRecord();
//			repairRecord.setRemarks(item.getRemarks());
//			repairRecord.setRepairApplyId(repairHandover.getDeviceRepairApplyId());
//			repairRecord.setDeviceAccountsId(item.getDeviceAccountsId());
//			repairRecord.setRepairResultType(item.getRepairResultType());
//			repairRecord.setRepairType(ZbDeviceRepairApply.REPAIR_TYPE_INNER);
//			repairRecord.setRepairOrganName("维修中心");
//			repairRecordService.saveModel(repairRecord);
//		}
	}
}