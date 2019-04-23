package com.sou1fy.serialize.demo.controller;

import com.sou1fy.serialize.demo.TestPOJO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/test")
public class BizController {

    @ResponseBody
    @RequestMapping("/call")
    public TestPOJO testPOJO(@RequestParam("name") String name, @RequestParam("age") int age) {
        log.info("call user ---> name:{} age:{}", name, age);
        TestPOJO pojo = new TestPOJO();
        pojo.setAge(25);
        pojo.setBirthDay(new Date());
        pojo.setStartTime(new Date());
        pojo.setMoney(88.8);
        pojo.setId("A001");
        return pojo;
    }
}
