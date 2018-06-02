package com.haotian.analysis.ui.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Screen {
    private int width;
    private int height;

    @JsonProperty("font-size")
    private int fontSize;

    public boolean support(int width, int height) {
        return this.width >= width && this.height >= height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }
}
