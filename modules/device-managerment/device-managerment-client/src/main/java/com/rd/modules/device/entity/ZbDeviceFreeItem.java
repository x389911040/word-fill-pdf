/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 设备闲置申请Entity
 * @author xuejh
 * @version 2020-05-06
 */
@Table(name="zb_device_free_item", alias="a", columns={
		@Column(name="id", attrName="id", label="主键Id", isPK=true),
		@Column(name="free_id", attrName="freeId.id", label="主表Id"),
		@Column(name="device_accounts_id", attrName="deviceAccountsId", label="台账Id"),
		@Column(name="create_by", attrName="createBy", label="创建人", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="修改人", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="修改时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", queryType=QueryType.LIKE),
	}, orderBy="a.create_date ASC"
)
public class ZbDeviceFreeItem extends DataEntity<ZbDeviceFreeItem> {
	
	private static final long serialVersionUID = 1L;
	private ZbDeviceFree freeId;		// 主表Id 父类
	private String deviceAccountsId;		// 台账Id
	private String accountsCode;	// 台账编码
	private String deviceName;	//设备名称
	private String unitType;	//型号
	private String spec;	//规格

	public ZbDeviceFreeItem() {
		this(null);
	}


	public ZbDeviceFreeItem(ZbDeviceFree freeId){
		this.freeId = freeId;
	}
	
	@NotBlank(message="主表Id不能为空")
	@Length(min=0, max=64, message="主表Id长度不能超过 64 个字符")
	public ZbDeviceFree getFreeId() {
		return freeId;
	}

	public void setFreeId(ZbDeviceFree freeId) {
		this.freeId = freeId;
	}
	
	@NotBlank(message="台账Id不能为空")
	@Length(min=0, max=64, message="台账Id长度不能超过 64 个字符")
	public String getDeviceAccountsId() {
		return deviceAccountsId;
	}

	public void setDeviceAccountsId(String deviceAccountsId) {
		this.deviceAccountsId = deviceAccountsId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
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

	public String getAccountsCode() {
		return accountsCode;
	}

	public void setAccountsCode(String accountsCode) {
		this.accountsCode = accountsCode;
	}
}