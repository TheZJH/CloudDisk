package com.zjh.clouddisk.controller.file;


import com.obs.services.ObsClient;
import com.zjh.clouddisk.dao.ObsFile;
import com.zjh.clouddisk.service.ObsService;
import com.zjh.clouddisk.util.ObsConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * @author TheZJH
 * @version 1.0
 */
@Controller
@RequestMapping("/file")
public class FileController {
    @Resource
    private ObsService obsService;

    @GetMapping("/upload")
    public String upload() {
        return "file/upload";
    }

    /**
     * Multipart File spring 中form上传的文件
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file,
                         HttpSession session) throws IOException {
        if (!file.isEmpty()) {
            // 获取文件名
            String fileName = file.getOriginalFilename();
            // 获取文件后缀
            File temp = new File("D:/" + fileName);
            file.transferTo(temp);
            ObsClient obsClient = new ObsClient(ObsConfig.ak, ObsConfig.sk, ObsConfig.endPoint);
            //上传文件(桶名称,文件名,文件)
            obsClient.putObject("xpu", fileName, temp);
            obsClient.close();
            ObsFile file1 = new ObsFile();
            file1.setFileAuthor((String) session.getAttribute("realname"));

            Date date = new Date();

            file1.setCreatedTime(date);
            String size = String.valueOf(file.getSize());
            file1.setFileSize(size);
            obsService.upload(file1);
            //删除文件
            deleteFile(temp);
            return "test";
        }
        return "error";
    }

    private void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
