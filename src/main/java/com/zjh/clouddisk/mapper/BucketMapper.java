package com.zjh.clouddisk.mapper;

import org.apache.ibatis.annotations.Select;

/**
 * @author TheZJH
 * @version 1.0
 */
public interface BucketMapper {
    @Select("select bucket_id from bucket where bucket_name=#{bucketName}")
    public Integer bucketId(String bucketName);
}
