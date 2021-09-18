package com.zjh.clouddisk.service;

import com.zjh.clouddisk.dao.Folder;
import java.util.List;

/**
 * @author TheZJH
 * @version 1.0
 */
public interface FolderService {
    List<Folder> findAllRootFolder(Integer bucketId);
    List<Folder> findFolder(Integer bucketId,Integer folderId);
}