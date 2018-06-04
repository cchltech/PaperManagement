package com.cchl.service.admin;

import com.cchl.entity.Title;
import com.cchl.entity.User;

import java.util.List;

/**
 * 管理员审核接口
 */
public interface ExamineService {

    /**
     * 查找总页数
     * @param type 简化一下代码，通过类型判定查找那张表的总页数
     *             0:user  1:title
     * @param userId
     * @return 结果集
     */
    int totalNumber(int type, int userId);

    /**
     * @param page 第几页
     * @param number 每页条数
     * @return 返回待审核的用户数据
     */
    List<User> users(int userId, int page, int number);

    /**
     * 用户审核结果集
     * @param id 用户id数组
     * @param status 审核状态数组
     * @return 循环插入，如果有出错则将出错的条数的序号存在数组中返回
     */
    Integer[] resultUser(Integer[] id, Byte[] status);

    /**
     * @return 返回待审核的题目申请表
     */
    List<Title> title(int page, int limit, int userId);

    /**
     * 题目审核结果集
     * @param id 题目id数组
     * @param status 审核状态数据
     * @return 循环插入，如果有出错则将出错的条数的序号存在数组中返回
     */
    Integer[] resultTitle(Integer[] id, Byte[] status);

}
