/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_move.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.rd.modules.device.entity.ZbDeviceAccounts;
import com.rd.modules.device.entity.ZbDeviceMove;
import com.rd.modules.device.entity.ZbDeviceMoveItem;
import com.rd.modules.device.entity.ZbDeviceMoveRecord;
import com.rd.modules.device_move.service.ZbDeviceMoveService;
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
import java.util.Arrays;
import java.util.List;

/**
 * 设备移交Controller
 * @author xuejh
 * @version 2020-04-15
 */
@Controller
@RequestMapping(value = "${adminPath}/device/device_move/zbDeviceMove")
public class ZbDeviceMoveController extends BaseController {

	@Autowired
	private ZbDeviceMoveService zbDeviceMoveService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ZbDeviceMove get(String id, boolean isNewRecord) {
		return zbDeviceMoveService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("device_move:zbDeviceMove:view")
	@RequestMapping(value = {"list", ""})
	public String list(ZbDeviceMove zbDeviceMove, Model model) {
		model.addAttribute("zbDeviceMove", zbDeviceMove);
		return "modules/device_move/zbDeviceMoveList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("device_move:zbDeviceMove:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ZbDeviceMove> listData(ZbDeviceMove zbDeviceMove, HttpServletRequest request, HttpServletResponse response) {
		zbDeviceMove.setPage(new Page<>(request, response));
		Page<ZbDeviceMove> page = zbDeviceMoveService.findPage(zbDeviceMove);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("device_move:zbDeviceMove:view")
	@RequestMapping(value = "form")
	public String form(HttpServletRequest request, ZbDeviceMove zbDeviceMove, Model model) throws Exception {
		zbDeviceMove = zbDeviceMoveService.getPageShowData(zbDeviceMove);

		// 设备台账页面快速跳转请求
		String deviceAccountsId = request.getParameter("deviceAccountsId");
		String deviceAccountsCode = request.getParameter("deviceAccountsCode");
		String deviceName = request.getParameter("deviceName");
		if (StringUtils.isNotBlank(deviceAccountsCode) && StringUtils.isNotBlank(deviceName)) {
			ZbDeviceMoveItem item = new ZbDeviceMoveItem();
			item.setDeviceAccountsId(deviceAccountsId);
			item.setAccountsCode(deviceAccountsCode);
			item.setDeviceName(deviceName);
			List<ZbDeviceMoveItem> items = Arrays.asList(new ZbDeviceMoveItem[]{item});
			zbDeviceMove.setZbDeviceMoveItemList(items);
		}

		model.addAttribute("zbDeviceMove", zbDeviceMove);
		return "modules/device_move/zbDeviceMoveForm";
	}

	/**
	 * 审核移交单页面跳转
	 * @author xuejh
	 * @create 2020/4/17 10:47
	 * @param zbDeviceMove
	 * @param model
	 * @return java.lang.String
	 */
	@RequiresPermissions("device_move:zbDeviceMove:view")
	@RequestMapping(value = "audit_form")
	public String auditForm(ZbDeviceMove zbDeviceMove, Model model) throws Exception {
		zbDeviceMove = zbDeviceMoveService.getPageShowData(zbDeviceMove);
		model.addAttribute("zbDeviceMove", zbDeviceMove);
		return "modules/device_move/zbDeviceMoveAuditForm";
	}

	/**
	 * 保存设备移交
	 */
	@RequiresPermissions("device_move:zbDeviceMove:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ZbDeviceMove zbDeviceMove) throws Exception {
		zbDeviceMoveService.saveModel(zbDeviceMove);
		return renderResult(Global.TRUE, text("保存设备移交成功！"));
	}
	
	/**
	 * 删除设备移交
	 */
	@RequiresPermissions("device_move:zbDeviceMove:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ZbDeviceMove zbDeviceMove) throws Exception {
		zbDeviceMoveService.deleteMoveOrder(zbDeviceMove);
		return renderResult(Global.TRUE, text("删除设备移交成功！"));
	}

	/**
	 * 移交单审核
	 * @author xuejh
	 * @create 2020/4/17 9:19
	 * @param zbDeviceMove
	 * @return java.lang.String
	 */
	@RequiresPermissions("device_move:zbDeviceMove:edit")
	@PostMapping(value = "audit")
	@ResponseBody
	public String audit(@Validated ZbDeviceMove zbDeviceMove) throws Exception {
		zbDeviceMoveService.audit(zbDeviceMove);
		return renderResult(Global.TRUE, text("审核成功！"));
	}

	/**
	 * 根据台账Id,查询设备移交记录
	 * @author xuejh
	 * @create 2020/4/29 15:08
	 * @param deviceAccounts
	 * @return java.util.List<com.rd.modules.device.entity.ZbDeviceMoveRecord>
	 */
	@PostMapping(value = "move_record_list")
	@ResponseBody
	public Page<ZbDeviceMoveRecord> findMoveRecordPage(ZbDeviceAccounts deviceAccounts, HttpServletRequest request, HttpServletResponse response) {
		deviceAccounts.setPage(new Page<>(request, response));
		Page<ZbDeviceMoveRecord> page = zbDeviceMoveService.findMoveRecordPage(deviceAccounts);
		return page;
	}
}