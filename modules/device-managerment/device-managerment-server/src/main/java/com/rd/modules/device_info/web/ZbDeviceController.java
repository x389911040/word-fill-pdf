package com.rd.modules.device_info.web;

import com.jeesite.common.entity.Page;
import com.rd.modules.device.entity.ZbDevice;
import com.rd.modules.device_info.service.ZbDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author xuejh
 * @description 设备详情控制类
 * @create 2020-05-12 13:39
 **/
@Controller
@RequestMapping(value = "${adminPath}/device/zbDevice")
public class ZbDeviceController {

    @Autowired
    private ZbDeviceService deviceService;

	@RequestMapping(value = "findPageDeviceData")
	@ResponseBody
	public Page<ZbDevice> findPageDeviceData(ZbDevice zbDevice, Model model) {
		return deviceService.findPageDeviceData(new ZbDevice());
	}
}
