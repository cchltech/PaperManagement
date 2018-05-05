package com.cchl.dao;

import com.cchl.entity.Task;
import java.util.List;

public interface TaskMapper {
    int insert(Task record);

    List<Task> selectAll();
}