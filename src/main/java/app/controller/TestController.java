package app.controller;

import app.annotation.AuditLog;
import app.annotation.LogParam;
import app.enums.MethodType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class TestController {

    @AuditLog(methodType = MethodType.query)
    @GetMapping("/testApi1")
    public String testApi1() {
        return "api 1 response";
    }

    @AuditLog(methodType = MethodType.insert)
    @PostMapping("/testApi2")
    public String testApi2(@RequestBody @LogParam Map<String, String> params) {
        return "api 2 response";
    }

    @AuditLog(methodType = MethodType.execute)
    @PostMapping("/testApi3")
    public String testApi3(@RequestBody @LogParam Map<String, String> params) {
        throw new RuntimeException("api 3 error");
    }
}
