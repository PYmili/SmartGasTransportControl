package cyou.pymiliblog.smartgastransportcontrol.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.util.Objects;

/**
 * <h1>统一API响应格式封装类</h1>
 *
 * @param <T> 数据泛型类型
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ApiResponse<T> {

    // 状态码（推荐使用Code中的常量）
    private int code;
    // 提示信息
    private String message;
    // 数据内容（null时不序列化）
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功响应（带数据）
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), message, data);
    }

    /**
     * 成功响应（不带数据）
     */
    public static <T> ApiResponse<T> success(String message) {
        return success(message, null);
    }

    /**
     * 错误响应（自定义状态码）
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    /**
     * 快速构建常见错误响应
     */
    public static <T> ApiResponse<T> badRequest(String message) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }

    public static <T> ApiResponse<T> internalError(String message) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}