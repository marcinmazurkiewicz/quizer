package io.dudek.florystyka.domain;

import javax.persistence.*;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue
    private int id;
    private String content;
    private String answerA;
    private String answerB;
    private String answerC;
    private String answerD;
    @Column(name = "ok")
    private String correct;
    private String img;


    public Question() {}

    public Question(int id, String content, String answerA, String answerB, String answerC, String answerD, String correct) {
        this.id = id;
        this.content = content;
        this.answerA = answerA;
        this.answerB = answerB;
        this.answerC = answerC;
        this.answerD = answerD;
        this.correct = correct;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswerA() {
        return answerA;
    }

    public void setAnswerA(String answerA) {
        this.answerA = answerA;
    }

    public String getAnswerB() {
        return answerB;
    }

    public void setAnswerB(String answerB) {
        this.answerB = answerB;
    }

    public String getAnswerC() {
        return answerC;
    }

    public void setAnswerC(String answerC) {
        this.answerC = answerC;
    }

    public String getAnswerD() {
        return answerD;
    }

    public void setAnswerD(String answerD) {
        this.answerD = answerD;
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "Question [id=" + id + ", content=" + content + ", answers=" + answerA + ", " + answerB + ", " + answerC
                +", " + answerD + ", correct=" + correct
                + ", img=" + img + "]";
    }


}