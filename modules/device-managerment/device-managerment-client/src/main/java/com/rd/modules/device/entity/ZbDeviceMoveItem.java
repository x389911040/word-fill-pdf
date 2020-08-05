/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 设备移交Entity
 * @author xuejh
 * @version 2020-04-15
 */
@Table(name="zb_device_move_item", alias="a", columns={
		@Column(name="id", attrName="id", label="主键Id", isPK=true),
		@Column(name="device_move_id", attrName="deviceMoveId.id", label="移交单主键Id"),
		@Column(name="device_accounts_id", attrName="deviceAccountsId", label="设备台账Id"),
		@Column(name="create_by", attrName="createBy", label="登记人", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="登记时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="修改人", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="修改时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注", isQuery=false),
	}, orderBy="a.id ASC"
)
public class ZbDeviceMoveItem extends DataEntity<ZbDeviceMoveItem> {
	
	private static final long serialVersionUID = 1L;
	private ZbDeviceMove deviceMoveId;		// 移交单主键Id 父类
	private String deviceAccountsId;	// 设备台账主键
	private String accountsCode;		// 设备台账编号
	private String deviceName;		// 设备名称
	private String unitType;		// 型号
	private String spec;		// 规格
	private String manufacturer;		// 供应商
	private String location;		// 位置


	public ZbDeviceMoveItem() {
		this(null);
	}


	public ZbDeviceMoveItem(ZbDeviceMove deviceMoveId){
		this.deviceMoveId = deviceMoveId;
	}
	
	@NotBlank(message="移交单主键Id不能为空")
	@Length(min=0, max=50, message="移交单主键Id长度不能超过 50 个字符")
	public ZbDeviceMove getDeviceMoveId() {
		return deviceMoveId;
	}

	public void setDeviceMoveId(ZbDeviceMove deviceMoveId) {
		this.deviceMoveId = deviceMoveId;
	}
	
	@NotBlank(message="设备名称不能为空")
	@Length(min=0, max=64, message="设备名称长度不能超过 64 个字符")
	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceAccountsId() {
		return deviceAccountsId;
	}

	public void setDeviceAccountsId(String deviceAccountsId) {
		this.deviceAccountsId = deviceAccountsId;
	}

	public String getAccountsCode() {
		return accountsCode;
	}

	public void setAccountsCode(String accountsCode) {
		this.accountsCode = accountsCode;
	}

	public String getUnitType() {
		return unitType;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}