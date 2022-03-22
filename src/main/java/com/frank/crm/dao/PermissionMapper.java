package com.frank.crm.dao;

import com.frank.crm.base.BaseMapper;
import com.frank.crm.vo.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission, Integer> {

    Integer countPermissionByRoleId(Integer roleId);

    void deletePermissionByRoleId(Integer roleId);

    List<Integer> queryModuleIdsByRoleId(Integer roleId);

    List<String> queryPermissionsByUserId(Integer userId);

    Integer countPermissionByModuleId(Integer id);

    Integer deletePermissionByModuleId(Integer id);
}