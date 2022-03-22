package com.frank.crm.controller;

import com.frank.crm.annotation.RequiredPermission;
import com.frank.crm.base.BaseController;
import com.frank.crm.base.ResultInfo;
import com.frank.crm.enums.StateStatus;
import com.frank.crm.query.SaleChanceQuery;
import com.frank.crm.service.SaleChanceService;
import com.frank.crm.utils.UserIDBase64;
import com.frank.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;

    /**
     * Sale chance (and Customer development) multi-conditions query (and divided into pages)
     *      - If flag is not null and has 1 as value -> querying on customer development
     *      - Else -> querying on sale chance
     * @param saleChanceQuery
     * @return
     */
    @RequiredPermission(code = "101001")
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery, Integer flag, @CookieValue("userIdEncoded") String userId) {
        // Querying on customer development
        if (flag != null && flag == 1) {
            // Set state to be "assigned"
            saleChanceQuery.setState(StateStatus.STATED.getType());
            Integer id = UserIDBase64.decoderUserID(userId);
            // Set assign man to be the current user id (Got from cookie)
            saleChanceQuery.setAssignMan(id);
        }
        return saleChanceService.querySaleChanceByParams(saleChanceQuery);
    }

    /**
     * Enters into the Sale chance management page
     * @return
     */
    @RequiredPermission(code = "1010")
    @RequestMapping("index")
    public String index() {
        return "saleChance/sale_chance";
    }

    /**
     * Adds sale chance
     * @param saleChance
     * @return
     */
    @RequiredPermission(code = "101002")
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addSaleChance(SaleChance saleChance, @CookieValue(name = "userName", required = false) String userName) {
        saleChance.setCreateMan(userName);
        saleChanceService.addSaleChance(saleChance);
        // Method in BaseController to make it simple to build ResultInfo
        return success("Success adding sale chance!");
    }

    /**
     * Updates sale chance
     * @param saleChance
     * @return
     */
    @RequiredPermission(code = "101004")
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateSaleChance(SaleChance saleChance) {
        saleChanceService.updateSaleChance(saleChance);
        // Method in BaseController to make it simple to build ResultInfo
        return success("Success updating sale chance!");
    }

    /**
     * Enters the page that has input text to add or update sale chance
     * @return
     */
    @RequestMapping("toSaleChancePage")
    public String toSaleChancePage(Integer saleChanceId, HttpServletRequest request) {
        // If there is an id, it means the user is accessing the "edit" operation
        if (saleChanceId != null) {
            // Gets the sale chance data corresponding to the data to be edited by primary key
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(saleChanceId);
            // Sets the data to the request scope
            request.setAttribute("saleChance", saleChance);
        }
        return "saleChance/add_update";
    }

    /**
     * Deletes sale chance
     * @param ids
     * @return
     */
    @RequiredPermission(code = "101003")
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteSaleChance(Integer[] ids) {
        saleChanceService.deleteBatch(ids);
        return success("Deleting sale chance successfully");
    }

    /**
     * Updates the dev result of sale chance
     * @param id
     * @param devResult
     * @return
     */
    @PostMapping("updateSaleChanceDevResult")
    @ResponseBody
    public ResultInfo updateSaleChanceDevResult(Integer id, Integer devResult) {
        saleChanceService.updateSaleChanceDevResult(id, devResult);
        return success("Dev result updating success!");
    }
}
