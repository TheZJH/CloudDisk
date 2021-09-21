package com.zjh.clouddisk.controller.file;

import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.HeaderResponse;
import com.obs.services.model.ListBucketsRequest;
import com.obs.services.model.ObsBucket;
import com.zjh.clouddisk.util.CloudConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author TheZJH
 * @version 1.0
 */
@Controller
public class BucketController {
    /**
     * 创建ObsClient实例
     */
    final ObsClient obsClient = new ObsClient(CloudConfig.ak, CloudConfig.sk, CloudConfig.endPoint);

    @PostMapping ("/bucket/upload")
    public String uploadBucket(String bucketName, Map<String, Object> map, Model model) {
        /**
         * ● 桶的名字是全局唯一的，所以您需要确保不与已有的桶名称重复。
         * ● 桶命名规则如下：
         * ● 3～63个字符，数字或字母开头，支持小写字母、数字、“-”、“.”。
         * ● 禁止使用类IP地址。
         * ● 禁止以“-”或“.”开头及结尾。
         * ● 禁止两个“.”相邻（如：“my..bucket”）。
         * ● 禁止“.”和“-”相邻（如：“my-.bucket”和“my.-bucket”）。
         */
        boolean exists = obsClient.headBucket(bucketName);
        try {
            //创建桶成功
            HeaderResponse response = obsClient.createBucket(bucketName);
            String requestId = response.getRequestId();
        } catch (ObsException e) {
            //创建桶失败
            map.put("HTTP Code: ", e.getResponseCode());
            map.put("Error Code", e.getErrorCode());
            map.put("Error Message", e.getErrorMessage());
            map.put("Request ID", e.getErrorRequestId());
            map.put("Host ID", e.getErrorHostId());
            model.addAttribute("obsException", e);

        }
        return "bucket-add";
    }

    @GetMapping("/bucket/add")
    public String toAddBucketPage() {

        return "bucket-add";
    }

    @GetMapping("/bucket/delete")
    public String deleteBucket(Integer bucketId) {
        //删除桶
        obsClient.deleteBucket("bucketName");
        return "test";
    }

    @GetMapping("/bucket/list")
    public String bucketList(Model model) {
        ListBucketsRequest request = new ListBucketsRequest();
        request.setQueryLocation(true);
        List<ObsBucket> bucketList = obsClient.listBuckets(request);
        //BucketName,CreationDate,Location
        model.addAttribute("bucketList", bucketList);
        return "bucket-list";
    }
}
