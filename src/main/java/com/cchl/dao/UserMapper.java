package com.cchl.dao;

import com.cchl.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface UserMapper {

    int insert(User user);

    List<User> selectAll();

    /**
     * 更新状态
     */
    int updateStatus(@Param(value = "id") int id, @Param(value = "status") byte status);

    /**
     * 查找未审核的用户集合
     */
    List<User> selectUnaudited(@Param(value = "departmentId")int departmentId, @Param(value = "limit") int limit, @Param(value = "number") int number);

    /**
     * @param all 是否需要查找所有条数
     * @param status 当all为false时，传如状态值，true时不作要求因为status不参与查询
     */
    int totalNumber(@Param(value = "all") boolean all, @Param(value = "status") byte status, @Param(value = "departmentId")int departmentId);


}