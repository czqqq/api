package com.api.controller.dto;

import java.util.HashMap;
import java.util.Map;

public class BaseResult {
    private String code ="200";
    private String message ="成功";
    private Map<String,Object> datas;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getDatas() {
        return datas;
    }

    public void setDatas(Map<String, Object> datas) {
        this.datas = datas;
    }
}
