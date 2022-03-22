package com.frank.crm.annotation;

import java.lang.annotation.*;

/**
 * This annotation class is used on request mapping handler method to define the required ACL code needed to access the
 * method (resource) annotated.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiredPermission {
    // ACL Code
    String code() default "";
}
