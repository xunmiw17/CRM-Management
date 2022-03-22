package com.frank.crm.service;

import com.frank.crm.base.BaseService;
import com.frank.crm.dao.UserRoleMapper;
import com.frank.crm.vo.UserRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserRoleService extends BaseService<UserRole, Integer> {

    @Resource
    private UserRoleMapper userRoleMapper;


}
