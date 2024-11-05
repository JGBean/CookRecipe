package com.cook_recipe_app.firebase;

public class MenuItem {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;

    private int type;       // 헤더인지 아이템인지 구분
    private String name;    // 아이템 이름

    public MenuItem(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}

