package com.zjh.clouddisk;


import com.obs.services.ObsClient;
import com.zjh.clouddisk.util.CloudConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class CloudDiskApplicationTests {

    /**
     * 创建ObsClient实例
     */
    final ObsClient obsClient = new ObsClient(CloudConfig.ak, CloudConfig.sk, CloudConfig.endPoint);

    @Test
    public void  delete(){
        obsClient.deleteObject("xpu","test/");
    }

}
