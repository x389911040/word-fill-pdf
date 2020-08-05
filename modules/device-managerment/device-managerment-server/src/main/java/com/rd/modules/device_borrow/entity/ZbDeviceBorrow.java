/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_borrow.entity;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 设备借用单Entity
 * @author xuejh
 * @version 2020-05-15
 */
@Table(name="zb_device_borrow", alias="a", columns={
		@Column(name="id", attrName="id", label="主键Id", isPK=true),
		@Column(name="bill_code", attrName="billCode", label="借用单编号"),
		@Column(name="apply_dept_code", attrName="applyDeptCode", label="申请部门编码"),
		@Column(name="apply_code", attrName="applyCode", label="申请人编码"),
		@Column(name="borrow_dept_code", attrName="borrowDeptCode", label="借出部门编码"),
		@Column(name="borrow_code", attrName="borrowCode", label="借出人编码"),
		@Column(name="state", attrName="state", label="状态；0", comment="状态；0：待提交；1：未审核,2：已审核；", isQuery=false),
		@Column(name="create_by", attrName="createBy", label="创建人", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="修改人", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="修改时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="借用原因", isQuery=false),
		@Column(name="apply_text", attrName="applyText", label="借用人意见", isQuery=false),
		@Column(name="apply_leader_text", attrName="applyLeaderText", label="借用领导意见", isQuery=false),
		@Column(name="borrow_text", attrName="borrowText", label="借出人意见", isQuery=false),
		@Column(name="borrow_leader_text", attrName="borrowLeaderText", label="借出领导意见", isQuery=false),
		@Column(name="business_text", attrName="businessText", label="业务部门意见", isQuery=false),
	}, orderBy="a.update_date DESC"
)
public class ZbDeviceBorrow extends DataEntity<ZbDeviceBorrow> {
	
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

	private String billCode;		// 借用单编号
	private String applyDeptCode;		// 申请部门编码
	private String applyCode;		// 申请人编码
	private String borrowDeptCode;		// 借出部门编码
	private String borrowCode;		// 借出人编码
	private String applyName;		// 申请人名称
	private String applyDeptName;		// 申请部门名称
	private String borrowName;		// 借出人名称
	private String borrowDeptName;		// 借出部门名称
	private String state;		// 状态；0：待提交；1：未审核,2：已审核；
	private String submitType;	// 是否保存草稿
	private String applyText;
	private String applyLeaderText;
	private String borrowText;
	private String borrowLeaderText;
	private String businessText;
	private List<ZbDeviceBorrowItem> zbDeviceBorrowItemList = ListUtils.newArrayList();		// 子表列表
	
	public ZbDeviceBorrow() {
		this(null);
	}

	public ZbDeviceBorrow(String id){
		super(id);
	}
	
	@NotBlank(message="借用单编号不能为空")
	@Length(min=0, max=64, message="借用单编号长度不能超过 64 个字符")
	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	
	@NotBlank(message="申请部门编码不能为空")
	@Length(min=0, max=64, message="申请部门编码长度不能超过 64 个字符")
	public String getApplyDeptCode() {
		return applyDeptCode;
	}

	public void setApplyDeptCode(String applyDeptCode) {
		this.applyDeptCode = applyDeptCode;
	}
	
	@NotBlank(message="申请人编码不能为空")
	@Length(min=0, max=64, message="申请人编码长度不能超过 64 个字符")
	public String getApplyCode() {
		return applyCode;
	}

	public void setApplyCode(String applyCode) {
		this.applyCode = applyCode;
	}
	
	@NotBlank(message="借出部门编码不能为空")
	@Length(min=0, max=64, message="借出部门编码长度不能超过 64 个字符")
	public String getBorrowDeptCode() {
		return borrowDeptCode;
	}

	public void setBorrowDeptCode(String borrowDeptCode) {
		this.borrowDeptCode = borrowDeptCode;
	}
	
	@NotBlank(message="借出人编码不能为空")
	@Length(min=0, max=64, message="借出人编码长度不能超过 64 个字符")
	public String getBorrowCode() {
		return borrowCode;
	}

	public void setBorrowCode(String borrowCode) {
		this.borrowCode = borrowCode;
	}
	
	@Length(min=0, max=1, message="状态；0长度不能超过 1 个字符")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public List<ZbDeviceBorrowItem> getZbDeviceBorrowItemList() {
		return zbDeviceBorrowItemList;
	}

	public void setZbDeviceBorrowItemList(List<ZbDeviceBorrowItem> zbDeviceBorrowItemList) {
		this.zbDeviceBorrowItemList = zbDeviceBorrowItemList;
	}

	public String getApplyName() {
		return applyName;
	}

	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}

	public String getApplyDeptName() {
		return applyDeptName;
	}

	public void setApplyDeptName(String applyDeptName) {
		this.applyDeptName = applyDeptName;
	}

	public String getBorrowName() {
		return borrowName;
	}

	public void setBorrowName(String borrowName) {
		this.borrowName = borrowName;
	}

	public String getBorrowDeptName() {
		return borrowDeptName;
	}

	public void setBorrowDeptName(String borrowDeptName) {
		this.borrowDeptName = borrowDeptName;
	}

	public String getSubmitType() {
		return submitType;
	}

	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}

	public String getApplyText() {
		return applyText;
	}

	public void setApplyText(String applyText) {
		this.applyText = applyText;
	}

	public String getApplyLeaderText() {
		return applyLeaderText;
	}

	public void setApplyLeaderText(String applyLeaderText) {
		this.applyLeaderText = applyLeaderText;
	}

	public String getBorrowText() {
		return borrowText;
	}

	public void setBorrowText(String borrowText) {
		this.borrowText = borrowText;
	}

	public String getBorrowLeaderText() {
		return borrowLeaderText;
	}

	public void setBorrowLeaderText(String borrowLeaderText) {
		this.borrowLeaderText = borrowLeaderText;
	}

	public String getBusinessText() {
		return businessText;
	}

	public void setBusinessText(String businessText) {
		this.businessText = businessText;
	}
}