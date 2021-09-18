package com.zjh.clouddisk.controller.file;

import com.obs.services.ObsClient;
import com.obs.services.model.ObsObject;
import com.zjh.clouddisk.dao.Folder;
import com.zjh.clouddisk.dao.CloudFile;
import com.zjh.clouddisk.dao.User;
import com.zjh.clouddisk.service.FolderService;
import com.zjh.clouddisk.service.FileService;
import com.zjh.clouddisk.util.CloudConfig;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;
import sun.nio.ch.IOUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author TheZJH
 * @version 1.0
 */
@Controller
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private FolderService folderService;

    /**
     * 创建ObsClient实例
     */
    final ObsClient obsClient = new ObsClient(CloudConfig.ak, CloudConfig.sk, CloudConfig.endPoint);

    /**
     * 全部文件展示
     *
     * @param folderId
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/file")
    public String toFilePage(Integer folderId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        List<CloudFile> fileList = null;
        List<Folder> folderList = null;
        //说明是根目录
        if (folderId == null) {
            folderList = folderService.findAllRootFolder(1);
            fileList = fileService.findAllRootFile(1);
        } else {
            //说明不是根目录,folderId就是子文件和文件夹的parentId
            folderList = folderService.findFolder(1, folderId);
            fileList = fileService.findAllFiles(1, folderId);
        }
        model.addAttribute("folderList", folderList);
        model.addAttribute("fileList", fileList);
        model.addAttribute("folderId", folderId);
        return "test";
    }

    @GetMapping("/download")
    public String download(Integer fileId,
                           HttpServletResponse response,
                           HttpServletRequest request) {
        CloudFile file = fileService.getFileByFileId(fileId, 1);
        String fileName = file.getFileName();

        ObsObject obsObject = obsClient.getObject("xpu", fileName);

        InputStream input = obsObject.getObjectContent();
        try {
            //缓冲文件输入流
            BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            //防止文件名出现乱码
            final String userAgent = request.getHeader("USER-AGENT");
            //IE浏览器
            if (StringUtils.contains(userAgent, "MSIE")) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else {
                //Google,火狐浏览器
                if (StringUtils.contains(userAgent, "Mozilla")) {
                    fileName = new String(fileName.getBytes(), "ISO8859-1");
                } else {
                    //其他浏览器
                    fileName = URLEncoder.encode(fileName, "UTF-8");
                }
            }
            response.setContentType("application/x-download");
            //设置让浏览器弹出下载框,之前忘了在filename后加=
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            IOUtils.copy(input, outputStream);
            outputStream.flush();
            outputStream.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

}
