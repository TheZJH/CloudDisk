package com.zjh.clouddisk.service.impl;
import com.zjh.clouddisk.dao.ObsFile;
import com.zjh.clouddisk.model.FileDao;
import com.zjh.clouddisk.service.ObsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author TheZJH
 * @version 1.0
 */
@Service
public class ObsServiceImpl implements ObsService {
    @Resource
    private FileDao fileDao;
    @Override
    public void createBucket(String endPoint, String ak, String sk) {

    }

    @Override
    public int upload(ObsFile file) {

        return fileDao.insert(file);
    }

}
