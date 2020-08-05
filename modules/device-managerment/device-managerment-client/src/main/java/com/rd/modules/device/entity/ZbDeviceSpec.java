package com.rd.modules.device.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;

/**
 * @author xuejh
 * @description 设备规格实体类
 * @create 2020-04-22 11:35
 **/
@Table(name="zb_device_spec", alias="a", columns={
        @Column(name="id", attrName="id", label="主键Id", isPK=true),
        @Column(name="device_id", attrName="deviceId", label="设备主键"),
        @Column(name="spec_value", attrName="specValue", label="设备规格"),
        @Column(name="create_by", attrName="createBy", label="登记人", isUpdate=false, isQuery=false),
        @Column(name="create_date", attrName="createDate", label="登记时间", isUpdate=false, isQuery=false),
        @Column(name="update_by", attrName="updateBy", label="修改人", isQuery=false),
        @Column(name="update_date", attrName="updateDate", label="修改时间", isQuery=false),
        @Column(name="remarks", attrName="remarks", label="备注", isQuery=false),
})
public class ZbDeviceSpec extends DataEntity<ZbDeviceSpec> {

    private static final long serialVersionUID = 1L;
    private String deviceId;    // 设备主键Id
    private String specValue;        // 设备规格
    private String deviceCode;		// 设备编码
    private String deviceName;		// 设备名称
    private String deviceType;		// 设备类型
    private String unitType;		// 设备型号

    public ZbDeviceSpec() {
        this(null);
    }

    public ZbDeviceSpec(String id) {
        super(id);
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSpecValue() {
        return specValue;
    }

    public void setSpecValue(String specValue) {
        this.specValue = specValue;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }
}
