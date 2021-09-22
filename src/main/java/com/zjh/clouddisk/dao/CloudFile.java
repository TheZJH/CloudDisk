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

    private Integer fileType;

    private String fileAuthor;

    private Date createdTime;

    private Date updateTime;

    private String fileSize;

    private String fileName;
    /**
     * 0代表未删除 ,1代表以删除
     */
    private Integer fileDelete;

    private String postfix;

    private String objectKey;

    private Integer parentFolderId;

    private Integer bucketId;

    public CloudFile(Integer fileId, Integer fileType, String fileAuthor, Date createdTime, Date updateTime, String fileSize, String fileName, Integer fileDelete, String postfix, String objectKey, Integer parentFolderId, Integer bucketId) {
        this.fileId = fileId;
        this.fileType = fileType;
        this.fileAuthor = fileAuthor;
        this.createdTime = createdTime;
        this.updateTime = updateTime;
        this.fileSize = fileSize;
        this.fileName = fileName;
        this.fileDelete = fileDelete;
        this.postfix = postfix;
        this.objectKey = objectKey;
        this.parentFolderId = parentFolderId;
        this.bucketId = bucketId;
    }

    public CloudFile() {
    }
}