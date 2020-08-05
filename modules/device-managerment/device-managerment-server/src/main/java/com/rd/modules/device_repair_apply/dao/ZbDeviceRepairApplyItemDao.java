/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_repair_apply.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.rd.modules.device.entity.ZbDeviceRepairApply;
import com.rd.modules.device.entity.ZbDeviceRepairApplyItem;

import java.util.List;

/**
 * 设备维修申请单DAO接口
 * @author xuejh
 * @version 2020-04-20
 */
@MyBatisDao
public interface ZbDeviceRepairApplyItemDao extends CrudDao<ZbDeviceRepairApplyItem> {

    /**
     * 根据申请单主键获取明细
     * @author xuejh
     * @create 2020/4/27 15:49
     * @param repairApply
     * @return java.util.List<com.rd.modules.device.entity.ZbDeviceRepairApplyItem>
     */
    List<ZbDeviceRepairApplyItem> findListById(ZbDeviceRepairApply repairApply);
}