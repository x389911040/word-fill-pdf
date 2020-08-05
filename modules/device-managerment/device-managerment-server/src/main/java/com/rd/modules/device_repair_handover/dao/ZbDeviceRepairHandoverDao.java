/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_repair_handover.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.rd.modules.device.entity.ZbDeviceRepairHandover;

import java.util.List;

/**
 * 设备维修交接单DAO接口
 * @author xuejh
 * @version 2020-04-20
 */
@MyBatisDao
public interface ZbDeviceRepairHandoverDao extends CrudDao<ZbDeviceRepairHandover> {

    /**
     * 交接单列表查询
     * @author xuejh
     * @create 2020/4/27 18:45
     * @param repairHandover
     * @return java.util.List<com.rd.modules.device.entity.ZbDeviceRepairHandover>
     */
    List<ZbDeviceRepairHandover> findPage(ZbDeviceRepairHandover repairHandover);
}