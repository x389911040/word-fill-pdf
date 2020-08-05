/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device.entity;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 设备维修申请单Entity
 * @author xuejh
 * @version 2020-04-20
 */
@Table(name="zb_device_repair_apply", alias="a", columns={
		@Column(name="id", attrName="id", label="主键Id", isPK=true),
		@Column(name="bill_code", attrName="billCode", label="申请单编号"),
		@Column(name="state", attrName="state", label="状态 1", comment="状态 1：未审核；2：已审核；"),
		@Column(name="dept_opinion", attrName="deptOpinion", label="部门意见"),
		@Column(name="unit_opinion", attrName="unitOpinion", label="所意见"),
//		@Column(name="repair_type", attrName="repairType", label="维修类型"),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", isQuery=false),
	}, orderBy="a.update_date DESC"
)
public class ZbDeviceRepairApply extends DataEntity<ZbDeviceRepairApply> {
	
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
	 * 单据状态：待提交
	 */
	public static final String AUDIT_STATE_WAIT = "0";

	/**
	 * 单据状态：未审核
	 */
	public static final String AUDIT_STATE_N = "1";

	/**
	 * 单据状态：已审核
	 */
	public static final String AUDIT_STATE_Y = "2";

	private String billCode;		// 申请单编号
	private String state;		// 审核状态 1：未审核；2：已审核
	private String deptOpinion;		// 部门意见
	private String unitOpinion;		// 所意见
	private String submitType;	// 是否保存草稿
	private String createDeptName;	// 归属单位
//	private String repairType;	// 维修类型 1：院内维修；2：外送维修；
	private List<ZbDeviceRepairApplyItem> zbDeviceRepairApplyItemList = ListUtils.newArrayList();		// 子表列表
	
	public ZbDeviceRepairApply() {
		this(null);
	}

	public ZbDeviceRepairApply(String id){
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
	
	public List<ZbDeviceRepairApplyItem> getZbDeviceRepairApplyItemList() {
		return zbDeviceRepairApplyItemList;
	}

	public void setZbDeviceRepairApplyItemList(List<ZbDeviceRepairApplyItem> zbDeviceRepairApplyItemList) {
		this.zbDeviceRepairApplyItemList = zbDeviceRepairApplyItemList;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDeptOpinion() {
		return deptOpinion;
	}

	public void setDeptOpinion(String deptOpinion) {
		this.deptOpinion = deptOpinion;
	}

	public String getUnitOpinion() {
		return unitOpinion;
	}

	public void setUnitOpinion(String unitOpinion) {
		this.unitOpinion = unitOpinion;
	}

	public String getSubmitType() {
		return submitType;
	}

	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}

	public String getCreateDeptName() {
		return createDeptName;
	}

	public void setCreateDeptName(String createDeptName) {
		this.createDeptName = createDeptName;
	}
}