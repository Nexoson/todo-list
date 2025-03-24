package com.max.todo_list.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 待办事项DTO实体类
 *
 * @author huangxun
 * @date 2025-03-22 18:13:28
 */
@Data
public class TodoDTO {

    /**
     * 待办事项ID:待办事项ID
     */
    @NotNull(groups = { UpdateGroup.class }, message = "待办事项ID不能为空")
    @Schema(description = "待办事项ID:待办事项ID")
    private Integer id;

    /**
     * 任务名:任务名，必填
     */
    @NotBlank(groups = { CreateGroup.class }, message = "任务名不能为空")
    @Schema(description = "任务名:任务名，必填")
    private String taskName;

    /**
     * 时间:时间，必填
     */
    @NotNull(groups = { CreateGroup.class }, message = "时间不能为空")
    @Schema(description = "时间:时间，必填")
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
     * 完成状态:完成状态，必填
     */
    @NotNull(groups = { CreateGroup.class }, message = "完成状态不能为空")
    @Schema(description = "完成状态:完成状态，必填")
    private Boolean isCompleted;
}
