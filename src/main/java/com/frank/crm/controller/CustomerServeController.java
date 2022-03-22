package com.frank.crm.controller;

import com.frank.crm.base.BaseController;
import com.frank.crm.base.ResultInfo;
import com.frank.crm.query.CustomerServeQuery;
import com.frank.crm.service.CustomerServeService;
import com.frank.crm.service.UserService;
import com.frank.crm.utils.UserIDBase64;
import com.frank.crm.vo.CustomerServe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("customer_serve")
public class CustomerServeController extends BaseController {

    @Resource
    private CustomerServeService customerServeService;

    @Resource
    private UserService userService;

    /**
     * Query the serve data list
     * @param customerServeQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> queryCustomerServeByParams(CustomerServeQuery customerServeQuery, @RequestParam(required = false) Integer flag, @CookieValue String userIdEncoded) {
        if (flag != null && flag == 1) {
            Integer userId = UserIDBase64.decoderUserID(userIdEncoded);
            customerServeQuery.setAssigner(userId);
        }
        return customerServeService.queryCustomerServeByParams(customerServeQuery);
    }

    /**
     * Enters a specific serve page according to different serve type
     * @param type
     * @return
     */
    @RequestMapping("index/{type}")
    public String index(@PathVariable Integer type) {
        if (type != null) {
            if (type == 1) {
                return "customerServe/customer_serve";
            } else if (type == 2) {
                return "customerServe/customer_serve_assign";
            } else if (type == 3) {
                return "customerServe/customer_serve_proce";
            } else if (type == 4) {
                return "customerServe/customer_serve_feed_back";
            } else if (type == 5) {
                return "customerServe/customer_serve_archive";
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * Enters the adding customer serve page
     * @return
     */
    @RequestMapping("toAddCustomerServePage")
    public String toAddCustomerServePage() {
        return "customerServe/customer_serve_add";
    }

    /**
     * Adds the customer service
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo add(CustomerServe customerServe) {
        customerServeService.addCustomerServe(customerServe);
        return success("Adding customer service success");
    }

    /**
     * Updates the service state
     * @param customerServe
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo update(CustomerServe customerServe) {
        customerServeService.updateCustomerServeState(customerServe);
        return success("Updating customer service state success");
    }

    /**
     * Opens the page to assign service to a person
     * @param id
     * @return
     */
    @RequestMapping("toCustomerServeAssignPage")
    public String toCustomerServeAssignPage(@RequestParam Integer id, Model model) {
        CustomerServe customerServe = customerServeService.selectByPrimaryKey(id);
        model.addAttribute("customerServe", customerServe);
        return "customerServe/customer_serve_assign_add";
    }

    /**
     * Opens the page to handle a service
     * @return
     */
    @RequestMapping("toCustomerServeProcePage")
    public String toCustomerServeProcePage(@RequestParam Integer id, Model model) {
        model.addAttribute("customerServe", customerServeService.selectByPrimaryKey(id));
        return "customerServe/customer_serve_proce_add";
    }

    /**
     * Opens the page to provide feedback
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("toCustomerServeFeedBackPage")
    public String toCustomerServeFeedBackPage(@RequestParam Integer id, Model model) {
        model.addAttribute("customerServe", customerServeService.selectByPrimaryKey(id));
        return "customerServe/customer_serve_feed_back_add";
    }
}
