/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_scrap_move.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.rd.modules.device.entity.ZbDeviceScrapMove;
import com.rd.modules.device_scrap_move.service.ZbDeviceScrapMoveService;
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
 * 报废处置移交单Controller
 * @author xuejh
 * @version 2020-06-01
 */
@Controller
@RequestMapping(value = "${adminPath}/device/device_scrap_move/zbDeviceScrapMove")
public class ZbDeviceScrapMoveController extends BaseController {

	@Autowired
	private ZbDeviceScrapMoveService zbDeviceScrapMoveService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ZbDeviceScrapMove get(String id, boolean isNewRecord) {
		return zbDeviceScrapMoveService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("device_scrap_move:zbDeviceScrapMove:view")
	@RequestMapping(value = {"list", ""})
	public String list(ZbDeviceScrapMove zbDeviceScrapMove, Model model) {
		model.addAttribute("zbDeviceScrapMove", zbDeviceScrapMove);
		return "modules/device_scrap_move/zbDeviceScrapMoveList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("device_scrap_move:zbDeviceScrapMove:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ZbDeviceScrapMove> listData(ZbDeviceScrapMove zbDeviceScrapMove, HttpServletRequest request, HttpServletResponse response) {
		zbDeviceScrapMove.setPage(new Page<>(request, response));
		Page<ZbDeviceScrapMove> page = zbDeviceScrapMoveService.findPage(zbDeviceScrapMove);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("device_scrap_move:zbDeviceScrapMove:view")
	@RequestMapping(value = "form")
	public String form(ZbDeviceScrapMove zbDeviceScrapMove, Model model) {
		zbDeviceScrapMove = zbDeviceScrapMoveService.getPageShowData(zbDeviceScrapMove);
		model.addAttribute("zbDeviceScrapMove", zbDeviceScrapMove);
		return "modules/device_scrap_move/zbDeviceScrapMoveForm";
	}

	/**
	 * 保存报废处置移交单
	 */
	@RequiresPermissions("device_scrap_move:zbDeviceScrapMove:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ZbDeviceScrapMove zbDeviceScrapMove) throws Exception {
		zbDeviceScrapMoveService.saveModel(zbDeviceScrapMove);
		return renderResult(Global.TRUE, text("保存报废处置移交单成功！"));
	}

	/**
	 * 确认完成
	 */
	@RequiresPermissions("device_scrap_move:zbDeviceScrapMove:edit")
	@PostMapping(value = "confirmOver")
	@ResponseBody
	public String confirm(@Validated ZbDeviceScrapMove zbDeviceScrapMove) throws Exception {
		zbDeviceScrapMoveService.confirm(zbDeviceScrapMove);
		return renderResult(Global.TRUE, text("确认成功！"));
	}
	
	/**
	 * 删除报废处置移交单
	 */
	@RequiresPermissions("device_scrap_move:zbDeviceScrapMove:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ZbDeviceScrapMove zbDeviceScrapMove) {
		zbDeviceScrapMoveService.delete(zbDeviceScrapMove);
		return renderResult(Global.TRUE, text("删除报废处置移交单成功！"));
	}
	
}