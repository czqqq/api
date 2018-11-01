package com.api.controller.dto;


/**
 * 使用 datatables 插件的基本请求类参考 http://www.datatables.club/manual/server-side.html
 */
public class DatatablesReq{
    private Integer draw;
    private Integer start;
    private Integer length;

    public Integer getDraw() {
        return draw;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }
}