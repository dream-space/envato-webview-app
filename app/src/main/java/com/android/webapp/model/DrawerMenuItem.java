package com.android.webapp.model;

public class DrawerMenuItem {
    public int id;
    public int icon;
    public String title;
    public String url;

    public DrawerMenuItem(int id, int icon, String title, String url) {
        this.id = id;
        this.icon = icon;
        this.title = title;
        this.url = url;
    }
}
