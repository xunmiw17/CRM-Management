package com.frank.crm.controller;

import com.frank.crm.base.BaseController;
import com.frank.crm.base.ResultInfo;
import com.frank.crm.query.RoleQuery;
import com.frank.crm.service.RoleService;
import com.frank.crm.vo.Role;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController extends BaseController {

    @Resource
    private RoleService roleService;

    /**
     * Query all roles
     * @return
     */
    @RequestMapping("queryAllRoles")
    @ResponseBody
    public List<Map<String, Object>> queryAllRoles(Integer userId) {
        return roleService.queryAllRoles(userId);
    }

    /**
     * Query the role list by page
     * @param roleQuery
     * @return
     */
    @GetMapping("list")
    @ResponseBody
    public Map<String, Object> selectByParams(RoleQuery roleQuery) {
        return roleService.queryByParamsForTable(roleQuery);
    }

    /**
     * Enters the role management page
     * @return
     */
    @RequestMapping("index")
    public String index() {
        return "role/role";
    }

    /**
     * Adds the role
     * @param role
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addRole(Role role) {
        roleService.addRole(role);
        return success("Adding role success");
    }

    /**
     * Enters the add/update role page
     * @return
     */
    @RequestMapping("toAddOrUpdateRolePage")
    public String toAddOrUpdateRolePage(@RequestParam(required = false, value = "id") Integer roleId, Model model) {
        if (roleId != null) {
            Role role = roleService.selectByPrimaryKey(roleId);
            System.out.println(role.getRoleName());
            model.addAttribute("role", role);
        }
        return "role/add_update";
    }

    /**
     * Updates the given role
     * @param role
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateRole(Role role) {
        roleService.updateRole(role);
        return success("Updating role success");
    }

    /**
     * Deletes the given role
     * @param roleId
     * @return
     */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteRole(Integer roleId) {
        roleService.deleteRole(roleId);
        return success("Deleting operation success");
    }

    /**
     * Grants the given resources to a role
     * @param roleId
     * @param mIds
     * @return
     */
    @RequestMapping("grant")
    @ResponseBody
    public ResultInfo grant(Integer roleId, Integer[] mIds) {
        roleService.grant(roleId, mIds);
        return success("Granting resources to role success");
    }
}
