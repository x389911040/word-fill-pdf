/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_repair_apply.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.rd.modules.device.entity.ZbDeviceRepairApply;

import java.util.List;

/**
 * 设备维修申请单DAO接口
 * @author xuejh
 * @version 2020-04-20
 */
@MyBatisDao
public interface ZbDeviceRepairApplyDao extends CrudDao<ZbDeviceRepairApply> {

    /**
     * 单据列表分页查询
     * @author xuejh
     * @create 2020/4/20 15:44
     * @param zbDeviceRepairApply
     * @return java.util.List<com.rd.modules.device.entity.ZbDeviceRepairApply>
     */
    List<ZbDeviceRepairApply> findPage(ZbDeviceRepairApply zbDeviceRepairApply);
}