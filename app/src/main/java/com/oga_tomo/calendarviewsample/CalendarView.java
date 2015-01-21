package com.oga_tomo.calendarviewsample;

import android.text.Layout;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by tomoko on 15/01/11.
 */
public class CalendarView extends LinearLayout {
    private OnDateClickListener mDateListener;
    private OnNextBackClickListener mNextBackListener;

    private Context context;

    // 色は、attrs.xmlで定義された、属性値でxmlに指定可能
    // 今日のフォント色
    private static int TODAY_COLOR;
    // 通常のフォント色
    private static int DEFAULT_COLOR;
    // 日曜のフォント色
    private static int SUN_COLOR;
    // 土曜のフォント色
    private static int SAT_COLOR;
    // 今週の背景色
    private static int TODAY_BACKGROUND_COLOR;
    // 通常の背景色
    private static int DEFAULT_BACKGROUND_COLOR;

    // ≫　and　≪　の背景色
    private static int FOCUSED_BACKGROUND_COLOR;

    // クリック状態時の背景色
    private static int BUTTON_BACKGROUND_COLOR;

    //週のはじめの曜日
    private static int BEGINNING_DAY_OF_WEEK;

    // タイトル部分の構成Ｖｉｅｗ
    private LinearLayout mTitleLayout;
    private TextView mTitleView;
    private Button btnFwd;
    private Button btnBack;

    // 曜日の見出し部分の構成Ｖｉｅｗ
    private LinearLayout mWeekLayout;

    // 日付の表示部分、一つのLinearLayoutの要素として一行（週毎）ずつ配列へ代入。
    private ArrayList<LinearLayout> mWeeks;

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        this.setOrientation(VERTICAL);
        mWeeks = new ArrayList<LinearLayout>();
        LinearLayout weeklineLayout;

