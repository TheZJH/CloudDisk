package com.zjh.clouddisk.mapper;

import com.zjh.clouddisk.dao.Folder;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author TheZJH
 * @version 1.0
 */
public interface FolderMapper {
    /**
     * 查询根目录下的文件夹
     *
     * @param bucketId
     * @return
     */
    @Select("select * from folder where parent_folder_id is null and bucket_id=#{bucketId}")
    @Results(id = "folderMap", value = {
            @Result(id = true, column = "folder_id", property = "folderId"),
            @Result(column = "folder_name", property = "folderName"),
            @Result(column = "parent_folder_id", property = "parentFolderId"),
            @Result(column = "bucket_id", property = "bucketId"),
            @Result(column = "time", property = "time"),
            @Result(column = "folder_path", property = "folderPath")
    })
    List<Folder> findAllRootFolder(Integer bucketId);

    /**
     * 查询其他目录下的文件夹
     *
     * @param bucketId
     * @param folderId
     * @return
     */
    @Select("select * from folder where bucket_id=#{bucketId} and parent_folder_id=#{folderId}")
    @ResultMap("folderMap")
    List<Folder> findFolder(Integer bucketId, Integer folderId);

    /**
     * 查询文件夹路径
     * @param bucketId
     * @param folderId
     * @return
     */
    @Select("select folder_path from folder where bucket_id=#{bucketId} and folder_id=#{folderId}")
    String findFolderPath(Integer bucketId, Integer folderId);
}
