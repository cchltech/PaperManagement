package com.cchl.dao;

import com.cchl.entity.Task;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface TaskMapper {

    int insert(Task record);

    List<Task> selectAll();
}