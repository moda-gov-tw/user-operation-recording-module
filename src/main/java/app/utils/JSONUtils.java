package app.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JSON 轉換工具類
 **/
public final class JSONUtils {
    private static final Logger log = LoggerFactory.getLogger(JSONUtils.class);

    private static final ObjectMapper MAPPER = JsonMapper.builder()
            .serializationInclusion(JsonInclude.Include.NON_NULL) // 略過 null
//            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .addModule(new JavaTimeModule())
            .addModule(new Jdk8Module())
            .build();

    private JSONUtils() {
        throw new AssertionError();
    }

    /**
     * 將物件轉成JSon String
     *
     * @param value
     * @return
     */
    public static String toJSONString(final Object value) {
        try {
            return MAPPER.writeValueAsString(value);
        } catch (Exception e) {
            log.warn("toJSONString fail, Object=" + value, e);
            return null;
        }
    }
}
