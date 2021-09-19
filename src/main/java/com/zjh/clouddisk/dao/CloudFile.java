package com.zjh.clouddisk.dao;

import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * file
 *
 * @author
 */
@Data
@Builder
public class CloudFile implements Serializable {
    private Integer fileId;

    private String fileType;

    private String fileAuthor;

    private Date createdTime;

    private Date updateTime;

    private String fileSize;

    private String fileName;

    private String filePath;

    private String remark;

    private String fileTag;

    private Integer parentFolderId;

    private Integer bucketId;

    public CloudFile(Integer fileId, String fileType, String fileAuthor, Date createdTime, Date updateTime, String fileSize, String fileName, String filePath, String remark, String fileTag, Integer parentFolderId, Integer bucketId) {
        this.fileId = fileId;
        this.fileType = fileType;
        this.fileAuthor = fileAuthor;
        this.createdTime = createdTime;
        this.updateTime = updateTime;
        this.fileSize = fileSize;
        this.fileName = fileName;
        this.filePath = filePath;
        this.remark = remark;
        this.fileTag = fileTag;
        this.parentFolderId = parentFolderId;
        this.bucketId = bucketId;
    }

    public CloudFile() {
    }
}