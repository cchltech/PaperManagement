package com.cchl.dao;

import com.cchl.entity.Title;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface TitleMapper {

    int insert(Title title);

    List<Title> selectAll();

    /**
     * 更新状态
     */
    int updateStatus(@Param(value = "id") int id, @Param(value = "status") byte status);

    /**
     * 查找未审核的题目集合
     */
    List<Title> selectByStatus(@Param(value = "page") int page, @Param(value = "limit") int limit, @Param(value = "status") int status, @Param(value = "all")boolean all, @Param(value = "departmentId") int DepartmentId);

    /**
     * @param all 是否需要查找所有条数
     * @param status 当all为false时，传如状态值，true时不作要求因为status不参与查询
     */
    int totalNumber(@Param(value = "all") boolean all, @Param(value = "status") byte status, @Param(value = "departmentId") int departmentId);

    /**
     * 按学院查找所有通过审核的题目
     */
    List<Title> selectWithParam(@Param(value = "departmentId") int departmentId,
                                @Param(value = "content") String content,
                                @Param(value = "teacherId") Long teacherId,
                                @Param(value = "page") int page, @Param(value = "limit") int limit);

    /**
     * 查找总条数
     * @param departmentId
     * @param content
     * @param teacherId
     * @return
     */
    int totalOfTitle(@Param(value = "departmentId") int departmentId,
                     @Param(value = "content") String content,
                     @Param(value = "teacherId") Long teacherId);

    /**
     * 按主键查找
     */
    Title selectById(int id);

    /**
     * 人数加一
     */
    int updateTotal(int titleId);

    /**
     * 查找未被学生选满的课程
     * @return
     */
    List<Title> selectUnFull();

}