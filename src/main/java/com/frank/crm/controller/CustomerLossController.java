package com.frank.crm.controller;

import com.frank.crm.base.BaseController;
import com.frank.crm.base.ResultInfo;
import com.frank.crm.query.CustomerLossQuery;
import com.frank.crm.service.CustomerLossService;
import com.frank.crm.vo.CustomerLoss;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("customer_loss")
public class CustomerLossController extends BaseController {

    @Resource
    private CustomerLossService customerLossService;

    /**
     * Enters the customer loss management page
     *
     * @return
     */
    @RequestMapping("index")
    public String index() {
        return "customerLoss/customer_loss";
    }

    /**
     * Query by multi-condition through paging
     *
     * @param customerLossQuery
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    public Map<String, Object> queryCustomerLossByParams(CustomerLossQuery customerLossQuery) {
        return customerLossService.queryCustomerLossByParams(customerLossQuery);
    }

    /**
     * Opens the adding suspension or info page
     *
     * @param lossId
     * @return
     */
    @RequestMapping("toCustomerLossPage")
    public String toCustomerLossPage(@RequestParam Integer lossId, Model model) {
        CustomerLoss customerLoss = customerLossService.selectByPrimaryKey(lossId);
        model.addAttribute("customerLoss", customerLoss);
        return "customerLoss/customer_rep";
    }

    /**
     * Updates the customer loss state by id
     * @param id
     * @param lossReason
     * @return
     */
    @PostMapping("updateCustomerLossStateById")
    @ResponseBody
    public ResultInfo updateCustomerLossStateById(@RequestParam Integer id, @RequestParam String lossReason) {
        customerLossService.updateCustomerLossStateById(id, lossReason);
        return success("Confirming customer loss success");
    }
}