        // 属性値の読み取り
        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.CalendarView);
        TODAY_COLOR = array.getColor(R.styleable.CalendarView_TODAY_COLOR,
                Color.MAGENTA);
        DEFAULT_COLOR = array.getColor(R.styleable.CalendarView_DEFAULT_COLOR,
                Color.DKGRAY);
        SUN_COLOR = array.getColor(R.styleable.CalendarView_SUN_COLOR,
                Color.RED);
        SAT_COLOR = array.getColor(R.styleable.CalendarView_SAT_COLOR,
                Color.BLUE);
        TODAY_BACKGROUND_COLOR = array.getColor(
                R.styleable.CalendarView_TODAY_BACKGROUND_COLOR, Color.LTGRAY);
        DEFAULT_BACKGROUND_COLOR = array.getColor(
                R.styleable.CalendarView_DEFAULT_BACKGROUND_COLOR,
                Color.TRANSPARENT);
        FOCUSED_BACKGROUND_COLOR = array.getColor(
                R.styleable.CalendarView_FOCUSED_BACKGROUND_COLOR,
                Color.argb(0x66, 0, 0xff, 0));

        BUTTON_BACKGROUND_COLOR = array.getColor(
                R.styleable.CalendarView_BUTTON_BACKGROUND_COLOR,
                Color.TRANSPARENT);

        int dayofweek = array.getInt(
                R.styleable.CalendarView_BEGINNING_DAY_OF_WEEK, 0);
        if (BEGINNING_DAY_OF_WEEK != 0) {
            BEGINNING_DAY_OF_WEEK = MonthlyCalendar.getDayOfWeek(dayofweek);
            MonthlyCalendar.setBeginningDayOfWeek(BEGINNING_DAY_OF_WEEK);
        }
        array.recycle();

        // タイトル部分
        addView(createTitleView(context), new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        // 曜日見出し部分
        addView(createWeekViews(context), new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        // 日付部分　 最大6行必要
        LinearLayout monthlyLayout = new LinearLayout(context);
        monthlyLayout.setOrientation(LinearLayout.VERTICAL);
        monthlyLayout.setBackgroundColor(Color.DKGRAY);

        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        llp.weight = 1;

        for (int i = 0; i < MonthlyCalendar.MAX_WEEK; i++) {
            weeklineLayout = createDayViews(context);
            mWeeks.add(weeklineLayout);
//            addView(weeklineLayout, new LinearLayout.LayoutParams(
//                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            monthlyLayout.addView(weeklineLayout, llp);
        }
        addView(monthlyLayout, llp);

    }

    /**
     * 年月表示用のタイトルViewを生成する
     *
     * @param context
     *            context
     */
    private LinearLayout createTitleView(Context context) {

        mTitleLayout = new LinearLayout(context);
        // << ボタン
        btnBack = new Button(context);
        btnBack.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, 5)); // 末尾のパラメタはweight
        btnBack.setBackgroundColor(BUTTON_BACKGROUND_COLOR);
        btnBack.setText("<<");
        btnBack.setFocusable(false);
        btnBack.setFocusableInTouchMode(false);

        mTitleLayout.addView(btnBack);

        float scaleDensity = context.getResources().getDisplayMetrics().density;

        mTitleView = new TextView(context);
        mTitleView.setTextAppearance(context,android.R.style.TextAppearance_Large);
        mTitleView.setGravity(Gravity.CENTER_HORIZONTAL); // 中央に表示
        mTitleView.setTypeface(null, Typeface.BOLD); // 太字
        mTitleView.setPadding(0, 0, 0, (int) (scaleDensity * 16));
        mTitleLayout.addView(mTitleView);

        // >> ボタン
        btnFwd = new Button(context);
        btnFwd.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, 5)); // 末尾のパラメタはweight
        btnFwd.setBackgroundColor(BUTTON_BACKGROUND_COLOR);
        btnFwd.setFocusable(false);
        btnFwd.setFocusableInTouchMode(false);
        btnFwd.setText(">>");
        mTitleLayout.addView(btnFwd);
        return mTitleLayout;
    }

    /**
     * 曜日表示用のViewを生成する
     *
     * @param context
     *            context
     */
    private LinearLayout createWeekViews(Context context) {

        mWeekLayout = new LinearLayout(context);
        float scaleDensity = context.getResources().getDisplayMetrics().density;

        for (int i = 0; i < MonthlyCalendar.WEEKDAYS; i++) {
            TextView textView = new TextView(context);
            textView.setTextAppearance(context,
                    android.R.style.TextAppearance_Medium);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(0, 0, (int) (scaleDensity * 4), 0);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(0,
                    LayoutParams.WRAP_CONTENT);
            llp.weight = 1;
            mWeekLayout.addView(textView, llp);
        }

        return mWeekLayout;
    }

    /**
     * 日付表示用のViewを生成する
     *
     * @param context
     *            context
     */
    private LinearLayout createDayViews(Context context) {
        float scaleDensity = context.getResources().getDisplayMetrics().density;
        LinearLayout weeklineLayout = new LinearLayout(context);
        weeklineLayout.setOrientation(HORIZONTAL);

        // 1週間分の日付ビュー作成
        for (int i = 0; i < MonthlyCalendar.WEEKDAYS; i++) {
            LinearLayout dailyLayout = new LinearLayout(context);
            dailyLayout.setOrientation(VERTICAL);
            dailyLayout.setBackgroundColor(Color.WHITE);
//            int padding = (int)scaleDensity * 4;
//            dailyLayout.setPadding(padding, 0, padding, 0);
//            dailyLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.layout_frame));

            TextView dayView = new TextView(context);
            dayView.setGravity(Gravity.TOP | Gravity.LEFT);
//            dayView.setPadding(0, (int) (scaleDensity * 4),
//                    (int) (scaleDensity * 4), 0);
            dayView.setClickable(true);
//            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(0,
//                    (int) (scaleDensity * 500));
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            llp.weight = 1;
            dailyLayout.addView(dayView, llp);

/*
            dayView = new TextView(context);
            dayView.setGravity(Gravity.TOP | Gravity.LEFT);
            dayView.setClickable(true);
            dailyLayout.addView(dayView, llp);
*/
            llp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            llp.weight = 1;
            llp.setMargins(1,1,1,1);
            weeklineLayout.addView(dailyLayout, llp);
        }
        return weeklineLayout;

    }

    /**
     * 年と月を指定して、カレンダーの表示を初期化する
     *
     * @param year
     *            年の指定
     * @param month
     *            月の指定
     * @param flexibleLine
     *            日数に応じて、行数を可変にするかどうか？true：可変
     *
     */
    public void set(int year, int month, boolean flexibleLine,boolean hasNextbackButton) {
        setTitle(year, month,hasNextbackButton);
        setWeeks();
        setDays(year, month, flexibleLine);
    }

    /**
     * タイトルを設定する
     *
     * @param year
     *            年の指定
     * @param month
     *            月の指定
     * @param hasNextbackButton
     *            ≪　や　≫　の翌前月へ遷移するボタンを付けるかの指定
     */
    private void setTitle(final int year, final int month, boolean hasNextbackButton) {

        if(hasNextbackButton){

            // クリック時に背景を変更する為に、stateの準備
            StateListDrawable stateBackDrawable = new StateListDrawable();
            StateListDrawable stateFwdDrawable = new StateListDrawable();
            Drawable tap = new ColorDrawable(FOCUSED_BACKGROUND_COLOR);
            stateBackDrawable.addState(
                    new int[] { android.R.attr.state_selected }, tap);
            stateBackDrawable.addState(
                    new int[] { android.R.attr.state_pressed }, tap);
            stateFwdDrawable.addState(
                    new int[] { android.R.attr.state_selected }, tap);
            stateFwdDrawable.addState(
                    new int[] { android.R.attr.state_pressed }, tap);
            btnBack.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int backyear = -1;
                    int backmonth = -1;
                    if (mNextBackListener != null) {
                        if(month == 1){
                            backyear = year -1;
                            backmonth = 12;
                        } else {
                            backyear = year;
                            backmonth = month - 1;
                        }
                        mNextBackListener.onNextBackClick(backyear, backmonth, MonthlyCalendar.BACK_MONTH);
                    }
                }
            });

            // クリック時に背景を変更するselectorのセット
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                btnBack.setBackgroundDrawable(stateBackDrawable);
            } else {
                btnBack.setBackground(stateBackDrawable);
            }

            btnFwd.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int nextyear = -1;
                    int nextmonth = -1;
                    if (mNextBackListener != null) {
                        if(month == 12){
                            nextyear = year + 1;
                            nextmonth = 1;
                        } else {
                            nextyear = year;
                            nextmonth = month + 1;
                        }
                        mNextBackListener.onNextBackClick(nextyear, nextmonth, MonthlyCalendar.NEXT_MONTH);
                    }
                }
            });

            // クリック時に背景を変更するselectorのセット
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                btnFwd.setBackgroundDrawable(stateFwdDrawable);
            } else {
                btnFwd.setBackground(stateFwdDrawable);
            }
        } else {
            btnBack.setVisibility(View.INVISIBLE);
            btnFwd.setVisibility(View.INVISIBLE);
        }
        // 年月フォーマット文字列
        mTitleView.setText(year + "年" + month + "月");
    }

    /**
     * 曜日を設定する（曜日の見出し）
     */
    private void setWeeks() {
        String[] weeks = MonthlyCalendar.getWeeks();
        for (int i = 0; i < MonthlyCalendar.WEEKDAYS; i++) {
            TextView textView = (TextView) mWeekLayout.getChildAt(i);
            textView.setText(weeks[i]); // テキストに曜日を表示
            if (i == MonthlyCalendar.getSunSatPosition(MonthlyCalendar.SUNDAY)) {
                textView.setTextColor(SUN_COLOR);
            } else if (i == MonthlyCalendar
                    .getSunSatPosition(MonthlyCalendar.SATURDAY)) {
                textView.setTextColor(SAT_COLOR);
            }
        }
    }

    /**
     * 日付を設定する
     *
     * @param year
     *            年の指定
     * @param month
     *            月の指定
     * @param flexibleLine
     *            日数に応じて、行数を可変にするかどうか？true：可変、false ６行固定
     */
    private void setDays(final int year, final int month, boolean flexibleLine) {

        MonthlyCalendar calendar = new MonthlyCalendar(year, month);

        int[][] calendarDay = calendar.getCalendarMatrix();
        boolean isNextMonth = calendar.isNextMonth();
        boolean isThisMonth = calendar.isThisMonth();
        boolean isBackMonth = calendar.isBackMonth();
        int maxWeek = calendar.getMaxWeek(flexibleLine);

        int thisDay = MonthlyCalendar.getToday(MonthlyCalendar.TODAY_DAY);
        int sun_position = MonthlyCalendar
                .getSunSatPosition(MonthlyCalendar.SUNDAY);
        int sat_position = MonthlyCalendar
                .getSunSatPosition(MonthlyCalendar.SATURDAY);

        // 日付のセット
        for (int i = 0; i < maxWeek; i++) {
            LinearLayout weekLayout = mWeeks.get(i);
            weekLayout.setBackgroundColor(DEFAULT_BACKGROUND_COLOR);
            for (int j = 0; j < MonthlyCalendar.WEEKDAYS; j++) {

                // クリック時に背景を変更する為に、stateの準備
                StateListDrawable stateDrawable = new StateListDrawable();
                Drawable tap = new ColorDrawable(FOCUSED_BACKGROUND_COLOR);
                stateDrawable.addState(
                        new int[] { android.R.attr.state_selected }, tap);
                stateDrawable.addState(
                        new int[] { android.R.attr.state_pressed }, tap);

                final LinearLayout dayLayout = (LinearLayout) weekLayout.getChildAt(j);
//                dayLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.layout_frame));
                final TextView dayView = (TextView) dayLayout.getChildAt(0);
//                final TextView dayView2 = (TextView) dayLayout.getChildAt(1);
//                dayView2.setText("test");
                int c = calendarDay[i][j];

                if (c == 0) {
                    dayView.setText(" ");
                } else if (isThisMonth && c == thisDay) {
                    dayView.setTextAppearance(context,
                            android.R.style.TextAppearance_Medium);
                    dayView.setTextColor(TODAY_COLOR);
                    dayView.setTypeface(null, Typeface.BOLD);
                    dayView.setText(String.valueOf(c));

                    // クリック時に背景を変更するselectorのセット
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        dayView.setBackgroundDrawable(stateDrawable);
                    } else {
                        dayView.setBackground(stateDrawable);
                    }
//                    weekLayout.setBackgroundColor(TODAY_BACKGROUND_COLOR);

                } else if (isNextMonth && c == -thisDay && c < -20) {
                    dayView.setTextAppearance(context,
                            android.R.style.TextAppearance_Medium);
                    dayView.setTextColor(TODAY_COLOR);
                    dayView.setTypeface(null, Typeface.BOLD);
                    dayView.setText(String.valueOf(Math.abs(c)));

                    // クリック時に背景を変更するselectorのセット
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        dayView.setBackgroundDrawable(stateDrawable);
                    } else {
                        dayView.setBackground(stateDrawable);
                    }

//                    weekLayout.setBackgroundColor(TODAY_BACKGROUND_COLOR);

                } else if (isBackMonth && c == -thisDay && c >= -20) {
                    dayView.setTextAppearance(context,
                            android.R.style.TextAppearance_Medium);
                    dayView.setTextColor(TODAY_COLOR);
                    dayView.setTypeface(null, Typeface.BOLD);
                    dayView.setText(String.valueOf(Math.abs(c)));

                    // クリック時に背景を変更するselectorのセット
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        dayView.setBackgroundDrawable(stateDrawable);
                    } else {
                        dayView.setBackground(stateDrawable);
                    }

//                    weekLayout.setBackgroundColor(TODAY_BACKGROUND_COLOR);

                } else {
                    dayView.setTextAppearance(context,
                            android.R.style.TextAppearance_Medium);
                    if (j == sun_position) {
                        dayView.setTextColor(SUN_COLOR);
                    } else if (j == sat_position) {
                        dayView.setTextColor(SAT_COLOR);
                    } else {
                        dayView.setTextColor(DEFAULT_COLOR);
                    }
                    dayView.setTypeface(null, Typeface.NORMAL);
                    dayView.setText(String.valueOf(Math.abs(c)));

                    // クリック時に背景を変更するselectorのセット
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        dayView.setBackgroundDrawable(stateDrawable);
                    } else {
                        dayView.setBackground(stateDrawable);
                    }

                }
                dayView.setTag(String.valueOf(c));
                dayView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mDateListener != null) {
                            int day = Integer.valueOf((String) dayView.getTag());
                            if(day < -20){
                                //前月対応
                                int backMonth = month - 1;
                                int backYear = year;
                                if(backMonth == 0){
                                    backYear = backYear -1;
                                    backMonth = 12;
                                }
                                mDateListener.onDateClick(backYear, backMonth,Math.abs(day));
                            }else if(day < 0 && day >= -20){
                                //翌月対応
                                int nextMonth = month + 1;
                                int nextYear = year;
                                if(nextMonth == 13){
                                    nextMonth = 1;
                                    nextYear = nextYear + 1;
                                }
                                mDateListener.onDateClick(nextYear, nextMonth,Math.abs(day));
                            }else{
                                mDateListener.onDateClick(year, month,day);
                            }
                        }
                    }
                });

                dayLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mDateListener != null) {
                            int day = Integer.valueOf((String) dayView.getTag());
                            if(day < -20){
                                //前月対応
                                int backMonth = month - 1;
                                int backYear = year;
                                if(backMonth == 0){
                                    backYear = backYear -1;
                                    backMonth = 12;
                                }
                                mDateListener.onDateClick(backYear, backMonth,Math.abs(day));
                            }else if(day < 0 && day >= -20){
                                //翌月対応
                                int nextMonth = month + 1;
                                int nextYear = year;
                                if(nextMonth == 13){
                                    nextMonth = 1;
                                    nextYear = nextYear + 1;
                                }
                                mDateListener.onDateClick(nextYear, nextMonth,Math.abs(day));
                            }else{
                                mDateListener.onDateClick(year, month,day);
                            }
                        }

                    }
                });
            }
        }
    }

    /**
     * リスナーを追加する
     *
     * @param listener
     */
    public void setOnDateClickListener(OnDateClickListener listener) {
        this.mDateListener = listener;
    }
    public void setOnNextBackClickListener(OnNextBackClickListener listener) {
        this.mNextBackListener = listener;
    }
    /**
     * リスナーを削除する
     */
    public void removeOnDateClickListener() {
        this.mDateListener = null;
    }
    public void removeOnNextBackClickListener() {
        this.mNextBackListener = null;
    }
}
