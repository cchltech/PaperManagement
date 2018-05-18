package com.cchl.eumn;

public enum DocType {
    //开题报告
    OPEN_REPORT("OpenReport"),
    //中期检查
    MID_CHECK("MidReport"),
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
