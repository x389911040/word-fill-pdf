package com.rd.modules.common.pdf_convert;

/**
 * @author xuejh
 * @description 文件上传工具类优化
 * @create 2020-08-04 11:24
 **/

import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.io.FileUtils;
import com.jeesite.common.lang.ExceptionUtils;
import com.jeesite.modules.file.entity.FileEntity;
import com.jeesite.modules.file.entity.FileUpload;
import com.jeesite.modules.file.entity.FileUploadParams;
import com.jeesite.modules.file.service.support.FileUploadServiceExtendSupport;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 文件上传本地磁盘 v4.0.7+
 * @author ThinkGem
 * @version 2018年8月17日
 */
@Service
public class FileUploadServiceExtendImpl extends FileUploadServiceExtendSupport {

    /**
     * 验证文件是否真实的存在
     * @param fileEntity 文件实体信息
     * @return 文件存在true，不存在false
     */
    public boolean fileExists(FileEntity fileEntity){
        String path = fileEntity.getFileRealPath();
        File localFile = new File(path);
        return localFile.exists();
    }

    /**
     * 上传文件，首次上传文件都调用（保存到文件实体表之前调用）
     * @param fileEntity 文件实体信息
     * @exception //支持抛出 throw ServiceException("文件不符合要求") v4.1.5
     */
    public void uploadFile(FileEntity fileEntity){

        // 当前上传的文件磁盘路径
        String fileName = fileEntity.getFileRealPath();

        System.out.println(fileName);

        try {
            // 这里写上传到文件服务器的代码

        } catch (Exception e) {
            throw ExceptionUtils.unchecked(e);
        } finally {
            // 文件上传后删除临时文件
            FileUtils.deleteFile(fileName);
        }
    }

    /**
     * 保存上传文件信息，每次上传都调用（保存文件和用户关系数据之前调用）
     * @param fileUpload 文件上传信息，包括文件实体
     * @exception //支持抛出 throw ServiceException("文件不符合要求") v4.1.5
     */
    public void saveUploadFile(FileUpload fileUpload){

    }

    /**
     * 获取访问文件的URL地址
     * @param fileUpload 文件上传的信息，包括文件实体
     * @return 无文件下载地址，则返回null，方便后续处理
     */
    public String getFileUrl(FileUpload fileUpload){
        return fileUpload.getFileEntity().getFileUrl();
    }

    /**
     * 下载文件到浏览器
     * @param fileUpload 文件上传的信息
     * @param request 请求对象，可能断点续传用
     * @param response 响应对象，输出文件流使用
     * @return 如果不是文件流数据，也可返回文件的URL地址进行跳转，如果文件不存在返回404字符串
     */
    public String downFile(FileUpload fileUpload, HttpServletRequest request, HttpServletResponse response){
        FileEntity fileEntity = fileUpload.getFileEntity();
        File file = new File(fileEntity.getFileRealPath());
        if (file.exists()){
            FileUtils.downFile(file, request, response, fileUpload.getFileName());
            return null;
        }
        return "404";
    }

}
