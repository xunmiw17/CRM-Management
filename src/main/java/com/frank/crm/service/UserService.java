/*
    The UserService class handles the user login request by checking the databases and returns the encapsulated user
    information if the username and password are correct.
 */

package com.frank.crm.service;

import com.frank.crm.base.BaseService;
import com.frank.crm.dao.UserMapper;
import com.frank.crm.dao.UserRoleMapper;
import com.frank.crm.model.UserModel;
import com.frank.crm.utils.AssertUtil;
import com.frank.crm.utils.Md5Util;
import com.frank.crm.utils.PhoneUtil;
import com.frank.crm.utils.UserIDBase64;
import com.frank.crm.vo.User;
import com.frank.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserService extends BaseService<User, Integer> {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    /**
     * Check if the username and password are empty. If not, then check if the username exists in the database. If the
     * username exists, then check if the password is correct. If no exception is thrown, we encapsulate user information
     * and returns it to the client side.
     * @param userName
     * @param userPassword
     */
    public UserModel userLogin(String userName, String userPassword) {
        checkLoginParams(userName, userPassword);
        User user = userMapper.queryUserByName(userName);
        AssertUtil.isTrue(user == null, "User name does not exist");
        checkUserPassword(userPassword, user.getUserPwd());
        return buildUserInfo(user);
    }

    /**
     * The updatePassword method changes the password, given the parameters of user id, old password, new password,
     * and confirmed password.
     * @param userId
     * @param oldPassword
     * @param newPassword
     * @param confirmedPassword
     */
    @Transactional(propagation = Propagation.REQUIRED) // Starts a transaction on the database before executing
    public void updatePassword(Integer userId, String oldPassword, String newPassword, String confirmedPassword) {
        User user = userMapper.selectByPrimaryKey(userId);
        AssertUtil.isTrue(user == null, "The record to update does not exist");
        checkPasswordParams(user, oldPassword, newPassword, confirmedPassword);
        user.setUserPwd(Md5Util.encode(newPassword));
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1, "Failure for changing user password");
    }

    /**
     * This method does the password parameters checking to see if the passwords are valid.
     * @param user
     * @param oldPassword
     * @param newPassword
     * @param confirmedPassword
     */
    private void checkPasswordParams(User user, String oldPassword, String newPassword, String confirmedPassword) {
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword), "Old password cannot be empty");
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPassword)), "Old password is incorrect");
        AssertUtil.isTrue(StringUtils.isBlank(newPassword), "New password cannot be empty");
        AssertUtil.isTrue(newPassword.equals(oldPassword), "New password should not be equal to old password");
        AssertUtil.isTrue(StringUtils.isBlank(confirmedPassword), "Confirmed password should not be empty");
        AssertUtil.isTrue(!confirmedPassword.equals(newPassword), "Confirmed password should be equal to new password");
    }

    /**
     * Builds user information, which only contains user id, name, and true name, that needs to be returned to the
     * client side.
     * @param user
     */
    private UserModel buildUserInfo(User user) {
        UserModel userModel = new UserModel();
        userModel.setUserIdEncoded(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    /**
     * Encodes the password the user provides and check if it corresponds to the password in the database. If they do not
     * correspond to each other, we throw an exception.
     * @param userPassword
     * @param dbPassword
     */
    private void checkUserPassword(String userPassword, String dbPassword) {
        userPassword = Md5Util.encode(userPassword);
        AssertUtil.isTrue(!userPassword.equals(dbPassword), "User password incorrect");
    }

    /**
     * Checks if both the username and user password are empty. If at least one of them is empty, we throw an exception.
     * @param userName
     * @param userPassword
     */
    private void checkLoginParams(String userName, String userPassword) {
        AssertUtil.isTrue(StringUtils.isBlank(userName), "User name should not be empty");
        AssertUtil.isTrue(StringUtils.isBlank(userPassword), "User password should not be empty");
    }

    /**
     * Query all sales people
     * @return
     */
    public List<Map<String, Object>> queryAllSales() {
        return userMapper.queryAllSales();
    }

    /**
     * Adds user.
     *      1. Check parameters
     *              userName -> Not null, unique
     *              email -> Not null
     *              phone -> Not null, right format
     *      2. Sets default value
     *              isValid -> 1
     *              createDate -> Current system time
     *              updateDate -> Current system time
     *              default password -> Encoded "123456"
     *      3. Executes adding operation and determine the affected number of lines
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user) {
        checkUserParams(user.getUserName(), user.getEmail(), user.getPhone(), user.getId());
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd(Md5Util.encode("123456"));
        AssertUtil.isTrue(userMapper.insertSelective(user) != 1, "The adding operation failed");
        // Associate the user with his/her roles
        // *** user.getId() is able to get the id because we've set useGeneratedKeys="true" keyProperty="id" keyColumn="id" in the UserMapper.xml ***
        associateUserWithRoles(user.getId(), user.getRoleIds());
    }

    /**
     * Associates the given user with given roles
     * @param userId
     * @param roleIds
     */
    private void associateUserWithRoles(Integer userId, String roleIds) {
        Integer count = userRoleMapper.countUserRoleByUserId(userId);
        if (count > 0) {
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId) != count, "The user roles associating failure");
        }
        if (StringUtils.isNotBlank(roleIds)) {
            List<UserRole> userRoleList = new ArrayList<>();
            String[] roleIdArray = roleIds.split(",");
            for (int i = 0; i < roleIdArray.length; i++) {
                UserRole userRole = new UserRole();
                userRole.setRoleId(Integer.parseInt(roleIdArray[i]));
                userRole.setUserId(userId);
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                userRoleList.add(userRole);
            }
            // Insert Batch
            AssertUtil.isTrue(userRoleMapper.insertBatch(userRoleList) != userRoleList.size(), "The user roles associating failure");
        }
    }

    /**
     * Checks the parameters of user
     * @param userName
     * @param email
     * @param phone
     */
    private void checkUserParams(String userName, String email, String phone, Integer id) {
        AssertUtil.isTrue(StringUtils.isBlank(userName), "The user name should not be empty");
        User temp = userMapper.queryUserByName(userName);
        AssertUtil.isTrue(temp != null && !temp.getId().equals(id), "User name already exists. Please try another user name");
        AssertUtil.isTrue(StringUtils.isBlank(email), "User email should not be empty");
        AssertUtil.isTrue(StringUtils.isBlank(phone), "User phone should not be empty");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone), "The phone format is incorrect");
    }

    /**
     * Updates the given user
     *      1. Check parameters
     *          userId -> Not null and data record exuserName -> Not null, unique
     *          userName -> Not null, unique
     *          email -> Not null
     *          phone -> Not null, right format
     *      2. Set default value
     *          updateDate -> current system time
     *      3. Executes the updating operation and determine the affected number of lines.
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user) {
        Integer userId = user.getId();
        AssertUtil.isTrue(userId == null || userMapper.selectByPrimaryKey(userId) == null, "The user to be updated does not exist");
        checkUserParams(user.getUserName(), user.getEmail(), user.getPhone(), user.getId());
        user.setUpdateDate(new Date());
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) != 1, "The updating operation failed");

        associateUserWithRoles(user.getId(), user.getRoleIds());
    }

    /**
     * Deleting users
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteByIds(Integer[] ids) {
        AssertUtil.isTrue(ids == null || ids.length == 0, "The data records to be deleted do not exist");
        AssertUtil.isTrue(userMapper.deleteBatch(ids) != ids.length, "User deleting failed");

        // Deletes the roles associated with the user to be deleted
        for (Integer userId : ids) {
            Integer count = userRoleMapper.countUserRoleByUserId(userId);
            if (count > 0) {
                AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId) != count, "User deleting failed");
            }
        }
    }

    /**
     * Query all customer managers
     * @return
     */
    public List<Map<String, Object>> queryAllCustomerManagers() {
        return userMapper.queryAllCustomerManagers();
    }
}