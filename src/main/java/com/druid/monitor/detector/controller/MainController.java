package com.druid.monitor.detector.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

  @RequestMapping("/")
  @ResponseBody
  public String index() {
    return "<a href='/druid'>Druid Monitor</a><br/><br/>Proudly handcrafted by " + 
        "<a href='http://jawf.site'>Jawf Neo Lee</a> :)";
  }

}
