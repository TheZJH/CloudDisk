package com.zjh.clouddisk.mapper;

import org.apache.ibatis.annotations.Select;

/**
 * @author TheZJH
 * @version 1.0
 */
public interface BucketMapper {
    @Select("select bucket_id from bucket where bucket_name=#{bucketName}")
    Integer bucketId(String bucketName);

    @Select("select bucket_name from bucket where bucket_id=#{bucketId}")
    String findBucketName(Integer bucketId);
}
