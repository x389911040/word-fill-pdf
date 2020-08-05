/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_scrap_move.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.rd.modules.device.entity.ZbDeviceScrapMove;

import java.util.List;

/**
 * 报废处置移交单DAO接口
 * @author xuejh
 * @version 2020-06-01
 */
@MyBatisDao
public interface ZbDeviceScrapMoveDao extends CrudDao<ZbDeviceScrapMove> {

    /**
     * 分页查询处置移交单数据
     * @author xuejh
     * @create 2020/6/2 16:44
     * @param zbDeviceScrapMove
     * @return java.util.List<com.rd.modules.device.entity.ZbDeviceScrapMove>
     */
    List<ZbDeviceScrapMove> findPage(ZbDeviceScrapMove zbDeviceScrapMove);
}