package com.zjh.clouddisk.mapper;

import com.zjh.clouddisk.dao.Folder;
import org.apache.ibatis.annotations.*;

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
    @Select("select * from folder where parent_folder_id=0 and bucket_id=#{bucketId}")
    @Results(id = "folderMap", value = {
            @Result(id = true, column = "folder_id", property = "folderId"),
            @Result(column = "folder_name", property = "folderName"),
            @Result(column = "parent_folder_id", property = "parentFolderId"),
            @Result(column = "bucket_id", property = "bucketId"),
            @Result(column = "time", property = "time"),
            @Result(column = "folder_path", property = "folderPath"),
            @Result(column = "is_group",property = "group")
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
     *
     * @param bucketId
     * @param folderId
     * @return
     */
    @Select("select folder_path from folder where bucket_id=#{bucketId} and folder_id=#{folderId}")
    String findFolderPath(Integer bucketId, Integer folderId);

    /**
     * 查询文件夹父文件夹Id=父文件夹Id的文件夹
     *
     * @param bucketId
     * @param parentFolderId
     * @return
     */
    @Select("select * from folder where bucket_id=#{bucketId} and folder_id=#{parentFolderId}")
    @ResultMap("folderMap")
    Folder findParentFolderId(Integer bucketId, Integer parentFolderId);

    /**
     * 添加文件夹
     *
     * @param folder
     * @return
     */
    @Insert("insert into folder(folder_name,parent_folder_id,bucket_id,time,folder_path,is_group)values(#{folderName},#{parentFolderId},#{bucketId},#{time},#{folderPath},#{group})")
    int addFolder(Folder folder);

    /**
     * 查询当前文件夹的的子文件夹
     *
     * @param bucketId
     * @param folderId
     * @return
     */
    @Select("select * from folder where bucket_id=#{bucketId} and parent_folder_id=#{folderId}")
    @ResultMap("folderMap")
    List<Folder> findSonFolder(Integer bucketId, Integer folderId);

    @Delete("delete from folder where bucket_id=#{bucketId} and folder_id=#{folderId}")
    int deleteFolder(Integer bucketId, Integer folderId);

    @Select("SELECT *FROM folder  ORDER BY time DESC LIMIT 0,4")
    List<Folder> indexFolder();

    @Select("select folder_id from folder where folder_path=#{filePath}")
    Integer findFolderId(String filePath);
}
