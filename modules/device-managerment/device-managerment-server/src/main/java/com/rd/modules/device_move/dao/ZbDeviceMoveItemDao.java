/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_move.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.rd.modules.device.entity.ZbDeviceMove;
import com.rd.modules.device.entity.ZbDeviceMoveItem;

import java.util.List;

/**
 * 设备移交DAO接口
 * @author xuejh
 * @version 2020-04-15
 */
@MyBatisDao
public interface ZbDeviceMoveItemDao extends CrudDao<ZbDeviceMoveItem> {

    /**
     * 查询移交单明细，通过Id
     * @author xuejh
     * @create 2020/4/26 16:17
     * @param deviceMove
     * @return java.util.List<com.rd.modules.device.entity.ZbDeviceMoveItem>
     */
    List<ZbDeviceMoveItem> findListById(ZbDeviceMove deviceMove);
}