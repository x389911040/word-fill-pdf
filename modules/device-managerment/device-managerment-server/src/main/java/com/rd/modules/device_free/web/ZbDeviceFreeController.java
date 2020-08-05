/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.rd.modules.device_free.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.web.BaseController;
import com.rd.modules.common.utils.ExportExcelUtils;
import com.rd.modules.device.entity.ZbDeviceAccounts;
import com.rd.modules.device.entity.ZbDeviceFree;
import com.rd.modules.device_accounts.service.ZbDeviceAccountsService;
import com.rd.modules.device_free.service.ZbDeviceFreeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 设备闲置申请Controller
 * @author xuejh
 * @version 2020-05-06
 */
@Controller
@RequestMapping(value = "${adminPath}/device/device_free/zbDeviceFree")
public class ZbDeviceFreeController extends BaseController {

	@Autowired
	private ZbDeviceFreeService zbDeviceFreeService;

	@Autowired
	private ZbDeviceAccountsService accountsService;

//	@Autowired
//	private DeviceInfoServiceClient deviceInfoServiceClient;

	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ZbDeviceFree get(String id, boolean isNewRecord) {
		return zbDeviceFreeService.get(id, isNewRecord);
	}

	/**
	 * 查询全院闲置设备列表
	 */
	@RequiresPermissions("device_free:zbDeviceFree:view")
	@RequestMapping(value = {"allList", ""})
	public String allList(ZbDeviceAccounts deviceAccounts, Model model) {
		model.addAttribute("ZbDeviceAccounts", deviceAccounts);
		return "modules/device_free/zbDeviceAllFreeList";
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("device_free:zbDeviceFree:view")
	@RequestMapping(value = {"list", ""})
	public String list(ZbDeviceFree zbDeviceFree, Model model) {
		model.addAttribute("zbDeviceFree", zbDeviceFree);
		return "modules/device_free/zbDeviceFreeList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("device_free:zbDeviceFree:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ZbDeviceFree> listData(ZbDeviceFree zbDeviceFree, HttpServletRequest request, HttpServletResponse response) {
		zbDeviceFree.setPage(new Page<>(request, response));
		Page<ZbDeviceFree> page = zbDeviceFreeService.findPage(zbDeviceFree);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("device_free:zbDeviceFree:view")
	@RequestMapping(value = "form")
	public String form(ZbDeviceFree zbDeviceFree, Model model) {
		zbDeviceFree = zbDeviceFreeService.getPageShowData(zbDeviceFree);
		model.addAttribute("zbDeviceFree", zbDeviceFree);
		return "modules/device_free/zbDeviceFreeForm";
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("device_free:zbDeviceFree:view")
	@RequestMapping(value = "audit_form")
	public String auditForm(ZbDeviceFree zbDeviceFree, Model model) {
		zbDeviceFree = zbDeviceFreeService.getPageShowData(zbDeviceFree);
		model.addAttribute("zbDeviceFree", zbDeviceFree);
		return "modules/device_free/zbDeviceFreeAuditForm";
	}

	/**
	 * 保存设备闲置申请
	 */
	@RequiresPermissions("device_free:zbDeviceFree:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ZbDeviceFree zbDeviceFree) throws Exception {
		zbDeviceFreeService.saveModel(zbDeviceFree);
		return renderResult(Global.TRUE, text("保存设备闲置申请成功！"));
	}
	
	/**
	 * 删除设备闲置申请
	 */
	@RequiresPermissions("device_free:zbDeviceFree:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ZbDeviceFree zbDeviceFree) throws Exception {
		zbDeviceFreeService.deleteModel(zbDeviceFree);
		return renderResult(Global.TRUE, text("删除设备闲置申请成功！"));
	}

	/**
	 * 审核设备闲置申请
	 */
	@RequiresPermissions("device_free:zbDeviceFree:edit")
	@RequestMapping(value = "audit")
	@ResponseBody
	public String audit(ZbDeviceFree zbDeviceFree) throws Exception {
		zbDeviceFreeService.audit(zbDeviceFree);
		return renderResult(Global.TRUE, text("审核单据成功！"));
	}

	/**
	 * 查看编辑表单
	 */
//	@RequiresPermissions("device_free:zbDeviceFree:view")
//	@RequestMapping(value = "findPageDeviceData")
//	@ResponseBody
//	public Page<ZbDevice> findPageDeviceData(ZbDeviceFree zbDeviceFree, Model model) {
//		return deviceInfoServiceClient.findPageDeviceData(new ZbDevice());
//	}

	/**
	 * 导出列表
	 * @author xuejh
	 * @create 2020/5/28 11:26
	 * @param accounts
	 * @param model
	 * @return java.lang.String
	 */
	@RequiresPermissions("device_free:zbDeviceFree:view")
	@RequestMapping(value = "exportList")
	public void exportList(ZbDeviceAccounts accounts, Model model, HttpServletResponse response) throws Exception {
		// 默认导出所有数据
		Page page = new Page(1, Integer.MAX_VALUE, 0);
		accounts.setPage(page);

		// 查询数据
		Page<ZbDeviceAccounts> result = accountsService.findPage(accounts);

		// 导出excel相关参数
		String fileName = "全院闲置设备";
		String title = "全院闲置设备";
		String time = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
		String exportHead = "[{\"field\":\"sequence\", \"name\":\"序号\"},{\"field\":\"accountsCode\",\"name\":\"设备编码\"},{\"field\":\"deviceName\",\"name\":\"设备名称\"},{\"field\":\"brand\", \"name\":\"品牌\"}" +
				",{\"field\":\"spec\",\"name\":\"规格\"},{\"field\":\"ownerOfficeName\", \"name\":\"归属部门\"},{\"field\":\"ownerName\",\"name\":\"责任人\"},{\"field\":\"createDate\", \"name\":\"创建日期\"},{\"field\":\"zb_device_state\", \"name\":\"状态\"}]";

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition",
				"attachment;filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "-" + time + ".xlsx\"");

		// 获取输出流
		ServletOutputStream os = response.getOutputStream();

		// 合计行数据组装
		Map<String, Object> totalData = new HashMap<>();
		JSONArray dataArray = new JSONArray();

		if (result != null && result.getList().size() > 0) {
			// 排除itemList属性和其他值为null的属性
			SimplePropertyPreFilter filter = new SimplePropertyPreFilter(
					"accountsCode", "deviceName" , "brand" , "spec", "ownerOfficeName", "ownerName", "createDate", "state");//会解析写入的属性名称
			String jsonString = JSON.toJSONString(result.getList() ,filter , SerializerFeature.DisableCircularReferenceDetect);
			dataArray = JSONArray.parseArray(jsonString);
		}

		ExportExcelUtils.exportExcelLargeData(title, exportHead, dataArray, totalData, page, os);
	}
}