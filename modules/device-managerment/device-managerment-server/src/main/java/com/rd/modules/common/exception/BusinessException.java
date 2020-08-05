package com.rd.modules.common.exception;

import com.jeesite.common.config.Global;
import org.springframework.stereotype.Component;

/**
 * @author xuejh
 * @description 自定义业务异常
 * @create 2020-04-07 17:04
 **/
@Component
public class BusinessException extends Exception {

    /**
     * 错误码
     */
    private String code;

    public BusinessException(String code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message) {
        super(message);
        this.code = Global.FALSE;
    }

    public BusinessException() {

    }

    public String getcode() {
        return code;
    }

    public void setcode(String code) {
        this.code = code;
    }
}
