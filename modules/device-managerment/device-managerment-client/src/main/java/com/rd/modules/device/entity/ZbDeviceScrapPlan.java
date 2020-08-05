/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device.entity;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 设备报废计划Entity
 * @author xuejh
 * @version 2020-06-01
 */
@Table(name="zb_device_scrap_plan", alias="a", columns={
		@Column(name="id", attrName="id", label="主键Id", isPK=true),
		@Column(name="bill_code", attrName="billCode", label="申请单编号"),
		@Column(name="dept_code", attrName="deptCode", label="部门编号"),
		@Column(name="state", attrName="state", label="状态", comment="状态 1：未审核；2：已审核；"),
		@Column(name="create_by", attrName="createBy", label="创建者", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新者", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", queryType=QueryType.LIKE),
	}, orderBy="a.update_date DESC"
)
public class ZbDeviceScrapPlan extends DataEntity<ZbDeviceScrapPlan> {
	
	private static final long serialVersionUID = 1L;

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
	private String deptCode;		// 部门编号
	private String deptName;	// 部门名称
	private String state;		// 状态 1：未审核；2：已审核；
	private String submitType;	// 是否保存草稿
	private List<ZbDeviceScrapPlanItem> zbDeviceScrapPlanItemList = ListUtils.newArrayList();		// 子表列表
	
	public ZbDeviceScrapPlan() {
		this(null);
	}

	public ZbDeviceScrapPlan(String id){
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
	
	@NotBlank(message="部门编号不能为空")
	@Length(min=0, max=64, message="部门编号长度不能超过 64 个字符")
	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	
	@Length(min=0, max=1, message="状态 1长度不能超过 1 个字符")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public List<ZbDeviceScrapPlanItem> getZbDeviceScrapPlanItemList() {
		return zbDeviceScrapPlanItemList;
	}

	public void setZbDeviceScrapPlanItemList(List<ZbDeviceScrapPlanItem> zbDeviceScrapPlanItemList) {
		this.zbDeviceScrapPlanItemList = zbDeviceScrapPlanItemList;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getSubmitType() {
		return submitType;
	}

	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}
}