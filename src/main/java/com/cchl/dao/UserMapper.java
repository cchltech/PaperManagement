package com.cchl.dao;

import com.cchl.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface UserMapper {

    int insert(User user);

    List<User> selectAll();

}