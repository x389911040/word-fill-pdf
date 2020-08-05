/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_borrow.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.rd.modules.device_borrow.entity.ZbDeviceBorrow;
import com.rd.modules.device_borrow.entity.ZbDeviceBorrowItem;

import java.util.List;

/**
 * 设备借用单DAO接口
 * @author xuejh
 * @version 2020-05-15
 */
@MyBatisDao
public interface ZbDeviceBorrowItemDao extends CrudDao<ZbDeviceBorrowItem> {

    /**
     * 根据借用单主键获取明细详情
     * @author xuejh
     * @create 2020/5/15 14:15
     * @param zbDeviceBorrow
     * @return java.util.List<com.rd.modules.device_borrow.entity.ZbDeviceBorrowItem>
     */
    List<ZbDeviceBorrowItem> findListById(ZbDeviceBorrow zbDeviceBorrow);
}