package com.zjh.clouddisk.controller.file;

import com.obs.services.ObsClient;
import com.zjh.clouddisk.dao.CloudFile;
import com.zjh.clouddisk.mapper.FileMapper;
import com.zjh.clouddisk.mapper.FolderMapper;
import com.zjh.clouddisk.util.CloudConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;

/**
 * @author TheZJH
 * @version 1.0
 */
@Controller
public class FileCopyController {
    @Resource
    private FileMapper fileMapper;

    @Resource
    private FolderMapper folderMapper;

    final ObsClient obsClient = new ObsClient(CloudConfig.ak, CloudConfig.sk, CloudConfig.endPoint);

    @PostMapping("/file/copy")
    public String moveFile(Integer fileId, Integer folderId, String filePath) {
        if (folderId == 0) {
            CloudFile file = fileMapper.getFileByFileId(fileId, 1);
            String fileName = file.getFileName();

            String objectKey = filePath + fileName;
            //操作OBS复制文件
            obsClient.copyObject("xpu", fileName,
                    "xpu", objectKey);

            file.setParentFolderId(folderMapper.findFolderId(filePath));
            fileMapper.addFile(file);
        }else {
            CloudFile file = fileMapper.getFileByFileId(fileId, 1);
            String name = file.getFileName();
            String prefix = folderMapper.findFolderPath(1, folderId);
            String fileName = prefix + name;
            //操作OBS复制文件
            obsClient.copyObject("xpu", fileName,
                    "xpu", filePath + fileName);

            file.setParentFolderId(folderMapper.findFolderId(filePath));
            fileMapper.addFile(file);
        }

        return "redirect:/file?folderId=" + folderId;
    }
}
