/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_repair_record.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.file.utils.FileUploadUtils;
import com.rd.modules.common.exception.BusinessException;
import com.rd.modules.device.entity.ZbDeviceAccounts;
import com.rd.modules.device.entity.ZbDeviceRepairApply;
import com.rd.modules.device.entity.ZbDeviceRepairRecord;
import com.rd.modules.device_accounts.service.ZbDeviceAccountsService;
import com.rd.modules.device_info.service.ZbDeviceService;
import com.rd.modules.device_repair_apply.service.ZbDeviceRepairApplyService;
import com.rd.modules.device_repair_record.dao.ZbDeviceRepairRecordDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 设备维修记录表Service
 * @author xuejh
 * @version 2020-04-20
 */
@Service
@Transactional(readOnly=true)
public class ZbDeviceRepairRecordService extends CrudService<ZbDeviceRepairRecordDao, ZbDeviceRepairRecord> {

	@Autowired
	private ZbDeviceRepairRecordDao repairRecordDao;

	@Autowired
	private ZbDeviceAccountsService accountsService;

	@Autowired
	private ZbDeviceRepairApplyService repairApplyService;

	@Autowired
	private ZbDeviceService ZbDeviceService;
	
	/**
	 * 获取单条数据
	 * @param zbDeviceRepairRecord
	 * @return
	 */
	@Override
	public ZbDeviceRepairRecord get(ZbDeviceRepairRecord zbDeviceRepairRecord) {
		return super.get(zbDeviceRepairRecord);
	}
	
	/**
	 * 查询分页数据
	 * @param zbDeviceRepairRecord 查询条件
	 * @return
	 */
	@Override
	public Page<ZbDeviceRepairRecord> findPage(ZbDeviceRepairRecord zbDeviceRepairRecord) {
		List<ZbDeviceRepairRecord> repairRecordList = repairRecordDao.findPage(zbDeviceRepairRecord);
		if (CollectionUtils.isNotEmpty(repairRecordList)) {
			return new Page<>(zbDeviceRepairRecord.getPageNo(), zbDeviceRepairRecord.getPageSize(), repairRecordDao.findCount(zbDeviceRepairRecord), repairRecordList);
		}
		return new Page<>();
	}
	
	/**
	 * 生成设备维修记录
	 * @author xuejh
	 * @create 2020/4/21 10:54
	 * @param zbDeviceRepairRecord
	 * @return void
	 */
	@Transactional(readOnly=false)
	public void saveModel(ZbDeviceRepairRecord zbDeviceRepairRecord) throws Exception {
		if (zbDeviceRepairRecord == null) {
			throw new BusinessException("维修记录数据为空");
		}

//		if (StringUtils.isBlank(zbDeviceRepairRecord.getRepairOrganName())) {
//			throw new BusinessException("维修单位名称不能为空");
//		}

		super.save(zbDeviceRepairRecord);

		// 修改设备台账状态
		ZbDeviceAccounts deviceAccounts = accountsService.get(zbDeviceRepairRecord.getDeviceAccountsId());
		if (deviceAccounts == null) {
			throw new BusinessException("根据台账Id["+ zbDeviceRepairRecord.getDeviceAccountsId() +"]未找到台账信息");
		}

		// 院内维修生成记录/外送维修完善资料
		if (StringUtils.isNotBlank(zbDeviceRepairRecord.getRepairResultType())) {
			if (zbDeviceRepairRecord.getRepairResultType().equals(ZbDeviceRepairRecord.RESULT_TYPE_COMPLETE)) {
				// 维修完毕 台账正常
				deviceAccounts.setState(ZbDeviceAccounts.DEVICE_STATE_NORMAL);
			} else {
				// 台账待报废申请
//				deviceAccounts.setState(ZbDeviceAccounts.DEVICE_STATE_SCRAP_APPLY);
			}
			accountsService.update(deviceAccounts);
		}

		// 编辑记录上传附件
		if (!zbDeviceRepairRecord.getIsNewRecord()) {
			// 保存上传附件
			FileUploadUtils.saveFileUpload(zbDeviceRepairRecord.getId(), "zbDeviceRepairRecord_file");
		}
	}
	
	/**
	 * 更新状态
	 * @param zbDeviceRepairRecord
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ZbDeviceRepairRecord zbDeviceRepairRecord) {
		super.updateStatus(zbDeviceRepairRecord);
	}
	
	/**
	 * 删除数据
	 * @param zbDeviceRepairRecord
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(ZbDeviceRepairRecord zbDeviceRepairRecord) {
		super.delete(zbDeviceRepairRecord);
	}

	/**
	 * 获取编辑页面初始数据
	 * @author xuejh
	 * @create 2020/4/28 14:32
	 * @param repairRecord
	 * @return com.rd.modules.device.entity.ZbDeviceRepairRecord
	 */
    public ZbDeviceRepairRecord getPageShowData(ZbDeviceRepairRecord repairRecord) {
    	// 获取台账信息
		ZbDeviceAccounts deviceAccounts = accountsService.get(repairRecord.getDeviceAccountsId());
		if (deviceAccounts != null) {
			repairRecord.setDeviceAccountsCode(deviceAccounts.getAccountsCode());
		}

		// 获取维修申请单信息
		ZbDeviceRepairApply repairApply = repairApplyService.get(repairRecord.getRepairApplyId());
		if (repairApply != null) {
			repairRecord.setBillCode(repairApply.getBillCode());
		}

		// 获取设备信息
//		ZbDevice device = deviceService.get(deviceAccounts.getDeviceId());
//		if (device != null) {
//			repairRecord.setDeviceName(device.getDeviceName());
//		}

		return repairRecord;
	}
}