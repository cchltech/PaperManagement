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
    List<Title> selectUnaudited(int page, int number);

    /**
     * @param all 是否需要查找所有条数
     * @param status 当all为false时，传如状态值，true时不作要求因为status不参与查询
     */
    int totalNumber(@Param(value = "all") boolean all, @Param(value = "status") byte status);

}