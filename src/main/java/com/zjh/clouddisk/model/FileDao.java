package com.zjh.clouddisk.model;

import com.zjh.clouddisk.dao.ObsFile;

public interface FileDao {
    int deleteByPrimaryKey(Integer fileId);

    int insert(ObsFile record);

    int insertSelective(ObsFile record);

    ObsFile selectByPrimaryKey(Integer fileId);

    int updateByPrimaryKeySelective(ObsFile record);

    int updateByPrimaryKey(ObsFile record);
}