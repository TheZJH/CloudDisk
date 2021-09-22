package com.zjh.clouddisk.mapper;

import com.zjh.clouddisk.dao.CloudFile;
import com.zjh.clouddisk.dao.Folder;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author TheZJH
 * @version 1.0
 */
public interface FileRootMapper {
    @Select("select folder_id from folder_user where user_id=#{userId}")
    List<Integer> findFile(Integer userId);

    /**
     * 挺蠢的方法
     *
     * @return
     */
    @Select("SELECT folder_id FROM folder ORDER BY TIME DESC LIMIT 1")
    Integer findLatestFolder();

    @Select("select user_id from folder_user where folder_id=#{folderId}")
    List<Integer> findUser(Integer folderId);

    @Insert("insert into folder_user(folder_id,user_id) values (#{folderId},#{userId})")
    Integer insertFolderUser(Integer folderId, Integer userId);

    @Update("update file set file_name=#{fileName} where file_id=#{fileId}")
    Integer changeFileName(String fileName, Integer fileId);

    @Select("select * from file where objectKey=#{objectKey}")
    @Results(id = "fileMap", value = {
            @Result(id = true, column = "file_id", property = "fileId"),
            @Result(column = "file_type", property = "fileType"),
            @Result(column = "file_author", property = "fileAuthor"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "file_size", property = "fileSize"),
            @Result(column = "file_name", property = "fileName"),
            @Result(column = "file_delete", property = "fileDelete"),
            @Result(column = "postfix", property = "postfix"),
            @Result(column = "objectKey", property = "objectKey"),
            @Result(column = "parent_folder_id", property = "parentFolderId"),
            @Result(column = "bucket_id", property = "bucketId"),
    })
    CloudFile getObjectKey(String objectKey);
}
