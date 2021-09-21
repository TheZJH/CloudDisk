package com.zjh.clouddisk.dao;

import com.obs.services.model.BucketQuota;
import lombok.Builder;
import lombok.Data;

/**
 * @author TheZJH
 * @version 1.0
 */
@Data
@Builder
public class BucketVO {
    private Integer bucketId;
    private String bucketName;
    private Long bucketObjectNumber;
    //桶已使用大小
    private String bucketSize;
    //桶配额
    private BucketQuota bucketQuota;
}
