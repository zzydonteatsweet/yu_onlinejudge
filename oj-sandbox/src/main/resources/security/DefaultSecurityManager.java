package com.yupi.ojsandbox.demos.web.security;

import java.security.Permission;

public class DefaultSecurityManager extends SecurityManager{
    @Override
    public void checkPermission(Permission perm) {
        System.out.println("此处不做任何限制");
        System.out.println(perm);
    }
}
