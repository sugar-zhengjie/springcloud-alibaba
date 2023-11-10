package com.zj.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chen
 */
public class JsonUtils {


    private static final JsonHandler jsonHandler;

    private JsonUtils() {
    }

    static {
        jsonHandler = new JacksonHandler();
    }

    public static String toJson(Object object) {
        if (object == null) {
            return null;
        }

        return jsonHandler.toJson(object);
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        if (json == null) {
            return null;
        }
        if (clazz == null) {
            throw new IllegalArgumentException("clazz is null");
        }

        return jsonHandler.toObject(json, clazz);
    }

    public static <T> List<T> toArray(String json, Class<T> clazz) {
        if (json == null) {
            return null;
        }
        if (clazz == null) {
            throw new IllegalArgumentException("clazz is null");
        }

        return jsonHandler.toArray(json, clazz);
    }

    private interface JsonHandler {
        /**
         * toJson
         * @param object
         * @return
         */
        String toJson(Object object);

        /**
         * toObject
         * @param json
         * @param clazz
         * @param <T>
         * @return
         */
        <T> T toObject(String json, Class<T> clazz);

        /**
         * toArray
         * @param json
         * @param clazz
         * @param <T>
         * @return
         */
        <T> List<T> toArray(String json, Class<T> clazz);
    }

    private static class JacksonHandler implements JsonHandler {

        private static final com.fasterxml.jackson.databind.ObjectMapper OBJECT_MAPPER
                = new com.fasterxml.jackson.databind.ObjectMapper();

        @Override
        public String toJson(Object object) {
            try {
                return OBJECT_MAPPER.writeValueAsString(object);
            } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public <T> T toObject(String json, Class<T> clazz) {
            try {
                return OBJECT_MAPPER.readValue(json, clazz);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public <T> List<T> toArray(String json, Class<T> clazz) {
            com.fasterxml.jackson.databind.type.CollectionLikeType type =
                    OBJECT_MAPPER.getTypeFactory().constructCollectionLikeType(ArrayList.class, clazz);
            try {
                return OBJECT_MAPPER.readValue(json, type);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
