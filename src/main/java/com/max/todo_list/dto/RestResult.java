package com.max.todo_list.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 响应结果
 *
 * @author huangxun
 * @date 2025-03-22 18:13:28
 */
@Data
@Schema(description = "响应结果")
public class RestResult<T> {

    /**
     * 业务返回码
     */
    @Schema(description = "业务返回码")
    private String code;

    /**
     * 业务提示信息
     */
    @Schema(description = "业务提示信息")
    private String msg;

    /**
     * 业务数据
     */
    @Schema(description = "业务数据")
    private T data;

    public RestResult() {
    }

    public RestResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public RestResult(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

}
