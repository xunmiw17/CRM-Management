package com.frank.crm.service;

import com.frank.crm.base.BaseService;
import com.frank.crm.dao.ModuleMapper;
import com.frank.crm.dao.PermissionMapper;
import com.frank.crm.dao.RoleMapper;
import com.frank.crm.utils.AssertUtil;
import com.frank.crm.vo.Permission;
import com.frank.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RoleService extends BaseService<Role, Integer> {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private ModuleMapper moduleMapper;

    /**
     * Query all roles
     * @return
     */
    public List<Map<String, Object>> queryAllRoles(Integer userId) {
        return roleMapper.queryAllRoles(userId);
    }

    /**
     * Adds new role
     *      1. Check parameters
     *              roleName -> Not null, unique
     *      2. Sets default parameters
     *              valid
     *              createTime
     *              updateTime
     *      3. Executes adding operation and determine the number of lines affected
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRole(Role role) {
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()), "Role name should not be blank");
        AssertUtil.isTrue(roleMapper.selectByRoleName(role.getRoleName()) != null, "The role name is not available");
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(roleMapper.insertSelective(role) != 1, "The adding operation failed");
    }

    /**
     * Updates the given role
     *      1. Check parameters
     *              id -> Not null; data exist
     *              roleName -> Not null, unique
     *      2. Sets default parameters
     *              updateDate
     *      3. Executes the updating operation and determine the number of affected lines
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role) {
        AssertUtil.isTrue(role.getId() == null || roleMapper.selectByPrimaryKey(role.getId()) == null, "The data to be updated does not exist");
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()), "The role name should not be empty");
        Role temp = roleMapper.selectByRoleName(role.getRoleName());
        AssertUtil.isTrue(temp != null && !temp.getId().equals(role.getId()), "The role name already exists. Please user another name");
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) != 1, "The updating operation failed");
    }

    /**
     * Deletes the given role
     *      1. Check parameters
     *              roleId -> Not null and exists in database
     *      2. Sets default value
     *              is_valid -> 0
     *              updateDate -> current system time
     *      3. Executes the updating operation
     * @param roleId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRole(Integer roleId) {
        AssertUtil.isTrue(roleId == null, "The data to be deleted does not exist");
        Role role = roleMapper.selectByPrimaryKey(roleId);
        AssertUtil.isTrue(role == null, "The data to be deleted does not exist");
        role.setIsValid(0);
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) != 1, "The deleting operation failed");
    }

    /**
     * Grant the roles
     * Cannot directly add to permission table because repeated rows would arise
     * Should delete the resources that already exist and then add the current resources
     *      1. Query the permission records according to the current roleId
     *      2. If the permission rows exist, then delete these rows
     *      3. If there is resources to add, add the resources
     * @param roleId
     * @param mIds
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void grant(Integer roleId, Integer[] mIds) {
        Integer count = permissionMapper.countPermissionByRoleId(roleId);
        if (count > 0) {
            permissionMapper.deletePermissionByRoleId(roleId);
        }
        if (mIds != null && mIds.length > 0) {
            List<Permission> permissionList = new ArrayList<>();
            for (int i = 0; i < mIds.length; i++) {
                Permission permission = new Permission();
                permission.setRoleId(roleId);
                permission.setModuleId(mIds[i]);
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mIds[i]).getOptValue());
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                permissionList.add(permission);
            }
            AssertUtil.isTrue(permissionMapper.insertBatch(permissionList) != permissionList.size(), "Permission adding failure");
        }
    }
}
