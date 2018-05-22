package com.cchl.eumn;

public enum DocType {
    //题目申请表
    TITLE_APPLY("TitleApply"),
    //开题报告
    OPEN_REPORT("OpenReport"),
    //中期检查
    MID_CHECK("MidCheck"),
    //任务书
    TASK("Task"),
    //周计划
    WEEKS_PLAN("WeeksPlan");

    //类型名
    private String type;

    DocType(String type){
        this.type = type;
    }

    public String getType(){
        return this.type;
    }

}
