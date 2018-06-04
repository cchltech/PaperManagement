package com.cchl.dao;

import com.cchl.entity.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface TaskMapper {

    int insert(Task record);

    List<Task> selectAll();

    int isExist(Integer paperId);

    int updateFilePath(@Param(value = "paperId") Integer paperId, @Param(value = "filePath") String filePath);

    Task selectByPaperId(Integer paperId);
}