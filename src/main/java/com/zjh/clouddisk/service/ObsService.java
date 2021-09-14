package com.zjh.clouddisk.service;

import com.zjh.clouddisk.dao.ObsFile;

/**
 * @author TheZJH
 * @version 1.0
 */
public interface ObsService {
    void createBucket(String endPoint,String ak,String sk);

    int upload(ObsFile file);
}
