package app.service;

import app.domain.AuditLog;
import app.dto.AuditLogDTO;
import app.mapper.AuditLogMapper;
import app.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    private final AuditLogMapper auditLogMapper;

    @Transactional
    public AuditLogDTO save(final AuditLogDTO dto) {
        AuditLog entity = auditLogMapper.toEntity(dto);
        entity = auditLogRepository.save(entity);
        return auditLogMapper.toDto(entity);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public AuditLogDTO saveAuditLog(AuditLogDTO dto) {
        return this.save(dto);
    }

    public List<AuditLogDTO> findAll() {
        return auditLogMapper.toDto(auditLogRepository.findAll());
    }
}
