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
@Table(name="zb_device_accounts_item", alias="a", columns={
		@Column(name="id", attrName="id", label="主键Id", isPK=true),
		@Column(name="device_accounts_id", attrName="deviceAccountsId.id", label="移交单主键Id"),
		@Column(name="device_name", attrName="deviceName", label="设备名称"),
		@Column(name="manufacturer", attrName="manufacturer", label="生产厂家"),
		@Column(name="unit_type", attrName="unitType", label="型号"),
		@Column(name="spec", attrName="spec", label="规格"),
		@Column(name="num", attrName="num", label="数量"),
		@Column(name="description", attrName="description", label="描述"),
		@Column(name="create_by", attrName="createBy", label="登记人", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="登记时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="修改人", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="修改时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注", isQuery=false),
	}, orderBy="a.id ASC"
)
public class ZbDeviceAccountsItem extends DataEntity<ZbDeviceAccountsItem> {

	private static final long serialVersionUID = 1L;
	private ZbDeviceAccounts deviceAccountsId;		// 移交单主键Id 父类
	private String deviceName;		// 设备名称
	private String manufacturer;		// 生产厂家
	private String unitType;		// 型号
	private String spec;		// 规格
	private int num;		// 数量
	private String description;	// 描述

	public ZbDeviceAccountsItem() {
		this(null);
	}


	public ZbDeviceAccountsItem(ZbDeviceAccounts deviceAccountsId){
		this.deviceAccountsId = deviceAccountsId;
	}
	
	public ZbDeviceAccounts getDeviceAccountsId() {
		return deviceAccountsId;
	}

	public void setDeviceAccountsId(ZbDeviceAccounts deviceAccountsId) {
		this.deviceAccountsId = deviceAccountsId;
	}
	
	@NotBlank(message="设备名称不能为空")
	@Length(min=0, max=64, message="设备名称长度不能超过 64 个字符")
	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
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

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}