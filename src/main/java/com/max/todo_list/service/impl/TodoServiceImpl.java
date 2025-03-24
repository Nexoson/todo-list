package com.max.todo_list.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.max.todo_list.dao.TodoMapper;
import com.max.todo_list.domain.TodoDO;
import com.max.todo_list.dto.TodoDTO;
import com.max.todo_list.service.TodoService;

import java.lang.Boolean;
import java.lang.Override;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.max.todo_list.exception.BusinessException;
import com.max.todo_list.constant.ResultCodeConstant;

import java.util.Date;

/**
 * 待办事项管理的实现
 *
 * @author huangxun
 * @date 2025-03-22 18:13:28
 */
@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoMapper todoMapper;

    @Override
    public Boolean addTodo(TodoDTO todoDTO) {
        TodoDO todoDO = new TodoDO();
        todoDO.setTaskName(todoDTO.getTaskName());
        todoDO.setDueTime(todoDTO.getDueTime());
        todoDO.setPriority(todoDTO.getPriority());
        todoDO.setNotes(todoDTO.getNotes());
        todoDO.setIsCompleted(todoDTO.getIsCompleted());
        todoDO.setCreateBy("admin");
        todoDO.setCreateTime(new Date());
        try {
            int result = todoMapper.insert(todoDO);
            return result > 0;
        } catch (Exception e) {
            throw new BusinessException(ResultCodeConstant.CODE_999999, ResultCodeConstant.CODE_999999_MSG);
        }
    }

    @Override
    public Boolean updateTodo(TodoDTO todoDTO) {
        TodoDO existingTodo = todoMapper.selectById(todoDTO.getId());
        if (existingTodo == null) {
            throw new BusinessException(ResultCodeConstant.CODE_000001, ResultCodeConstant.CODE_000001_MSG);
        }
        TodoDO todoDO = new TodoDO();
        todoDO.setId(todoDTO.getId());
        todoDO.setTaskName(todoDTO.getTaskName());
        todoDO.setDueTime(todoDTO.getDueTime());
        todoDO.setPriority(todoDTO.getPriority());
        todoDO.setNotes(todoDTO.getNotes());
        todoDO.setIsCompleted(todoDTO.getIsCompleted());
        todoDO.setUpdateBy("admin");
        todoDO.setUpdateTime(new Date());
        try {
            int result = todoMapper.updateById(todoDO);
            return result > 0;
        } catch (Exception e) {
            throw new BusinessException(ResultCodeConstant.CODE_999999, ResultCodeConstant.CODE_999999_MSG);
        }
    }

    @Override
    public Boolean deleteTodo(TodoDTO todoDTO) {
        TodoDO existingTodo = todoMapper.selectById(todoDTO.getId());
        if (existingTodo == null) {
            throw new BusinessException(ResultCodeConstant.CODE_000001, ResultCodeConstant.CODE_000001_MSG);
        }
        try {
            int result = todoMapper.deleteById(todoDTO.getId());
            return result > 0;
        } catch (Exception e) {
            throw new BusinessException(ResultCodeConstant.CODE_999999, ResultCodeConstant.CODE_999999_MSG);
        }
    }

    @Override
    public List<TodoDO> getAllTodos(String searchKey) {
        try {
            TodoDO todoDO = new TodoDO();
            QueryWrapper<TodoDO> queryWrapper = Wrappers.query();
            queryWrapper.lambda().like(TodoDO::getTaskName, searchKey);
            if (StringUtils.isNoneBlank(searchKey)) {
                todoDO.setTaskName(searchKey);
            }
            return todoMapper.selectList(queryWrapper);
        } catch (Exception e) {
            throw new BusinessException(ResultCodeConstant.CODE_999999, ResultCodeConstant.CODE_999999_MSG);
        }
    }
}
