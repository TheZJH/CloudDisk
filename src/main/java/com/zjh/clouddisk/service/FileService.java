package com.zjh.clouddisk.service;

import com.zjh.clouddisk.dao.CloudFile;

import java.util.List;

/**
 * @author TheZJH
 * @version 1.0
 */
public interface FileService {
    List<CloudFile> findAllRootFile(Integer bucketId);

    List<CloudFile> findAllFiles(Integer bucketId,Integer folderId);

    CloudFile getFileByFileId(Integer fileId, Integer bucketId);

    int addFile(CloudFile file);

    int deleteFile(Integer fileId);
}
