package com.cchl.entity.vo;

/**
 * 教师内容实体
 */
public class TeacherMessage {

    //消息内容
    private String content;
    //创建时间
    private String createTime;
    //学院id
    private Integer departmentId;
    //版本号
    private Integer version;

    @Override
    public String toString() {
        return "TeacherMessage{" +
                "content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                ", departmentId=" + departmentId +
                ", version=" + version +
                '}';
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
