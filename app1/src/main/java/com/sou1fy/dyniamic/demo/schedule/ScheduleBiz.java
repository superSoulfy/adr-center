package com.sou1fy.dyniamic.demo.schedule;

import com.sou1fy.dyniamic.ddb.RouterUtils;
import com.sou1fy.dyniamic.test.TestBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleBiz {

    @Autowired(required = false)
    private TestBean testBean;

    @Autowired
    private RouterUtils routerUtils;

    @Scheduled(cron = "*/20 * * * * *")
    public void doSomeThings() {
        System.out.println("123456/...");
        testBean.sayHi();
        routerUtils.excute("200", ScheduleBiz::process);
    }

    private static void process() {
        System.out.println("do bizzzzzzzzzz!");
    }
}
