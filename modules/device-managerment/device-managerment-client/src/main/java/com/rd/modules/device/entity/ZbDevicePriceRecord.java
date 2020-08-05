/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * 设备采购价格记录表Entity
 * @author xuejh
 * @version 2020-04-21
 */
@Table(name="zb_device_price_record", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="device_id", attrName="deviceId", label="device_id", isUpdate=false, isQuery=true),
		@Column(name="supplier_id", attrName="supplierId", label="供应商Id"),
		@Column(name="batch_number", attrName="batchNumber", label="采购批次号", isUpdate=false),
		@Column(name="price", attrName="price", label="采购单价", isUpdate=false, isQuery=false),
		@Column(name="num", attrName="num", label="采购数量", isQuery=false),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),@Column(name="create_by", attrName="createBy", label="登记人", isUpdate=false, isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注", isQuery=false),
	}, orderBy="a.update_date DESC"
)
public class ZbDevicePriceRecord extends DataEntity<ZbDevicePriceRecord> {
	
	private static final long serialVersionUID = 1L;
	private String deviceId;		// device_id
	private String batchNumber;		// 采购批次号
	private BigDecimal price;		// 采购单价
	private Integer num;		// 采购数量
	private String supplierId;	// 供应商Id
	
	public ZbDevicePriceRecord() {
		this(null);
	}

	public ZbDevicePriceRecord(String id){
		super(id);
	}
	
	@NotBlank(message="device_id不能为空")
	@Length(min=0, max=64, message="device_id长度不能超过 64 个字符")
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	@NotBlank(message="采购批次号不能为空")
	@Length(min=0, max=64, message="采购批次号长度不能超过 64 个字符")
	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}
}