package com.sou1fy.dyniamic.demo;

import com.sou1fy.dyniamic.test.TestBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Autowired
	TestBean testBean;

	@Test
	public void contextLoads() {
		testBean.sayHi();
	}

}
