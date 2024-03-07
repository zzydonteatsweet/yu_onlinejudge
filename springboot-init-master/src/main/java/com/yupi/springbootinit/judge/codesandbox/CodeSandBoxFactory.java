package com.yupi.springbootinit.judge.codesandbox;

import com.yupi.springbootinit.judge.codesandbox.CodeSandBox;
import com.yupi.springbootinit.judge.codesandbox.Impl.CodeSandExampleBox;
import com.yupi.springbootinit.judge.codesandbox.Impl.CodeSandRemoteBox;
import com.yupi.springbootinit.judge.codesandbox.Impl.CodeSandThirdBox;
import org.springframework.context.annotation.Bean;

public class CodeSandBoxFactory {

    public static CodeSandBox getCodeSandBox(String type) {
        switch (type){
            case "example":
                return new CodeSandExampleBox();
            case "remote":
                return new CodeSandRemoteBox();
            case "thirdparty":
                return new CodeSandThirdBox();
            default:
                return new CodeSandExampleBox();
        }
    }
}
