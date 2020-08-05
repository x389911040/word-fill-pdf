/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_info.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.file.utils.FileUploadUtils;
import com.rd.modules.common.exception.BusinessException;
import com.rd.modules.device.api.DeviceInfoServiceApi;
import com.rd.modules.device.entity.ZbDevice;
import com.rd.modules.device.entity.ZbDevicePriceRecord;
import com.rd.modules.device_info.dao.ZbDeviceDao;
import com.rd.modules.device_price_record.dao.ZbDevicePriceRecordDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * 设备信息表Service
 * @author xuejh
 * @version 2020-04-21
 */
@Service
@RestController
@Transactional(readOnly=true)
public class ZbDeviceService extends CrudService<ZbDeviceDao, ZbDevice> implements DeviceInfoServiceApi {

	@Autowired
	private ZbDeviceDao deviceDao;

	@Autowired
	private ZbDevicePriceRecordDao priceRecordDao;

	/**
	 * 获取单条数据
	 * @param zbDevice
	 * @return
	 */
	@Override
	public ZbDevice get(ZbDevice zbDevice) {
		return super.get(zbDevice);
	}
	
	/**
	 * 查询分页数据
	 * @param zbDevice 查询条件
	 * @return
	 */
	public Page<ZbDevice> findPageDeviceData(ZbDevice zbDevice) {
		if (zbDevice.getPage() == null) {
			zbDevice.setPageNo(1);
			zbDevice.setPageSize(Integer.MAX_VALUE);
		}

		ZbDevicePriceRecord tempModel = new ZbDevicePriceRecord();
		List<ZbDevice> deviceList = deviceDao.findPage(zbDevice);
		if (CollectionUtils.isNotEmpty(deviceList)) {
			for (ZbDevice device : deviceList) {
				// 查询设备最近一次的采购详情
				tempModel.setDeviceId(device.getId());
				ZbDevicePriceRecord priceRecord = priceRecordDao.getLastPriceRecord(tempModel);
				if (priceRecord != null) {
					device.setPrice(priceRecord.getPrice());
					device.setNum(priceRecord.getNum());
					device.setSupplierId(priceRecord.getId());
					device.setBatchNumber(priceRecord.getBatchNumber());
				}
			}

			return new Page<>(zbDevice.getPageNo(), zbDevice.getPageSize(), deviceDao.findCount(zbDevice), deviceList);
		}

		return new Page<>();
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param zbDevice
	 */
	@Transactional(readOnly=false)
	public ZbDevice saveDevice(ZbDevice zbDevice) throws Exception {
		// 保存上传图片
//		FileUploadUtils.saveFileUpload(zbDevice.getId(), "zbDevice_image");
//		// 保存上传附件
//		FileUploadUtils.saveFileUpload(zbDevice.getId(), "zbDevice_file");

		// 重新按照系统规则生成设备编号,不重复
//		String deviceCode = GenerateBillCodeUtils.getDeviceCode(new Date());
//		zbDevice.setDeviceCode(deviceCode);
		if (StringUtils.isBlank(zbDevice.getDeviceName())) {
			throw new BusinessException("设备名称不能为空");
		}

		if (StringUtils.isBlank(zbDevice.getSpec())) {
			throw new BusinessException("设备规格不能为空");
		}

		if (StringUtils.isBlank(zbDevice.getBrand())) {
			throw new BusinessException("设备品牌不能为空");
		}

		if (StringUtils.isBlank(zbDevice.getUnitType())) {
			throw new BusinessException("设备型号不能为空");
		}
		// 保存设备主表
		super.save(zbDevice);

		return zbDevice;
	}

	/**
	 * 删除数据
	 * @param zbDevice
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(ZbDevice zbDevice) {
		super.delete(zbDevice);
	}

	/**
	 * 根据设备Id，查询是设备价格记录
	 * @author xuejh
	 * @create 2020/5/14 14:21
	 * @param priceRecord
	 * @return java.util.List<com.rd.modules.device.entity.ZbDevice>
	 */
	@Override
	public List<ZbDevicePriceRecord> findDevicePriceRecordList(ZbDevicePriceRecord priceRecord) {
		List<ZbDevicePriceRecord> priceRecordList = priceRecordDao.findList(priceRecord);
		if (CollectionUtils.isNotEmpty(priceRecordList)) {
			return priceRecordList;
		}

		return new ArrayList<>();
	}
}