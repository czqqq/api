package com.api.controller;

import com.api.controller.dto.BaseResult;
import com.api.controller.dto.ResultCode;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PictureController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String WINBANNER = "E:/workspace/app_web/src/main/webapp/pic/banner";
    private static final String LINUXBANNER = "/usr/local/app/web/pic/banner";
    private static final String WINPRODUCT = "E:/workspace/app_web/src/main/webapp/pic/product";
    private static final String LINUXPRODUCT = "/usr/local/app/web/pic/product";

    @GetMapping("/fetchBanner")
    public BaseResult fetchBanner(){
        BaseResult baseResult = new BaseResult();
/*        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new BaseResult(ResultCode.SUCCESS, "验证不通过", null);
        }*/

        String path = getBannerPicPath();
        File file = new File(path);
        File[] files = file.listFiles();
        if (null == files || files.length == 0) {
            logger.error("banner文件夹为空");
        } else {
            int length = files.length;
            List<String> list = new ArrayList<>(length);
            for(int i = 0 ; i< length ; i++){
                //todo 只写服务器上的地址
                String os = System.getProperty("os.name");
                if (os.toLowerCase().startsWith("win")) {
                    list.add("http://localhost/pic/banner/"+files[i].getName());
                }else {
                    list.add("http://47.107.79.61/pic/banner/"+files[i].getName());
                }
            }
            baseResult.setData(list);
        }
        return baseResult;
    }







    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "上传失败，请选择文件";
        }

        String fileName = file.getOriginalFilename();
        String filePath = "/Users/itinypocket/workspace/temp/";
        File dest = new File(filePath + fileName);
        try {
            file.transferTo(dest);
            logger.info("上传成功");
            return "上传成功";
        } catch (IOException e) {
            logger.error(e.toString(), e);
        }
        return "上传失败！";
    }

    @PostMapping("/multiUpload")
    @ResponseBody
    public String multiUpload(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        String filePath = getBannerPicPath();

        File uploadFile = new File(filePath);
        File[] uploadFiles = uploadFile.listFiles();
        int imgs = uploadFiles.length;

        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            if (file.isEmpty()) {
                return "上传第" + (i++) + "个文件失败";
            }
            imgs += 1;
            if((imgs > 6)){
                imgs = 1;
            }
            String fileName = "banner" + imgs + "." +(file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".")));

            File dest = new File(filePath + fileName);
            try {
                file.transferTo(dest);
                logger.info("第" + (i + 1) + "个文件上传成功");
            } catch (IOException e) {
                logger.error(e.toString(), e);
                return "上传第" + (i++) + "个文件失败";
            }
        }

        return "上传成功";

    }

    private String getBannerPicPath() {
        File file;
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            file = new File(WINBANNER);
        } else {
            file = new File(LINUXBANNER);
        }

        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
        return file.getPath() + "/";
    }

    private String getProductPicPath() {
        File file;
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            file = new File(WINPRODUCT);
        } else {
            file = new File(LINUXPRODUCT);
        }

        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
        return file.getPath()+ "/";
    }

}
