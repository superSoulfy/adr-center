package com.sou1fy.serialize.demo.fastjson;

import com.alibaba.fastjson.serializer.ValueFilter;

public class PropertyFilter implements ValueFilter {
    @Override
    public Object process(Object object, String name, Object value) {
        //todo  make bean's field value to be change
        return value;
    }
}
