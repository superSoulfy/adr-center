package com.sou1fy.serialize.demo.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.sou1fy.serialize.demo.annotation.FormatDateSimplify;

import java.io.IOException;
import java.util.List;

public class SerializerModifier extends BeanSerializerModifier {

    private static final JsonSerializer JSON_SERIALIZER = new JsonSerializer<Object>() {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            //todo costumize
            gen.writeObject(value);
        }
    };

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
                                                     List<BeanPropertyWriter> beanProperties) {
        for (BeanPropertyWriter writer : beanProperties) {
            if (isExplicitByAnnotation(writer)) {
                writer.assignSerializer(JSON_SERIALIZER);
            }
        }
        return beanProperties;
    }

    private boolean isExplicitByAnnotation(BeanPropertyWriter writer) {
        return writer.getAnnotation(FormatDateSimplify.class) != null;

    }

}
