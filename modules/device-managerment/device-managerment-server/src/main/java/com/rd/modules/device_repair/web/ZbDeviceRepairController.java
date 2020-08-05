/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_repair.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.rd.modules.device.entity.ZbDeviceRepair;
import com.rd.modules.device_repair.service.ZbDeviceRepairService;
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
 * 维修单Controller
 * @author xuejh
 * @version 2020-05-07
 */
@Controller
@RequestMapping(value = "${adminPath}/device/device_repair/zbDeviceRepair")
public class ZbDeviceRepairController extends BaseController {

	@Autowired
	private ZbDeviceRepairService zbDeviceRepairService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ZbDeviceRepair get(String id, boolean isNewRecord) {
		return zbDeviceRepairService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("device_repair:zbDeviceRepair:view")
	@RequestMapping(value = {"list", ""})
	public String list(ZbDeviceRepair zbDeviceRepair, Model model) {
		model.addAttribute("zbDeviceRepair", zbDeviceRepair);
		return "modules/device_repair/zbDeviceRepairList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("device_repair:zbDeviceRepair:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ZbDeviceRepair> listData(ZbDeviceRepair zbDeviceRepair, HttpServletRequest request, HttpServletResponse response) {
		zbDeviceRepair.setPage(new Page<>(request, response));
		Page<ZbDeviceRepair> page = zbDeviceRepairService.findPage(zbDeviceRepair);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("device_repair:zbDeviceRepair:view")
	@RequestMapping(value = "form")
	public String form(ZbDeviceRepair zbDeviceRepair, Model model) {
		zbDeviceRepair = zbDeviceRepairService.getPageShowData(zbDeviceRepair);
		model.addAttribute("zbDeviceRepair", zbDeviceRepair);
		return "modules/device_repair/zbDeviceRepairForm";
	}

	/**
	 * 保存维修单
	 */
	@RequiresPermissions("device_repair:zbDeviceRepair:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ZbDeviceRepair zbDeviceRepair) throws Exception {
		zbDeviceRepairService.saveModel(zbDeviceRepair);
		return renderResult(Global.TRUE, text("保存维修单成功！"));
	}
	
	/**
	 * 删除维修单
	 */
	@RequiresPermissions("device_repair:zbDeviceRepair:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ZbDeviceRepair zbDeviceRepair) {
		zbDeviceRepairService.delete(zbDeviceRepair);
		return renderResult(Global.TRUE, text("删除维修单成功！"));
	}
	
}