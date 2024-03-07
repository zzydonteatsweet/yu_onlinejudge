package com.yupi.springbootinit.judge.codesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExecuteCodeRequest {

    private List<String> inputList;

    /**
     * 语言信息
     */
    private String language;
    /**
     * 执行状态
     */
    private String code;

}
