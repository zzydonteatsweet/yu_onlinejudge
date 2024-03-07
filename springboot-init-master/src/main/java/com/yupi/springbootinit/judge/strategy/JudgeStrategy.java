package com.yupi.springbootinit.judge.strategy;

import com.yupi.springbootinit.judge.codesandbox.model.JudgeInfo;
import com.yupi.springbootinit.model.dto.question.JudgeConfig;

public interface JudgeStrategy {
    JudgeInfo doJudge(JudgeContext judgeContext);
}
