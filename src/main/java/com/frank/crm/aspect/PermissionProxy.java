package com.frank.crm.aspect;

import com.frank.crm.annotation.RequiredPermission;
import com.frank.crm.exceptions.AuthException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * This aspect class listens on the calling of controller methods with the @RequiredPermission annotation. Every time
 * a user wants to access a resource (which corresponds to a method with @RequiredPermission annotation), the
 * PermissionProxy class checks the current user's permissions and see if the user permissions contains the permission
 * required to access this resource. If not, it throws an AuthException, otherwise it lets the user proceed (
 * result = proceedingJoinPoint.proceed(). Notice that the around() method returns an "Object", and we could understand
 * it as a signal to indicate whether the user is able to access this resource. If the user is capable, then we set
 * this object (in this case "result") to be proceedingJoinPoint.proceed(), and return that object.
 */
@Component
@Aspect
public class PermissionProxy {

    @Resource
    private HttpSession session;

    /**
     * This method would intercept the RequiredPermission annotation in the given package
     * @param proceedingJoinPoint
     * @return
     */
    @Around(value = "@annotation(com.frank.crm.annotation.RequiredPermission)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = null;
        // Gets the resources the current user has
        List<String> permissions = (List<String>) session.getAttribute("permissions");
        // Determines if the user has the permission
        if (permissions == null || permissions.size() == 0) {
            throw new AuthException();
        }
        // Gets the targeted method
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        // Gets the annotation of the targeted method
        RequiredPermission requiredPermission = methodSignature.getMethod().getDeclaredAnnotation(RequiredPermission.class);
        // Determines if the user permissions contain the method's acl code
        if (!permissions.contains(requiredPermission.code())) {
            throw new AuthException();
        }

        result = proceedingJoinPoint.proceed();
        return result;
    }
}
