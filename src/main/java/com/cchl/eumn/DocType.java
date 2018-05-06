package com.cchl.eumn;

public enum DocType {
    //开题报告
    OPEN_REPORT("open_report"),
    //中期检查
    MID_CHECK("mid_check"),
    //任务书
    TASK("task"),
    //周计划
    WEEKS_PLAN("weeks_plan");

    //类型名
    private String type;

    DocType(String type){
        this.type = type;
    }

    public String getType(){
        return this.type;
    }

}
