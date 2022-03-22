package com.frank.crm.service;

import com.frank.crm.base.BaseService;
import com.frank.crm.dao.ModuleMapper;
import com.frank.crm.dao.PermissionMapper;
import com.frank.crm.model.TreeModel;
import com.frank.crm.utils.AssertUtil;
import com.frank.crm.vo.Module;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModuleService extends BaseService<Module, Integer> {

    @Resource
    private ModuleMapper moduleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    /**
     * Query the list of all modules
     * @return
     */
    public List<TreeModel> queryAllModules(Integer roleId) {
        List<TreeModel> treeModelList = moduleMapper.queryAllModules();
        List<Integer> permissionIds = permissionMapper.queryModuleIdsByRoleId(roleId);
        if (permissionIds != null && permissionIds.size() > 0) {
            treeModelList.forEach(treeModel -> {
                if (permissionIds.contains(treeModel.getId())) {
                    treeModel.setChecked(true);
                }
            });
        }
        return treeModelList;
    }

    /**
     * Query all resources
     * @return
     */
    public Map<String, Object> queryModuleList() {
        Map<String, Object> map = new HashMap<>();
        List<Module> moduleList = moduleMapper.queryModuleList();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", moduleList.size());
        map.put("data", moduleList);
        return map;
    }

    /**
     * Adds the module passed from the front end
     *      1. Check parameters
     *          moduleName -> Not null, and unique in the same grade
     *          url -> Not null and unique for grade = 1
     *          parentId -> null for grade = 0, not null and parent exists for grade = 1 or grade = 2
     *          grade -> Not null and value be one of 0, 1, 2
     *          optValue -> Not null and unique
     *      2. Sets default value of parameters
     *          isValid -> 1
     *          createDate -> current system time
     *          updateDate -> current system time
     *      3. Executes the adding operation and determine the number of affected rows
     * @param module
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addModule(Module module) {
        Integer grade = module.getGrade();
        AssertUtil.isTrue(grade == null || !(grade == 0 || grade == 1 || grade == 2), "The menu grade is invalid");
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()), "The module name should not be empty");
        AssertUtil.isTrue(moduleMapper.queryModuleByGradeAndModuleName(grade, module.getModuleName()) != null, "The module name exists in the same grade");
        if (grade == 1) {
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()), "The url should not be empty");
            AssertUtil.isTrue(moduleMapper.queryModuleByGradeAndUrl(grade, module.getUrl()) != null, "This url is not available");
        }
        if (grade == 0) {
            module.setParentId(-1);
        } else {
            AssertUtil.isTrue(module.getParentId() == null, "The parent menu should not be empty");
            AssertUtil.isTrue(moduleMapper.selectByPrimaryKey(module.getParentId()) == null, "The parent menu does not exist");
        }
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()), "The opt value should not be empty");
        AssertUtil.isTrue(moduleMapper.queryModuleByOptValue(module.getOptValue()) != null, "The opt value already exists");

        module.setIsValid((byte) 1);
        module.setCreateDate(new Date());
        module.setUpdateDate(new Date());

        AssertUtil.isTrue(moduleMapper.insertSelective(module) != 1, "The adding operation failed");
    }

    /**
     * Updates the given resource
     *      1. Check parameters
     *          id -> Not null and data exist
     *          grade -> Not null, and value of 0, 1, or 2
     *          moduleName -> Not null and module name unique (not including the record itself to be updated)
     *          url -> (grade = 1) Not null and url unique (not including the record itself to be updated)
     *          optValue -> Not null and unique  (not including the record itself to be updated)
     *      2. Sets default value
     *          updateDate -> current system date
     *      3. Executes the updating operation and determine the number of lines affected
     * @param module
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateModule(Module module) {
        Integer moduleId = module.getId();
        AssertUtil.isTrue(moduleId == null || moduleMapper.selectByPrimaryKey(moduleId) == null, "The data record to be updated does not exist");
        Integer grade = module.getGrade();
        AssertUtil.isTrue(grade == null || !(grade == 0 || grade == 1 || grade == 2), "The assigned grade is invalid");
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()), "The module name should not be empty");
        Module temp = moduleMapper.queryModuleByGradeAndModuleName(module.getGrade(), module.getModuleName());
        AssertUtil.isTrue(temp != null && !(temp.getId().equals(moduleId)), "The module name already exists in the given grade");
        if (grade == 1) {
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()), "The menu url should not be empty");
            temp = moduleMapper.queryModuleByGradeAndUrl(grade, module.getUrl());
            AssertUtil.isTrue(temp != null && !(temp.getId().equals(moduleId)), "The url already exists in the given grade");
        }
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()), "The opt value should not be empty");
        temp = moduleMapper.queryModuleByOptValue(module.getOptValue());
        AssertUtil.isTrue(temp != null && !(temp.getId().equals(moduleId)), "The opt value already exists");

        module.setUpdateDate(new Date());
        AssertUtil.isTrue(moduleMapper.updateByPrimaryKeySelective(module) != 1, "The updating operation failed");
    }

    /**
     * Deletes the given resource
     *      1. Determines if the data to be deleted exist
     *      2. If the current resource has child resources, then it is not allowed to be deleted
     *      3. Deletes the data in permission table as well
     *      4. Executes the updating operation
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteModule(Integer id) {
        AssertUtil.isTrue(id == null, "The data to be deleted does not exist");
        Module temp = moduleMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(temp == null, "The data to be deleted does not exist");
        Integer childCount = moduleMapper.queryModuleByParentId(id);
        AssertUtil.isTrue(childCount > 0, "The current resource has child resource and is not allowed to be deleted");
        Integer count = permissionMapper.countPermissionByModuleId(id);
        if (count > 0) {
            permissionMapper.deletePermissionByModuleId(id);
        }
        temp.setIsValid((byte) 0);
        temp.setUpdateDate(new Date());
        AssertUtil.isTrue(moduleMapper.updateByPrimaryKeySelective(temp) != 1, "The updating operation failed");
    }
}
