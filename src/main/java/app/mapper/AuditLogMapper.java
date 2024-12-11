package app.mapper;

import app.domain.AuditLog;
import app.dto.AuditLogDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link AuditLog} and its DTO {@link AuditLogDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AuditLogMapper extends EntityMapper<AuditLogDTO, AuditLog> {}
