package com.sou1fy.te;

import com.sou1fy.test.Application;
import com.sou1fy.dyniamic.ddb.DynamicDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = Application.class)
public class SpringTest {
    @Autowired
    DynamicDataSource dynamicDataSource;

    @Test
    public void test01() {
        dynamicDataSource.toString();
        List<String> lstr=new ArrayList<String>();
        lstr.getClass().getTypeParameters();
    }

}
