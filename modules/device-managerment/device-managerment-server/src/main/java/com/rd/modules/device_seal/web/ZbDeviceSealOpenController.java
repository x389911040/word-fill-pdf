package com.rd.modules.device_seal.web; /**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.utils.word.WordExport;
import com.jeesite.common.web.BaseController;
import com.rd.modules.common.convert.context.DocContext;
import com.rd.modules.common.convert.entity.DocParams;
import com.rd.modules.common.convert.strategyImpls.Doc2PdfStrategyImpl;
import com.rd.modules.device.common.DeviceUtils;
import com.rd.modules.device.entity.ZbDeviceSeal;
import com.rd.modules.device_seal.service.ZbDeviceSealService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备启封表Controller
 * @author xuejh
 * @version 2020-04-29
 */
@Controller
@RequestMapping(value = "${adminPath}/device/device_seal_open/zbDeviceSeal")
@Log4j2
public class ZbDeviceSealOpenController extends BaseController {

	@Autowired
	private ZbDeviceSealService zbDeviceSealService;

	@Autowired
	private Doc2PdfStrategyImpl docToPdf;
	
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
	@RequiresPermissions("device_seal_open:zbDeviceSeal:view")
	@RequestMapping(value = {"list", ""})
	public String list(ZbDeviceSeal zbDeviceSeal, Model model) {
		model.addAttribute("zbDeviceSeal", zbDeviceSeal);
		return "modules/device_seal_open/zbDeviceSealList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("device_seal_open:zbDeviceSeal:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ZbDeviceSeal> listData(ZbDeviceSeal zbDeviceSeal, HttpServletRequest request, HttpServletResponse response) {
		zbDeviceSeal.setPage(new Page<>(request, response));
		zbDeviceSeal.setSealType(ZbDeviceSeal.SEAL_TYPE_OPEN);
		Page<ZbDeviceSeal> page = zbDeviceSealService.findPage(zbDeviceSeal);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("device_seal_open:zbDeviceSeal:view")
	@RequestMapping(value = "form")
	public String form(ZbDeviceSeal zbDeviceSeal, Model model) {
		zbDeviceSeal = zbDeviceSealService.getPageShowData(zbDeviceSeal);
		model.addAttribute("zbDeviceSeal", zbDeviceSeal);
		return "modules/device_seal_open/zbDeviceSealForm";
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("device_seal_open:zbDeviceSeal:view")
	@RequestMapping(value = "audit_form")
	public String auditForm(ZbDeviceSeal zbDeviceSeal, Model model) {
		zbDeviceSeal = zbDeviceSealService.getPageShowData(zbDeviceSeal);
		model.addAttribute("zbDeviceSeal", zbDeviceSeal);
		return "modules/device_seal_open/zbDeviceSealAuditForm";
	}

	/**
	 * 保存设备启封
	 */
	@RequiresPermissions("device_seal_open:zbDeviceSeal:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ZbDeviceSeal zbDeviceSeal) throws Exception {
		// 单据类型为启封单
		zbDeviceSeal.setSealType(ZbDeviceSeal.SEAL_TYPE_OPEN);
		zbDeviceSealService.saveModel(zbDeviceSeal);
		return renderResult(Global.TRUE, text("保存设备启封成功！"));
	}
	
	/**
	 * 删除设备启封
	 */
	@RequiresPermissions("device_seal_open:zbDeviceSeal:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ZbDeviceSeal zbDeviceSeal) throws Exception {
		zbDeviceSealService.deleteModel(zbDeviceSeal);
		return renderResult(Global.TRUE, text("删除设备启封成功！"));
	}

	/**
	 * 审核设备启封
	 */
	@RequiresPermissions("device_seal_open:zbDeviceSeal:edit")
	@RequestMapping(value = "audit")
	@ResponseBody
	public String audit(ZbDeviceSeal zbDeviceSeal) throws Exception {
		zbDeviceSealService.audit(zbDeviceSeal);
		return renderResult(Global.TRUE, text("审核单据成功！"));
	}

	/**
	 * 打印单据详情数据
	 */
	@RequiresPermissions("device_seal_open:zbDeviceSeal:view")
	@RequestMapping(value = "printListData")
	@ResponseBody
	public List<ZbDeviceSeal> printListData(ZbDeviceSeal zbDeviceSeal) {
		List<ZbDeviceSeal> printList = ListUtils.newArrayList();

		String[] idsArr = DeviceUtils.getIdsArrayByStr(zbDeviceSeal.getSealIds());
		if (ArrayUtils.isNotEmpty(idsArr)) {
			for (String id : idsArr) {
				ZbDeviceSeal deviceSeal = zbDeviceSealService.get(id);
				if (deviceSeal != null) {
					deviceSeal = zbDeviceSealService.getPageShowData(deviceSeal);
					printList.add(deviceSeal);
				}
			}
		}

		return printList;
	}

	@RequestMapping("fillValToWordTemplate")
	@ResponseBody
	public String fillValToWordTemplate(ZbDeviceSeal zbDeviceSeal) throws Exception {
		// 新建Word模板对象
		WordExport wordExport = new WordExport();
//		wordExport.setTemplate("C:\\Users\\xjh95\\Desktop\\设备启封单无表格模板.docx");
		String rootPath = ZbDeviceSealOpenController.class.getClassLoader().getResource("static/download/doc-template").toString().substring(6);
		String templatePath = rootPath + "/设备启封单无表格模板.docx";

		log.info("============读取的word模板位置================>" + templatePath);
		System.out.println(templatePath);
		wordExport.setTemplate(templatePath);

		// 获取填充键值对map
		Map<String, String> wordValMap = new HashMap<>();
		Field[] declaredFields = zbDeviceSeal.getClass().getDeclaredFields();
		if (ArrayUtils.isNotEmpty(declaredFields)) {
			for (Field field : declaredFields) {
				// 设置允许访问私有化
				field.setAccessible(true);
				String fieldName = field.getName();
				Object fieldVal = field.get(zbDeviceSeal);

				if (fieldVal != null) {
					if (StringUtils.isNotBlank(fieldVal+"")) {
						wordValMap.put(fieldName, fieldVal+"");
					}
				}
			}
		}

		wordValMap.put("createByName", zbDeviceSeal.getCreateByName());
		wordValMap.put("remarks", zbDeviceSeal.getRemarks());

		// 填充模板
		wordExport.replaceBookMark(wordValMap);

//		List<Map<String, String>> tableItems = new ArrayList<>();
//		Map<String, String> row1 = new HashMap<>();
//		row1.put("设备编码", "设备编码");
//		row1.put("设备名称", "设备名称");
//		row1.put("型号", "型号");
//		row1.put("规格", "规格");
//
//		Map<String, String> row2 = new HashMap<>();
//		row2.put("设备编码", "115566");
//		row2.put("设备名称", "切割机");
//		row2.put("型号", "AK-377");
//		row2.put("规格", "台");
//
//		Map<String, String> row3 = new HashMap<>();
//		row3.put("设备编码", "115577");
//		row3.put("设备名称", "铲车");
//		row3.put("型号", "CD-288");
//		row3.put("规格", "辆");
//
//		tableItems.add(row1);
//		tableItems.add(row2);
//		tableItems.add(row3);
//
//		// 填充列表
//		wordExport.fillTableAtBookMark("tableItems", tableItems);

		// 写出Word
		String fillValWordPath = "D:\\project-file\\userfiles\\fileupload\\202008\\小薛测试.docx";
		wordExport.saveAs(fillValWordPath);
		log.info("============填充值后写出的word模板位置================>" + fillValWordPath);

		// 将word转为pdf
		long start = System.currentTimeMillis();

		DocParams docParams = new DocParams();
		docParams.setFilepath(fillValWordPath);
//
		DocContext context = new DocContext(docToPdf);

//        FileInputStream inputStream = new FileInputStream(docPath + "房屋租赁合同.docx");
//        FileOutputStream outputStream = new FileOutputStream("D://my-pdf/new.pdf");
//        DocxToPDFConverter docxToPDFConverter = new DocxToPDFConverter(inputStream, outputStream, true, true);
//        docxToPDFConverter.convert();

		String pdfFilePath = context.docConvert(docParams);
		log.info("============转换后的PDF文件存放位置================>" + pdfFilePath);

		if (StringUtils.isNotBlank(pdfFilePath)) {
			String[] split = pdfFilePath.split("/");
			pdfFilePath = split[split.length-1];

//			pdfFilePath = pdfFilePath.substring(pdfFilePath.length()-21, pdfFilePath.length());
		}

		long end = System.currentTimeMillis();
		log.info("============转换PDF文件耗时================>" + (end - start) /1000.0 +"秒");

		return renderResult(Global.TRUE, text(pdfFilePath));
	}
}