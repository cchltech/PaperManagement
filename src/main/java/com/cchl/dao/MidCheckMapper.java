package com.cchl.dao;

import com.cchl.entity.MidCheck;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface MidCheckMapper {

    int insert(MidCheck record);

    List<MidCheck> selectAll();

    int isExist(Integer paperId);

    int updateFilePath(@Param(value = "paperId") Integer paperId, @Param(value = "filePath") String filePath);

    MidCheck selectByPaperId(Integer paperId);

    List<MidCheck> selectByDepartmentId(@Param(value = "departmentId") int departmentId,
                                        @Param(value = "page") int page,
                                        @Param(value = "limit") int limit);

    int selectByDepartmentIdCount(@Param(value = "departmentId") int departmentId);

    int updateStatus(@Param(value = "id")Integer id, @Param("status")int status);

}