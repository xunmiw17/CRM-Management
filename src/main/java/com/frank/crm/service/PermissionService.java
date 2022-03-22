package com.frank.crm.service;

import com.frank.crm.base.BaseService;
import com.frank.crm.dao.PermissionMapper;
import com.frank.crm.vo.Permission;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PermissionService extends BaseService<Permission, Integer> {

    @Resource
    private PermissionMapper permissionMapper;

    /**
     * Query permissions by user id
     * @param userId
     * @return
     */
    public List<String> queryPermissionsByUserId(Integer userId) {
        return permissionMapper.queryPermissionsByUserId(userId);
    }
}
