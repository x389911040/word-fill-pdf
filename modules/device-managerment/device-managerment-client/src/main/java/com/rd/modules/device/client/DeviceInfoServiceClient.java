package com.rd.modules.device.client;

import com.jeesite.modules.cloud.feign.condition.ConditionalOnNotCurrentApplication;
import com.rd.modules.device.api.DeviceInfoServiceApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author xuejh
 * @description 设备详情服务客户端
 * @create 2020-05-12 13:58
 **/
@FeignClient(name="${service.device.name}", path="${service.device.path}")
@ConditionalOnNotCurrentApplication(name="${service.device.name}")
public interface DeviceInfoServiceClient extends DeviceInfoServiceApi {

}
