package com.example.a10.MyView.datepicker.bizs.themes;


public final class DPTManager {
    private static DPTManager sManager;

    private DPTheme theme;// 主题对象

    private DPTManager() {
        initCalendar(new DPCNTheme());
    }

    /**
     * 获取日历主题管理器
     * 
     * Get DatePicker theme manager
     *
     * @return 日历主题管理器 DatePicker theme manager
     */
    public static DPTManager getInstance() {
        if (null == sManager) {
            sManager = new DPTManager();
        }
        return sManager;
    }

    /**
     * 初始化主题对象
     * 
     * Initialization Theme
     *
     * @param theme ...
     */
    public void initCalendar(DPTheme theme) {
        this.theme = theme;
    }

    public int colorTitleBG() {
        return theme.colorTitleBG();
    }

    public int colorBG() {
        return theme.colorBG();
    }

    public int colorBGCircle() {
        return theme.colorBGCircle();
    }

    public int colorTitle() {
        return theme.colorTitle();
    }

    public int colorToday() {
        return theme.colorToday();
    }

    public int colorG() {
        return theme.colorG();
    }

    public int colorF() {
        return theme.colorF();
    }

    public int colorWeekend() {
        return theme.colorWeekend();
    }

    public int colorHoliday() {
        return theme.colorHoliday();
    }

    public int colorL() {
        if (theme instanceof DPCNTheme) {
            return ((DPCNTheme) theme).colorL();
        }
        return 0;
    }

    public int colorDeferred() {
        if (theme instanceof DPCNTheme) {
            return ((DPCNTheme) theme).colorDeferred();
        }
        return 0;
    }
}
