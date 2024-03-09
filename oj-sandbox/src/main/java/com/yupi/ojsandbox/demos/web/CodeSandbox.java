package com.yupi.ojsandbox.demos.web;

import com.yupi.ojsandbox.demos.web.model.ExecuteCodeResponse;
import com.yupi.ojsandbox.demos.web.model.ExecuteCodeRequest;

/**
 * 代码沙箱接口定义
 */
public interface CodeSandbox {

    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
