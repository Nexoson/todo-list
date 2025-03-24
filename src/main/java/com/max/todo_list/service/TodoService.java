package com.max.todo_list.service;

import com.max.todo_list.domain.TodoDO;
import com.max.todo_list.dto.TodoDTO;
import java.lang.Boolean;
import java.util.List;

/**
 * 待办事项管理
 *
 * @author huangxun
 * @date 2025-03-22 18:13:28
 */
public interface TodoService {

    /**
     * 新增待办事项:验证入参对象属性是否合法
     *
     * @param todoDTO 待办事项DTO实体类
     * @return
     */
    Boolean addTodo(TodoDTO todoDTO);

    /**
     * 修改待办事项:验证待办事项ID是否存在
     *
     * @param todoDTO 待办事项DTO实体类
     * @return
     */
    Boolean updateTodo(TodoDTO todoDTO);

    /**
     * 删除待办事项:验证待办事项ID是否存在
     *
     * @param todoDTO 待办事项DTO实体类
     * @return
     */
    Boolean deleteTodo(TodoDTO todoDTO);

    /**
     * 查询所有待办事项:查询所有待办事项
     *
     * @return
     */
    List<TodoDO> getAllTodos(String searchKey);
}
