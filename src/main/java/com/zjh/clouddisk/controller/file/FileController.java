package com.zjh.clouddisk.controller.file;

import com.obs.services.ObsClient;
import com.obs.services.model.ObsObject;
import com.obs.services.model.fs.RenameRequest;
import com.zjh.clouddisk.dao.Folder;
import com.zjh.clouddisk.dao.CloudFile;
import com.zjh.clouddisk.dao.User;
import com.zjh.clouddisk.service.FolderService;
import com.zjh.clouddisk.service.FileService;
import com.zjh.clouddisk.util.CloudConfig;
import com.zjh.clouddisk.util.GetSize;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
     * 接受RestFul风格参数
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
        List<Folder> pathList = new ArrayList<>();
        //说明是根目录
        if (folderId == null || folderId == 0) {
            folderId = 0;
            folderList = folderService.findAllRootFolder(1);
            fileList = fileService.findAllRootFile(1);
        } else {
            //说明不是根目录,folderId就是子文件和文件夹的parentId
            folderList = folderService.findFolder(1, folderId);
            fileList = fileService.findAllFiles(1, folderId);
            Folder newFolder = folderService.findParentFolderId(1, folderId);

            Folder temp = newFolder;
            pathList.add(temp);
            while (temp.getParentFolderId() != 0) {
                temp = folderService.findParentFolderId(1, temp.getParentFolderId());
                pathList.add(temp);
            }
        }
        Collections.reverse(pathList);
        model.addAttribute("folderList", folderList);
        model.addAttribute("fileList", fileList);
        model.addAttribute("folderId", folderId);
        //文件路径
        model.addAttribute("pathList", pathList);
        return "page-files";
    }

    /**
     * 更新文件
     *
     * @param fileId
     * @param folderId
     * @return
     */
    @GetMapping("/file/update")
    public String updateFile(String fileId, Integer folderId) {
        // 重命名对象
        RenameRequest request = new RenameRequest();
        request.setBucketName("xpu");
        // objectKey 为原对象的完整对象名
        request.setObjectKey("folderName/originalObjectName");
        // newObjectKey 为目标对象的完整对象名
        request.setNewObjectKey("newFolderName/newObjectName");
        obsClient.renameFile(request);


        return "redirect:/file?folderId=" + folderId;
    }

    /**
     * 下载文件
     *
     * @param fileId
     * @param response
     * @param request
     * @return
     */
    @GetMapping("/file/download")
    public String download(Integer fileId, Integer folderId,
                           HttpServletResponse response,
                           HttpServletRequest request) {
        String fileName;
        if (folderId == null || folderId == 0) {
            CloudFile file = fileService.getFileByFileId(fileId, 1);
            fileName = file.getFileName();
        } else {
            String prefix = folderService.findFolderPath(1, folderId);
            CloudFile file = fileService.getFileByFileId(fileId, 1);
            String name = file.getFileName();
            fileName = prefix + name;
        }
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
        return "redirect:/file?folderId=" + folderId;
    }

    /**
     * 上传文件页面
     * 忘了取消ResponseBody注解
     *
     * @return
     */
    @PostMapping("/file/upload")
    public String toUpdatePage(Integer folderId, @RequestParam("file") MultipartFile multipartFile) throws IOException {
        //获取当前时间
        Date date = new Date();
        InputStream inputStream = multipartFile.getInputStream();
        String objectKey;
        if (folderId == null || folderId == 0) {
            //当前目录为根目录
            objectKey = multipartFile.getOriginalFilename();
            fileService.addFile(CloudFile.builder().
                    fileName(objectKey)
                    .bucketId(1)
                    .parentFolderId(folderId)
                    .fileSize(GetSize.getSize(multipartFile.getSize()))
                    .createdTime(date).build());
        } else {
            String prefix = folderService.findFolderPath(1, folderId);
            objectKey = prefix + multipartFile.getOriginalFilename();
            fileService.addFile(CloudFile.builder().
                    fileName(multipartFile.getOriginalFilename())
                    .bucketId(1)
                    .fileSize(GetSize.getSize(multipartFile.getSize()))
                    .parentFolderId(folderId)
                    .createdTime(date).build());
        }
        obsClient.putObject("xpu", objectKey, inputStream);
        inputStream.close();
        obsClient.close();
        return "redirect:/file?folderId=" + folderId;
    }

    /**
     * 删除文件
     *
     * @param folderId
     * @param fileId
     * @return
     */
    @GetMapping("/file/delete")
    public String toDeletePage(Integer folderId, Integer fileId) {
        String fileName;
        if (folderId == 0 || folderId == null) {
            //当前目录为根目录
            CloudFile file = fileService.getFileByFileId(fileId, 1);
            fileName = file.getFileName();
            //直接删除
        } else {
            //不是根目录
            CloudFile file = fileService.getFileByFileId(fileId, 1);
            String objectKey = file.getFileName();
            String prefix = folderService.findFolderPath(1, folderId);
            fileName = prefix + objectKey;

        }
        fileService.deleteFile(fileId);
        obsClient.deleteObject("xpu", fileName);
        return "redirect:/file?folderId=" + folderId;
    }

    /**
     * 新建文件夹
     *
     * @param folderName
     * @param folderId
     * @return
     */
    @PostMapping("/folder/update")
    public String addFolder(Integer folderId, String folderName) {
        Date date = new Date();
        String objectKey;
        if (folderId == 0 || folderId == null) {
            //向根目录添加文件夹
            folderService.addFolder(Folder.builder()
                    .folderName(folderName)
                    .parentFolderId(folderId)
                    .bucketId(1)
                    .time(date)
                    .folderPath(folderName + "/").build());
            //在OBS下创建文件夹
            objectKey = folderName + "/";

        } else {
            //在其他目录下添加文件夹
            String folderPath = folderService.findFolderPath(1, folderId);
            folderService.addFolder(Folder.builder()
                    .folderName(folderName)
                    .parentFolderId(folderId)
                    .bucketId(1)
                    .time(date)
                    .folderPath(folderPath + folderName + "/").build());
            objectKey = folderPath + folderName + "/";

        }
        obsClient.putObject("xpu", objectKey, new ByteArrayInputStream(new byte[0]));
        return "redirect:/file?folderId=" + folderId;
    }

    /**
     * 删除文件夹
     *
     * @param folderId
     * @return
     */
    @GetMapping("/folder/delete")
    public String deleteFolder(Integer folderId) {

        return "";
    }

}
