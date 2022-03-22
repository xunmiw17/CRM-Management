package com.frank.crm.dao;

import com.frank.crm.base.BaseMapper;
import com.frank.crm.vo.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role, Integer> {

    // Query all roles (Only needs id and role name)
    List<Map<String, Object>> queryAllRoles(Integer userId);

    Role selectByRoleName(String roleName);
}