/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_price_record.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.rd.modules.device.entity.ZbDevicePriceRecord;
import com.rd.modules.device_price_record.dao.ZbDevicePriceRecordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 设备库存表Service
 * @author xuejh
 * @version 2020-04-21
 */
@Service
@Transactional(readOnly=true)
public class ZbDevicePriceRecordService extends CrudService<ZbDevicePriceRecordDao, ZbDevicePriceRecord> {

	@Autowired
	private ZbDevicePriceRecordDao priceRecordDao;
	
	/**
	 * 获取单条数据
	 * @param zbDevicePriceRecord
	 * @return
	 */
	@Override
	public ZbDevicePriceRecord get(ZbDevicePriceRecord zbDevicePriceRecord) {
		return super.get(zbDevicePriceRecord);
	}
	
	/**
	 * 查询分页数据
	 * @param zbDevicePriceRecord 查询条件
	 * @return
	 */
	@Override
	public Page<ZbDevicePriceRecord> findPage(ZbDevicePriceRecord zbDevicePriceRecord) {
		return super.findPage(zbDevicePriceRecord);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param zbDevicePriceRecord
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(ZbDevicePriceRecord zbDevicePriceRecord) {
		super.save(zbDevicePriceRecord);
	}
	
	/**
	 * 更新状态
	 * @param zbDevicePriceRecord
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(ZbDevicePriceRecord zbDevicePriceRecord) {
		super.updateStatus(zbDevicePriceRecord);
	}
	
	/**
	 * 删除数据
	 * @param zbDevicePriceRecord
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(ZbDevicePriceRecord zbDevicePriceRecord) {
		super.delete(zbDevicePriceRecord);
	}
}