package com.max.todo_list.vo;

import lombok.Data;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 待办事项VO实体类
 *
 * @author huangxun
 * @date 2025-03-22 18:13:28
 */
@Data
public class TodoVO {

    /**
     * 待办事项ID:待办事项ID
     */
    @Schema(description = "待办事项ID:待办事项ID")
    private Integer id;

    /**
     * 任务名:任务名
     */
    @Schema(description = "任务名:任务名")
    private String taskName;

    /**
     * 时间:时间
     */
    @Schema(description = "时间:时间")
    private Date dueTime;

    /**
     * 优先级:优先级
     */
    @Schema(description = "优先级:优先级")
    private String priority;

    /**
     * 备注:备注
     */
    @Schema(description = "备注:备注")
    private String notes;

    /**
     * 完成状态:完成状态
     */
    @Schema(description = "完成状态:完成状态")
    private Boolean isCompleted;

    /**
     * 创建人:创建人
     */
    @Schema(description = "创建人:创建人")
    private String createBy;

    /**
     * 创建时间:创建时间
     */
    @Schema(description = "创建时间:创建时间")
    private Date createTime;

    /**
     * 修改人:修改人
     */
    @Schema(description = "修改人:修改人")
    private String updateBy;

    /**
     * 修改时间:修改时间
     */
    @Schema(description = "修改时间:修改时间")
    private Date updateTime;
}
