package com.cchl.dao;

import com.cchl.entity.Paper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PaperMapper {

    int insert(Paper record);

    List<Paper> selectAll();

    int isExist(Integer paperId);

    int updateFilePath(@Param(value = "paperId") Integer paperId, @Param(value = "filePath") String filePath);

    Paper selectByPaperId(Integer paperId);

    int updatePaper(@Param(value = "studentId") Long studentId, @Param(value = "grade") int grade, @Param(value = "content") String content);
}