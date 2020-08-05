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
 * 报废处置移交单Entity
 * @author xuejh
 * @version 2020-06-01
 */
@Table(name="zb_device_scrap_move", alias="a", columns={
		@Column(name="id", attrName="id", label="主键Id", isPK=true),
		@Column(name="bill_code", attrName="billCode", label="申请单编号"),
		@Column(name="device_accounts_id", attrName="deviceAccountsId", label="台账Id", isUpdate=false, isQuery=false),
		@Column(name="state", attrName="state", label="状态 1", comment="状态 1：未审核；2：已审核；"),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", isQuery=false),
	}, orderBy="a.update_date DESC"
)
public class ZbDeviceScrapMove extends DataEntity<ZbDeviceScrapMove> {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 单据状态：待确认
	 */
	public static final String STATE_WAIT_CONFIRM = "0";

	/**
	 * 单据状态：确认完成
	 */
	public static final String STATE_OVER_CONFIRM = "1";

	private String billCode;		// 申请单编号
	private String deviceAccountsId;		// 台账Id
	private String state;		// 状态 0：待确认；1：确认完成；
	private String accountsCode;	// 设备编号
	private String deviceName;		// 设备名称
	private String unitType;		// 设备型号
	private String spec;		// 设备规格
	private String ownerName;		// 责任人
	private String ownerOfficeName;		// 归属单位

	public ZbDeviceScrapMove() {
		this(null);
	}

	public ZbDeviceScrapMove(String id){
		super(id);
	}
	
	@NotBlank(message="申请单编号不能为空")
	@Length(min=0, max=64, message="申请单编号长度不能超过 64 个字符")
	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	
	public String getDeviceAccountsId() {
		return deviceAccountsId;
	}

	public void setDeviceAccountsId(String deviceAccountsId) {
		this.deviceAccountsId = deviceAccountsId;
	}
	
	@Length(min=0, max=1, message="状态 1长度不能超过 1 个字符")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getOwnerOfficeName() {
		return ownerOfficeName;
	}

	public void setOwnerOfficeName(String ownerOfficeName) {
		this.ownerOfficeName = ownerOfficeName;
	}

	public String getUnitType() {
		return unitType;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
}