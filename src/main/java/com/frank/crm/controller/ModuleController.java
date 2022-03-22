package com.frank.crm.controller;

import com.frank.crm.base.BaseController;
import com.frank.crm.base.ResultInfo;
import com.frank.crm.model.TreeModel;
import com.frank.crm.service.ModuleService;
import com.frank.crm.vo.Module;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Result;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("module")
public class ModuleController extends BaseController {

    @Resource
    private ModuleService moduleService;

    /**
     * Query the list of all modules
     * @return
     */
    @RequestMapping("queryAllModules")
    @ResponseBody
    public List<TreeModel> queryAllModules(@RequestParam Integer roleId) {
        return moduleService.queryAllModules(roleId);
    }

    /**
     * Enters the granting resources page
     * @param roleId
     * @return
     */
    @RequestMapping("toAddGrantPage")
    public String toAddGrantPage(Integer roleId, HttpServletRequest request) {
        request.setAttribute("roleId", roleId);
        return "role/grant";
    }

    /**
     * Query the module list
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> queryModuleList() {
        return moduleService.queryModuleList();
    }

    /**
     * Enters the resources management page
     * @return
     */
    @RequestMapping("index")
    public String index() {
        return "module/module";
    }

    /**
     * Adds the given module
     * @param module
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addModule(Module module) {
        moduleService.addModule(module);
        return success("Adding module succeeds");
    }

    /**
     * Enters the page to add module
     * @param grade
     * @param parentId
     * @return
     */
    @RequestMapping("toAddModulePage")
    public String toAddModulePage(@RequestParam Integer grade, @RequestParam Integer parentId, HttpServletRequest request) {
        request.setAttribute("grade", grade);
        request.setAttribute("parentId", parentId);
        return "module/add";
    }

    /**
     * Updates the given module
     * @param module
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateModule(Module module) {
        moduleService.updateModule(module);
        return success("Updating module succeeds");
    }

    /**
     * Opens the dialog to update module
     * @param id
     * @return
     */
    @RequestMapping("toUpdateModulePage")
    public String toUpdateModulePage(@RequestParam Integer id, HttpServletRequest request) {
        Module module = moduleService.selectByPrimaryKey(id);
        request.setAttribute("module", module);
        return "module/update";
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteModule(@RequestParam Integer id) {
        moduleService.deleteModule(id);
        return success("Deleting resource success");
    }
}
