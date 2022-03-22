/*
    The UserController class receives the encapsulated user information from UserService class and encapsulates the
    information and error code and message, if any, into a ResultInfo object and returns it to client.
 */

package com.frank.crm.controller;

import com.frank.crm.base.BaseController;
import com.frank.crm.base.ResultInfo;
import com.frank.crm.model.UserModel;
import com.frank.crm.query.UserQuery;
import com.frank.crm.service.UserService;
import com.frank.crm.utils.LoginUserUtil;
import com.frank.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller

// The mapping for the whole controller class. So the request URL to the method userLogin would be localhost:8080/crm/user/login,
// rather than localhost:8080/crm/login
@RequestMapping("user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    /**
     * The user login method receives the encapsulated user information from UserService class and encapsulates the
     * information and error code and message, if any, into a ResultInfo object and returns it to client. The try-catch
     * part is deleted because our GlobalExceptionResolver class handles the global exception.
     * @param userName
     * @param userPassword
     * @return
     */
    @PostMapping("login")
    @ResponseBody // Indicates that the method returns a JSON object
    public ResultInfo userLogin(String userName, String userPassword) {
        ResultInfo resultInfo = new ResultInfo();
        UserModel userModel = userService.userLogin(userName, userPassword);
        resultInfo.setResult(userModel);
//        try {
//            UserModel userModel = userService.userLogin(userName, userPassword);
//            resultInfo.setResult(userModel);
//        } catch (ParamsException err) {
//            resultInfo.setCode(err.getCode());
//            resultInfo.setMsg(err.getMsg());
//            err.printStackTrace();
//        } catch (Exception err) {
//            resultInfo.setCode(500);
//            resultInfo.setMsg("Login failed");
//        }
        return resultInfo; // A JSON object
    }

    /**
     * User changing password.
     * @param request
     * @param oldPassword
     * @param newPassword
     * @param confirmedPassword
     * @return
     */
    @PostMapping("updatePassword")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest request,
                                         String oldPassword, String newPassword, String confirmedPassword) {
        ResultInfo resultInfo = new ResultInfo();
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        userService.updatePassword(userId, oldPassword, newPassword, confirmedPassword);
//        try {
//            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
//            userService.updatePassword(userId, oldPassword, newPassword, confirmedPassword);
//        } catch (ParamsException e) {
//            resultInfo.setCode(e.getCode());
//            resultInfo.setMsg(e.getMsg());
//            e.printStackTrace();
//        } catch (Exception e) {
//            resultInfo.setCode(500);
//            resultInfo.setMsg("Failure changing password");
//        }
        return resultInfo;
    }

    /**
     * This method lets the user to enter the password changing page.
     * @return
     */
    @RequestMapping("toPasswordPage")
    public String toPasswordPage() {
        return "user/password"; // Returning "user/password.ftl"
    }

    /**
     * This method query all the sales people for the dropdown menu when the user adds or updates the sale chance.
     * @return
     */
    @RequestMapping("queryAllSales")
    @ResponseBody
    public List<Map<String, Object>> queryAllSales() {
        return userService.queryAllSales();
    }

    /**
     * Querys the user list with paging and multi-conditions
     * @param userQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> selectByParams(UserQuery userQuery) {
        return userService.queryByParamsForTable(userQuery);
    }

    /**
     * Enters the user list page
     * @return
     */
    @RequestMapping("index")
    public String index() {
        return "user/user";
    }

    @PostMapping("add")
    @ResponseBody
    public ResultInfo addUser(User user) {
        userService.addUser(user);
        return success("User adding success");
    }

    /**
     * Opens the adding or updating user page
     * @return
     */
    @RequestMapping("toAddOrUpdateUserPage")
    public String toAddUserPage(@RequestParam(required = false) Integer id, Model model) {
        if (id != null) {
            User user = userService.selectByPrimaryKey(id);
            model.addAttribute("userInfo", user);
        }
        return "user/add_update";
    }

    /**
     * Updates the user
     * @param user
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateUser(User user) {
        userService.updateUser(user);
        return success("User updating success");
    }

    /**
     * Deletes the given user(s)
     * @param ids
     * @return
     */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids) {
        userService.deleteByIds(ids);
        return success("User deleting success");
    }

    /**
     * Query all the customer manager
     * @return
     */
    @RequestMapping("queryAllCustomerManagers")
    @ResponseBody
    public List<Map<String, Object>> queryAllCustomerManagers() {
        return userService.queryAllCustomerManagers();
    }
}
