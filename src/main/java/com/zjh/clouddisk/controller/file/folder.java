package com.zjh.clouddisk.controller.file;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author TheZJH
 * @version 1.0
 */
@Controller
@RequestMapping("/file")
public class folder {
    @GetMapping("/folder")
    public String folder(){

        return "";
    }
}
