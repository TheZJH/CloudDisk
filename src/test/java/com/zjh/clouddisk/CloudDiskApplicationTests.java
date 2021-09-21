package com.zjh.clouddisk;


import com.obs.services.ObsClient;
import com.obs.services.model.BucketQuota;
import com.obs.services.model.BucketStorageInfo;
import com.obs.services.model.fs.RenameRequest;
import com.obs.services.model.fs.RenameResult;
import com.zjh.clouddisk.util.CloudConfig;
import com.zjh.clouddisk.util.GetSize;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class CloudDiskApplicationTests {

    /**
     * 创建ObsClient实例
     */
    final ObsClient obsClient = new ObsClient(CloudConfig.ak, CloudConfig.sk, CloudConfig.endPoint);

    @Test
    public void delete() {
        obsClient.deleteObject("xpu", "test/");
    }

    @Test
    public void rename() {
        /**
         * 华为OBS的
         */
        //如果是根目录
        RenameRequest request = new RenameRequest();
        //桶名
        request.setBucketName("xpu");
        //原对象完整文件名
        request.setObjectKey("2.jpg");
        //目标对象名
        request.setNewObjectKey("1.jpg");
        //更新OBS文件名
        RenameResult result = obsClient.renameFile(request);
    }

    @Test
    public void bucketEx() {
        BucketStorageInfo storageInfo = obsClient.getBucketStorageInfo("xpu");
        //桶中对象数量
        System.out.println("\t" + storageInfo.getObjectNumber());
        //桶中对象大小
        System.out.println("\t" + GetSize.getSize(storageInfo.getSize()));
        BucketQuota quota = obsClient.getBucketQuota("xpu");
        //桶配额
        System.out.println("\t" + quota.getBucketQuota());
    }
    @Test
    public void createBucket(){
        obsClient.createBucket("32842384208");
    }
}
