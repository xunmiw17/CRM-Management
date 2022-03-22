package com.frank.crm.controller;

import com.frank.crm.base.BaseController;
import com.frank.crm.base.ResultInfo;
import com.frank.crm.query.CustomerQuery;
import com.frank.crm.service.CustomerService;
import com.frank.crm.vo.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("customer")
public class CustomerController extends BaseController {

    @Resource
    private CustomerService customerService;

    /**
     * Query the customer list through paging
     * @param customerQuery
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    public Map<String, Object> queryCustomerByParams(CustomerQuery customerQuery) {
        return customerService.queryCustomerByParams(customerQuery);
    }

    /**
     * Enters the customer information management page
     * @return
     */
    @RequestMapping("index")
    public String index() {
        return "customer/customer";
    }

    /**
     * Adds customer info
     * @param customer
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo addCustomer(Customer customer) {
        customerService.addCustomer(customer);
        return success("Adding customer info success");
    }

    @RequestMapping("toAddOrUpdateCustomerPage")
    public String toAddOrUpdateCustomerPage(@RequestParam(required = false) Integer id, HttpServletRequest request) {
        if (id != null) {
            Customer customer = customerService.selectByPrimaryKey(id);
            request.setAttribute("customer", customer);
        }
        return "customer/add_update";
    }

    /**
     * Updates customer info
     * @param customer
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateCustomer(Customer customer) {
        customerService.updateCustomer(customer);
        return success("Updating customer info success");
    }

    /**
     * Deletes the given customer
     * @param id
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteCustomer(@RequestParam Integer id) {
        customerService.deleteCustomer(id);
        return success("Deleting customer success");
    }

    @RequestMapping("toCustomerOrderPage")
    public String toCustomerOrderPage(@RequestParam Integer id, Model model) {
        Customer customer = customerService.selectByPrimaryKey(id);
        model.addAttribute("customer", customer);
        return "customer/customer_order";
    }

    /**
     * Query the customer contribution analysis
     * @param customerQuery
     * @return
     */
    @RequestMapping("queryCustomerContributionByParams")
    @ResponseBody
    public Map<String, Object> queryCustomerContributionByParams(CustomerQuery customerQuery) {
        return customerService.queryCustomerContributionByParams(customerQuery);
    }

    /**
     * Query the customer constitution (line chart)
     * @return
     */
    @RequestMapping("countCustomerMakeup")
    @ResponseBody
    public Map<String, Object> countCustomerMakeup() {
        return customerService.countCustomerMakeup();
    }

    /**
     * Query the customer constitution (pie chart)
     * @return
     */
    @RequestMapping("countCustomerMakeup02")
    @ResponseBody
    public Map<String, Object> countCustomerMakeup02() {
        return customerService.countCustomerMakeup02();
    }
}
