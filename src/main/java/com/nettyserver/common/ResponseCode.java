package com.nettyserver.common;

public enum ResponseCode {

    SUCCESS(200, "success"),
    INVALID_PARAM(400, " is required"),
    NO_DATA_FOUND(404, "no data found"),
    INTERNAL_ERROR(500, "failed"),
    METHOD_NOT_IMPL(501, "not found method:");

    private final int code;
    private final String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
