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
public class Folder {
    private Integer folderId;

    private String folderName;

    private Integer parentFolderId;

    private Integer bucketId;

    private Date time;

    private String folderPath;
}