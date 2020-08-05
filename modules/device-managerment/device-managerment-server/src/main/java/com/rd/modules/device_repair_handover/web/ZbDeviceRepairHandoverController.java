/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_repair_handover.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.rd.modules.device.entity.ZbDeviceRepairHandover;
import com.rd.modules.device_repair_handover.service.ZbDeviceRepairHandoverService;
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
 * 设备维修交接单Controller
 * @author xuejh
 * @version 2020-04-20
 */
@Controller
@RequestMapping(value = "${adminPath}/device/device_repair_handover/zbDeviceRepairHandover")
public class ZbDeviceRepairHandoverController extends BaseController {

	@Autowired
	private ZbDeviceRepairHandoverService zbDeviceRepairHandoverService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ZbDeviceRepairHandover get(String id, boolean isNewRecord) {
		return zbDeviceRepairHandoverService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("device_repair_handover:repairHandover:view")
	@RequestMapping(value = {"list", ""})
	public String list(ZbDeviceRepairHandover zbDeviceRepairHandover, Model model) {
		model.addAttribute("zbDeviceRepairHandover", zbDeviceRepairHandover);
		return "modules/device_repair_handover/zbDeviceRepairHandoverList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("device_repair_handover:repairHandover:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ZbDeviceRepairHandover> listData(ZbDeviceRepairHandover zbDeviceRepairHandover, HttpServletRequest request, HttpServletResponse response) {
		zbDeviceRepairHandover.setPage(new Page<>(request, response));
		Page<ZbDeviceRepairHandover> page = zbDeviceRepairHandoverService.findPage(zbDeviceRepairHandover);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("device_repair_handover:repairHandover:view")
	@RequestMapping(value = "form")
	public String form(ZbDeviceRepairHandover zbDeviceRepairHandover, Model model) {
		zbDeviceRepairHandover = zbDeviceRepairHandoverService.getPageShowData(zbDeviceRepairHandover);
		model.addAttribute("zbDeviceRepairHandover", zbDeviceRepairHandover);
		return "modules/device_repair_handover/zbDeviceRepairHandoverForm";
	}

	/**
	 * 审核交接单页面
	 */
	@RequiresPermissions("device_repair_handover:repairHandover:view")
	@RequestMapping(value = "audit_form")
	public String auditForm(ZbDeviceRepairHandover zbDeviceRepairHandover, Model model) {
		zbDeviceRepairHandover = zbDeviceRepairHandoverService.getPageShowData(zbDeviceRepairHandover);
		model.addAttribute("zbDeviceRepairHandover", zbDeviceRepairHandover);
		return "modules/device_repair_handover/zbDeviceRepairHandoverAuditForm";
	}

	/**
	 * 保存设备维修交接单
	 */
	@RequiresPermissions("device_repair_handover:repairHandover:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ZbDeviceRepairHandover zbDeviceRepairHandover) throws Exception {
		zbDeviceRepairHandoverService.saveModel(zbDeviceRepairHandover);
		return renderResult(Global.TRUE, text("保存设备维修交接单成功！"));
	}

	/**
	 * 保存设备维修交接单
	 */
	@RequiresPermissions("device_repair_handover:repairHandover:edit")
	@PostMapping(value = "audit")
	@ResponseBody
	public String audit(@Validated ZbDeviceRepairHandover zbDeviceRepairHandover) throws Exception {
		zbDeviceRepairHandoverService.audit(zbDeviceRepairHandover);
		return renderResult(Global.TRUE, text("审核设备维修交接单成功！"));
	}

	/**
	 * 下载交接单
	 * @author xuejh
	 * @create 2020/5/7 18:07
	 * @param repairHandover
	 * @return void
	 */
	@PostMapping(value = "downloadBill")
	public void downloadBill(ZbDeviceRepairHandover repairHandover) {
		// 获取交接单详情
		
	}
}