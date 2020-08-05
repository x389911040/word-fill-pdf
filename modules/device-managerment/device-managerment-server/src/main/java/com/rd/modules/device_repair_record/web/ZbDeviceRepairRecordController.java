/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_repair_record.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.rd.modules.device.entity.ZbDeviceRepairRecord;
import com.rd.modules.device_repair_record.service.ZbDeviceRepairRecordService;
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
 * 设备维修记录表Controller
 * @author xuejh
 * @version 2020-04-20
 */
@Controller
@RequestMapping(value = "${adminPath}/device_repair_record/zbDeviceRepairRecord")
public class ZbDeviceRepairRecordController extends BaseController {

	@Autowired
	private ZbDeviceRepairRecordService zbDeviceRepairRecordService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ZbDeviceRepairRecord get(String id, boolean isNewRecord) {
		return zbDeviceRepairRecordService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("device_repair_record:zbDeviceRepairRecord:view")
	@RequestMapping(value = {"list", ""})
	public String list(ZbDeviceRepairRecord zbDeviceRepairRecord, Model model) {
		model.addAttribute("zbDeviceRepairRecord", zbDeviceRepairRecord);
		return "modules/device_repair_record/zbDeviceRepairRecordList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("device_repair_record:zbDeviceRepairRecord:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ZbDeviceRepairRecord> listData(ZbDeviceRepairRecord zbDeviceRepairRecord, HttpServletRequest request, HttpServletResponse response) {
		zbDeviceRepairRecord.setPage(new Page<>(request, response));
		Page<ZbDeviceRepairRecord> page = zbDeviceRepairRecordService.findPage(zbDeviceRepairRecord);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("device_repair_record:zbDeviceRepairRecord:view")
	@RequestMapping(value = "form")
	public String form(ZbDeviceRepairRecord zbDeviceRepairRecord, Model model) {
		zbDeviceRepairRecord = zbDeviceRepairRecordService.getPageShowData(zbDeviceRepairRecord);
		model.addAttribute("zbDeviceRepairRecord", zbDeviceRepairRecord);
		return "modules/device_repair_record/zbDeviceRepairRecordForm";
	}

	/**
	 * 保存设备维修记录表
	 */
	@RequiresPermissions("device_repair_record:zbDeviceRepairRecord:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ZbDeviceRepairRecord zbDeviceRepairRecord) throws Exception {
		zbDeviceRepairRecordService.saveModel(zbDeviceRepairRecord);
		return renderResult(Global.TRUE, text("保存设备维修记录表成功！"));
	}
	
	/**
	 * 删除设备维修记录表
	 */
	@RequiresPermissions("device_repair_record:zbDeviceRepairRecord:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ZbDeviceRepairRecord zbDeviceRepairRecord) {
		zbDeviceRepairRecordService.delete(zbDeviceRepairRecord);
		return renderResult(Global.TRUE, text("删除设备维修记录表成功！"));
	}
	
}