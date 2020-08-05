/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_free.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.rd.modules.device.entity.ZbDeviceFree;

import java.util.List;

/**
 * 设备闲置申请DAO接口
 * @author xuejh
 * @version 2020-05-06
 */
@MyBatisDao
public interface ZbDeviceFreeDao extends CrudDao<ZbDeviceFree> {

    /**
     * 分页查询列表数据
     * @author xuejh
     * @create 2020/5/6 15:57
     * @param zbDeviceFree
     * @return java.util.List<com.rd.modules.device.entity.ZbDeviceFree>
     */
    List<ZbDeviceFree> findPage(ZbDeviceFree zbDeviceFree);
}