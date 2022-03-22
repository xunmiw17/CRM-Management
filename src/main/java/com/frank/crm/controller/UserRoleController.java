package com.frank.crm.controller;

import com.frank.crm.base.BaseController;
import com.frank.crm.service.UserRoleService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
public class UserRoleController extends BaseController {

    @Resource
    private UserRoleService userRoleService;


}
