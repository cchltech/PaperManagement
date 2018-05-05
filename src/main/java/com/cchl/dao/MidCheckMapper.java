package com.cchl.dao;

import com.cchl.entity.MidCheck;
import java.util.List;

public interface MidCheckMapper {
    int insert(MidCheck record);

    List<MidCheck> selectAll();
}