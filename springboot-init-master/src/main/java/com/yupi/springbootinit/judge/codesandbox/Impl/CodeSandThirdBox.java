package com.yupi.springbootinit.judge.codesandbox.Impl;

import com.yupi.springbootinit.judge.codesandbox.CodeSandBox;
import com.yupi.springbootinit.judge.codesandbox.model.ExecuteCodeRequest;
import com.yupi.springbootinit.judge.codesandbox.model.ExecuteCodeResponse;

public class CodeSandThirdBox implements CodeSandBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方沙箱");
        return null;
    }
}
