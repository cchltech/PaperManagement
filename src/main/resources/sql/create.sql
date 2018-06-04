CREATE DATABASE paper_management;
USE paper_management;
# 论文计划表
CREATE TABLE paper_plan(
  id INT AUTO_INCREMENT NOT NULL ,
  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  title_id INT COMMENT '所属题目',
  PRIMARY KEY (id)
)CHARACTER SET=utf8 AUTO_INCREMENT = 1000 COMMENT '论文计划表';
# 账户表
CREATE TABLE user(
  id INT AUTO_INCREMENT NOT NULL ,
  status TINYINT DEFAULT 0,
  type TINYINT DEFAULT 0,
  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (id)
)AUTO_INCREMENT = 1000 COMMENT '账户表';
# 账户与论文计划的关联表
CREATE TABLE user_paper(
  id INT AUTO_INCREMENT NOT NULL ,
  user_id INT NOT NULL ,
  paper_plan_id INT NOT NULL ,
  PRIMARY KEY (id)
)AUTO_INCREMENT = 1000 COMMENT '账户与论文计划的关系表';
# 添加外键约束
ALTER TABLE user_paper ADD CONSTRAINT fk_user_id_paper FOREIGN KEY user_paper(user_id) REFERENCES user(id);
ALTER TABLE user_paper ADD CONSTRAINT fk_plan_id_paper FOREIGN KEY user_paper(paper_plan_id) REFERENCES paper_plan(id);
# 管理员信息表
CREATE TABLE admin_info(
  id INT AUTO_INCREMENT NOT NULL ,
  power TINYINT NOT NULL COMMENT '该账户拥有的权限' ,
  user_id INT NOT NULL COMMENT '关联账户的id' ,
  PRIMARY KEY (id)
)AUTO_INCREMENT = 1000 CHARSET = utf8 COMMENT '管理员信息表，因为一个账户可能有多个权限，所以用一张表单独管理';
# 添加外键约束
ALTER TABLE admin_info ADD CONSTRAINT fk_admin_info_user FOREIGN KEY admin_info(user_id) REFERENCES user(id) ;
# 学院表
CREATE TABLE department(
  id INT NOT NULL ,
  name VARCHAR(30) ,
  PRIMARY KEY (id)
)CHARACTER SET = utf8 COMMENT '学院表';
# 专业表
CREATE TABLE major(
  id INT NOT NULL ,
  name VARCHAR(30) ,
  department_id INT NOT NULL ,
  PRIMARY KEY (id)
)CHARACTER SET = utf8 COMMENT '专业表';
ALTER TABLE major ADD CONSTRAINT fk_major_department FOREIGN KEY major(department_id) REFERENCES department(id);
# 学生表
CREATE TABLE student(
  id BIGINT NOT NULL ,
  name VARCHAR(8) NOT NULL COMMENT '姓名' ,
  password VARCHAR(50) NOT NULL COMMENT '账目密码' ,
  sex TINYINT NOT NULL ,
  phone BIGINT NOT NULL ,
  email VARCHAR(18) ,
  department_id INT NOT NULL COMMENT '学院对应的id，关联学院表' ,
  grade TINYINT NOT NULL COMMENT '年级' ,
  major_id INT NOT NULL COMMENT '专业对应的id，关联专业表',
  user_id INT NOT NULL ,
  PRIMARY KEY (id)
)CHARACTER SET = utf8 COMMENT '学生表';
# 添加外键约束
ALTER TABLE student ADD CONSTRAINT fk_student_user FOREIGN KEY student(user_id) REFERENCES user(id) ON DELETE CASCADE ON UPDATE CASCADE ;
ALTER TABLE student ADD CONSTRAINT fk_student_department FOREIGN KEY student(department_id) REFERENCES department(id)  ON DELETE CASCADE ON UPDATE CASCADE ;
ALTER TABLE student ADD CONSTRAINT fk_student_major FOREIGN KEY student(major_id) REFERENCES major(id)  ON DELETE CASCADE ON UPDATE CASCADE ;
# 教师表
CREATE TABLE teacher(
  id BIGINT AUTO_INCREMENT NOT NULL ,
  name VARCHAR(8) NOT NULL ,
  password VARCHAR(50) NOT NULL COMMENT '账户密码',
  sex TINYINT NOT NULL ,
  phone BIGINT NOT NULL ,
  email VARCHAR(18) ,
  department_id INT NOT NULL COMMENT '所属学院',
  user_id INT NOT NULL COMMENT '账户编号',
  PRIMARY KEY (id)
)CHARACTER SET = utf8 COMMENT '教师表';
# 添加外键约束
ALTER TABLE teacher ADD CONSTRAINT fk_teacher_user FOREIGN KEY teacher(user_id) REFERENCES user(id) ON DELETE CASCADE ON UPDATE CASCADE ;
ALTER TABLE teacher ADD CONSTRAINT fk_teacher_department FOREIGN KEY teacher(department_id) REFERENCES department(id) ON DELETE CASCADE ON UPDATE CASCADE ;
# 题目表
CREATE TABLE title(
  id INT AUTO_INCREMENT NOT NULL ,
  content VARCHAR(255) NOT NULL COMMENT '题目内容' ,
  instruction VARCHAR(255) COMMENT '题目说明' ,
  status TINYINT COMMENT '题目当前状态，是否通过审核' ,
  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ,
  total_number INT NOT NULL COMMENT '待选人数',
  has_select INT DEFAULT 0 ,
  department_id INT NOT NULL ,
  teacher_id BIGINT NOT NULL ,
  PRIMARY KEY (id)
)AUTO_INCREMENT = 1000 CHARSET = utf8 COMMENT '题目表，用于记录论文题目的内容';
ALTER TABLE title ADD CONSTRAINT fk_title_department FOREIGN KEY title(department_id) REFERENCES department(id) ON DELETE CASCADE ON UPDATE CASCADE ;
ALTER TABLE title ADD CONSTRAINT fk_teacher_id FOREIGN KEY title(teacher_id) REFERENCES teacher(id);
# 周计划表
CREATE TABLE weeks_plan(
  id INT AUTO_INCREMENT NOT NULL ,
  file_path VARCHAR(50) COMMENT '该文件的存储路径' ,
  paper_plan_id INT NOT NULL COMMENT '管理论文计划的id' ,
  PRIMARY KEY (id)
)AUTO_INCREMENT = 1000 COMMENT '周计划表';
# 添加外键约束
ALTER TABLE weeks_plan ADD CONSTRAINT fk_weeks_paper_plan FOREIGN KEY weeks_plan(paper_plan_id) REFERENCES paper_plan(id) ON UPDATE CASCADE ON DELETE CASCADE ;
# 任务书
CREATE TABLE task(
  id INT AUTO_INCREMENT NOT NULL ,
  file_path VARCHAR(50) COMMENT '该文件的存储路径' ,
  paper_plan_id INT NOT NULL COMMENT '管理论文计划的id' ,
  PRIMARY KEY (id)
)AUTO_INCREMENT = 1000 COMMENT '任务书表';
# 添加外键约束
ALTER TABLE task ADD CONSTRAINT fk_task_paper_plan FOREIGN KEY task(paper_plan_id) REFERENCES paper_plan(id) ON DELETE CASCADE ON UPDATE CASCADE ;
# 开题报告
CREATE TABLE open_report(
  id INT AUTO_INCREMENT NOT NULL ,
  file_path VARCHAR(50) COMMENT '该文件的存储路径' ,
  paper_plan_id INT NOT NULL COMMENT '管理论文计划的id' ,
  PRIMARY KEY (id)
)AUTO_INCREMENT = 1000 COMMENT '开题报告表';
# 添加外键约束
ALTER TABLE open_report ADD CONSTRAINT fk_open_paper_plan FOREIGN KEY open_report(paper_plan_id) REFERENCES paper_plan(id) ON DELETE CASCADE ON UPDATE CASCADE ;
# 中期检查
CREATE TABLE mid_check(
  id INT AUTO_INCREMENT NOT NULL ,
  file_path VARCHAR(50) COMMENT '该文件的存储路径' ,
  paper_plan_id INT NOT NULL COMMENT '管理论文计划的id' ,
  PRIMARY KEY (id)
)AUTO_INCREMENT = 1000 COMMENT '中期检查表';
# 添加外键约束
ALTER TABLE mid_check ADD CONSTRAINT fk_mid_paper_plan FOREIGN KEY mid_check(paper_plan_id) REFERENCES paper_plan(id) ON UPDATE CASCADE ON DELETE CASCADE ;
# 论文
CREATE TABLE paper(
  id INT AUTO_INCREMENT NOT NULL ,
  file_path VARCHAR(50) COMMENT '该文件的存储路径' ,
  score INT DEFAULT 0 COMMENT '论文成绩，默认为0' ,
  paper_plan_id INT NOT NULL COMMENT '管理论文计划的id' ,
  PRIMARY KEY (id)
)AUTO_INCREMENT = 1000 COMMENT '用于记录论文信息';
# 添加外键约束
ALTER TABLE paper ADD CONSTRAINT fk_paper_paper_plan FOREIGN KEY paper(paper_plan_id) REFERENCES paper_plan(id) ON DELETE CASCADE ON UPDATE CASCADE ;
# 评价表
CREATE TABLE evaluate(
  id INT AUTO_INCREMENT NOT NULL ,
  content VARCHAR(255) COMMENT '评语' ,
  score TINYINT NOT NULL COMMENT '评分',
  evaluator INT NOT NULL COMMENT '评价人' ,
  target INT NOT NULL COMMENT '被评价人' ,
  paper_plan_id INT NOT NULL COMMENT '管理论文计划的id' ,
  PRIMARY KEY (id)
)AUTO_INCREMENT = 1000 CHARACTER SET = utf8 COMMENT '评价表，用于记录师生之间互评的数据';
# 添加外键约束
ALTER TABLE evaluate ADD CONSTRAINT fk_evaluator_user FOREIGN KEY evaluate(evaluator) REFERENCES user(id) ON DELETE CASCADE ON UPDATE CASCADE ;
ALTER TABLE evaluate ADD CONSTRAINT fk_target_user FOREIGN KEY evaluate(target) REFERENCES user(id) ON DELETE CASCADE ON UPDATE CASCADE ;
ALTER TABLE evaluate ADD CONSTRAINT fk_evaluate_paper_plan FOREIGN KEY evaluate(paper_plan_id) REFERENCES paper_plan(id) ON DELETE CASCADE ON UPDATE CASCADE ;
# 时间表
CREATE TABLE timer(
  id INT AUTO_INCREMENT NOT NULL ,
  content VARCHAR(255) COMMENT '任务说明' ,
  begin_time DATETIME NOT NULL COMMENT '开始时间',
  end_time DATETIME NOT NULL COMMENT '结束时间',
  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ,
  target_type TINYINT DEFAULT 0 COMMENT '设置此任务面向的用户类型，默认为0，表示向所有用户开放' ,
  PRIMARY KEY (id)
)AUTO_INCREMENT = 1000 CHARACTER SET = utf8 COMMENT '专业管理院可以设置时段让用户进行某些特定操作，如规定学生在一定时间内完成选题等，此表用于记录';
# 选题表
CREATE TABLE choice_title(
  id INT AUTO_INCREMENT NOT NULL ,
  begin_time DATETIME NOT NULL COMMENT '开始时间',
  end_time DATETIME NOT NULL COMMENT '结束时间',
  department_id INT NOT NULL COMMENT '面向的学院',
  PRIMARY KEY (id)
)AUTO_INCREMENT = 1000 CHARACTER SET = utf8 COMMENT '选题表，管理员为各个学院设置选题时间';
ALTER TABLE choice_title ADD CONSTRAINT fk_choice_department FOREIGN KEY choice_title(id) REFERENCES department(id);
# 分组表
CREATE TABLE group(
  id INT AUTO_INCREMENT NOT NULL COMMENT '组号',
  student1_id BIGINT NOT NULL ,
  student2_id BIGINT ,
  student3_id BIGINT ,
  student4_id BIGINT ,
  student1_name VARCHAR(8) NOT NULL ,
  student2_name VARCHAR(8) ,
  student3_name VARCHAR(8) ,
  student4_name VARCHAR(8) ,
  department_id INT NOT NULL ,
  grade TINYINT NOT NULL ,
  major_id INT NOT NULL ,
  teacher1_name VARCHAR(8) ,
  teacher2_name VARCHAR(8) ,
  teacher3_name VARCHAR(8) ,
  teacher4_name VARCHAR(8) ,
  PRIMARY KEY (id)
)AUTO_INCREMENT = 1000 CHARACTER SET = utf8 COMMENT '分组表，用于记录小组与教师之间的分配关系';
# 添加外键约束
ALTER TABLE grouping ADD CONSTRAINT fk_grouping_department FOREIGN KEY grouping(department_id) REFERENCES department(id);
ALTER TABLE grouping ADD CONSTRAINT fk_grouping_major FOREIGN KEY grouping(major_id) REFERENCES major(id);
ALTER TABLE grouping ADD CONSTRAINT fk_grouping_student FOREIGN KEY grouping(student1_id) REFERENCES student(id);
ALTER TABLE grouping ADD CONSTRAINT fk_grouping_student FOREIGN KEY grouping(student2_id) REFERENCES student(id);
ALTER TABLE grouping ADD CONSTRAINT fk_grouping_student FOREIGN KEY grouping(student3_id) REFERENCES student(id);
ALTER TABLE grouping ADD CONSTRAINT fk_grouping_student FOREIGN KEY grouping(student4_id) REFERENCES student(id);
ALTER TABLE grouping ADD CONSTRAINT fk_grouping_teacher FOREIGN KEY grouping(teacher1_name) REFERENCES teacher(name);
ALTER TABLE grouping ADD CONSTRAINT fk_grouping_teacher FOREIGN KEY grouping(teacher2_name) REFERENCES teacher(name);
ALTER TABLE grouping ADD CONSTRAINT fk_grouping_teacher FOREIGN KEY grouping(teacher3_name) REFERENCES teacher(name);
ALTER TABLE grouping ADD CONSTRAINT fk_grouping_teacher FOREIGN KEY grouping(teacher4_name) REFERENCES teacher(name);
