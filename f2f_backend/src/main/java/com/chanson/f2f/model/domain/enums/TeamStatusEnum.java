package com.chanson.f2f.model.domain.enums;

/**
 * 队伍状态枚举
 */
public enum TeamStatusEnum {


    /**
     * 队伍的三种状态
     */
    PUBLIC(0, "公开"),
    PRIVATE(1, "私有"),
    SECRET(2, "加密");


    private int value;
    private String text;


    TeamStatusEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public static TeamStatusEnum getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }
        TeamStatusEnum[] values = TeamStatusEnum.values();
        for (TeamStatusEnum teamStatusEnum : values) {
            if (teamStatusEnum.getValue() == value) {
                return teamStatusEnum;
            }
        }
        return null;
    }


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
