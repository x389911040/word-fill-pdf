/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_free.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.rd.modules.device.entity.ZbDeviceFree;
import com.rd.modules.device.entity.ZbDeviceFreeItem;

import java.util.List;

/**
 * 设备闲置申请DAO接口
 * @author xuejh
 * @version 2020-05-06
 */
@MyBatisDao
public interface ZbDeviceFreeItemDao extends CrudDao<ZbDeviceFreeItem> {

    /**
     * 根据主表Id查询明细信息
     * @author xuejh
     * @create 2020/5/6 15:51
     * @param zbDeviceFree
     * @return java.util.List<com.rd.modules.device.entity.ZbDeviceFreeItem>
     */
    List<ZbDeviceFreeItem> findListById(ZbDeviceFree zbDeviceFree);
}