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
 * 设备维修申请单Entity
 * @author xuejh
 * @version 2020-04-20
 */
@Table(name="zb_device_repair_apply_item", alias="a", columns={
		@Column(name="id", attrName="id", label="主键Id", isPK=true),
		@Column(name="repair_apply_id", attrName="repairApplyId.id", label="维修申请单Id"),
		@Column(name="device_accounts_id", attrName="deviceAccountsId", label="设备台账Id"),
		@Column(name="repair_type", attrName="repairType", label="维修类型 1", comment="维修类型 1：院内维修；2：外送维修；"),
		@Column(name="create_by", attrName="createBy", label="登记人", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="登记时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="修改人", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="修改时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="维修原因", queryType=QueryType.LIKE),
	}, orderBy="a.id ASC"
)
public class ZbDeviceRepairApplyItem extends DataEntity<ZbDeviceRepairApplyItem> {
	
	private static final long serialVersionUID = 1L;
	private ZbDeviceRepairApply repairApplyId;		// 维修申请单Id 父类
	private String deviceAccountsId;
	private String accountsCode;
	private String unitType;
	private String spec;
	private String repairType;		// 维修类型 1：院内维修；2：外送维修；
	private String deviceName;		// 维修设备名称

	public ZbDeviceRepairApplyItem() {
		this(null);
	}


	public ZbDeviceRepairApplyItem(ZbDeviceRepairApply repairApplyId){
		this.repairApplyId = repairApplyId;
	}
	
	@NotBlank(message="维修申请单Id不能为空")
	@Length(min=0, max=64, message="维修申请单Id长度不能超过 64 个字符")
	public ZbDeviceRepairApply getRepairApplyId() {
		return repairApplyId;
	}

	public void setRepairApplyId(ZbDeviceRepairApply repairApplyId) {
		this.repairApplyId = repairApplyId;
	}

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

	public String getRepairType() {
		return repairType;
	}

	public void setRepairType(String repairType) {
		this.repairType = repairType;
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
}