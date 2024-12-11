package app.context;

import app.annotation.AuditLog;
import app.dto.AuditLogDTO;
import com.google.common.base.Stopwatch;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ThreadLocalData {

    private String apiUrl;

    private String userIp;

    private AuditLogDTO auditLogDTO;

    private AuditLog auditLog;

    private long threadId;

    private Stopwatch stopwatch;
}
