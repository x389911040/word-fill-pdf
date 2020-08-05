package com.rd.modules.device_move.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.rd.modules.device.entity.ZbDeviceAccounts;
import com.rd.modules.device.entity.ZbDeviceMoveRecord;

import java.util.List;

/**
 * @author xuejh
 * @description 设备变更记录DAO接口
 * @create 2020-04-27 10:59
 **/
@MyBatisDao
public interface ZbDeviceMoveRecordDao extends CrudDao<ZbDeviceMoveRecord> {

    /**
     * 根据设备台账Id查询变更记录
     * @author xuejh
     * @create 2020/4/27 11:18
     * @param deviceAccounts
     * @return java.util.List<com.rd.modules.device.entity.ZbDeviceMoveRecord>
     */
    List<ZbDeviceMoveRecord> findListByAccountsId(ZbDeviceAccounts deviceAccounts);
}
