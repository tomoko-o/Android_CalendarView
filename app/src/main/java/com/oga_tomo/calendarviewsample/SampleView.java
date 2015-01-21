package com.oga_tomo.calendarviewsample;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by tomoko on 15/01/11.
 */
public class SampleView extends LinearLayout {
    public SampleView(Context context) {
        this(context, null);
    }

    public SampleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setOrientation(VERTICAL);

        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);

        LinearLayout titleLayout = new LinearLayout(context);
        TextView title = new TextView(context);
        title.setText("タイトル");
        titleLayout.addView(title);
        addView(titleLayout, llp);

        llp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        llp.weight = 1;

        LinearLayout monthlyLayout = new LinearLayout(context);
        monthlyLayout.setOrientation(VERTICAL);

        int colors[] = {Color.GRAY, Color.BLUE, Color.GREEN};
        for(int i = 0; i < 3; i++){

            LinearLayout weeklyLayout = new LinearLayout(context);
            weeklyLayout.setOrientation(HORIZONTAL);
//            weeklyLayout.setBackgroundColor(colors[i]);

            for (int j = 0; j < 3; j++){
                LinearLayout dailyLayout = new LinearLayout(context);
                dailyLayout.setBackgroundColor(colors[i]);
                dailyLayout.setOrientation(VERTICAL);

                TextView text = new TextView(context);
                text.setText(String.valueOf(j + 1));
                text.setTextSize(20);

                LinearLayout.LayoutParams textLlp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
                textLlp.weight = 1;

                dailyLayout.addView(text, textLlp);
                llp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT);
                llp.setMargins(1,1,1,1);
                llp.weight = 1;

                weeklyLayout.addView(dailyLayout, llp);
            }
            llp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            llp.weight = 1;

            monthlyLayout.addView(weeklyLayout, llp);

        }
        llp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        llp.weight = 1;

        addView(monthlyLayout, llp);

    }
}
