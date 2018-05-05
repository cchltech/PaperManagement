package com.cchl.dao;

import com.cchl.entity.OpenReport;
import java.util.List;

public interface OpenReportMapper {
    int insert(OpenReport record);

    List<OpenReport> selectAll();
}