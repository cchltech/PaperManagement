package com.cchl.dao;

import com.cchl.entity.User;
import java.util.List;

public interface UserMapper {
    int insert(User record);

    List<User> selectAll();
}