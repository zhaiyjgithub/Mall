package com.westriver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by zack on 2018/11/2.
 */
@Controller
public class WebViewController {
    @RequestMapping("/index")
    public String index() {
        return "index";
    }
}
