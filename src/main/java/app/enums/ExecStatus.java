package app.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum ExecStatus {

    execute("1", "執行中"),

    success("2", "執行成功"),

    fail("3", "執行失敗"),

    attack("4", "被攻擊")

    ;

    private final String code;
    private final String desc;

    private static final Map<String, ExecStatus> CODE_MAP = Arrays.stream(ExecStatus.values())
            .collect(Collectors.toMap(ExecStatus::getCode, Function.identity()));

    public static Optional<ExecStatus> ofCode(String code) {
        return Optional.ofNullable(CODE_MAP.get(code));
    }
}
