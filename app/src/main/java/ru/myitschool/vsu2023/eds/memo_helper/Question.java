package ru.myitschool.vsu2023.eds.memo_helper;

public class Question {
    private Integer id;
    private String text;
    private String rightLet;
    private String wrongLet;
    private String imageName;
    private Integer categoryId;

    public Integer getId() {
        return id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public String getText() {
        return text;
    }

    public String getRightLet() {
        return rightLet;
    }

    public String getWrongLet() {
        return wrongLet;
    }

    public String getImageName() {
        return imageName;
    }

    public Question(Integer id, String text, String rightLet, String wrongLet, String imageName, Integer categoryId) {
        this.id = id;
        this.text = text;
        this.rightLet = rightLet;
        this.wrongLet = wrongLet;
        this.imageName = imageName;
        this.categoryId = categoryId;
    }
}
