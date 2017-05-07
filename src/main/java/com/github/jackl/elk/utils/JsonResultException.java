package com.github.jackl.elk.utils;

/**
 * Created by jackl on 2017/2/15.
 */
public class JsonResultException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private String msg;
    private int code = 500;

    public JsonResultException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public JsonResultException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public JsonResultException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public JsonResultException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
