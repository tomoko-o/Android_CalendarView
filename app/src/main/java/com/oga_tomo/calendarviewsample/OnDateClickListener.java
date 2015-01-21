package com.oga_tomo.calendarviewsample;

import java.util.EventListener;

/**
 * Created by tomoko on 15/01/11.
 */
public interface OnDateClickListener extends EventListener{
    public void onDateClick(int year,int month,int day);
}
