package com.zjh.clouddisk.service.impl;

import com.zjh.clouddisk.dao.Folder;
import com.zjh.clouddisk.mapper.FolderMapper;
import com.zjh.clouddisk.service.FolderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author TheZJH
 * @version 1.0
 */
@Service
public class FolderServiceImpl implements FolderService {
    @Resource
    private FolderMapper folderMapper;

    @Override
    public List<Folder> findAllRootFolder(Integer bucketId) {
        return folderMapper.findAllRootFolder(bucketId);
    }

    @Override
    public List<Folder> findFolder(Integer bucketId, Integer folderId) {
        return folderMapper.findFolder(bucketId, folderId);
    }

    @Override
    public String findFolderPath(Integer bucketId, Integer folderId) {
        return folderMapper.findFolderPath(bucketId, folderId);
    }

    @Override
    public Folder findParentFolderId(Integer bucketId, Integer parentFolderId) {
        return folderMapper.findParentFolderId(bucketId, parentFolderId);
    }

    @Override
    public int addFolder(Folder folder) {
        return folderMapper.addFolder(folder);
    }
}
