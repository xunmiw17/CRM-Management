package com.frank.crm.controller;

import com.frank.crm.base.BaseController;
import com.frank.crm.base.ResultInfo;
import com.frank.crm.query.CusDevPlanQuery;
import com.frank.crm.service.CusDevPlanService;
import com.frank.crm.service.SaleChanceService;
import com.frank.crm.vo.CusDevPlan;
import com.frank.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequestMapping("cus_dev_plan")
@Controller
public class CusDevPlanController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;

    @Resource
    private CusDevPlanService cusDevPlanService;

    /**
     * Enters into the Customer Dev Plan page
     * @return
     */
    @RequestMapping("index")
    public String index() {
        return "cusDevPlan/cus_dev_plan";
    }

    /**
     * Enters the customer dev plan subpage
     * @return
     */
    @RequestMapping("toCusDevPlanPage")
    public String toCusDevPlanPage(@RequestParam Integer id, HttpServletRequest request) {
        // Query sale chance object by id
        SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);

        // Sets the saleChance object to the "request scope"
        request.setAttribute("saleChance", saleChance);
        return "cusDevPlan/cus_dev_plan_data";
    }

    /**
     * Customer dev plan multi-conditions query (and divided into pages)
     *      - If flag is not null and has 1 as value -> querying on customer development
     *      - Else -> querying on sale chance
     * @param cusDevPlanQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> queryCusDevPlanByParams(CusDevPlanQuery cusDevPlanQuery) {
        return cusDevPlanService.queryCusDevPlanByParams(cusDevPlanQuery);
    }

    /**
     * Adds customer dev plan
     * @param cusDevPlan
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addCusDevPlan(CusDevPlan cusDevPlan) {
        cusDevPlanService.addCusDevPlan(cusDevPlan);
        return success("Customer development plan adding successful");
    }

    /**
     * Enters the plan item add or update page
     */
    @RequestMapping("toAddOrUpdateCusDevPlanPage")
    public String toAddOrUpdateCusDevPlanPage(Integer sId, Integer id, HttpServletRequest request) {
        // Sets the sale chance id into the request scope for the dev plan page use
        request.setAttribute("sId", sId);
        CusDevPlan cusDevPlan = cusDevPlanService.selectByPrimaryKey(id);
        request.setAttribute("cusDevPlan", cusDevPlan);
        return "cusDevPlan/add_update";
    }

    /**
     * Updates customer dev plan
     * @param cusDevPlan
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateCusDevPlan(CusDevPlan cusDevPlan) {
        cusDevPlanService.updateCusDevPlan(cusDevPlan);
        return success("Customer development plan updating successful");
    }

    /**
     * Deletes customer dev plan
     * @param id
     * @return
     */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteCusDevPlan(Integer id) {
        cusDevPlanService.deleteDevPlan(id);
        return success("Customer development plan deleting successful");
    }
}
