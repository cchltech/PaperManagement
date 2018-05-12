package com.cchl.dao;

import com.cchl.entity.ChoiceTitle;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChoiceTitleMapper {

    ChoiceTitle selectByDepartmentId(int id);

    int insert();

}
