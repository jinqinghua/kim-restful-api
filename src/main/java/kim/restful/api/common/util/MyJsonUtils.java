package kim.restful.api.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@UtilityClass
@Slf4j
public class MyJsonUtils {
    private static final String JSON_WARNING = "JsonWarning";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static String stringify(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof String string) {
            return string;
        }

        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            log.warn(JSON_WARNING, e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> parse(String json, @NonNull Class<T> clazz) {
        if (null == json) {
            return Optional.empty();
        }
        if (clazz.equals(String.class)) {
            return Optional.of((T) json);
        }

        try {
            return Optional.of(objectMapper.readValue(json, clazz));
        } catch (Exception e) {
            log.warn(JSON_WARNING, e);
            return Optional.empty();
        }
    }

    public static <T> Optional<T> parse(String json, @NonNull TypeReference<T> typeReference) {
        if (null == json) {
            return Optional.empty();
        }
        try {
            return Optional.of(objectMapper.readValue(json, typeReference));
        } catch (Exception e) {
            log.warn(JSON_WARNING, e);
            return Optional.empty();
        }
    }

    public static <T> Optional<T> convert(Object fromObject, @NonNull TypeReference<T> toTypeReference) {
        if (null == fromObject) {
            return Optional.empty();
        }
        try {
            return Optional.of(objectMapper.convertValue(fromObject, toTypeReference));
        } catch (Exception e) {
            log.warn(JSON_WARNING, e);
            return Optional.empty();
        }
    }

    public static JsonNode readTree(String json) {
        if (null == json || json.length() == 0) {
            return null;
        }
        try {
            return objectMapper.readTree(json);
        } catch (Exception e) {
            log.error("JSON::readTree error", e);
            return null;
        }
    }
}
