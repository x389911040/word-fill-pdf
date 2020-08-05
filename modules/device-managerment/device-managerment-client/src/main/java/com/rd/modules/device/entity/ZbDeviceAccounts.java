/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;

import java.util.Date;
import java.util.List;

/**
 * 设备登记Entity
 * @author xuejh
 * @version 2020-04-15
 */
@Table(name="zb_device_accounts", alias="a", columns={
		@Column(name="id", attrName="id", label="主键Id", isPK=true),
		@Column(name="accounts_code", attrName="accountsCode", label="台账编号"),
		@Column(name="device_name", attrName="deviceName", label="设备名称"),
		@Column(name="state", attrName="state", label="设备状态", comment="设备状态;1: 正常，2: 报废;"),
		@Column(name="manufacturer", attrName="manufacturer", label="生产厂家"),
		@Column(name="unit_type", attrName="unitType", label="型号"),
		@Column(name="spec", attrName="spec", label="规格"),
		@Column(name="location", attrName="location", label="位置"),
		@Column(name="produce_date", attrName="produceDate", label="出厂日期"),
		@Column(name="purchase_date", attrName="purchaseDate", label="购入日期"),
		@Column(name="operate_date", attrName="operateDate", label="投运日期"),
		@Column(name="produce_code", attrName="produceCode", label="出厂编号"),
		@Column(name="original_value", attrName="originalValue", label="出厂原值"),
		@Column(name="brand", attrName="brand", label="品牌", isQuery=false),
		@Column(name="description", attrName="description", label="功能描述", isQuery=false),
		@Column(name="owner_type", attrName="ownerType", label="拥有者类型;1", comment="拥有者类型;1:个人,2: 部门;"),
		@Column(name="owner_code", attrName="ownerCode", label="拥有者编码", isQuery=false),
		@Column(name="owner_office_code", attrName="ownerOfficeCode", label="拥有者编码", isQuery=false),
		@Column(name="status", attrName="status", label="台账状态", comment="台账状态;0: 正常，1: 废弃;", isQuery=false),
		@Column(name="create_by", attrName="createBy", label="登记人", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="登记时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="修改人", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="修改时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注", isQuery=false),
	}, orderBy="a.update_date DESC"
)
public class ZbDeviceAccounts extends DataEntity<ZbDeviceAccounts> {

	private static final long serialVersionUID = 1L;

	/**
	 * 拥有者类型：个人
	 */
	public static final String OWNER_TYPE_EMP = "1";

	/**
	 * 拥有者类型：部门
	 */
	public static final String OWNER_TYPE_DEPT = "2";

	/**
	 * 台账状态：正常
	 */
	public static final String DEVICE_STATE_NORMAL = "1";

	/**
	 * 台账状态：已报废
	 */
	public static final String DEVICE_STATE_SCRAP = "2";

	/**
	 * 台账状态：移交中
	 */
	public static final String DEVICE_STATE_MOVE= "3";

	/**
	 * 台账状态：维修中
	 */
	public static final String DEVICE_STATE_REPAIR= "4";

	/**
	 * 台账状态：封存
	 */
	public static final String DEVICE_STATE_SEAL= "5";

	/**
	 * 台账状态：闲置
	 */
	public static final String DEVICE_STATE_FREE= "6";

	/**
	 * 台账状态：借用中
	 */
	public static final String DEVICE_STATE_BORROW= "7";

	/**
	 * 台账状态：待报废
	 */
	public static final String DEVICE_STATE_WAIT_SCRAP = "8";

	private String accountsCode;	// 台账编号
	private String ownerType;		// 拥有者类型;1:个人,2: 部门;
	private String ownerCode;		// 拥有者编码
	private String ownerName;		// 拥有者名称
	private String state;		// 台账状态 1：正常；2：报废；
	private String createDateStart;	// 查询开始时间
	private String createDateEnd;		// 查询结束时间
	private String deviceName;		// 设备名称
	private String deviceType;		// 设备类型
	private String manufacturer;	// 生产厂家
	private String unitType;	// 型号
	private String spec;	// 规格
	private String location;	// 位置
	private Date produceDate;	// 出厂日期
	private Date operateDate;	// 投运日期
	private Date purchaseDate;	// 购入日期
	private String produceCode;	// 出厂编号
	private String originalValue;	// 设备原值
	private String ownerOfficeCode;	// 归属单位
	private String ownerOfficeName;	// 归属单位名称
	private String brand;	// 品牌
	private String description;	// 功能描述
	private String accountsIds;	// 台账Ids
	private String searchScrapSign;	// 是否查询报废列表

	private List<ZbDeviceAccountsItem> itemList = ListUtils.newArrayList();	// 台账附件

	public ZbDeviceAccounts() {
		this(null);
	}

	public ZbDeviceAccounts(String id){
		super(id);
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

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}
	
	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getCreateDateStart() {
		return createDateStart;
	}

	public void setCreateDateStart(String createDateStart) {
		this.createDateStart = createDateStart;
	}

	public String getCreateDateEnd() {
		return createDateEnd;
	}

	public void setCreateDateEnd(String createDateEnd) {
		this.createDateEnd = createDateEnd;
	}

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

	public List<ZbDeviceAccountsItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<ZbDeviceAccountsItem> itemList) {
		this.itemList = itemList;
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@JsonFormat(
			pattern = "yyyy-MM-dd HH:mm:ss"
	)
	public Date getProduceDate() {
		return produceDate;
	}

	public void setProduceDate(Date produceDate) {
		this.produceDate = produceDate;
	}

	@JsonFormat(
			pattern = "yyyy-MM-dd HH:mm:ss"
	)
	public Date getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	public String getProduceCode() {
		return produceCode;
	}

	public void setProduceCode(String produceCode) {
		this.produceCode = produceCode;
	}

	public String getOriginalValue() {
		return originalValue;
	}

	public void setOriginalValue(String originalValue) {
		this.originalValue = originalValue;
	}

	public String getOwnerOfficeCode() {
		return ownerOfficeCode;
	}

	public void setOwnerOfficeCode(String ownerOfficeCode) {
		this.ownerOfficeCode = ownerOfficeCode;
	}

	public String getOwnerOfficeName() {
		return ownerOfficeName;
	}

	public void setOwnerOfficeName(String ownerOfficeName) {
		this.ownerOfficeName = ownerOfficeName;
	}

	@JsonFormat(
			pattern = "yyyy-MM-dd HH:mm:ss"
	)
	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAccountsIds() {
		return accountsIds;
	}

	public void setAccountsIds(String accountsIds) {
		this.accountsIds = accountsIds;
	}

	public String getSearchScrapSign() {
		return searchScrapSign;
	}

	public void setSearchScrapSign(String searchScrapSign) {
		this.searchScrapSign = searchScrapSign;
	}
}