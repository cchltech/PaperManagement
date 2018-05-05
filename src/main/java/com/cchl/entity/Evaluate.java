package com.cchl.entity;

/**
 * 评价表
 */
public class Evaluate {
    //编号
    private Integer id;
    //评语
    private String content;
    //分数
    private Byte score;
    //评价人的账户编号
    private Integer evaluator;
    //被评价人的账户编号
    private Integer target;
    //论文计划编号
    private Integer paperPlanId;
    //关联评价人
    private User userEvaluator;
    //关联被评价人
    private User userTarget;
    //关联论文计划
    private PaperPlan paperPlan;

    public User getUserEvaluator() {
        return userEvaluator;
    }

    public void setUserEvaluator(User userEvaluator) {
        this.userEvaluator = userEvaluator;
    }

    public User getUserTarget() {
        return userTarget;
    }

    public void setUserTarget(User userTarget) {
        this.userTarget = userTarget;
    }

    public PaperPlan getPaperPlan() {
        return paperPlan;
    }

    public void setPaperPlan(PaperPlan paperPlan) {
        this.paperPlan = paperPlan;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Byte getScore() {
        return score;
    }

    public void setScore(Byte score) {
        this.score = score;
    }

    public Integer getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(Integer evaluator) {
        this.evaluator = evaluator;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

    public Integer getPaperPlanId() {
        return paperPlanId;
    }

    public void setPaperPlanId(Integer paperPlanId) {
        this.paperPlanId = paperPlanId;
    }
}