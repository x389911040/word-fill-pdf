/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;

/**
 * 设备维修交接单Entity
 * @author xuejh
 * @version 2020-04-20
 */
@Table(name="zb_device_repair_handover", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="device_repair_id", attrName="deviceRepairId", label="设备维修单Id", isUpdate=false, isQuery=false),
		@Column(name="bill_code", attrName="billCode", label="单据编号"),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", isQuery=false),
	}, orderBy="a.update_date DESC"
)
public class ZbDeviceRepairHandover extends DataEntity<ZbDeviceRepairHandover> {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 交接单状态 1：待确认
	 */
	public static final String STATE_CONFIRM_N = "1";

	/**
	 * 交接单状态 2：已确认
	 */
	public static final String STATE_CONFIRM_Y = "2";

	private String deviceRepairId;		// 设备维修单Id
	private String billCode;		// 单据编号
	private String repairBillCode;	// 维修单编号

	public ZbDeviceRepairHandover() {
		this(null);
	}

	public ZbDeviceRepairHandover(String id){
		super(id);
	}

	public String getDeviceRepairId() {
		return deviceRepairId;
	}

	public void setDeviceRepairId(String deviceRepairId) {
		this.deviceRepairId = deviceRepairId;
	}

	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public String getRepairBillCode() {
		return repairBillCode;
	}

	public void setRepairBillCode(String repairBillCode) {
		this.repairBillCode = repairBillCode;
	}
}