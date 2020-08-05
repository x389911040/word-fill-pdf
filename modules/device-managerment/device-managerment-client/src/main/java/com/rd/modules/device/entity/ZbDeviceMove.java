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
import java.util.Date;
import java.util.List;

/**
 * 设备移交Entity
 * @author xuejh
 * @version 2020-04-15
 */
@Table(name="zb_device_move", alias="a", columns={
		@Column(name="id", attrName="id", label="主键Id", isPK=true),
		@Column(name="bill_code", attrName="billCode", label="单据编号"),
		@Column(name="move_dept_code", attrName="moveDeptCode", label="移交部门编码"),
		@Column(name="move_code", attrName="moveCode", label="移交人编码"),
		@Column(name="receive_dept_code", attrName="receiveDeptCode", label="接收部门编码", isQuery=false),
		@Column(name="receive_code", attrName="receiveCode", label="接收人编码", isQuery=false),
		@Column(name="audit_code", attrName="auditCode", label="审核人编码", isQuery=false),
		@Column(name="audit_date", attrName="auditDate", label="审核时间", isQuery=false),
		@Column(name="create_by", attrName="createBy", label="创建人", isUpdate=false, isQuery=false),
		@Column(name="create_date", attrName="createDate", label="创建时间", isUpdate=false, isQuery=false),
		@Column(name="update_by", attrName="updateBy", label="修改人", isQuery=false),
		@Column(name="update_date", attrName="updateDate", label="修改时间", isQuery=false),
		@Column(name="remarks", attrName="remarks", label="移交原因", isQuery=false),
		@Column(name="state", attrName="state", label="状态", comment="状态；1：未审核,2：已审核"),
		@Column(name="move_text", attrName="moveText", label="移交人意见", isQuery=false),
		@Column(name="move_leader_text", attrName="moveLeaderText", label="移交领导意见", isQuery=false),
		@Column(name="receive_text", attrName="receiveText", label="接收人意见", isQuery=false),
		@Column(name="receive_leader_text", attrName="receiveLeaderText", label="接收领导意见", isQuery=false),
		@Column(name="business_text", attrName="businessText", label="业务部门意见", isQuery=false),
	},
//		joinTable = {
//			@JoinTable(
//				type = JoinTable.Type.JOIN,
//				entity = User.class,
//				alias = "u1",
//				on = "u1.user_code=a.move_code",
//				attrName = "moveCode",
//				columns = {
//						@Column(name="user_code", label="用户编号", isPK = true),
//						@Column(name="user_name", label="用户昵称", isQuery = true)
//				}
//			)
//		},
		orderBy="a.update_date DESC"
)
public class ZbDeviceMove extends DataEntity<ZbDeviceMove> {
	
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

	private String billCode;		// 单据编号
	private String moveDeptCode;		// 移交部门编号
	private String moveDeptName;		// 移交部门编号
	private String moveCode;		// 移交人编码
	private String moveName;		// 移交人名称
	private String receiveDeptCode;		// 接收部门编码
	private String receiveDeptName;		// 接收部门名称
	private String receiveCode;		// 接收人编码
	private String receiveName;		// 接收人名称
	private String auditCode;		// 审核人编码
	private String auditName;		// 审核人名称
	private Date auditDate;		// 审核时间
	private String state;		// 单据状态 0：待提交；1：未审核；2：已审核；
	private String submitType;	// 是否保存草稿
	private String moveText;
	private String moveLeaderText;
	private String receiveText;
	private String receiveLeaderText;
	private String businessText;
	private List<ZbDeviceMoveItem> zbDeviceMoveItemList = ListUtils.newArrayList();		// 子表列表
	
	public ZbDeviceMove() {
		this(null);
	}

	public ZbDeviceMove(String id){
		super(id);
	}

	@NotBlank(message="移交人编码不能为空")
	@Length(min=0, max=64, message="移交人编码长度不能超过 64 个字符")
	public String getMoveCode() {
		return moveCode;
	}

	public void setMoveCode(String moveCode) {
		this.moveCode = moveCode;
	}

	public String getMoveName() {
		return moveName;
	}

	public void setMoveName(String moveName) {
		this.moveName = moveName;
	}

	@NotBlank(message="接收部门编码不能为空")
	@Length(min=0, max=64, message="接收部门编码长度不能超过 64 个字符")
	public String getReceiveDeptCode() {
		return receiveDeptCode;
	}

	public void setReceiveDeptCode(String receiveDeptCode) {
		this.receiveDeptCode = receiveDeptCode;
	}
	
	public String getReceiveDeptName() {
		return receiveDeptName;
	}

	public void setReceiveDeptName(String receiveDeptName) {
		this.receiveDeptName = receiveDeptName;
	}

	@NotBlank(message="接收人编码不能为空")
	@Length(min=0, max=64, message="接收人编码长度不能超过 64 个字符")
	public String getReceiveCode() {
		return receiveCode;
	}

	public void setReceiveCode(String receiveCode) {
		this.receiveCode = receiveCode;
	}
	
	public String getReceiveName() {
		return receiveName;
	}

	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}

	@Length(min=0, max=64, message="审核人编码长度不能超过 64 个字符")
	public String getAuditCode() {
		return auditCode;
	}

	public void setAuditCode(String auditCode) {
		this.auditCode = auditCode;
	}
	
	@Length(min=0, max=64, message="审核人名称长度不能超过 64 个字符")
	public String getAuditName() {
		return auditName;
	}

	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}

	@NotBlank(message="单据编号不能为空")
	@Length(min=0, max=64, message="单据编号长度不能超过 64 个字符")
	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public List<ZbDeviceMoveItem> getZbDeviceMoveItemList() {
		return zbDeviceMoveItemList;
	}

	public void setZbDeviceMoveItemList(List<ZbDeviceMoveItem> zbDeviceMoveItemList) {
		this.zbDeviceMoveItemList = zbDeviceMoveItemList;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMoveDeptCode() {
		return moveDeptCode;
	}

	public void setMoveDeptCode(String moveDeptCode) {
		this.moveDeptCode = moveDeptCode;
	}

	public String getMoveDeptName() {
		return moveDeptName;
	}

	public void setMoveDeptName(String moveDeptName) {
		this.moveDeptName = moveDeptName;
	}

	public String getSubmitType() {
		return submitType;
	}

	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}

	public String getMoveText() {
		return moveText;
	}

	public void setMoveText(String moveText) {
		this.moveText = moveText;
	}

	public String getMoveLeaderText() {
		return moveLeaderText;
	}

	public void setMoveLeaderText(String moveLeaderText) {
		this.moveLeaderText = moveLeaderText;
	}

	public String getReceiveText() {
		return receiveText;
	}

	public void setReceiveText(String receiveText) {
		this.receiveText = receiveText;
	}

	public String getReceiveLeaderText() {
		return receiveLeaderText;
	}

	public void setReceiveLeaderText(String receiveLeaderText) {
		this.receiveLeaderText = receiveLeaderText;
	}

	public String getBusinessText() {
		return businessText;
	}

	public void setBusinessText(String businessText) {
		this.businessText = businessText;
	}
}