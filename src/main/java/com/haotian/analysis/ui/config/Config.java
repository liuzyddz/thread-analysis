package com.haotian.analysis.ui.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Config {
    private Map<String, String> menu;
    @JsonProperty("query-type")
    private Item[] queryType;
    @JsonProperty("selector")
    private String[] selector;

    private String title;
    @JsonProperty("min-width")
    private int minWidth;
    @JsonProperty("min-height")
    private int minHeight;

    @JsonProperty("screen")
    private List<Screen> screenList;

    @JsonProperty("h2-config")
    private H2DBConfig h2DBConfig;

    public Screen getScreen(int width, int height) {
        if (screenList == null) {
            return null;
        }
        screenList.sort(new Comparator<Screen>() {
            @Override
            public int compare(Screen o1, Screen o2) {
                if (o1 == o2) {
                    return 0;
                }
                if (o1 == null) {
                    return -1;
                }
                if (o2 == null) {
                    return 1;
                }

                int widthComp = Integer.compare(o1.getWidth() - o2.getWidth(), 0);
                return widthComp == 0 ? Integer.compare(o1.getHeight() - o2.getHeight(), 0) : widthComp;
            }
        });
        for (Screen screen : screenList)  {
            if (screen.support(width, height)) {
                return screen;
            }
        }
        return null;
    }

    public boolean support(File selectedFile) {
        if (selectedFile == null) {
            return false;
        }
        for (String selected: selector) {
            if (selectedFile.getName().toLowerCase().endsWith(selected)) {
                return true;
            }
        }
        return false;
    }

    public Item[] getQueryType() {
        return queryType;
    }

    public void setQueryType(Item[] queryType) {
        this.queryType = queryType;
    }

    public Map<String, String> getMenu() {
        return menu;
    }

    public void setMenu(Map<String, String> menu) {
        this.menu = menu;
    }

    public String[] getSelector() {
        return selector;
    }

    public void setSelector(String[] selector) {
        this.selector = selector;
    }

    public List<Screen> getScreenList() {
        return screenList;
    }

    public void setScreenList(List<Screen> screenList) {
        this.screenList = screenList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    public H2DBConfig getH2DBConfig() {
        return h2DBConfig;
    }

    public void setH2DBConfig(H2DBConfig h2DBConfig) {
        this.h2DBConfig = h2DBConfig;
    }
}
