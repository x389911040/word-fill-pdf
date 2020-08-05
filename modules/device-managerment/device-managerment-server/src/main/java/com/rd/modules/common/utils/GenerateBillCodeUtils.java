package com.rd.modules.common.utils;

import com.jeesite.common.lang.DateUtils;
import com.rd.modules.device.entity.ZbDeviceRepair;

import java.util.Date;

/**
 * @author xuejh
 * @description 生成单据编号工具类
 * @create 2020-04-16 11:46
 **/
public class GenerateBillCodeUtils {

    /**
     * 生成设备编号
     * @author xuejh
     * @create 2020/4/26 9:50
     * @param date
     * @return java.lang.String
     */
    public static String getDeviceCode(Date date) {
        return DateUtils.formatDate(date, "yyyyMMddHHmmssSSS");
    }

    /**
     * 生成设备移交单据编号
     * @author xuejh
     * @create 2020/4/16 11:47
     * @param
     * @return java.lang.String
     */
    public static String getDeviceMoveBillCode(Date date) {
        String dateStr = DateUtils.formatDate(date, "yyyyMMddHHmmssSSS");
        return "YJ" + dateStr;
    }

    /**
     * 生成设备借用单据编号
     * @author xuejh
     * @create 2020/4/16 11:47
     * @param
     * @return java.lang.String
     */
    public static String getDeviceBorrowBillCode(Date date) {
        String dateStr = DateUtils.formatDate(date, "yyyyMMddHHmmssSSS");
        return "JY" + dateStr;
    }

    /**
     * 生成设备维修申请单据编号
     * @author xuejh
     * @create 2020/4/16 11:47
     * @param
     * @return java.lang.String
     */
    public static String getDeviceRepairApplyBillCode(Date date) {
        String dateStr = DateUtils.formatDate(date, "yyyyMMddHHmmssSSS");
        return "RA" + dateStr;
    }

    /**
     * 生成设备维修单据编号
     * @author xuejh
     * @create 2020/4/16 11:47
     * @param
     * @return java.lang.String
     */
    public static String getDeviceRepairBillCode(Date date, String repairType) {
        String dateStr = DateUtils.formatDate(date, "yyyyMMddHHmmssSSS");
        if (repairType.equals(ZbDeviceRepair.REPAIR_TYPE_INNER)) {
            return "YN" + dateStr;
        }
        return "WS" + dateStr;
    }

    /**
     * 生成维修交接单据编号
     * @author xuejh
     * @create 2020/4/21 10:23
     * @param date
     * @return java.lang.String
     */
    public static String getDeviceRepairHandoverBillCode(Date date) {
        String dateStr = DateUtils.formatDate(date, "yyyyMMddHHmmssSSS");
        return "RH" + dateStr;
    }

    /**
     * 生成设备台账编号
     * @author xuejh
     * @create 2020/4/22 15:03
     * @param date
     * @return java.lang.String
     */
    public static String getDeviceAccountsCode(Date date) {
        String dateStr = DateUtils.formatDate(date, "yyyyMMddHHmmssSSS");
        return "AC" + dateStr;
    }

    /**
     * 生成设备封存申请单据编号
     * @author xuejh
     * @create 2020/4/16 11:47
     * @param
     * @return java.lang.String
     */
    public static String getDeviceSealBillCode(Date date) {
        String dateStr = DateUtils.formatDate(date, "yyyyMMddHHmmssSSS");
        return "FC" + dateStr;
    }

    /**
     * 生成设备闲置申请单编号
     * @author xuejh
     * @create 2020/5/6 16:57
     * @param date
     * @return java.lang.String
     */
    public static String getDeviceFreeBillCode(Date date) {
        String dateStr = DateUtils.formatDate(date, "yyyyMMddHHmmssSSS");
        return "XZ" + dateStr;
    }

    /**
     * 生成报废计划单号
     * @author xuejh
     * @create 2020/6/1 15:30
     * @param date
     * @return java.lang.String
     */
    public static String getDeviceScrapPlanBillCode(Date date) {
        String dateStr = DateUtils.formatDate(date, "yyyyMMddHHmmssSSS");
        return "SP" + dateStr;
    }

    /**
     * 生成报废计划单号
     * @author xuejh
     * @create 2020/6/1 15:30
     * @param date
     * @return java.lang.String
     */
    public static String getDeviceScrapMoveBillCode(Date date) {
        String dateStr = DateUtils.formatDate(date, "yyyyMMddHHmmssSSS");
        return "SM" + dateStr;
    }
}
