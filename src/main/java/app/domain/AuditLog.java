package app.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Entity
@Table(name = "audit_log")
public class AuditLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 100)
    @Column(name = "api_url", length = 100)
    private String apiUrl;

    @Size(max = 50)
    @Column(name = "service_class", length = 50)
    private String serviceClass;

    @Size(max = 50)
    @Column(name = "service_method", length = 50)
    private String serviceMethod;

    @Size(max = 1)
    @Column(name = "method_type", length = 1)
    private String methodType;

    @Size(max = 1)
    @Column(name = "status", length = 1)
    private String status;

    @Column(name = "exec_time")
    private Integer execTime;

    @Size(max = 20)
    @Column(name = "user_ip", length = 20)
    private String userIp;

    @Size(max = 200)
    @Column(name = "error_msg", length = 200)
    private String errorMsg;

    @Size(max = 4000)
    @Column(name = "method_params", length = 4000)
    private String methodParams;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuditLog)) {
            return false;
        }
        return id != null && id.equals(((AuditLog) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
