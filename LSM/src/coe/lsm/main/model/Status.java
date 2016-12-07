package coe.lsm.main.model;

import java.awt.*;

/**
 * Created by jesus on 2/18/16.
 */
public enum Status {
    OK(new Color(9, 197, 42), "success", 'O'),
    WARNING(new Color(255, 201, 54), "warning", 'W'),
    CRITICAL(new Color(255, 30, 12), "danger", 'C'),
    ERROR(new Color(32, 43, 180), "primary", 'E'),
    UNKNOWN(Color.GRAY, "default", 'U');

    private Color color;
    private String cssBoots;
    private char firstLetter;

    private Status(Color color, String cssBoots, char firstLetter) {
        this.color = color;
        this.cssBoots = cssBoots;
        this.firstLetter = firstLetter;
    }

    public Color getColor() {
        return color;
    }

    public String getCssBoots() {
        return cssBoots;
    }

    public char getFirstLetter() {
        return firstLetter;
    }
}
