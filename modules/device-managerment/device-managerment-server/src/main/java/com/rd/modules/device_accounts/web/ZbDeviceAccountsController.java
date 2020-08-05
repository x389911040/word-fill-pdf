/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_accounts.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.rd.modules.device.entity.ZbDeviceAccounts;
import com.rd.modules.device_accounts.service.ZbDeviceAccountsService;
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
 * 设备登记Controller
 * @author xuejh
 * @version 2020-04-15
 */
@Controller
@RequestMapping(value = "${adminPath}/device/device_accounts/zbDeviceAccounts")
public class ZbDeviceAccountsController extends BaseController {

	@Autowired
	private ZbDeviceAccountsService deviceAccountsService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ZbDeviceAccounts get(String id, boolean isNewRecord) {
		return deviceAccountsService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("device_accounts:zbDeviceAccounts:view")
	@RequestMapping(value = {"list", ""})
	public String list(ZbDeviceAccounts zbDeviceAccounts, Model model) {
		model.addAttribute("zbDevice", zbDeviceAccounts);
		return "modules/device_accounts/zbDeviceAccountsList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("device_accounts:zbDeviceAccounts:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ZbDeviceAccounts> listData(ZbDeviceAccounts zbDeviceAccounts, HttpServletRequest request, HttpServletResponse response) {
		zbDeviceAccounts.setPage(new Page<>(request, response));
		Page<ZbDeviceAccounts> page = deviceAccountsService.findPage(zbDeviceAccounts);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("device_accounts:zbDeviceAccounts:view")
	@RequestMapping(value = "form")
	public String form(ZbDeviceAccounts zbDeviceAccounts, Model model) throws Exception {
		zbDeviceAccounts = deviceAccountsService.getPageShowData(zbDeviceAccounts);
		model.addAttribute("zbDevice", zbDeviceAccounts);
		return "modules/device_accounts/zbDeviceAccountsForm";
	}

	/**
	 * 保存设备登记
	 */
	@RequiresPermissions("device_accounts:zbDeviceAccounts:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ZbDeviceAccounts zbDeviceAccounts) throws Exception {
		deviceAccountsService.saveAccounts(zbDeviceAccounts, 1, null);
		return renderResult(Global.TRUE, text("保存设备登记成功！"));
	}
	
	/**
	 * 删除设备登记
	 */
	@RequiresPermissions("device_accounts:zbDeviceAccounts:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ZbDeviceAccounts zbDeviceAccounts) {
		deviceAccountsService.delete(zbDeviceAccounts);
		return renderResult(Global.TRUE, text("废弃台账成功！"));
	}
	
}