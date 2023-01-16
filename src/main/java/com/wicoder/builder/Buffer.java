package com.wicoder.builder;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Buffer {

    final public static Logger logger = LoggerFactory.getLogger(Buffer.class);
    final private static String JWT = "%s -> Message: {} ";

    final public static String toJSON(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            return ex.getMessage();
        }
    }


    final public static <T extends Object> T jsonToObject(String data, Class<T> entity) throws JsonProcessingException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            T ob = mapper.readValue(data, entity);
            return ob;
        } catch (Exception ex) {
            Buffer.log("Error when you try to convert String to Json " + ex.getMessage());
            return null;

        }
    }

    final public static <T extends Object> T jsonToObjectUnknowns(String data, Class<T> entity) throws JsonProcessingException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            T ob = mapper.readValue(data, entity);
            return ob;
        } catch (Exception ex) {
            Buffer.log("Error when you try to convert String to Json " + ex.getMessage());
            return null;

        }
    }

    //<editor-fold defaultstate="collapsed" desc="Logger">
    final public static void log(Object obj) {
        logger.info(Buffer.toJSON(obj));
    }

    final public static void log(String message) {
        logger.info(message);
    }

    final public static void auth(String msg, Object exception) {
        logger.info(String.format(JWT, msg), exception);
    }

    final public static void log(String message, Object exception) {
        logger.info(message, exception);
    }

    final public static void log(String message, Object... exception) {
        logger.info(message, exception);
    }

    final public static void warn(String message) {
        logger.warn(message);
    }

    final public static void warn(String message, Object exception) {
        logger.warn(message, exception);
    }

    final public static void error(String message, Object exception) {
        logger.error(message, exception);
    }

    final public static void error(String message) {
        logger.error(message);
    }
//</editor-fold>
}