/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_accounts.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.rd.modules.device.entity.ZbDeviceAccounts;

import java.util.List;

/**
 * 设备登记DAO接口
 * @author xuejh
 * @version 2020-04-15
 */
@MyBatisDao
public interface ZbDeviceAccountsDao extends CrudDao<ZbDeviceAccounts> {

    /**
     * 查询分页数据通过日期
     * @author xuejh
     * @create 2020/4/15 17:13
     * @param zbDeviceAccounts
     * @return java.util.List<com.rd.modules.device_accounts.entity.ZbDeviceAccounts>
     */
    List<ZbDeviceAccounts> findPage(ZbDeviceAccounts zbDeviceAccounts);

    /**
     * 按设备编码倒叙获取最后一个设备
     * @author xuejh
     * @create 2020/5/8 18:17
     * @param
     * @return com.rd.modules.device.entity.ZbDeviceAccounts
     */
    ZbDeviceAccounts getLastDeviceSortCode();
}