/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_borrow.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.rd.modules.device_borrow.entity.ZbDeviceBorrow;

import java.util.List;

/**
 * 设备借用单DAO接口
 * @author xuejh
 * @version 2020-05-15
 */
@MyBatisDao
public interface ZbDeviceBorrowDao extends CrudDao<ZbDeviceBorrow> {

    /**
     * 分页查询借用单列表数据
     * @author xuejh
     * @create 2020/5/15 16:18
     * @param zbDeviceBorrow
     * @return java.util.List<com.rd.modules.device_borrow.entity.ZbDeviceBorrow>
     */
    List<ZbDeviceBorrow> findPage(ZbDeviceBorrow zbDeviceBorrow);
}