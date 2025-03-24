package com.max.todo_list.controller;

import com.max.todo_list.constant.ResultCodeConstant;
import com.max.todo_list.domain.TodoDO;
import com.max.todo_list.dto.CreateGroup;
import com.max.todo_list.dto.RestResult;
import com.max.todo_list.dto.TodoDTO;
import com.max.todo_list.dto.UpdateGroup;
import com.max.todo_list.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.lang.Boolean;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 待办事项管理
 *
 * @author huangxun
 * @date 2025-03-22 18:13:28
 */
@Tag(name = "待办事项管理")
@RequestMapping("todo")
@RestController
public class TodoController {

    @Autowired
    private TodoService todoService;

    /**
     * 新增待办事项:验证入参对象属性是否合法
     *
     * @param todoDTO 待办事项DTO实体类
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @Operation(summary = "新增待办事项:验证入参对象属性是否合法")
    @ResponseBody
    public RestResult<Boolean> addTodo(@RequestBody @Validated(CreateGroup.class) TodoDTO todoDTO) {
        Boolean result = todoService.addTodo(todoDTO);
        return new RestResult<>(ResultCodeConstant.CODE_SUCCESS, ResultCodeConstant.CODE_SUCCESS_MSG, result);
    }

    /**
     * 修改待办事项:验证待办事项ID是否存在
     *
     * @param todoDTO 待办事项DTO实体类
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @Operation(summary = "修改待办事项:验证待办事项ID是否存在")
    @ResponseBody
    public RestResult<Boolean> updateTodo(@RequestBody @Validated(UpdateGroup.class) TodoDTO todoDTO) {
        Boolean result = todoService.updateTodo(todoDTO);
        return new RestResult<>(ResultCodeConstant.CODE_SUCCESS, ResultCodeConstant.CODE_SUCCESS_MSG, result);
    }

    /**
     * 删除待办事项:验证待办事项ID是否存在
     *
     * @param todoDTO 待办事项DTO实体类
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @Operation(summary = "删除待办事项:验证待办事项ID是否存在")
    @ResponseBody
    public RestResult<Boolean> deleteTodo(@RequestBody @Validated TodoDTO todoDTO) {
        Boolean result = todoService.deleteTodo(todoDTO);
        return new RestResult<>(ResultCodeConstant.CODE_SUCCESS, ResultCodeConstant.CODE_SUCCESS_MSG, result);
    }

    /**
     * 查询所有待办事项:查询所有待办事项
     *
     * @return
     */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @Operation(summary = "查询所有待办事项:查询所有待办事项")
    public RestResult<List<TodoDO>> getAllTodos(String searchKey) {
        List<TodoDO> result = todoService.getAllTodos(searchKey);
        return new RestResult<>(ResultCodeConstant.CODE_SUCCESS, ResultCodeConstant.CODE_SUCCESS_MSG, result);
    }
}
