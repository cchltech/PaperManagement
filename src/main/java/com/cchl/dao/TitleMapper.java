package com.cchl.dao;

import com.cchl.entity.Title;
import java.util.List;

public interface TitleMapper {
    int insert(Title record);

    List<Title> selectAll();
}