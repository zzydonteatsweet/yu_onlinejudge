package com.yupi.springbootinit.judge.codesandbox;

import com.yupi.springbootinit.judge.codesandbox.model.ExecuteCodeRequest;
import com.yupi.springbootinit.judge.codesandbox.model.ExecuteCodeResponse;

public interface CodeSandBox {
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
