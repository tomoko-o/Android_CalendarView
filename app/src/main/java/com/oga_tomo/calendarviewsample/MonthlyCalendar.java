package com.oga_tomo.calendarviewsample;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by tomoko on 15/01/11.
 */
public class MonthlyCalendar {

    public static final int SUNDAY = Calendar.SUNDAY;
    public static final int MONDAY = Calendar.MONDAY;
    public static final int TUESDAY = Calendar.TUESDAY;
    public static final int WEDNESDAY = Calendar.WEDNESDAY;
    public static final int THURSDAY = Calendar.THURSDAY;
    public static final int FRIDAY = Calendar.FRIDAY;
    public static final int SATURDAY = Calendar.SATURDAY;

    public static final int TODAY_YEAR = 11;
    public static final int TODAY_MONTH = 12;
    public static final int TODAY_DAY = 13;

    public static final int BACK_MONTH = 14;
    public static final int NEXT_MONTH = 15;

    public static final int WEEKDAYS = 7;
    public static final int MAX_WEEK = 6;

    private static int BEGINNING_DAY_OF_WEEK = SUNDAY;

    private int year;
    private int month;

    private int[][] calendarMatrix = new int[MAX_WEEK][WEEKDAYS];

    private int startDate;
    private int lastDate;
    private int nextStartDate;
    private int backLastDate;
    private int maxWeekLine;

    /**
     *
     * @param year
     * @param month
     */
    public MonthlyCalendar(int year, int month){
        this.year = year;
        this.month = month;
        calcField(true);
    }

    private void calcField(boolean hasDaysOfNextBackMonth){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();

        // 月の初めの曜日を求めます。
        calendar.set(year, month - 1, 1);
        startDate = calendar.get(Calendar.DAY_OF_WEEK);

        // 月末の日付を求めます。
        calendar.add(Calendar.MONTH, 1);
        nextStartDate = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE, -1);
        lastDate = calendar.get(Calendar.DATE);

        // カレンダー表を作成します。
        int row = 0;
        int column = startDate - BEGINNING_DAY_OF_WEEK;
        if (column < 0)
            column = 7 + column;

        for (int date = 1; date <= lastDate; date++) {
            calendarMatrix[row][column] = date;
            if (column == MAX_WEEK) {
                row++;
                column = 0;
            } else {
                column++;
            }
            maxWeekLine = row;
        }

