package app.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 字串壓縮工具類
 */
public final class StringCompressUtils {

    private StringCompressUtils() {
        throw new AssertionError();
    }

    /**
     * 將字串資料壓縮再轉成base64格式
     */
    public static String compress(final String data) {
        if (data == null || data.length() == 0) {
            return data;
        }
        final byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        return encodeAsBase64(compress(bytes));
    }

    /**
     * 反轉base64格式再解壓縮回原字串資料
     */
    public static String uncompress(final String data) {
        if (data == null || data.length() == 0) {
            return data;
        }
        final byte[] result = uncompress(decodeBase64(data));
        return new String(result, StandardCharsets.UTF_8);
    }

    /**
     * 將字串資料壓縮再轉成Hex格式
     */
    public static String compressAsHex(final String data) {
        if (data == null || data.length() == 0) {
            return data;
        }
        final byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        return encodeAsHex(compress(bytes));
    }

    /**
     * 反轉Hex格式再解壓縮回原字串資料
     */
    public static String uncompressFromHex(final String data) {
        if (data == null || data.length() == 0) {
            return data;
        }
        final byte[] result = uncompress(decodeHex(data));
        return new String(result, StandardCharsets.UTF_8);
    }

    private static byte[] compress(final byte[] bytes) {
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final GZIPOutputStream gzip = new GZIPOutputStream(out);
            gzip.write(bytes);
            gzip.close();
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("資料壓縮失敗", e);
        }
    }

    private static byte[] uncompress(final byte[] bytes) {
        try (final GZIPInputStream in = new GZIPInputStream(new ByteArrayInputStream(bytes))) {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            IOUtils.copy(in, out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("資料解壓縮失敗", e);
        }
    }

    private static String encodeAsBase64(final byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private static byte[] decodeBase64(final String data) {
        return Base64.getDecoder().decode(data);
    }

    private static String encodeAsHex(final byte[] bytes) {
        return Hex.encodeHexString(bytes);
    }

    private static byte[] decodeHex(final String data) {
        try {
            return Hex.decodeHex(data);
        } catch (DecoderException e) {
            throw new RuntimeException(e);
        }
    }
}
