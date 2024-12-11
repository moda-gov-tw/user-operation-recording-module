package app.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 方法種類
 */
@Getter
@RequiredArgsConstructor
public enum MethodType {

    query("1", "查詢"),

    saveOrUpdate("2", "儲存"),

    delete("3", "刪除"),

    upload("4", "上傳檔案"),

    download("5", "下載檔案"),

    preview("6", "預覽"),

    insert("7", "新增"),

    update("8", "更新"),

    execute("9", "執行"),

    ;

    private final String code;
    private final String desc;

    private static final Map<String, MethodType> CODE_MAP = Arrays.stream(MethodType.values())
            .collect(Collectors.toMap(MethodType::getCode, Function.identity()));

    public static Optional<MethodType> ofCode(String code) {
        return Optional.ofNullable(CODE_MAP.get(code));
    }
}
