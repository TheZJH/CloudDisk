package com.zjh.clouddisk.service.impl;

import com.zjh.clouddisk.dao.CloudFile;
import com.zjh.clouddisk.mapper.FileMapper;
import com.zjh.clouddisk.service.FileService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author TheZJH
 * @version 1.0
 */
@Service
public class FileServiceImpl implements FileService {
    @Resource
    private FileMapper fileMapper;

    @Override
    public List<CloudFile> findAllRootFile(Integer bucketId) {
        return fileMapper.findAllRootFile(bucketId);
    }

    @Override
    public List<CloudFile> findAllFiles(Integer bucketId, Integer folderId) {
        return fileMapper.findAllFiles(bucketId, folderId);
    }

    @Override
    public CloudFile getFileByFileId(Integer fileId, Integer bucketId) {
        return fileMapper.getFileByFileId(fileId, bucketId);
    }
}