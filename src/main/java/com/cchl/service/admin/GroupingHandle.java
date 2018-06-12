package com.cchl.service.admin;

import com.cchl.dao.StudentMapper;
import com.cchl.dao.TeacherMapper;
import com.cchl.entity.GroupInfo;
import com.cchl.entity.Student;
import com.cchl.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class GroupingHandle {
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private StudentMapper studentMapper;

    private int departmentId;

    int groupNumber;
    int restStudentNumber =0;
    List<GroupInfo> groupList = new ArrayList<>();

    public List<GroupInfo> getGroupList(int userId) {
        getDepartment(userId);
        setStudentGroup();
        setTeacherGroup();
        return groupList;
    }

    private void getDepartment(int userId) {
        this.departmentId =  teacherMapper.selectDepartmentIdByUserId(userId);
    }

    private void setStudentGroup() {
        int studentNumber = studentMapper.totalNumber(departmentId);
        groupNumber = studentNumber/4;
        if(studentNumber%4 != 0) {
            restStudentNumber = studentNumber%4;
            groupNumber++;
        }
        int groupInfo[][] = new int[groupNumber][4]; //存放每个小组组员编号
        Random rand = new Random();
        boolean[] isSelected = new boolean[studentNumber];
        int randInt;
        int group = 0;
        for(int i=0;i<studentNumber;i++) {
            // 每次循环前先初始化布尔数组
            isSelected[i]=false;
        }
        do{
            if (group == groupNumber-1 && restStudentNumber!=0) {
                // 判断是否为最后一组,因为可能存在不满四人的情况
                for(int i = 0;i < restStudentNumber; i++) {
                    do {
                        randInt = rand.nextInt(studentNumber);
                    }while(isSelected[randInt]);
                    isSelected[randInt] = true; //true表示该学生已被选择
                    groupInfo[group][i]=randInt+1;
                }
                group++;
            }
            else {
                for (int i = 0; i < 4; i++) {
                    do {
                        randInt = rand.nextInt(studentNumber);
                    } while (isSelected[randInt]);
                    isSelected[randInt] = true; //true表示该学生已被选择
                    groupInfo[group][i] = randInt;
                }
                group++;
            }
        }while (group < groupNumber);

        List<Student> students = studentMapper.selectByDepartmentId(departmentId);

        for (int j = 0;j < groupNumber;j++) {
            int i = 0;
            GroupInfo g = new GroupInfo();
            do {
                int student = groupInfo[j][i]; //提取数组存放的学生随机顺序数
                Student s = students.get(student);

                g.setDepartmentId(s.getDepartmentId());
                g.setGrade(s.getGrade());
                g.setMajorId(s.getMajorId());
                if (i == 0) {
                    g.setStudentId1(s.getId());
                    g.setStudentName1(s.getName());
                } else if (i == 1) {
                    g.setStudentId2(s.getId());
                    g.setStudentName2(s.getName());
                } else if (i == 2) {
                    g.setStudentId3(s.getId());
                    g.setStudentName3(s.getName());
                } else if (i == 3) {
                    g.setStudentId4(s.getId());
                    g.setStudentName4(s.getName());
                }
                i++;
            }while(groupInfo[j][i]==0); //判断是否有下一条数据
            groupList.add(g);
        }
    }

    private void setTeacherGroup() {
        int teacherNumber = teacherMapper.totalNumber(false, (byte) 1, departmentId);
        int allocation[] = new int[teacherNumber]; //存放每个老师分配到的组数
        Random rand = new Random();
        boolean[] isSelected = new boolean[teacherNumber]; //判断同一次循环中是否教师被重复选择
        int randInt;
        if (groupList.isEmpty()) {
            // 集合为空不进行操作
        }
        else {
            List<Teacher> teachers = teacherMapper.selectByDepartmentId(departmentId);

            int location = 0;
            do {
                GroupInfo g = groupList.get(location);// 获取当前小组信息的集合

                for(int i=0;i<teacherNumber;i++) {
                    // 每次循环前先初始化布尔数组
                    isSelected[i] = false;
                }

                int number;
                if (groupNumber==1 && restStudentNumber!=0) {
                    // 判断是否为最后一组,因为可能存在不满四人的情况
                    number = restStudentNumber;
                }
                else {
                    // 不为最后一组，则默认一组四人
                    number = 4;
                }

                for (int i = 0; i < number; i++) {
                    // 每小组随机选择"number"位老师
                    do {
                        randInt = rand.nextInt(teacherNumber);
                        if (allocation[randInt] >= 2) {
                            // 每位老师最多负责两个小组
                            isSelected[randInt] = true;
                        }
                    } while (isSelected[randInt]);
                    isSelected[randInt] = true; //true表示该教师已被选择
                    allocation[randInt]++;

                    Teacher t = teachers.get(randInt);

                    if (i == 0) {
                        g.setTeacherName1(t.getName());
                    } else if (i == 1) {
                        g.setTeacherName2(t.getName());
                    } else if (i == 2) {
                        g.setTeacherName3(t.getName());
                    } else if (i == 3) {
                        g.setTeacherName4(t.getName());
                    }
                }
                groupList.set(location,g);
                location++;
            }while(groupNumber-- > 0);
        }
    }
}
