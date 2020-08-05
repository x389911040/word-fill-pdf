/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_repair_apply.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.rd.modules.device.entity.ZbDeviceRepairApply;
import com.rd.modules.device_repair_apply.service.ZbDeviceRepairApplyService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 设备维修申请单Controller
 * @author xuejh
 * @version 2020-04-20
 */
@Controller
@RequestMapping(value = "${adminPath}/device/device_repair_apply/zbDeviceRepairApply")
public class ZbDeviceRepairApplyController extends BaseController {

	@Autowired
	private ZbDeviceRepairApplyService zbDeviceRepairApplyService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ZbDeviceRepairApply get(String id, boolean isNewRecord) {
		return zbDeviceRepairApplyService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("device_repair_apply:zbDeviceRepairApply:view")
	@RequestMapping(value = {"list", ""})
	public String list(ZbDeviceRepairApply zbDeviceRepairApply, Model model) {
		model.addAttribute("zbDeviceRepairApply", zbDeviceRepairApply);
		return "modules/device_repair_apply/zbDeviceRepairApplyList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("device_repair_apply:zbDeviceRepairApply:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ZbDeviceRepairApply> listData(ZbDeviceRepairApply zbDeviceRepairApply, HttpServletRequest request, HttpServletResponse response) {
		zbDeviceRepairApply.setPage(new Page<>(request, response));
		Page<ZbDeviceRepairApply> page = zbDeviceRepairApplyService.findPage(zbDeviceRepairApply);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("device_repair_apply:zbDeviceRepairApply:view")
	@RequestMapping(value = "form")
	public String form(ZbDeviceRepairApply zbDeviceRepairApply, Model model) {
		zbDeviceRepairApply = zbDeviceRepairApplyService.getPageShowData(zbDeviceRepairApply);
		model.addAttribute("zbDeviceRepairApply", zbDeviceRepairApply);
		return "modules/device_repair_apply/zbDeviceRepairApplyForm";
	}

	/**
	 * 维修申请单审核页面
	 * @author xuejh
	 * @create 2020/4/21 9:32
	 * @param model
	 * @return java.lang.String
	 */
	@RequiresPermissions("device_repair_apply:zbDeviceRepairApply:view")
	@RequestMapping(value = "audit_form")
	public String auditForm(ZbDeviceRepairApply zbDeviceRepairApply, Model model) {
		zbDeviceRepairApply = zbDeviceRepairApplyService.getPageShowData(zbDeviceRepairApply);
		model.addAttribute("zbDeviceRepairApply", zbDeviceRepairApply);
		return "modules/device_repair_apply/zbDeviceRepairApplyAuditForm";
	}

	/**
	 * 保存设备维修申请单
	 */
	@RequiresPermissions("device_repair_apply:zbDeviceRepairApply:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ZbDeviceRepairApply zbDeviceRepairApply) throws Exception {
		zbDeviceRepairApplyService.saveModel(zbDeviceRepairApply);
		return renderResult(Global.TRUE, text("保存设备维修申请单成功！"));
	}
	
	/**
	 * 删除设备维修申请单
	 */
	@RequiresPermissions("device_repair_apply:zbDeviceRepairApply:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ZbDeviceRepairApply zbDeviceRepairApply) throws Exception {
		zbDeviceRepairApplyService.deleteModel(zbDeviceRepairApply);
		return renderResult(Global.TRUE, text("删除设备维修申请单成功！"));
	}

	/**
	 * 审核设备维修申请单
	 * @author xuejh
	 * @create 2020/4/21 9:40
	 * @param zbDeviceRepairApply
	 * @return java.lang.String
	 */
	@RequiresPermissions("device_repair_apply:zbDeviceRepairApply:edit")
	@RequestMapping(value = "audit")
	@ResponseBody
	public String audit(ZbDeviceRepairApply zbDeviceRepairApply) throws Exception {
		zbDeviceRepairApplyService.audit(zbDeviceRepairApply);
		return renderResult(Global.TRUE, text("审核设备维修申请单成功！"));
	}
	
}