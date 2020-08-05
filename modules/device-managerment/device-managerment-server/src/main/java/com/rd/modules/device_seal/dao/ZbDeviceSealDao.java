/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_seal.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.rd.modules.device.entity.ZbDeviceSeal;

import java.util.List;

/**
 * 设备封存表DAO接口
 * @author xuejh
 * @version 2020-04-29
 */
@MyBatisDao
public interface ZbDeviceSealDao extends CrudDao<ZbDeviceSeal> {

    /**
     * 封存申请单分页查询
     * @author xuejh
     * @create 2020/4/30 9:56
     * @param zbDeviceSeal
     * @return java.util.List<com.rd.modules.device.entity.ZbDeviceSeal>
     */
    List<ZbDeviceSeal> findPage(ZbDeviceSeal zbDeviceSeal);
}