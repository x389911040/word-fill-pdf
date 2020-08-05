/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_move.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.rd.modules.device.entity.ZbDeviceMove;

import java.util.List;

/**
 * 设备移交DAO接口
 * @author xuejh
 * @version 2020-04-15
 */
@MyBatisDao
public interface ZbDeviceMoveDao extends CrudDao<ZbDeviceMove> {

    /**
     * 查询设备移交单列表
     * @author xuejh
     * @create 2020/4/26 15:57
     * @param zbDeviceMove
     * @return java.util.List<com.rd.modules.device.entity.ZbDeviceMove>
     */
    List<ZbDeviceMove> findPage(ZbDeviceMove zbDeviceMove);
}