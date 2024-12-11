package app.formatter;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FormatResult {

    private String result;
    private boolean needCompress;

    public FormatResult(final String result) {
        this(result, false);
    }
}
