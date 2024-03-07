package com.yupi.springbootinit.judge;

import com.yupi.springbootinit.model.entity.QuestionSubmit;

public interface JudgeService {
    /**
     *
     * @param questionSubmitId
     * @return
     */
    QuestionSubmit doJudge(long questionSubmitId);

}
