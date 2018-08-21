package com.nettyserver.common;



import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.io.Serializable;

@Data
@Slf4j
public class ServerResponse<T> implements Serializable {

    private static ObjectMapper mapper = new ObjectMapper();

    private boolean success;
    private int code;
    private String message;
    private T data;

    public ServerResponse(boolean success, int code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> String success(T data) {
        ServerResponse response = new ServerResponse(true, ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getDesc(), data);
        return getObjectStr(response);
    }

    public static <T> String noDataFound() {
        ServerResponse response = new ServerResponse(true, ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getDesc(), "");
        return getObjectStr(response);
    }

    public static <T> String internalError(String message) {
        ServerResponse response = new ServerResponse(false, ResponseCode.INTERNAL_ERROR.getCode(), message, "");
        return getObjectStr(response);
    }

    public static <T> String invalidParam(String field) {
        ServerResponse response = new ServerResponse(true, ResponseCode.INVALID_PARAM.getCode(), field + ResponseCode.INVALID_PARAM.getDesc(), "");
        return getObjectStr(response);
    }

    public static <T> String methodNotImpl(String message) {
        ServerResponse response = new ServerResponse(true, ResponseCode.METHOD_NOT_IMPL.getCode(), ResponseCode.METHOD_NOT_IMPL.getDesc() + message, "");
        return getObjectStr(response);
    }

    public static String getObjectStr(ServerResponse response) {
        String str = "";
        try {
            str = mapper.writeValueAsString(response);
        } catch (Exception e) {
            log.error("press error ", e);
        }
        return str;
    }

}