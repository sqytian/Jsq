package cn.m1c.gczj.biz.model;

import cn.m1c.frame.model.BaseModel;

/**
 * 问答表
 * @author Administrator
 *
 */
public class Answer extends BaseModel {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8540898823046460212L;
	private String answer;
    private String question;


    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer == null ? null : answer.trim();
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question == null ? null : question.trim();
    }

}