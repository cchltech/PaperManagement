package com.cchl.entity;

public class Evaluate {

    private Integer id;

    private String content;

    private Byte score;

    private Integer evaluator;

    private Integer target;

    private Integer paperPlanId;

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