/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_borrow.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 设备借用单Entity
 * @author xuejh
 * @version 2020-05-15
 */
@Table(name="zb_device_borrow_item", alias="a", columns={
		@Column(name="id", attrName="id", label="主键Id", isPK=true),
		@Column(name="device_borrow_id", attrName="deviceBorrowId.id", label="借用单主键Id"),
		@Column(name="device_accounts_id", attrName="deviceAccountsId", label="设备台账Id"),
		@Column(name="return_date", attrName="returnDate", label="归还日期"),
		@Column(name="state", attrName="state", label="状态"),
		@Column(name="create_by", attrName="createBy", label="创建人", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="修改人", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="修改时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", queryType=QueryType.LIKE),
	}, orderBy="a.create_date ASC"
)
public class ZbDeviceBorrowItem extends DataEntity<ZbDeviceBorrowItem> {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 0：待归还
	 */
	public static final String STATE_RETURN_WAIT = "0";

	/**
	 * 1：已归还
	 */
	public static final String STATE_RETURN = "1";

	private ZbDeviceBorrow deviceBorrowId;		// 借用单主键Id 父类
	private String deviceAccountsId;		// 设备台账Id
	private Date returnDate;		// 归还日期
	private String accountsCode;		// 设备台账编号
	private String deviceName;		// 设备名称
	private String unitType;		// 型号
	private String spec;		// 规格
	private String manufacturer;		// 供应商
	private String state;	// 状态 0：待归还；1：已归还；
	
	public ZbDeviceBorrowItem() {
		this(null);
	}


	public ZbDeviceBorrowItem(ZbDeviceBorrow deviceBorrowId){
		this.deviceBorrowId = deviceBorrowId;
	}
	
	@NotBlank(message="借用单主键Id不能为空")
	@Length(min=0, max=64, message="借用单主键Id长度不能超过 64 个字符")
	public ZbDeviceBorrow getDeviceBorrowId() {
		return deviceBorrowId;
	}

	public void setDeviceBorrowId(ZbDeviceBorrow deviceBorrowId) {
		this.deviceBorrowId = deviceBorrowId;
	}
	
	@NotBlank(message="设备台账Id不能为空")
	@Length(min=0, max=64, message="设备台账Id长度不能超过 64 个字符")
	public String getDeviceAccountsId() {
		return deviceAccountsId;
	}

	public void setDeviceAccountsId(String deviceAccountsId) {
		this.deviceAccountsId = deviceAccountsId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}