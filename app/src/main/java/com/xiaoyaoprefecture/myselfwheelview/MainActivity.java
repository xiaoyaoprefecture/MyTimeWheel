package com.xiaoyaoprefecture.myselfwheelview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xiaoyaoprefecture.myselfwheelview.util.OnWheelChangedListener;
import com.xiaoyaoprefecture.myselfwheelview.util.WheelView;
import com.xiaoyaoprefecture.myselfwheelview.util.adapters.ArrayWheelAdapter;
import com.xiaoyaoprefecture.myselfwheelview.util.adapters.ListWheelAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,OnWheelChangedListener{
    private WheelView mYear;
    private WheelView mMonth;
    private WheelView mDay;
    private Button mBtn_Sure;
    private List<String>year=new ArrayList<>();
    private String[]month= new String[] { "1月", "2月", "3月", "4月", "5月",
            "6月", "7月", "8月", "9月", "10月", "11月", "12月" };
    String year_a;// 每次更新时存储到这个里面
    String month_a;
    String year_b;// 这个只记录第一次获取的时间
    String month_b;
    //需要精确到天数
    String day_a;
    String day_b;
    private List<String>day=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Calendar calendar=Calendar.getInstance();
        //获取当前的年
        year_a=year_b=calendar.get(Calendar.YEAR)+"";
        //获取当前的月
        month_a = month_b = calendar.get(Calendar.MONTH) + 1 + "";
        //获取当前的天数
        day_a = day_b = calendar.get(Calendar.DAY_OF_MONTH) + "";
        getYear();
        initView();
        setVisibleItem();
        setTime();
    }

    /**
     * 把当前的时间添加到时间选择框中
     */
    private void setTime() {
        int yy=0;
        int mm=0;
        int dd=0;
        for (int i=0;i<year.size();i++){
            if (year.get(i).equals(year_b+"年")){
                yy=i;
            }
        }
        for (int i=0;i<month.length;i++){
            if (month[i].equals(month_b+"月")){
                mm=i;
            }
        }
        for (int i = 0; i < day.size(); i++) {
            if (day.get(i).equals(day_b+"日")) {
                dd = i;
                System.out.println("dd  "+dd);
            }
        }
        mYear.setCurrentItem(yy);
        mMonth.setCurrentItem(mm);
        mDay.setCurrentItem(dd);
    }

    /**
     * 设置可视数目
     */
    private void setVisibleItem() {
        mYear.setVisibleItems(7);
        mMonth.setVisibleItems(7);
        mDay.setVisibleItems(7);
        addyear();
        addmonth();
    }

    /**
     * 将月份添加到滚轮中
     */
    private void addmonth() {
        mMonth.setViewAdapter(new ArrayWheelAdapter<String>(this,month));
        try {
            int pCurrent = mMonth.getCurrentItem();
            // 将当前的月赋值给全局
            month_a = month[pCurrent];
            getday(Integer.parseInt(year_a.replace("年", "")),
                    Integer.parseInt(month_a.replace("月", "")));
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 将年份添加到滚轮中
     */
    private void addyear() {
        mYear.setViewAdapter(new ListWheelAdapter<String>(this,year));
        try {
            int pCurrent = mYear.getCurrentItem();
            // 将当前的年赋值给全局
            year_a = year.get(pCurrent);
            // 因为在第一次进入的时候是还没有月份的数据的，所以需要排除掉
            if (month_a != null) {
                getday(Integer.parseInt(year_a.replace("年", "")),
                        Integer.parseInt(month_a.replace("月", "")));
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 初始化控件,并设置点击事件
     */
    private void initView() {
        mYear= (WheelView) findViewById(R.id.mYear);
        mMonth= (WheelView) findViewById(R.id.mMonth);
        mDay= (WheelView) findViewById(R.id.mDay);
        mBtn_Sure= (Button) findViewById(R.id.mBtn_Sure);
        mBtn_Sure.setOnClickListener(this);
        //设置循环
        mYear.setCyclic(true);
        mMonth.setCyclic(true);
        mDay.setCyclic(true);
        //给年份月份添加改变事件
        mYear.addChangingListener(this);
        mMonth.addChangingListener(this);
        mDay.addChangingListener(this);
    }
    /**
     * 根据当前的年月来获取日的信息
     *
     * @param year
     * @param month
     */
    private void getday(int year, int month) {
        day = new ArrayList<String>();
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month);
        a.set(Calendar.DATE, 1);
        a.set(Calendar.DATE, -1);
        int maxdate = a.get(Calendar.DATE);
        for (int i = 1; i <= maxdate + 1; i++) {
            day.add(i + "日");
        }
        mDay.setViewAdapter(new ListWheelAdapter<String>(
                this, day));
        upday();
    }
    private void upday() {

        try {
            int pCurrent = mDay.getCurrentItem();
            day_a = day.get(pCurrent);

        } catch (Exception e) {
            mDay.setCurrentItem(day.size() - 1);
            day_a = day.get(day.size() - 1);
        }
    }
    /**
     * 获取年份
     */
    private void getYear() {
        //这里去获取注册成为app使用者的那一个时间
        int a=2014;
        for (int i=0;i<(Integer.parseInt(year_a)-a+1);i++){
            year.add((a+i)+"年");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mBtn_Sure:
                showSelectedResult();
                break;
            default:
                break;
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel==mYear){//年份的滚动
            addyear();
        }else if(wheel==mMonth){
            addmonth();
        }
    }

    private void showSelectedResult() {
        Toast.makeText(this, "当前选中:" + year_a + month_a +day_a,
                Toast.LENGTH_SHORT).show();
    }
}
