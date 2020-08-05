/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * 设备封存表Entity
 * @author xuejh
 * @version 2020-04-29
 */
@Table(name="zb_device_seal", alias="a", columns={
		@Column(name="id", attrName="id", label="主键Id", isPK=true),
		@Column(name="bill_code", attrName="billCode", label="单据编号"),
		@Column(name="seal_type", attrName="sealType", label="单据类型 1", comment="单据类型 1：封存单；2：启封单；", isQuery=false),
		@Column(name="state", attrName = "state", label = "单据状态", comment = "1：未审核; 2：已审核；"),
		@Column(name="create_by", attrName="createBy", label="创建人", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="更新人", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="更新时间", isQuery=false),
		@Column(name="audit_by", attrName="auditBy", label="审核人", isQuery=false),
		@Column(name="audit_date", attrName="auditDate", label="审核时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="备注信息", isQuery=false),
	}, orderBy="a.update_date DESC"
)
public class ZbDeviceSeal extends DataEntity<ZbDeviceSeal> {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 单据状态：待提交
	 */
	public static final String AUDIT_STATE_WAIT = "0";

	/**
	 * 状态 1：未审核
	 */
	public static final String STATE_AUDIT_N = "1";

	/**
	 * 状态 2：已审核
	 */
	public static final String STATE_AUDIT_Y = "2";

	/**
	 * 状态 1：封存单
	 */
	public static final String SEAL_TYPE_CLOSE = "1";

	/**
	 * 状态 2：启封单
	 */
	public static final String SEAL_TYPE_OPEN = "2";

	private String billCode;		// 单据编号
	private String sealType;		// 单据类型 1：封存单；2：启封单；
	private String state;		// 状态 1：未审核；2：已审核；
	private String auditBy;		// 审核人
	private Date auditDate;		// 审核时间
	private String auditByName;	// 审核人
	private String createDeptName;	// 申请单位名称
	private String submitType;	// 是否保存草稿
	private String sealIds;		// 多个Id拼接
	private List<ZbDeviceSealItem> zbDeviceSealItemList = ListUtils.newArrayList();		// 子表列表
	
	public ZbDeviceSeal() {
		this(null);
	}

	public ZbDeviceSeal(String id){
		super(id);
	}
	
	@NotBlank(message="单据编号不能为空")
	@Length(min=0, max=64, message="单据编号长度不能超过 64 个字符")
	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	
	@Length(min=0, max=64, message="审核人长度不能超过 64 个字符")
	public String getAuditBy() {
		return auditBy;
	}

	public void setAuditBy(String auditBy) {
		this.auditBy = auditBy;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	
	public List<ZbDeviceSealItem> getZbDeviceSealItemList() {
		return zbDeviceSealItemList;
	}

	public void setZbDeviceSealItemList(List<ZbDeviceSealItem> zbDeviceSealItemList) {
		this.zbDeviceSealItemList = zbDeviceSealItemList;
	}

	public String getAuditByName() {
		return auditByName;
	}

	public void setAuditByName(String auditByName) {
		this.auditByName = auditByName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSealType() {
		return sealType;
	}

	public void setSealType(String sealType) {
		this.sealType = sealType;
	}

	public String getCreateDeptName() {
		return createDeptName;
	}

	public void setCreateDeptName(String createDeptName) {
		this.createDeptName = createDeptName;
	}

	public String getSubmitType() {
		return submitType;
	}

	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}

	public String getSealIds() {
		return sealIds;
	}

	public void setSealIds(String sealIds) {
		this.sealIds = sealIds;
	}
}