package com.rd.modules.common.intercept;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.config.Global;
import com.jeesite.common.web.BaseController;
import com.rd.modules.common.exception.BusinessException;
import lombok.extern.log4j.Log4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.apache.log4j.helpers.LogLog.error;

/**
 * @author xuejh
 * @description 全局异常拦截器
 * @create 2020-04-07 17:23
 **/
@ControllerAdvice(basePackages = "com.rd.modules")
@Log4j
public class GlobalExceptionIntercept extends BaseController {

    @ExceptionHandler(Exception.class)
    public void errorMsg (HttpServletRequest request, HttpServletResponse response, Exception ex) {
        error("错误链接"+request.getRequestURL().toString());
        ex.printStackTrace();
        String errorMessage = "系统异常";
        if (ex instanceof BusinessException) {
            errorMessage = ex.getMessage();
        }

//        renderResult(Global.FALSE, text(errorMessage), null);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", Global.FALSE);
        jsonObject.put("message", errorMessage);

        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }
}
