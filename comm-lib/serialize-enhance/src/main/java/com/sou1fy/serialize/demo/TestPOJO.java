package com.sou1fy.serialize.demo;

import com.alibaba.fastjson.annotation.JSONField;
import com.sou1fy.serialize.demo.annotation.FormatDateSimplify;
import com.sou1fy.serialize.demo.fastjson.POJOSerializer;
import lombok.Data;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
public class TestPOJO {
    private Date birthDay;
    @FormatDateSimplify
    private Date startTime;
    @JSONField(serializeUsing = POJOSerializer.class)
    private String id;
    private int age;
    private double money;
    private List<String> list = Arrays.asList("a", "b", "n");
    private int[] ints = {1, 2, 3};
}
