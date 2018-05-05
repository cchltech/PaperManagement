package com.cchl.dao;

import com.cchl.entity.OpenReport;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface OpenReportMapper {

    int insert(OpenReport record);

    List<OpenReport> selectAll();
}