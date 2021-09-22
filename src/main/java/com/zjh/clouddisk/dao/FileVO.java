package com.zjh.clouddisk.dao;

import lombok.Builder;
import lombok.Data;

/**
 * @author TheZJH
 * @version 1.0
 */
@Data
@Builder
public class FileVO {
    private Integer fileId;
    private Integer userId;
}
