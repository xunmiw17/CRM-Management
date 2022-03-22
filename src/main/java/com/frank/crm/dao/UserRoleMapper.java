package com.frank.crm.dao;

import com.frank.crm.base.BaseMapper;
import com.frank.crm.vo.UserRole;

public interface UserRoleMapper extends BaseMapper<UserRole, Integer> {

    // Selects the number of user roles according to user id
    Integer countUserRoleByUserId(Integer userId);

    // Deletes user role data according to user id
    Integer deleteUserRoleByUserId(Integer userId);
}