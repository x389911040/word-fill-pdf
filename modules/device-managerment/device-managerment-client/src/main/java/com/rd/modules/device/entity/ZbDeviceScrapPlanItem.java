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
 * 设备报废计划Entity
 * @author xuejh
 * @version 2020-06-01
 */
@Table(name="zb_device_scrap_plan_item", alias="a", columns={
		@Column(name="id", attrName="id", label="主键Id", isPK=true),
		@Column(name="scrap_plan_id", attrName="scrapPlanId.id", label="维修申请单Id"),
		@Column(name="device_accounts_id", attrName="deviceAccountsId", label="设备台账Id"),
		@Column(name="create_by", attrName="createBy", label="创建人", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="修改人", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="修改时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="维修原因", queryType=QueryType.LIKE),
	}, orderBy="a.create_date ASC"
)
public class ZbDeviceScrapPlanItem extends DataEntity<ZbDeviceScrapPlanItem> {
	
	private static final long serialVersionUID = 1L;
	private ZbDeviceScrapPlan scrapPlanId;		// 维修申请单Id 父类
	private String deviceAccountsId;		// 设备台账Id

	private String accountsCode;		// 设备台账编号
	private String deviceName;		// 设备名称
	private String unitType;		// 型号
	private String spec;		// 规格
	private String manufacturer;		// 供应商
	
	public ZbDeviceScrapPlanItem() {
		this(null);
	}


	public ZbDeviceScrapPlanItem(ZbDeviceScrapPlan scrapPlanId){
		this.scrapPlanId = scrapPlanId;
	}
	
	@NotBlank(message="维修申请单Id不能为空")
	@Length(min=0, max=64, message="维修申请单Id长度不能超过 64 个字符")
	public ZbDeviceScrapPlan getScrapPlanId() {
		return scrapPlanId;
	}

	public void setScrapPlanId(ZbDeviceScrapPlan scrapPlanId) {
		this.scrapPlanId = scrapPlanId;
	}
	
	@NotBlank(message="设备台账Id不能为空")
	@Length(min=0, max=64, message="设备台账Id长度不能超过 64 个字符")
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

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
}