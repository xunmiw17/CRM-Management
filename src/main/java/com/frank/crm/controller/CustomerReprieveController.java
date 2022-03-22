package com.frank.crm.controller;

import com.frank.crm.base.BaseController;
import com.frank.crm.base.ResultInfo;
import com.frank.crm.query.CustomerReprieveQuery;
import com.frank.crm.service.CustomerReprieveService;
import com.frank.crm.vo.CustomerReprieve;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("customer_rep")
public class CustomerReprieveController extends BaseController {

    @Resource
    private CustomerReprieveService customerReprieveService;

    /**
     * Multi-condition querying the loss customer reprieve operation list
     * @param customerReprieveQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> queryCustomerReprieveByParams(CustomerReprieveQuery customerReprieveQuery) {
        return customerReprieveService.queryCustomerReprieveByParams(customerReprieveQuery);
    }

    /**
     * Adds the reprieve data
     * @param customerReprieve
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo addCustomerRepr(CustomerReprieve customerReprieve) {
        customerReprieveService.addCustomerRepr(customerReprieve);
        return success("Adding reprieve data success");
    }

    /**
     * Updates the reprieve data
     * @param customerReprieve
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateCustomerRepr(CustomerReprieve customerReprieve) {
        customerReprieveService.updateCustomerRepr(customerReprieve);
        return success("Updating reprieve data success");
    }

    /**
     * Opens the adding or updating reprieve data page
     * @return
     */
    @RequestMapping("toAddOrUpdateCustomerReprPage")
    public String toAddOrUpdateCustomerReprPage(@RequestParam Integer lossId, @RequestParam(required = false) Integer id, HttpServletRequest request) {
        request.setAttribute("lossId", lossId);
        if (id != null) {
            CustomerReprieve customerReprieve = customerReprieveService.selectByPrimaryKey(id);
            request.setAttribute("customerRep", customerReprieve);
        }
        return "customerLoss/customer_rep_add_update";
    }

    /**
     * Deletes the customer reprieve by id
     * @param id
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteCustomerRepr(@RequestParam Integer id) {
        customerReprieveService.deleteCustomerRepr(id);
        return success("Deleting customer reprieve success");
    }
}
