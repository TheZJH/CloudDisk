package com.zjh.clouddisk.dao;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * file
 * @author 
 */
@Data
public class ObsFile implements Serializable {
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

    private static final long serialVersionUID = 1L;
}