/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_repair_record.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.rd.modules.device.entity.ZbDeviceAccounts;
import com.rd.modules.device.entity.ZbDeviceRepairRecord;

import java.util.List;

/**
 * 设备维修记录表DAO接口
 * @author xuejh
 * @version 2020-04-20
 */
@MyBatisDao
public interface ZbDeviceRepairRecordDao extends CrudDao<ZbDeviceRepairRecord> {

    /**
     * 维修记录查询
     * @author xuejh
     * @create 2020/4/27 18:22
     * @param zbDeviceRepairRecord
     * @return com.jeesite.common.entity.Page<com.rd.modules.device.entity.ZbDeviceRepairRecord>
     */
    List<ZbDeviceRepairRecord> findPage(ZbDeviceRepairRecord zbDeviceRepairRecord);

    /**
     * 根据
     * @author xuejh
     * @create 2020/4/28 15:11
     * @param zbDeviceAccounts
     * @return java.util.List<com.rd.modules.device.entity.ZbDeviceRepairRecord>
     */
    List<ZbDeviceRepairRecord> findListByAccountsId(ZbDeviceAccounts zbDeviceAccounts);
}