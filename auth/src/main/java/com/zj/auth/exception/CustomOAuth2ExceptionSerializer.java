package com.zj.auth.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/15 16:20
 */
public class CustomOAuth2ExceptionSerializer extends StdSerializer<CustomOAuth2Exception> {

    protected CustomOAuth2ExceptionSerializer() {
        super(CustomOAuth2Exception.class);
    }

    @Override
    public void serialize(CustomOAuth2Exception e, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("code", e.getHttpErrorCode());
        jsonGenerator.writeStringField("msg", e.getOAuth2ErrorCode());
        jsonGenerator.writeEndObject();
    }
}