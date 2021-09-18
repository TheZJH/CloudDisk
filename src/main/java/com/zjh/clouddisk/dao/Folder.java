package com.zjh.clouddisk.dao;

import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * folder
 *
 * @author
 */
@Data
@Builder
public class Folder {
    private Integer folderId;

    private String folderName;

    private Integer parentFolderId;

    private Integer bucketId;

    private Date time;

    public Folder(Integer folderId, String folderName, Integer parentFolderId, Integer bucketId, Date time) {
        this.folderId = folderId;
        this.folderName = folderName;
        this.parentFolderId = parentFolderId;
        this.bucketId = bucketId;
        this.time = time;
    }

    public Folder() {
    }
}