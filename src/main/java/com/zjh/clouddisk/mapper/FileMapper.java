package com.zjh.clouddisk.mapper;

import com.zjh.clouddisk.dao.CloudFile;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author TheZJH
 * @version 1.0
 */
public interface FileMapper {
    /**
     * 查询根目录下所有文件
     *
     * @param bucketId
     * @return
     */
    @Select("select * from file where parent_folder_id is null and bucket_id=#{bucketId}")
    @Results(id = "fileMap", value = {
            @Result(id = true, column = "file_id", property = "fileId"),
            @Result(column = "file_type", property = "fileType"),
            @Result(column = "file_author", property = "fileAuthor"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "file_size", property = "fileSize"),
            @Result(column = "file_name", property = "fileName"),
            @Result(column = "file_path", property = "filePath"),
            @Result(column = "remark", property = "remark"),
            @Result(column = "file_tag", property = "fileTag"),
            @Result(column = "parent_folder_id", property = "parentFolderId"),
            @Result(column = "bucket_id", property = "bucketId"),
    })
    List<CloudFile> findAllRootFile(Integer bucketId);

    /**
     * 查询其他目录下文件
     *
     * @param bucketId
     * @param folderId
     * @return
     */
    @Select("select * from file parent_folder_id =#{folderId} and bucket_id=#{bucketId}")
    @ResultMap("fileMap")
    List<CloudFile> findAllFiles(Integer bucketId, Integer folderId);

    /**
     * 查找文件
     *
     * @param fileId
     * @param bucketId
     * @return
     */
    @Select("select * from file file_id=#{fileId} and bucket_id=#{bucketId}")
    @ResultMap("fileMap")
    CloudFile getFileByFileId(Integer fileId, Integer bucketId);
}
