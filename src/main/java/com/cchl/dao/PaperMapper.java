package com.cchl.dao;

import com.cchl.entity.Paper;
import java.util.List;

public interface PaperMapper {
    int insert(Paper record);

    List<Paper> selectAll();
}