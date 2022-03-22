package com.frank.crm.dao;

import com.frank.crm.base.BaseMapper;
import com.frank.crm.model.TreeModel;
import com.frank.crm.vo.Module;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ModuleMapper extends BaseMapper<Module, Integer> {

    List<TreeModel> queryAllModules();

    List<Module> queryModuleList();

    Module queryModuleByGradeAndModuleName(@Param("grade") Integer grade, @Param("moduleName") String moduleName);

    Module queryModuleByGradeAndUrl(@Param("grade") Integer grade, @Param("url") String url);

    Module queryModuleByOptValue(String optValue);

    Integer queryModuleByParentId(Integer id);
}