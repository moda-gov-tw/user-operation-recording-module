package app.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum ApiTest {

    getApi1("GET api", RequestMethod.GET, "/api/testApi1", null),
    postApi("POST api", RequestMethod.POST, "/api/testApi2", Map.of("param1", "value1", "param2", "value2")),
    errorApi("Error api", RequestMethod.POST, "/api/testApi3", Map.of("param1", "value1")),
    ;

    private final String buttonName;
    private final RequestMethod method;
    private final String url;
    private final Map<String, String> params;
}
