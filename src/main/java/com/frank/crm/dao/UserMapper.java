/*
    The UserMapper interface lists out methods used to query database, and the methods definitions are written in
    UserMapper.xml.
 */
package com.frank.crm.dao;

import com.frank.crm.base.BaseMapper;
import com.frank.crm.vo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User, Integer> {

    public User queryUserByName(String userName);

    // Query all sales people as "Assign man"
    List<Map<String, Object>> queryAllSales();

    // Query all customer managers
    List<Map<String, Object>> queryAllCustomerManagers();
}