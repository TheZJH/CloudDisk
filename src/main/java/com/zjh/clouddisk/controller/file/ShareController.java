package com.zjh.clouddisk.controller.file;

import com.obs.services.ObsClient;
import com.obs.services.model.HttpMethodEnum;
import com.obs.services.model.TemporarySignatureRequest;
import com.obs.services.model.TemporarySignatureResponse;
import com.zjh.clouddisk.dao.CloudFile;
import com.zjh.clouddisk.dao.User;
import com.zjh.clouddisk.mapper.FileMapper;
import com.zjh.clouddisk.mapper.FolderMapper;
import com.zjh.clouddisk.util.CloudConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @author TheZJH
 * @version 1.0
 */
@Controller
public class ShareController {
    @Resource
    private FileMapper fileMapper;

    @Resource
    private FolderMapper folderMapper;
    /**
     * 创建ObsClient实例
     */
    final ObsClient obsClient = new ObsClient(CloudConfig.ak, CloudConfig.sk, CloudConfig.endPoint);

    /**
     * 文件分享
     *
     * @return
     */
    @GetMapping("/file/share")
    @ResponseBody
    public String shareFile(Integer fileId, Integer folderId, HttpSession session) {
        String fileName;
        if (folderId == 0 || folderId == null) {
            //说明是根目录
            CloudFile file = fileMapper.getFileByFileId(fileId, 1);
            fileName = file.getFileName();
        } else {
            CloudFile file = fileMapper.getFileByFileId(fileId, 1);
            String prefix = folderMapper.findFolderPath(1, folderId);
            String name = file.getFileName();
            fileName = prefix + name;
        }

        User user = (User) session.getAttribute("loginUser");
        //URL有效期
        Long expireSeconds = 360000L;
        TemporarySignatureRequest request = new TemporarySignatureRequest(HttpMethodEnum.GET, expireSeconds);
        request.setBucketName("xpu");
        request.setObjectKey(fileName);
        TemporarySignatureResponse response = obsClient.createTemporarySignature(request);
        String url = response.getSignedUrl();
        return url;
    }
}
