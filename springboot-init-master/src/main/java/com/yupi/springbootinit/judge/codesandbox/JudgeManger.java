package com.yupi.springbootinit.judge.codesandbox;

import com.yupi.springbootinit.judge.codesandbox.model.JudgeInfo;
import com.yupi.springbootinit.judge.strategy.DefaultJudgeStrategy;
import com.yupi.springbootinit.judge.strategy.JavaJudgeStrategy;
import com.yupi.springbootinit.judge.strategy.JudgeContext;
import com.yupi.springbootinit.judge.strategy.JudgeStrategy;
import com.yupi.springbootinit.model.dto.question.JudgeCase;
import com.yupi.springbootinit.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

@Service
public class JudgeManger {

    public JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if("java".equals(language)) {
            judgeStrategy = new JavaJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
