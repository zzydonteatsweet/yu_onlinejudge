package com.yupi.ojsandbox.demos.web;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
import cn.hutool.extra.tokenizer.Word;
import com.yupi.ojsandbox.demos.web.model.ExecuteCodeRequest;
import com.yupi.ojsandbox.demos.web.model.ExecuteCodeResponse;
import com.yupi.ojsandbox.demos.web.model.ExecuteMessage;
import com.yupi.ojsandbox.demos.web.model.JudgeInfo;
import com.yupi.ojsandbox.demos.web.security.DefaultSecurityManager;
import com.yupi.ojsandbox.demos.web.security.DenySecurityManager;
import com.yupi.ojsandbox.demos.web.utils.ProcessUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class JavaNativeSandbox implements CodeSandbox{
    private final String GLOBAL_CODE_DIRECTORY = "tmpCode";
    private final String GLOBAL_CODE_NAME = "Main.java";

    private final static long LIMITED_TIME = 5000L;

    private static final String SECURITY_MANAGER_CLASS_NAME = "MySecurityManager";

    private static final String SECURITY_MANAGER_PATH = "C:\\Projects\\ijjavaee\\Car300\\oj-sandbox\\src\\main\\resources\\security";
    private static final List<String> blocked_list = Arrays.asList("Files","exec");

    private static final WordTree WORD_TREE ;

    static {
        WORD_TREE = new WordTree();
        WORD_TREE.addWords(blocked_list);
    }
    public static void main(String args[]) {
        JavaNativeSandbox javaNativeSandbox = new JavaNativeSandbox();
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setInputList(Arrays.asList("1 1","2 2","3 3"));
//        String code = ResourceUtil.readStr("testCode/simpleCompute/Main.java", StandardCharsets.UTF_8);
        String code = ResourceUtil.readStr("testCode/simpleCompute/simpleInteractiveCompute/Main.java", StandardCharsets.UTF_8);
//        String code = ResourceUtil.readStr("testCode/simpleCompute/unsafe/SleepError.java", StandardCharsets.UTF_8);

//        String code = ResourceUtil.readStr("testCode/simpleCompute/unsafe/RunFileError.java", StandardCharsets.UTF_8);
        executeCodeRequest.setCode(code);
        executeCodeRequest.setLanguage("java");
        ExecuteCodeResponse executeCodeResponse = javaNativeSandbox.executeCode(executeCodeRequest);
        System.out.println("execode " + executeCodeResponse);
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {

        System.setSecurityManager(new DenySecurityManager());
        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();

        FoundWord foundWord = WORD_TREE.matchWord(code);
        if(foundWord != null) {
            System.out.println("包含禁止词 " + foundWord.getFoundWord());
            return null;
        }
        //  校验代码
        WordTree wordTree = new WordTree();
        wordTree.addWords();
        //  保存文件
        String userDir = System.getProperty("user.dir");
        String globalCodePathName = userDir + File.separator + GLOBAL_CODE_DIRECTORY;
        if(!FileUtil.exist(globalCodePathName)) {
            FileUtil.mkdir(globalCodePathName);
        }

        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();
        String userCodePath = userCodeParentPath + File.separator + GLOBAL_CODE_NAME;
        System.out.println(userCodePath);
        File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);

        //  编译代码
        try {
            String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsolutePath());
            Process process = Runtime.getRuntime().exec(compileCmd);
            ExecuteMessage executeMessage = ProcessUtils.runProcessandgetMessage(process,"编译");
            System.out.println(executeMessage);
        }catch (Exception e) {
            return raiseCompileWrongResponse(e);
        }

        // 执行代码
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for(String inputArgs : inputList) {
//            String exeCmd = String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s Main %s", userCodeParentPath, inputArgs);
            String exeCmd = String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s;%s -Djava.security.manager=%s Main %s", userCodeParentPath, SECURITY_MANAGER_PATH, SECURITY_MANAGER_CLASS_NAME, inputArgs);
            try {
                Process process = Runtime.getRuntime().exec(exeCmd);
                new Thread(() -> {
                    try {
                        Thread.sleep(LIMITED_TIME);
                        System.out.println("TIME_LIMITED");
                        process.destroy();
                    }catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).start();

                ExecuteMessage executeMessage = ProcessUtils.runInteractiveProcessandgetMessage(process, inputArgs);
                System.out.println("EXE_CODE " + executeMessage);
                executeMessageList.add(executeMessage);
            }catch (Exception e) {
                return raiseCompileWrongResponse(e);
            }
        }

        //  处理结果
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        long maxTime = 0;
        List<String> outputList = new ArrayList<>();
        for(ExecuteMessage executeMessage : executeMessageList) {
            String errorMessage = executeMessage.getErrorMessage();
            if(StrUtil.isNotBlank(errorMessage)) {
                executeCodeResponse.setStatus(3);
                break;
            }

            outputList.add(executeMessage.getMessage());
            Long time = executeMessage.getTime();
            if(time != null) {
                maxTime = Math.max(maxTime, time);
            }
        }

        JudgeInfo judgeInfo = new JudgeInfo();
        if(outputList.size() == executeMessageList.size()) {
            executeCodeResponse.setStatus(1);
        }

        //  没有修改judgeInfo是因为在判题机架构中执行
        judgeInfo.setTime(maxTime);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        executeCodeResponse.setOutputList(outputList);
        //  文件清理
        if(userCodeFile != null) {
            boolean del = FileUtil.del(userCodeFile);
            System.out.println("删除" + (del?"成功": "失败"));
        }
        return executeCodeResponse;
    }

    private ExecuteCodeResponse raiseCompileWrongResponse(Throwable e) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(new ArrayList<>());
        executeCodeResponse.setMessage(e.getMessage());
        executeCodeResponse.setStatus(2);
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        return executeCodeResponse;
    }
}
