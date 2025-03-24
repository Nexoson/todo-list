package com.max.todo_list.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.max.todo_list.domain.TodoDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 待办事项Mapper接口
 *
 * @author huangxun
 * @date 2025-03-22 18:13:28
 */
@Mapper
public interface TodoMapper extends BaseMapper<TodoDO> {
    // 由于继承了BaseMapper，所以优先使用BaseMapper上提供的方法实现
}
