package app;

import app.service.AuditLogService;
import app.window.ApiTestWindow;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.client.RestTemplate;

import java.awt.*;

@SpringBootApplication
public class UserOperationRecordingModuleApplication {

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");

        final ConfigurableApplicationContext context = SpringApplication.run(UserOperationRecordingModuleApplication.class, args);
        final RestTemplate restTemplate = context.getBean(RestTemplate.class);
        final AuditLogService service = context.getBean(AuditLogService.class);

        EventQueue.invokeLater(() -> {
            final ApiTestWindow window = new ApiTestWindow(restTemplate, service);
            window.getJFrame().setVisible(true);
        });
    }

}
