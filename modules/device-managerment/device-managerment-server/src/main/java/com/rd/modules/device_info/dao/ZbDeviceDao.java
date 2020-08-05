/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_info.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.rd.modules.device.entity.ZbDevice;

import java.util.List;

/**
 * 设备信息表DAO接口
 * @author xuejh
 * @version 2020-04-21
 */
@MyBatisDao
public interface ZbDeviceDao extends CrudDao<ZbDevice> {

    /**
     * 分页查询设备列表
     * @author xuejh
     * @create 2020/5/12 9:36
     * @param zbDevice
     * @return java.util.List<com.rd.modules.device.entity.ZbDevice>
     */
    List<ZbDevice> findPage(ZbDevice zbDevice);
}