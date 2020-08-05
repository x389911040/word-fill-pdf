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
 * 设备维修记录表Entity
 * @author xuejh
 * @version 2020-04-20
 */
@Table(name="zb_device_repair_record", alias="a", columns={
		@Column(name="id", attrName="id", label="主键Id", isPK=true),
		@Column(name="repair_apply_id", attrName="repairApplyId", label="维修申请单Id"),
		@Column(name="device_accounts_id", attrName="deviceAccountsId", label="设备台账Id"),
		@Column(name="repair_organ_name", attrName="repairOrganName", label="维修单位名称", queryType=QueryType.LIKE),
		@Column(name="repair_type", attrName="repairType", label="维修类型"),
		@Column(name="repair_result_type", attrName="repairResultType", label="维修结果判定"),	// 1.维修完毕；2：已报废
		@Column(name="file_path", attrName="filePath", label="维修相关文件路径", isQuery=false),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", isQuery=false),
		@Column(name="repair_opinion", attrName="repairOpinion", label="诊断意见", isQuery=false),
	}, orderBy="a.update_date DESC"
)
public class ZbDeviceRepairRecord extends DataEntity<ZbDeviceRepairRecord> {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 维修完毕
	 */
	public static final String RESULT_TYPE_COMPLETE = "1";

	/**
	 * 已报废
	 */
	public static final String RESULT_TYPE_SCRAP = "2";

	private String repairApplyId;		// 维修申请单Id
	private String deviceAccountsId;	// 设备台账Id
	private String deviceAccountsCode;	// 设备台账编号
	private String deviceCode;		// 设备编码
	private String deviceName;		// 设备名称
	private String repairOrganName;		// 维修单位名称
	private String repairType;		// 维修类型
	private String repairResultType;		// 维修结果判定
	private String filePath;		// 维修相关文件路径
	private String billCode;	// 申请单编号
	private String applyName;	// 维修申请人名称
	private String repairOpinion;	// 诊断意见

	public ZbDeviceRepairRecord() {
		this(null);
	}

	public ZbDeviceRepairRecord(String id){
		super(id);
	}
	
	@NotBlank(message="维修申请单Id不能为空")
	@Length(min=0, max=64, message="维修申请单Id长度不能超过 64 个字符")
	public String getRepairApplyId() {
		return repairApplyId;
	}

	public void setRepairApplyId(String repairApplyId) {
		this.repairApplyId = repairApplyId;
	}
	
	public String getRepairOrganName() {
		return repairOrganName;
	}

	public void setRepairOrganName(String repairOrganName) {
		this.repairOrganName = repairOrganName;
	}
	
	@Length(min=0, max=255, message="维修相关文件路径长度不能超过 255 个字符")
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getDeviceAccountsId() {
		return deviceAccountsId;
	}

	public void setDeviceAccountsId(String deviceAccountsId) {
		this.deviceAccountsId = deviceAccountsId;
	}

	public String getDeviceAccountsCode() {
		return deviceAccountsCode;
	}

	public void setDeviceAccountsCode(String deviceAccountsCode) {
		this.deviceAccountsCode = deviceAccountsCode;
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

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public String getApplyName() {
		return applyName;
	}

	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}

	public String getRepairResultType() {
		return repairResultType;
	}

	public void setRepairResultType(String repairResultType) {
		this.repairResultType = repairResultType;
	}

	public String getRepairOpinion() {
		return repairOpinion;
	}

	public void setRepairOpinion(String repairOpinion) {
		this.repairOpinion = repairOpinion;
	}
}