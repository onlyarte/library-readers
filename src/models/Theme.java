package models;

public class Theme {
    private int themeId;
    private String name;

    public Theme(int themeId, String name) {
        this.themeId = themeId;
        this.name = name;
    }

    public int getThemeId() { return themeId; }
    public String getName() { return name; }
}
