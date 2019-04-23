package com.sou1fy.serialize.demo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.sou1fy.serialize.demo.annotation.FormatDateSimplify;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SerializerModifier extends BeanSerializerModifier {

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
                                                     List<BeanPropertyWriter> beanProperties) {
        isHit(beanDesc);
        for (int i = 0; i < beanProperties.size(); i++) {
            BeanPropertyWriter writer = beanProperties.get(i);
            if (isDate(writer)) {
                writer.assignSerializer(new JsonSerializer<Object>() {
                    @Override
                    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {
                        try {
                            gen.writeObject(value);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

        return beanProperties;
    }

    boolean isHit(BeanDescription beanDesc) {
        beanDesc.getClassInfo().fields().forEach(annotatedField -> System.out.println(annotatedField.getAnnotation(FormatDateSimplify.class)));
        return false;
    }

    private boolean isDate(BeanPropertyWriter writer) {
        JavaType javaType = writer.getType();
        Annotation[] annotations = javaType.getRawClass().getAnnotations();
        if (annotations.length > 0) {
            System.out.println(annotations);
        }
        return javaType.getRawClass().equals(Date.class);

    }

}
