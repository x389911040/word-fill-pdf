/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_scrap_plan.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.rd.modules.device.entity.ZbDeviceScrapPlan;

import java.util.List;

/**
 * 设备报废计划DAO接口
 * @author xuejh
 * @version 2020-06-01
 */
@MyBatisDao
public interface ZbDeviceScrapPlanDao extends CrudDao<ZbDeviceScrapPlan> {

    /**
     * 设备报废计划列表
     * @author xuejh
     * @create 2020/6/1 11:42
     * @param zbDeviceScrapPlan
     * @return java.util.List<com.rd.modules.device.entity.ZbDeviceScrapPlan>
     */
    List<ZbDeviceScrapPlan> findPage(ZbDeviceScrapPlan zbDeviceScrapPlan);
}