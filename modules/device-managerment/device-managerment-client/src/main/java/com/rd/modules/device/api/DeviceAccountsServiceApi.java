package com.rd.modules.device.api;

import com.rd.modules.device.entity.ZbDeviceAccounts;
import com.rd.modules.device.entity.ZbDevicePriceRecord;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xuejh
 * @description 设备详情服务接口
 * @create 2020-05-12 13:58
 **/
@RequestMapping(value = "/api/device/device_accounts/ZbDeviceAccounts")
public interface DeviceAccountsServiceApi {

    /**
     *
     * @author xuejh
     * @create 2020/5/13 17:54
     * @param zbDeviceAccounts
     *          deviceName      // 设备名称         （必填）
     *          unitType        // 型号             （必填）
     *          spec            // 规格             （必填）
     *          brand           // 品牌             （必填）
     *          ownerOfficeCode // 归属单位编号     （必填）
     *          ownerCode       // 责任人           （必填）
     *          purchaseDate    // 购入日期         （必填）
     *          manufacturer    // 生产厂家
     *          produceDate     // 出厂日期
     *          operateDate     // 投运日期
     *          produceCode     // 出厂编号
     *          location        // 位置
     *          originalValue   // 设备原值
     * @param optType
     *          1：页面新增台账 （针对采购设备）
     *          2：采购到货验收初始化台账
     * @param priceRecord
     *          deviceId      // 设备Id      （必填）
     *          batchNumber   // 批次号      （必填）
     *          supplierId    // 供应商Id    （必填）
     *          price         // 采购单价    （必填）
     *          num           // 采购数量    （必填）
     * @return com.rd.modules.device.entity.ZbDevice
     */
    @PostMapping(value = "saveDeviceAccounts")
    void saveAccounts(ZbDeviceAccounts zbDeviceAccounts, int optType, ZbDevicePriceRecord priceRecord) throws Exception;
}
