package com.zjh.clouddisk.mapper;

import com.zjh.clouddisk.dao.CloudFile;
import org.apache.ibatis.annotations.*;

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
    @Select("select * from file where parent_folder_id=0 and bucket_id=#{bucketId}")
    @Results(id = "fileMap", value = {
            @Result(id = true, column = "file_id", property = "fileId"),
            @Result(column = "file_type", property = "fileType"),
            @Result(column = "file_author", property = "fileAuthor"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "file_size", property = "fileSize"),
            @Result(column = "file_name", property = "fileName"),
            @Result(column = "file_path", property = "filePath"),
            @Result(column = "postfix", property = "postfix"),
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
    @Select("select * from file where parent_folder_id=#{folderId} and bucket_id=#{bucketId}")
    @ResultMap("fileMap")
    List<CloudFile> findAllFiles(Integer bucketId, Integer folderId);

    /**
     * 查找文件
     *
     * @param fileId
     * @param bucketId
     * @return
     */
    @Select("select * from file where file_id=#{fileId} and bucket_id=#{bucketId}")
    @ResultMap("fileMap")
    CloudFile getFileByFileId(Integer fileId, Integer bucketId);

    @Insert("insert into file(file_author,created_time,file_size,file_name,file_path,parent_folder_id,bucket_id,postfix,file_type) values(#{fileAuthor},#{createdTime},#{fileSize},#{fileName},#{filePath},#{parentFolderId},#{bucketId},#{postfix},#{fileType})")
    int addFile(CloudFile file);

    /**
     * 根据文件Id删除文件
     *
     * @param fileId
     * @return
     */
    @Delete("delete from file where file_id=#{fileId}")
    int deleteFile(Integer fileId);

    @Update("update file set file_name =#{fileName} where file_id=#{fileId} and bucket_id=#{bucketId}")
    int updateFileName(Integer fileId, String fileName, Integer bucketId);
}
