package app.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class AuditLogDTO implements Serializable {

    private Long id;

    @Size(max = 100)
    private String apiUrl;

    @Size(max = 50)
    private String serviceClass;

    @Size(max = 50)
    private String serviceMethod;

    @Size(max = 1)
    private String methodType;

    @Size(max = 1)
    private String status;

    private Integer execTime;

    @Size(max = 20)
    private String userIp;

    @Size(max = 200)
    private String errorMsg;

    @Size(max = 4000)
    private String methodParams;

    private LocalDateTime createTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuditLogDTO auditLogDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, auditLogDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
