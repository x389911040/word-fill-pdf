/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_repair.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.rd.modules.device.entity.ZbDeviceRepair;

import java.util.List;

/**
 * 维修单DAO接口
 * @author xuejh
 * @version 2020-05-07
 */
@MyBatisDao
public interface ZbDeviceRepairDao extends CrudDao<ZbDeviceRepair> {

    /**
     * 分页查询维修单数据
     * @author xuejh
     * @create 2020/5/7 15:21
     * @param zbDeviceRepair
     * @return java.util.List<com.rd.modules.device.entity.ZbDeviceRepair>
     */
    List<ZbDeviceRepair> findPage(ZbDeviceRepair zbDeviceRepair);

    /**
     * 根据Id获取维修单据详情
     * @author xuejh
     * @create 2020/5/7 17:26
     * @param zbDeviceRepair
     * @return com.rd.modules.device.entity.ZbDeviceRepair
     */
    ZbDeviceRepair getRepairBillDetailById(ZbDeviceRepair zbDeviceRepair);
}