package com.zjh.clouddisk.controller.file;

import com.zjh.clouddisk.mapper.FileMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

/**
 * @author TheZJH
 * @version 1.0
 */
@Controller
public class TrashController {
    @Resource
    private FileMapper fileMapper;

    @GetMapping("/file/trash")
    public String toTrashPage() {

        return "file-trash";
    }
}
