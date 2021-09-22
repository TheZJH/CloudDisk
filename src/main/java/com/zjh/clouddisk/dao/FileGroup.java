package com.zjh.clouddisk.dao;

import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * file_group
 *
 * @author
 */
@Data
@Builder
public class FileGroup implements Serializable {
    private Integer groupId;

    private Integer userId;

    private Date time;

    private String groupPath;

    private String parentFolderId;

    private String groupName;

    private static final long serialVersionUID = 1L;
}