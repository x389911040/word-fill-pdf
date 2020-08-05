/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_scrap_plan.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.rd.modules.device.entity.ZbDeviceAccounts;
import com.rd.modules.device.entity.ZbDeviceScrapPlan;
import com.rd.modules.device_scrap_plan.service.ZbDeviceScrapPlanService;
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
 * 设备报废计划Controller
 * @author xuejh
 * @version 2020-06-01
 */
@Controller
@RequestMapping(value = "${adminPath}/device/device_scrap_plan/zbDeviceScrapPlan")
public class ZbDeviceScrapPlanController extends BaseController {

	@Autowired
	private ZbDeviceScrapPlanService zbDeviceScrapPlanService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ZbDeviceScrapPlan get(String id, boolean isNewRecord) {
		return zbDeviceScrapPlanService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("device_scrap_plan:zbDeviceScrapPlan:view")
	@RequestMapping(value = {"list", ""})
	public String list(ZbDeviceScrapPlan zbDeviceScrapPlan, Model model) {
		model.addAttribute("zbDeviceScrapPlan", zbDeviceScrapPlan);
		return "modules/device_scrap_plan/zbDeviceScrapPlanList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("device_scrap_plan:zbDeviceScrapPlan:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ZbDeviceScrapPlan> listData(ZbDeviceScrapPlan zbDeviceScrapPlan, HttpServletRequest request, HttpServletResponse response) {
		zbDeviceScrapPlan.setPage(new Page<>(request, response));
		Page<ZbDeviceScrapPlan> page = zbDeviceScrapPlanService.findPage(zbDeviceScrapPlan);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("device_scrap_plan:zbDeviceScrapPlan:view")
	@RequestMapping(value = "form")
	public String form(ZbDeviceScrapPlan zbDeviceScrapPlan, Model model) {
		zbDeviceScrapPlan = zbDeviceScrapPlanService.getPageShowData(zbDeviceScrapPlan);
		model.addAttribute("zbDeviceScrapPlan", zbDeviceScrapPlan);
		return "modules/device_scrap_plan/zbDeviceScrapPlanForm";
	}

	/**
	 * 审核表单
	 */
	@RequiresPermissions("device_scrap_plan:zbDeviceScrapPlan:edit")
	@RequestMapping(value = "audit_form")
	public String auditForm(ZbDeviceScrapPlan zbDeviceScrapPlan, Model model) {
		zbDeviceScrapPlan = zbDeviceScrapPlanService.getPageShowData(zbDeviceScrapPlan);
		model.addAttribute("zbDeviceScrapPlan", zbDeviceScrapPlan);
		return "modules/device_scrap_plan/zbDeviceScrapPlanAuditForm";
	}

	/**
	 * 审核表单
	 */
	@RequiresPermissions("device_scrap_plan:zbDeviceScrapPlan:view")
	@RequestMapping(value = "scrapList")
	public String scrapList(ZbDeviceAccounts accounts, Model model) {
		model.addAttribute("zbDeviceAccounts", accounts);
		return "modules/device_scrap_plan/zbDeviceAccountsScrapList";
	}

	/**
	 * 保存设备报废计划
	 */
	@RequiresPermissions("device_scrap_plan:zbDeviceScrapPlan:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ZbDeviceScrapPlan zbDeviceScrapPlan) throws Exception {
		zbDeviceScrapPlanService.saveModel(zbDeviceScrapPlan);
		return renderResult(Global.TRUE, text("保存设备报废计划成功！"));
	}

	/**
	 * 审核设备报废计划
	 */
	@RequiresPermissions("device_scrap_plan:zbDeviceScrapPlan:edit")
	@PostMapping(value = "audit")
	@ResponseBody
	public String audit(@Validated ZbDeviceScrapPlan zbDeviceScrapPlan) throws Exception {
		zbDeviceScrapPlanService.audit(zbDeviceScrapPlan);
		return renderResult(Global.TRUE, text("审核设备报废计划成功！"));
	}
	
	/**
	 * 删除设备报废计划
	 */
	@RequiresPermissions("device_scrap_plan:zbDeviceScrapPlan:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ZbDeviceScrapPlan zbDeviceScrapPlan) {
		zbDeviceScrapPlanService.delete(zbDeviceScrapPlan);
		return renderResult(Global.TRUE, text("删除设备报废计划成功！"));
	}
	
}