        if (hasDaysOfNextBackMonth) {

            // 前月の月終わりの日付を付加します。当月日付以外はマイナスを付ける。
            calendar.set(year, month - 1, 1);
            calendar.add(Calendar.DATE, -1);
            backLastDate = - calendar.get(Calendar.DATE);

            int back_column = startDate - BEGINNING_DAY_OF_WEEK;
            if (back_column < 0) back_column = 7 + back_column;
            if (back_column != 0) {
                for (int j = back_column - 1; j >= 0; j--) {
                    calendarMatrix[0][j] = backLastDate;
                    backLastDate = backLastDate + 1;
                }
            }
            //翌月の月初めの日付を付加します。当月日付以外はマイナスを付ける。
            int next_column = (BEGINNING_DAY_OF_WEEK - nextStartDate);
            if(next_column < 0) next_column = next_column + 7;
            int date = -1;
            if (next_column != 0) {
                for (int k = next_column - 1; k >= 0; k--) {
                    calendarMatrix[maxWeekLine][(WEEKDAYS-1)-k] = date;
                    date = date - 1;
                }
            }
            if(maxWeekLine < 5){
                for(int weekLineRow = maxWeekLine + 1;weekLineRow < 6;weekLineRow++){
                    for (int n = 0; n < 7; n++) {
                        calendarMatrix[weekLineRow][n] = date;
                        date = date - 1;
                    }
                }
            }
        }

    }

    /**
     *
     * @return
     */
    public static String[] getWeeks(){
        String[] strWeek = new String[7];
        Calendar week = Calendar.getInstance();
        week.set(Calendar.DAY_OF_WEEK, BEGINNING_DAY_OF_WEEK);

        SimpleDateFormat weekFormatter = new SimpleDateFormat("E");

        for(int i = 0; i < WEEKDAYS; i++){
            strWeek[i] = weekFormatter.format(week.getTime());
            week.add(Calendar.DAY_OF_MONTH, 1);
        }

        return strWeek;
    }

    /**
     *
     * @param flexibleLine
     * @return
     */
    public int getMaxWeek(boolean flexibleLine){
        if(flexibleLine){
            return maxWeekLine + 1;
        }else {
            return MAX_WEEK;
        }
    }

    /**
     *
     * @param todayConst
     * @return
     */
    public static int getToday(int todayConst){
        int today = 0;
        Calendar todayCalendar = Calendar.getInstance();

        switch (todayConst){
            case TODAY_YEAR:
                today = todayCalendar.get(Calendar.YEAR);
                break;
            case TODAY_MONTH:
                today = todayCalendar.get(Calendar.MONTH) + 1;
                break;
            case TODAY_DAY:
                today = todayCalendar.get(Calendar.DATE);
                break;
            default:
                today = 0;
        }

        return today;
    }

    /**
     *
     * @param dayOfWeek
     * @return
     */
    public static int getDayOfWeek(int dayOfWeek){
        switch (dayOfWeek) {
            case 0:
                dayOfWeek = SUNDAY;
                break;
            case 1:
                dayOfWeek = MONDAY;
                break;
            case 2:
                dayOfWeek = TUESDAY;
                break;
            case 3:
                dayOfWeek = WEDNESDAY;
                break;
            case 4:
                dayOfWeek = THURSDAY;
                break;
            case 5:
                dayOfWeek = FRIDAY;
                break;
            case 6:
                dayOfWeek = SATURDAY;
                break;
            default:
                dayOfWeek = -1;
        }
        return dayOfWeek;
    }

    /**
     *
     * @param dayOfWeek
     * @return
     */
    public static int getSunSatPosition(int dayOfWeek){
        int sun_position = -1;
        int sat_position = -1;

        switch (BEGINNING_DAY_OF_WEEK) {
            case SUNDAY:
                sat_position = 6;
                sun_position = 0;
                break;
            case MONDAY:
                sat_position = 5;
                sun_position = 6;
                break;
            case TUESDAY:
                sat_position = 4;
                sun_position = 5;
                break;
            case WEDNESDAY:
                sat_position = 3;
                sun_position = 4;
                break;
            case THURSDAY:
                sat_position = 2;
                sun_position = 3;
                break;
            case FRIDAY:
                sat_position = 1;
                sun_position = 2;
                break;
            case SATURDAY:
                sat_position = 0;
                sun_position = 1;
                break;
        }
        if (dayOfWeek == SUNDAY) {
            return sun_position;
        } else if (dayOfWeek == SATURDAY) {
            return sat_position;
        } else {
            return -1;
        }
    }

    // セッター(頭の曜日の設定)　・・・　MonthlyCalendarのインスタンス作成前に実施（デフォルト：日曜日）
    public static void setBeginningDayOfWeek(int BEGINNING_DAY_OF_WEEK) {
        MonthlyCalendar.BEGINNING_DAY_OF_WEEK = BEGINNING_DAY_OF_WEEK;
    }

    public int getYear(){
        return year;
    }

    public int getMonth(){
        return month;
    }

    public static int getBeginningDayOfWeek(){
        return BEGINNING_DAY_OF_WEEK;
    }

    public int[][] getCalendarMatrix(){
        return calendarMatrix;
    }

    public int getStartDate(){
        return startDate;
    }

    public int getLastDate(){
        return lastDate;
    }

    public boolean isBackMonth(){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(getToday(MonthlyCalendar.TODAY_YEAR), getToday(MonthlyCalendar.TODAY_MONTH) - 1, 1);
        calendar.add(Calendar.MONTH, -1);
        return (year == calendar.get(Calendar.YEAR) && month == calendar.get(Calendar.MONTH) + 1);
    }

    public boolean isThisMonth(){
        return (year == MonthlyCalendar.getToday(MonthlyCalendar.TODAY_YEAR)
                && month == MonthlyCalendar.getToday(MonthlyCalendar.TODAY_MONTH));
    }

    public boolean isNextMonth(){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(getToday(MonthlyCalendar.TODAY_YEAR), getToday(MonthlyCalendar.TODAY_MONTH) - 1, 1);
        calendar.add(Calendar.MONTH, +1);
        return (year == calendar.get(Calendar.YEAR) && month == calendar.get(Calendar.MONTH) + 1);
    }
}

