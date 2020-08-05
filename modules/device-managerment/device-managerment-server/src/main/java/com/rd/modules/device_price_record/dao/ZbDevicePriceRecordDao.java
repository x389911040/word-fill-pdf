/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_price_record.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.rd.modules.device.entity.ZbDevicePriceRecord;

/**
 * 设备库存表DAO接口
 * @author xuejh
 * @version 2020-04-21
 */
@MyBatisDao
public interface ZbDevicePriceRecordDao extends CrudDao<ZbDevicePriceRecord> {

    /**
     * 获取该设备最近一次采购记录详情
     * @author xuejh
     * @create 2020/5/12 16:36
     * @param tempModel
     * @return com.rd.modules.device.entity.ZbDevicePriceRecord
     */
    ZbDevicePriceRecord getLastPriceRecord(ZbDevicePriceRecord tempModel);
}