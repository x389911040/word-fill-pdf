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
import java.math.BigDecimal;

/**
 * 设备信息表Entity
 * @author xuejh
 * @version 2020-04-21
 */
@Table(name="zb_device", alias="a", columns={
		@Column(name="id", attrName="id", label="主键Id", isPK=true),
		@Column(name="device_name", attrName="deviceName", label="设备名称", queryType=QueryType.LIKE),
		@Column(name="unit_type", attrName="unitType", label="设备型号", queryType=QueryType.LIKE),
		@Column(name="spec", attrName="spec", label="设备规格"),
		@Column(name="brand", attrName="brand", label="设备品牌"),
		@Column(name="device_type", attrName="deviceType", label="设备类型"),
		@Column(name="img_path", attrName="imgPath", label="图片路径", isQuery=false),
		@Column(name="file_path", attrName="filePath", label="文件路径", isQuery=false),
		@Column(name="create_by", attrName="createBy", label="登记人", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="登记时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="修改人", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="修改时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注", isQuery=false),
	}, orderBy="a.update_date DESC"
)
public class ZbDevice extends DataEntity<ZbDevice> {
	
	private static final long serialVersionUID = 1L;

	private String deviceName;		// 设备名称
	private String unitType;		// 设备型号
	private String deviceType;		// 设备类型
	private String spec;	// 设备规格
	private String brand;	// 品牌
	private String imgPath;		// 图片路径
	private String filePath;		// 文件路径
	private BigDecimal price;	// 最后一次采购单价
	private String supplierId;	// 供应商Id
	private String batchNumber;	// 采购批次号
	private Integer num;	// 采购数量
	
	public ZbDevice() {
		this(null);
	}

	public ZbDevice(String id){
		super(id);
	}
	
	@NotBlank(message="设备名称不能为空")
	@Length(min=0, max=64, message="设备名称长度不能超过 64 个字符")
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
	
	@Length(min=0, max=255, message="图片路径长度不能超过 255 个字符")
	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	
	@Length(min=0, max=255, message="文件路径长度不能超过 255 个字符")
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
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

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
}