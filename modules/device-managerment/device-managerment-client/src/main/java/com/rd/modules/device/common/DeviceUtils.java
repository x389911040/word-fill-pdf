package com.rd.modules.device.common;

import com.jeesite.modules.sys.entity.Company;
import com.jeesite.modules.sys.entity.Employee;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.utils.EmpUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.jsoup.helper.StringUtil;

/**
 * @author xuejh
 * @description 设备模块共用工具类
 * @create 2020-05-11 9:46
 **/
public class DeviceUtils {

    /**
     * 根据用户获取用户归属单位信息
     * @author xuejh
     * @create 2020/5/7 14:37
     * @param user
     * @return java.lang.String
     */
    public static String getCreateDeptName(User user) {
        Employee employee = EmpUtils.get(user);
        if (employee != null) {
            Office office = employee.getOffice();
            Company company = employee.getCompany();
            if (office != null && company != null) {
                return company.getCompanyName() + "-" + office.getOfficeName();
            }
        }

        return null;
    }

    /**
     * 将页面传递的主键字符串（多个时必须以英文逗号分隔）转化为Long[]
     * <p>
     * 页面传递的主键字符串为空时，返回null<br>
     * 页面传递的主键字符串转换失败时，返回null<br>
     * </p>
     *
     * @param idsStr
     * 为空时返回null
     * @return
     * @author xuejh
     * @since 2020-05-26 13:55:25
     */
    public static String[] getIdsArrayByStr(String idsStr) {
        if (StringUtil.isBlank(idsStr)) {
            return null;
        }

        String[] idsStrArray = idsStr.split(",");
        if (ArrayUtils.isEmpty(idsStrArray)) {
            return null;
        }

        return idsStrArray;
    }
}
