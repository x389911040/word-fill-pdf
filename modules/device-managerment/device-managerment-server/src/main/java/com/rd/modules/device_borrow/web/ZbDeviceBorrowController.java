/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_borrow.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.rd.modules.device.entity.ZbDeviceAccounts;
import com.rd.modules.device_borrow.entity.ZbDeviceBorrow;
import com.rd.modules.device_borrow.service.ZbDeviceBorrowService;
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
 * 设备借用单Controller
 * @author xuejh
 * @version 2020-05-15
 */
@Controller
@RequestMapping(value = "${adminPath}/device/device_borrow/zbDeviceBorrow")
public class ZbDeviceBorrowController extends BaseController {

	@Autowired
	private ZbDeviceBorrowService zbDeviceBorrowService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ZbDeviceBorrow get(String id, boolean isNewRecord) {
		return zbDeviceBorrowService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("device_borrow:zbDeviceBorrow:view")
	@RequestMapping(value = {"list", ""})
	public String list(ZbDeviceBorrow zbDeviceBorrow, Model model) {
		model.addAttribute("zbDeviceBorrow", zbDeviceBorrow);
		return "modules/device_borrow/zbDeviceBorrowList";
	}

	/**
	 * 待归还设备列表
	 */
	@RequiresPermissions("device_borrow:zbDeviceBorrow:view")
	@RequestMapping(value = {"allList", ""})
	public String allList(ZbDeviceAccounts accounts, Model model) {
		model.addAttribute("zbDeviceBorrow", accounts);
		return "modules/device_borrow/zbDeviceAllBorrowList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("device_borrow:zbDeviceBorrow:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ZbDeviceBorrow> listData(ZbDeviceBorrow zbDeviceBorrow, HttpServletRequest request, HttpServletResponse response) {
		zbDeviceBorrow.setPage(new Page<>(request, response));
		Page<ZbDeviceBorrow> page = zbDeviceBorrowService.findPage(zbDeviceBorrow);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("device_borrow:zbDeviceBorrow:view")
	@RequestMapping(value = "form")
	public String form(ZbDeviceBorrow zbDeviceBorrow, Model model) {
		zbDeviceBorrow = zbDeviceBorrowService.getPageShowData(zbDeviceBorrow);
		model.addAttribute("zbDeviceBorrow", zbDeviceBorrow);
		return "modules/device_borrow/zbDeviceBorrowForm";
	}

	/**
	 * 审核表单
	 */
	@RequiresPermissions("device_borrow:zbDeviceBorrow:view")
	@RequestMapping(value = "audit_form")
	public String auditForm(ZbDeviceBorrow zbDeviceBorrow, Model model) {
		zbDeviceBorrow = zbDeviceBorrowService.getPageShowData(zbDeviceBorrow);
		model.addAttribute("zbDeviceBorrow", zbDeviceBorrow);
		return "modules/device_borrow/zbDeviceBorrowAuditForm";
	}

	/**
	 * 保存设备借用单
	 */
	@RequiresPermissions("device_borrow:zbDeviceBorrow:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ZbDeviceBorrow zbDeviceBorrow) throws Exception {
		zbDeviceBorrowService.saveBorrow(zbDeviceBorrow);
		return renderResult(Global.TRUE, text("保存设备借用单成功！"));
	}

	/**
	 * 审核设备借用单
	 */
	@RequiresPermissions("device_borrow:zbDeviceBorrow:edit")
	@PostMapping(value = "audit")
	@ResponseBody
	public String audit(@Validated ZbDeviceBorrow zbDeviceBorrow) throws Exception {
		zbDeviceBorrowService.audit(zbDeviceBorrow);
		return renderResult(Global.TRUE, text("审核设备借用单成功！"));
	}
	
	/**
	 * 删除设备借用单
	 */
	@RequiresPermissions("device_borrow:zbDeviceBorrow:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ZbDeviceBorrow zbDeviceBorrow) {
		zbDeviceBorrowService.delete(zbDeviceBorrow);
		return renderResult(Global.TRUE, text("删除设备借用单成功！"));
	}

	/**
	 * 借用设备归还
	 */
	@RequiresPermissions("device_borrow:zbDeviceBorrow:view")
	@RequestMapping(value = "returnBack")
	@ResponseBody
	public String returnBack(ZbDeviceAccounts accounts) throws Exception {
		zbDeviceBorrowService.returnBack(accounts);
		return renderResult(Global.TRUE, text("归还设备成功！"));
	}
}