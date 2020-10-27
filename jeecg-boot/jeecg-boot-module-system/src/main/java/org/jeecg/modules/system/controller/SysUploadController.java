package org.jeecg.modules.system.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.oss.entity.OSSFile;
import org.jeecg.modules.oss.service.IOSSFileService;
import org.jeecg.modules.system.util.MinioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * minio文件上传示例
 */
@Slf4j
@RestController
@RequestMapping("/sys/upload")
public class SysUploadController {
    @Autowired
    private IOSSFileService ossFileService;

    /**
     * 上传
     * @param request
     */
    @PostMapping(value = "/uploadMinio")
    public Result<?> uploadMinio(HttpServletRequest request) {
        Result<?> result = new Result<>();
        String bizPath = request.getParameter("biz");
        if(oConvertUtils.isEmpty(bizPath)){
            bizPath = "";
        }
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");// 获取上传文件对象
        String orgName = file.getOriginalFilename();// 获取文件名
        String file_url =  MinioUtil.upload(file,bizPath);
        //保存文件信息
        OSSFile minioFile = new OSSFile();
        minioFile.setFileName(orgName);
        minioFile.setUrl(file_url);
        ossFileService.save(minioFile);
        result.setMessage(file_url);
        result.setSuccess(true);
        return result;
    }

    /**
     * 下载文件
     * @param response:
     * @return: void
     */
    @GetMapping(value = "/downMinio")
    public  void downMinio(HttpServletRequest request, HttpServletResponse response) {

//        String fileName=fileUrl.substring(fileUrl.lastIndexOf("/")+1);
        // ISO-8859-1 ==> UTF-8 进行编码转换
//        String imgPath = extractPathFromPattern(request);
        String  fileName=request.getParameter("params");
//        MinioUtil.download(fileName,response);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            // 获取对象的元数据
//        final ObjectStat stat = minioClient.statObject(bucketName, fileName);
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes("UTF-8"),"iso-8859-1"));
            inputStream = MinioUtil.download(fileName);
            outputStream = response.getOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            response.flushBuffer();
        } catch (IOException e) {
            log.error("预览文件失败" + e.getMessage());
            // e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

    }

    /**
     *  把指定URL后的字符串全部截断当成参数
     *  这么做是为了防止URL中包含中文或者特殊字符（/等）时，匹配不了的问题
     * @param request
     * @return
     */
    private static String extractPathFromPattern(final HttpServletRequest request) {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        return new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);
    }

}
