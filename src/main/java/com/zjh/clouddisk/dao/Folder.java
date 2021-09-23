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
public class Folder implements Serializable {
    private Integer folderId;

    private String folderName;

    private Integer parentFolderId;

    private Integer bucketId;

    private Date time;

    private String folderPath;
    /**
     * 0代表群组,1代表普通文件夹
     */
    private Integer group;

    public Folder() {
    }

    public Folder(Integer folderId, String folderName, Integer parentFolderId, Integer bucketId, Date time, String folderPath, Integer group) {
        this.folderId = folderId;
        this.folderName = folderName;
        this.parentFolderId = parentFolderId;
        this.bucketId = bucketId;
        this.time = time;
        this.folderPath = folderPath;
        this.group = group;
    }
}