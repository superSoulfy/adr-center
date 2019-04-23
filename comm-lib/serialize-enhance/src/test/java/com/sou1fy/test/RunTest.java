package com.sou1fy.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sou1fy.serialize.demo.Application;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = Application.class)
public class RunTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Before
    public void before() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void test01() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/test/call")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("name", "jack").param("age", "25")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        //.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("SUCCESS")));
    }

}
