package com.rd.modules.device.api;

import com.jeesite.common.entity.Page;
import com.rd.modules.device.entity.ZbDevice;
import com.rd.modules.device.entity.ZbDevicePriceRecord;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author xuejh
 * @description 设备详情服务接口
 * @create 2020-05-12 13:58
 **/
@RequestMapping(value = "/api/device/device_info/ZbDevice")
public interface DeviceInfoServiceApi {

    /**
     * 分页查询设备详情信息
     * @author xuejh
     * @create 2020/5/12 14:42
     * @param device
     *          pageNo：查询页
     *          pageSize：每页显示条数  （不传则返回所有数据）
     * @return com.jeesite.common.entity.Page<com.rd.modules.device.entity.ZbDevice>
     */
    @GetMapping(value = "findPage")
    Page<ZbDevice> findPageDeviceData(ZbDevice device);

    /**
     * 保存、初始化设备基础数据，返回设备详情信息
     * @author xuejh
     * @create 2020/5/13 16:33
     * @param device
     *          deviceName: 设备名称   （必填）
     *          brand：品牌            （必填）
     *          unitType：型号         （必填）
     *          spec：规格             （必填）
     * @return com.rd.modules.device.entity.ZbDevice
     */
    @PostMapping(value = "saveDevice")
    ZbDevice saveDevice(ZbDevice device) throws Exception;

    /**
     * 根据设备Id查询设备价格记录
     * @author xuejh
     * @create 2020/5/14 14:18
     * @param  priceRecord
     *           deviceId：设备主键    （必填）
     * @return java.util.List<com.rd.modules.device.entity.ZbDevice>
     */
    @GetMapping(value = "findPriceRecordList")
    List<ZbDevicePriceRecord> findDevicePriceRecordList(ZbDevicePriceRecord priceRecord);
}
