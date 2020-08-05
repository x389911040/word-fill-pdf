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
 * 维修单Entity
 * @author xuejh
 * @version 2020-05-07
 */
@Table(name="zb_device_repair", alias="a", columns={
		@Column(name="id", attrName="id", label="主键Id", isPK=true),
		@Column(name="repair_apply_item_id", attrName="repairApplyItemId", label="申请单明细Id", isQuery=false),
		@Column(name="device_accounts_id", attrName="deviceAccountsId", label="台账Id", isQuery=false),
		@Column(name="bill_code", attrName="billCode", label="申请单编号"),
		@Column(name="repair_type", attrName="repairType", label="维修类型 1", comment="维修类型 1：院内维修；2：外送维修；", isQuery=false),
		@Column(name="repair_result_type", attrName="repairResultType", label="维修结果 1", comment="维修结果 1：正常维修；2：报废处理；3：采购器材；", isQuery=false),
		@Column(name="state", attrName="state", label="状态 1", comment="状态 1：未审核；2：已审核；", isQuery=false),
		@Column(name="repair_code", attrName="repairCode", label="维修人编码", isQuery=false),
		@Column(name="repair_unit_name", attrName="repairUnitName", label="外送维修单位名称", isQuery=false),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", isQuery=false),
		@Column(name="error_text", attrName="errorText", label="故障描述", isQuery=false),
		@Column(name="diagnose_text", attrName="diagnoseText", label="诊断描述", isQuery=false),
		@Column(name="outer_text", attrName="outerText", label="外送维修情况说明", isQuery=false),
		@Column(name="outer_file_path", attrName="outerFilePath", label="外送维修报告", isQuery=false),
		@Column(name="file_path", attrName="filePath", label="交接单附件", isQuery=false),
	}, orderBy="a.update_date DESC"
)
public class ZbDeviceRepair extends DataEntity<ZbDeviceRepair> {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 维修类型：院内维修
	 */
	public static final String REPAIR_TYPE_INNER = "1";

	/**
	 * 维修类型：外送维修
	 */
	public static final String REPAIR_TYPE_OUTER = "2";

	/**
	 * 待外送维修确认
	 */
	public static final String STATE_OUTER_CONFIRM = "0";

	/**
	 * 待交接
	 */
	public static final String STATE_HANDOVER = "1";

	/**
	 * 待诊断
	 */
	public static final String STATE_REPAIR_RESULT = "2";

	/**
	 * 维修中
	 */
	public static final String STATE_REPAIRING = "3";

	/**
	 * 维修完成
	 */
	public static final String STATE_REPAIR_OVER = "4";

	/**
	 * 诊断结果：正常维修
	 */
	public static final String REPAIR_RESULT_NORMAL = "1";

	/**
	 * 诊断结果：采购器材
	 */
	public static final String REPAIR_RESULT_PUR = "2";

	/**
	 * 诊断结果：报废处理
	 */
	public static final String REPAIR_RESULT_SCRAP = "3";

	private String repairApplyItemId;		// 申请单明细Id
	private String deviceAccountsId;		// 台账Id
	private String billCode;		// 申请单编号
	private String repairType;		// 维修类型 1：院内维修；2：外送维修；
	private String repairResultType;		// 维修结果 1：正常维修；2：报废处理；3：采购器材；
	private String state;		// 状态 1：待交接；2：待诊断；3：维修中；4：维修完成；
	private String repairCode;		// 维修人编码
	private String repairUnitName;		// 外送维修单位名称
	private String repairApplyCode;		// 维修申请单号
	private String deviceAccountsCode;	// 设备编码
	private String deviceName;	// 设备名称
	private String unitType;	// 型号
	private String spec; // 规格
	private String createDeptName;	// 归属部门
	private String repairName;	// 维修人名称
	private String searchInner;	// 有值查询院内维修单据
	private String errorText;	// 故障描述
	private String diagnoseText;	// 诊断描述
	private String outerText;	// 维修情况说明
	private String filePath;	// 交接单附件
	private String outerFilePath;	// 外送维修报告

	public ZbDeviceRepair() {
		this(null);
	}

	public ZbDeviceRepair(String id){
		super(id);
	}
	
	@NotBlank(message="申请单明细Id不能为空")
	@Length(min=0, max=64, message="申请单明细Id长度不能超过 64 个字符")
	public String getRepairApplyItemId() {
		return repairApplyItemId;
	}

	public void setRepairApplyItemId(String repairApplyItemId) {
		this.repairApplyItemId = repairApplyItemId;
	}
	
	@NotBlank(message="台账Id不能为空")
	@Length(min=0, max=64, message="台账Id长度不能超过 64 个字符")
	public String getDeviceAccountsId() {
		return deviceAccountsId;
	}

	public void setDeviceAccountsId(String deviceAccountsId) {
		this.deviceAccountsId = deviceAccountsId;
	}
	
	@NotBlank(message="申请单编号不能为空")
	@Length(min=0, max=64, message="申请单编号长度不能超过 64 个字符")
	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	
	@Length(min=0, max=1, message="维修类型 1长度不能超过 1 个字符")
	public String getRepairType() {
		return repairType;
	}

	public void setRepairType(String repairType) {
		this.repairType = repairType;
	}
	
	@Length(min=0, max=1, message="维修结果 1长度不能超过 1 个字符")
	public String getRepairResultType() {
		return repairResultType;
	}

	public void setRepairResultType(String repairResultType) {
		this.repairResultType = repairResultType;
	}
	
	@Length(min=0, max=1, message="状态 1长度不能超过 1 个字符")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	@Length(min=0, max=64, message="维修人编码长度不能超过 64 个字符")
	public String getRepairCode() {
		return repairCode;
	}

	public void setRepairCode(String repairCode) {
		this.repairCode = repairCode;
	}
	
	@Length(min=0, max=64, message="外送维修单位名称长度不能超过 64 个字符")
	public String getRepairUnitName() {
		return repairUnitName;
	}

	public void setRepairUnitName(String repairUnitName) {
		this.repairUnitName = repairUnitName;
	}

	public String getRepairApplyCode() {
		return repairApplyCode;
	}

	public void setRepairApplyCode(String repairApplyCode) {
		this.repairApplyCode = repairApplyCode;
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

	public String getCreateDeptName() {
		return createDeptName;
	}

	public void setCreateDeptName(String createDeptName) {
		this.createDeptName = createDeptName;
	}

	public String getRepairName() {
		return repairName;
	}

	public void setRepairName(String repairName) {
		this.repairName = repairName;
	}

	public String getUnitType() {
		return unitType;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	public String getSearchInner() {
		return searchInner;
	}

	public void setSearchInner(String searchInner) {
		this.searchInner = searchInner;
	}

	public String getErrorText() {
		return errorText;
	}

	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}

	public String getDiagnoseText() {
		return diagnoseText;
	}

	public void setDiagnoseText(String diagnoseText) {
		this.diagnoseText = diagnoseText;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getOuterFilePath() {
		return outerFilePath;
	}

	public void setOuterFilePath(String outerFilePath) {
		this.outerFilePath = outerFilePath;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getOuterText() {
		return outerText;
	}

	public void setOuterText(String outerText) {
		this.outerText = outerText;
	}
}