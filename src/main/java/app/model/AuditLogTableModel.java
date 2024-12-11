package app.model;

import app.dto.AuditLogDTO;
import app.enums.ExecStatus;
import app.enums.MethodType;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import javax.swing.table.AbstractTableModel;
import java.util.List;

@Setter
public class AuditLogTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = { "API", "執行種類", "執行狀態", "執行時間(毫秒)", "API參數", "建立時間", "錯誤訊息" };
    public static final int[] COLUMN_WIDTH = { 100, 70, 70, 110, 350, 150, 150 };

    private List<AuditLogDTO> auditLogDTOList;

    public AuditLogTableModel(List<AuditLogDTO> auditLogDTOList) {
        this.auditLogDTOList = auditLogDTOList;
    }

    @Override
    public int getRowCount() {
        return CollectionUtils.isEmpty(auditLogDTOList) ? 0 : auditLogDTOList.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        AuditLogDTO auditLogDTO = auditLogDTOList.get(rowIndex);
        if (auditLogDTO == null) {
            return null;
        }
        return switch (columnIndex) {
            case 0 -> auditLogDTO.getApiUrl();
            case 1 -> MethodType.ofCode(auditLogDTO.getMethodType())
                    .map(MethodType::getDesc)
                    .orElse(null);
            case 2 -> ExecStatus.ofCode(auditLogDTO.getStatus())
                    .map(ExecStatus::getDesc)
                    .orElse(null);
            case 3 -> auditLogDTO.getExecTime();
            case 4 -> auditLogDTO.getMethodParams();
            case 5 -> auditLogDTO.getCreateTime();
            case 6 -> auditLogDTO.getErrorMsg();
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }
}
