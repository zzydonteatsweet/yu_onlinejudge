package com.yupi.ojsandbox.demos.web.utils;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.yupi.ojsandbox.demos.web.model.ExecuteMessage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ProcessUtils {
    public static ExecuteMessage runProcessandgetMessage(Process process, String opName) {
        ExecuteMessage executeMessage = new ExecuteMessage();
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            int exitValue = process.waitFor();
            executeMessage.setExitValue(exitValue);
            if (exitValue == 0) {
                System.out.println(opName + " 成功");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder compileOutputStringBuilder = new StringBuilder();

                String compileOutputline;
                while((compileOutputline = bufferedReader.readLine()) != null) {
//                    System.out.println(successOutputline);
                    compileOutputStringBuilder.append(compileOutputline);
                }
                executeMessage.setMessage(compileOutputStringBuilder.toString());
            }else {
                System.out.println(opName + " 失败");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String compileOutputline;

                StringBuilder compileOutputStringBuilder = new StringBuilder();

                while((compileOutputline = bufferedReader.readLine()) != null) {
//                    System.out.println(successOutputline);
                    compileOutputStringBuilder.append(compileOutputline);
                }
                executeMessage.setMessage(compileOutputStringBuilder.toString());

                BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                StringBuilder errorMessageBuilder = new StringBuilder();
                String errorMessageline;
                while((errorMessageline = errorBufferedReader.readLine())!=null) {
                    errorMessageBuilder.append(errorMessageline);
                }
                executeMessage.setErrorMessage(errorMessageBuilder.toString());
            }
            stopWatch.stop();
            executeMessage.setTime(stopWatch.getLastTaskTimeMillis());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return executeMessage;
    }
    public static ExecuteMessage runInteractiveProcessandgetMessage(Process process, String args) {
        ExecuteMessage executeMessage = new ExecuteMessage();
        try {
            System.out.println("执行成功");
            OutputStream outputStream = process.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            String[] s = args.split(" ");
            String join = StrUtil.join("\n",s) + "\n";

            outputStreamWriter.write(join);
            //  输入
            outputStreamWriter.flush();

            InputStream inputStream = process.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder successOutputStringBuilder = new StringBuilder();

            String successOutputline;
            while((successOutputline = bufferedReader.readLine()) != null) {
//                    System.out.println(successOutputline);
                successOutputStringBuilder.append(successOutputline);
            }
            System.out.println("exe builder " + successOutputStringBuilder.toString());
            executeMessage.setMessage(successOutputStringBuilder.toString());
            outputStream.close();
            outputStreamWriter.close();
            inputStream.close();
            process.destroy();

        }catch (Exception e) {
            e.printStackTrace();
        }
        return executeMessage;
    }
}
