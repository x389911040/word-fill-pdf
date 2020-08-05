/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_accounts.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.rd.modules.device.entity.ZbDeviceAccounts;
import com.rd.modules.device.entity.ZbDeviceAccountsItem;

import java.util.List;

/**
 * 设备移交DAO接口
 * @author xuejh
 * @version 2020-04-15
 */
@MyBatisDao
public interface ZbDeviceAccountsItemDao extends CrudDao<ZbDeviceAccountsItem> {

    /**
     * 通过主表Id查询明细
     * @author xuejh
     * @create 2020/5/11 15:19
     * @param zbDeviceAccounts
     * @return java.util.List<com.rd.modules.device.entity.ZbDeviceAccountsItem>
     */
    List<ZbDeviceAccountsItem> findItemsById(ZbDeviceAccounts zbDeviceAccounts);
}