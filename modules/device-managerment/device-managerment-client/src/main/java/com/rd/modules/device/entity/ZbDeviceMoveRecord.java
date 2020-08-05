package com.rd.modules.device.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;

/**
 * @author xuejh
 * @description 设备变更记录表
 * @create 2020-04-27 10:47
 **/
@Table(name="zb_device_move_record", alias="a", columns={
        @Column(name="id", attrName="id", label="主键Id", isPK=true),
        @Column(name="device_move_id", attrName="deviceMoveId", label="移交单主键Id"),
        @Column(name="device_accounts_id", attrName="deviceAccountsId", label="设备台账Id"),
        @Column(name="create_by", attrName="createBy", label="创建人", isUpdate=false, isQuery=false),
        @Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
        @Column(name="update_by", attrName="updateBy", label="修改人", isQuery=false),
        @Column(name="update_date", attrName="updateDate", label="修改时间", isQuery=false),
        @Column(name="remarks", attrName="remarks", label="备注", isQuery=false),
    }, orderBy="a.id ASC"
)
public class ZbDeviceMoveRecord extends DataEntity<ZbDeviceMoveRecord> {

    private static final long serialVersionUID = 1L;
    private String deviceMoveId;
    private String deviceAccountsId;
    private String deviceAccountsCode;
    private String deviceName;
    private String moveName;
    private String receiveName;
    private String billCode;

    public String getDeviceMoveId() {
        return deviceMoveId;
    }

    public void setDeviceMoveId(String deviceMoveId) {
        this.deviceMoveId = deviceMoveId;
    }

    public String getDeviceAccountsId() {
        return deviceAccountsId;
    }

    public void setDeviceAccountsId(String deviceAccountsId) {
        this.deviceAccountsId = deviceAccountsId;
    }

    public String getDeviceAccountsCode() {
        return deviceAccountsCode;
    }

    public void setDeviceAccountsCode(String deviceAccountsCode) {
        this.deviceAccountsCode = deviceAccountsCode;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getMoveName() {
        return moveName;
    }

    public void setMoveName(String moveName) {
        this.moveName = moveName;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
}
