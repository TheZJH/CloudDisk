package com.zjh.clouddisk.controller.file;

import com.obs.services.ObsClient;
import com.obs.services.model.*;
import com.obs.services.model.fs.RenameRequest;
import com.obs.services.model.fs.RenameResult;
import com.zjh.clouddisk.dao.Folder;
import com.zjh.clouddisk.dao.CloudFile;
import com.zjh.clouddisk.dao.User;
import com.zjh.clouddisk.mapper.FileRootMapper;
import com.zjh.clouddisk.service.FolderService;
import com.zjh.clouddisk.service.FileService;
import com.zjh.clouddisk.util.CloudConfig;
import com.zjh.clouddisk.util.GetSize;
import com.zjh.clouddisk.util.GetType;
import com.zjh.clouddisk.util.Md5Util;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

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

    @Resource
    private FileRootMapper fileRootMapper;
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
        Integer userId = user.getUserId();
        Boolean flag = false;
        List<Integer> file = fileRootMapper.findFile(userId);
        for (Integer f : file) {
            if (f == folderId) {
                flag = true;
            }
        }
        List<CloudFile> fileList = null;
        List<Folder> folderList = null;
        List<Folder> pathList = new ArrayList<>();
        //说明是根目录
        if (folderId == null || folderId == 0) {
            folderId = 0;
            folderList = folderService.findAllRootFolder(1);
            fileList = fileService.findAllRootFile(1);
        } else {
            if (flag == true || user.getRole() == 0) {
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
            } else {
                session.setAttribute("msg", "你没有权限访问这个文件夹别乱点了");
                return "pages-error";
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
     * 下载文件,必须重新登录才会可以下载
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
            ObsObject obsObject = obsClient.getObject("xpu", file.getObjectKey());
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
        } else {
            String prefix = folderService.findFolderPath(1, folderId);
            CloudFile file = fileService.getFileByFileId(fileId, 1);
            //OBS下载路径
            String name = prefix + file.getObjectKey();
            //文件实际名
            fileName = file.getFileName();
            ObsObject obsObject = obsClient.getObject("xpu", name);
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
    public String toUpdatePage(Integer folderId, @RequestParam("file") MultipartFile multipartFile, HttpSession session) throws IOException {
        //获取当前时间
        Date date = new Date();
        InputStream inputStream = multipartFile.getInputStream();
        String objectKey;
        if (folderId == 0 || folderId == null) {
            objectKey = Md5Util.getMD5(multipartFile);
            CloudFile objectKey1 = fileRootMapper.getObjectKey(objectKey);
            if (objectKey1 != null) {
                //数据库中已经有此文件
                if (objectKey1.getParentFolderId() == folderId) {
                    //说明两个在同一个文件夹下
                    session.setAttribute("msg", "此文件已经存在于当前文件夹下");
                    return "pages-error";
                }
            }
            //当前目录为根目录
            String fileName = multipartFile.getOriginalFilename();
            //最后一个点出现的位置
            int index = fileName.lastIndexOf(".");
            //获取文件后缀名
            String postfix = fileName.substring(index, fileName.length());
            int type = GetType.getType(postfix);
            fileService.addFile(CloudFile.builder().
                    fileName(fileName)
                    .bucketId(1)
                    .parentFolderId(folderId)
                    .fileSize(GetSize.getSize(multipartFile.getSize()))
                    .postfix(postfix)
                    .fileType(type)
                    .createdTime(date).objectKey(objectKey).build());
        } else {
            String name1 = Md5Util.getMD5(multipartFile);
            CloudFile objectKey1 = fileRootMapper.getObjectKey(name1);
            if (objectKey1 != null) {
                //数据库中已经有此文件
                if (objectKey1.getParentFolderId() == folderId) {
                    //说明两个在同一个文件夹下
                    session.setAttribute("msg", "此文件已经存在于当前文件夹下");
                    return "pages-error";
                }
            }
            //文件夹路径
            String prefix = folderService.findFolderPath(1, folderId);
            String fileName = multipartFile.getOriginalFilename();
            int index = fileName.lastIndexOf(".");
            //上传路径
            objectKey = prefix + name1;
            //获取文件后缀名
            String postfix = fileName.substring(index, fileName.length());
            int type = GetType.getType(postfix);
            fileService.addFile(CloudFile.builder().
                    fileName(multipartFile.getOriginalFilename())
                    .bucketId(1)
                    .fileSize(GetSize.getSize(multipartFile.getSize()))
                    .parentFolderId(folderId)
                    .postfix(postfix)
                    .fileType(type).objectKey(name1)
                    .createdTime(date).build());
        }
        obsClient.putObject("xpu", objectKey, inputStream);
        inputStream.close();
        //obsClient.close(),关闭导致上传后无法下载
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
    public String toDeletePage(Integer folderId, Integer fileId) throws IOException {
        String objectKey;
        if (folderId == 0) {
            //当前目录为根目录
            CloudFile file = fileService.getFileByFileId(fileId, 1);
            objectKey = file.getObjectKey();
            //直接删除

        } else {
            //不是根目录
            CloudFile file = fileService.getFileByFileId(fileId, 1);
            String name = file.getObjectKey();
            String prefix = folderService.findFolderPath(1, folderId);
            objectKey = prefix + name;

        }
        //上传文件有速度,在上传未完成前删除会报null,应该
        fileService.deleteFile(fileId);
        obsClient.deleteObject("xpu", objectKey);
        return "redirect:/file?folderId=" + folderId;
    }

    @PostMapping("/file/rename")
    public String renameFile(Integer fileId, Integer folderId, String fileName) throws IOException {
        fileRootMapper.changeFileName(fileName, fileId);
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
    public String addFolder(Integer folderId, String folderName, HttpSession session) {
        User login = (User) session.getAttribute("loginUser");
        Integer userId = login.getUserId();
        Boolean flag = false;
        List<Integer> file = fileRootMapper.findFile(userId);
        for (Integer f : file) {
            if (f == folderId) {
                flag = true;
            }
        }
        Date date = new Date();
        String objectKey;
        if (folderId == 0 || folderId == null) {
            //向根目录添加文件夹
            if (login.getRole() == 0) {
                folderService.addFolder(Folder.builder()
                        .folderName(folderName)
                        .parentFolderId(folderId)
                        .bucketId(1)
                        .time(date)
                        .group(0)
                        .folderPath(folderName + "/").build());
                //在OBS下创建文件夹
                objectKey = folderName + "/";
            } else {
                session.setAttribute("msg", "你没有权限访问这个文件夹别乱点了");
                return "pages-error";
            }
        } else {
            if (login.getRole() == 0 || flag == true) {
                //在其他目录下添加文件夹
                String folderPath = folderService.findFolderPath(1, folderId);
                folderService.addFolder(Folder.builder()
                        .folderName(folderName)
                        .parentFolderId(folderId)
                        .bucketId(1)
                        .time(date)
                        .group(1)
                        .folderPath(folderPath + folderName + "/").build());
                Integer latestFolder = fileRootMapper.findLatestFolder();
                //说明是个人文件夹
                fileRootMapper.insertFolderUser(latestFolder, userId);
                List<Integer> user = fileRootMapper.findUser(latestFolder);
                //给其他群组成员文件权限
                for (Integer u : user) {
                    fileRootMapper.insertFolderUser(latestFolder, u);
                }

                objectKey = folderPath + folderName + "/";
            } else {
                session.setAttribute("msg", "你没有权限访问这个文件夹别乱点了");
                return "pages-error";
            }
        }
        obsClient.putObject("xpu", objectKey, new
                ByteArrayInputStream(new byte[0]));
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
        //先OBS删除再删数据库,否则会查询为空
        String objectKey = folderService.findFolderPath(1, folderId);
        obsClient.deleteObject("xpu", objectKey);
        deleteFolderF(folderId);
        return "redirect:/file?folderId=0";
    }

    /**
     * 递归删除文件夹里面的所有文件和子文件夹
     *
     * @param folderId
     */
    public void deleteFolderF(Integer folderId) {
        //获得当前文件夹下所有子文件夹
        List<Folder> folders = folderService.findSonFolder(1, folderId);
        //获取当前文件夹下的所有文件
        List<CloudFile> files = fileService.findAllFiles(1, folderId);
        if (files.size() != 0) {
            for (int i = 0; i < files.size(); i++) {
                Integer fileId = files.get(i).getFileId();
                fileService.deleteFile(fileId);
            }
        }
        if (folders.size() != 0) {
            for (int i = 0; i < folders.size(); i++) {
                deleteFolderF(folders.get(i).getFolderId());
            }
        }
        folderService.deleteFolder(1, folderId);
    }

}
