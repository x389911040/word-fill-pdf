/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_seal.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.rd.modules.device.entity.ZbDeviceSeal;
import com.rd.modules.device_seal.service.ZbDeviceSealService;
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
 * 设备封存表Controller
 * @author xuejh
 * @version 2020-04-29
 */
@Controller
@RequestMapping(value = "${adminPath}/device/device_seal/zbDeviceSeal")
public class ZbDeviceSealController extends BaseController {

	@Autowired
	private ZbDeviceSealService zbDeviceSealService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ZbDeviceSeal get(String id, boolean isNewRecord) {
		return zbDeviceSealService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("device_seal:zbDeviceSeal:view")
	@RequestMapping(value = {"list", ""})
	public String list(ZbDeviceSeal zbDeviceSeal, Model model) {
		model.addAttribute("zbDeviceSeal", zbDeviceSeal);
		return "modules/device_seal/zbDeviceSealList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("device_seal:zbDeviceSeal:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ZbDeviceSeal> listData(ZbDeviceSeal zbDeviceSeal, HttpServletRequest request, HttpServletResponse response) {
		zbDeviceSeal.setPage(new Page<>(request, response));
		zbDeviceSeal.setSealType(ZbDeviceSeal.SEAL_TYPE_CLOSE);
		Page<ZbDeviceSeal> page = zbDeviceSealService.findPage(zbDeviceSeal);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("device_seal:zbDeviceSeal:view")
	@RequestMapping(value = "form")
	public String form(ZbDeviceSeal zbDeviceSeal, Model model) {
		zbDeviceSeal = zbDeviceSealService.getPageShowData(zbDeviceSeal);
		model.addAttribute("zbDeviceSeal", zbDeviceSeal);
		return "modules/device_seal/zbDeviceSealForm";
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("device_seal:zbDeviceSeal:view")
	@RequestMapping(value = "audit_form")
	public String auditForm(ZbDeviceSeal zbDeviceSeal, Model model) {
		zbDeviceSeal = zbDeviceSealService.getPageShowData(zbDeviceSeal);
		model.addAttribute("zbDeviceSeal", zbDeviceSeal);
		return "modules/device_seal/zbDeviceSealAuditForm";
	}

	/**
	 * 保存设备封存
	 */
	@RequiresPermissions("device_seal:zbDeviceSeal:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ZbDeviceSeal zbDeviceSeal) throws Exception {
		// 单据类型为封存单
		zbDeviceSeal.setSealType(ZbDeviceSeal.SEAL_TYPE_CLOSE);
		zbDeviceSealService.saveModel(zbDeviceSeal);
		return renderResult(Global.TRUE, text("保存设备封存成功！"));
	}
	
	/**
	 * 删除设备封存
	 */
	@RequiresPermissions("device_seal:zbDeviceSeal:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ZbDeviceSeal zbDeviceSeal) throws Exception {
		zbDeviceSealService.deleteModel(zbDeviceSeal);
		return renderResult(Global.TRUE, text("删除设备封存成功！"));
	}

	/**
	 * 审核设备封存
	 */
	@RequiresPermissions("device_seal:zbDeviceSeal:edit")
	@RequestMapping(value = "audit")
	@ResponseBody
	public String audit(ZbDeviceSeal zbDeviceSeal) throws Exception {
		zbDeviceSealService.audit(zbDeviceSeal);
		return renderResult(Global.TRUE, text("审核单据成功！"));
	}
}