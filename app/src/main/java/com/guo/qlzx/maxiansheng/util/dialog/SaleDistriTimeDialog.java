package com.guo.qlzx.maxiansheng.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.adapter.DistriDayAdapter;
import com.guo.qlzx.maxiansheng.adapter.DistriTimeAdapter;
import com.guo.qlzx.maxiansheng.bean.DistriDayBean;
import com.guo.qlzx.maxiansheng.bean.DistriTimeBean;
import com.qlzx.mylibrary.util.TimeUtil;
import com.qlzx.mylibrary.util.ToastUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 李 on 2018/6/11.
 */

public class SaleDistriTimeDialog extends Dialog {

    private ImageView ivCancel;
    private ListView lvDay;
    private ListView lvTime;
    private Button btnSubmit;

    private DistriTimeDialog.OnConfimClickListener confimClickListener;

    private DistriDayAdapter dayAdapter;
    private DistriTimeAdapter timeAdapter;


    private ArrayList<DistriDayBean> days;
    private ArrayList<DistriDayBean> times;


    private Context mContext;
    private static DistriTimeBean timeBean;

    private String name = "";
    private long startTime=0,endTime=0;

    public SaleDistriTimeDialog(@NonNull Context context, DistriTimeBean timeBean) {
        super(context, R.style.dialogBase);
        mContext = context;
        this.timeBean = timeBean;

    }

    public void setConfimClickListener(DistriTimeDialog.OnConfimClickListener confimClickListener) {
        this.confimClickListener = confimClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_time);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = ViewGroup.LayoutParams.MATCH_PARENT;
        attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        attributes.dimAmount = 0.5f;
        attributes.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        attributes.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(attributes);
        //初始化界面控件
        initView();
        //初始化界面控件的事件
        initEvent();
        //放置数据
        initData();
    }


    private void initView(){
        ivCancel = findViewById(R.id.iv_cancel);
        lvDay = findViewById(R.id.lv_day);
        lvTime = findViewById(R.id.lv_time);
        btnSubmit = findViewById(R.id.btn_submit);
    }



    private void initEvent(){
        dayAdapter = new DistriDayAdapter(lvDay);
        lvDay.setAdapter(dayAdapter);
        lvDay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setDayFalse();
                name = dayAdapter.getData().get(i).getName();
                dayAdapter.getData().get(i).setSelected(true);
                dayAdapter.notifyDataSetChanged();
                cutTime(i);
            }
        });

        timeAdapter = new DistriTimeAdapter(lvTime);
        lvTime.setAdapter(timeAdapter);
        lvTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setTimeFalse();
                timeAdapter.getData().get(i).setSelected(true);
                timeAdapter.notifyDataSetChanged();
                startTime = timeAdapter.getData().get(i).getStartTime();
                endTime = timeAdapter.getData().get(i).getEndTime();
            }
        });

        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startTime <1 || endTime<1){
                    ToastUtil.showToast(mContext,"请选择时间");
                    return;
                }
                int starTimeStamp =0;
                int endTimeStamp =0;
                if (getOldDate(0).equals(name)){
                    starTimeStamp = (int) (startTime/1000);
                    endTimeStamp = (int) (endTime/1000);
                }else if (getOldDate(1).equals(name)){
                    starTimeStamp = (int) (startTime/1000)+86400;
                    endTimeStamp = (int) (endTime/1000)+86400;
                }else if (getOldDate(2).equals(name)){//后天
                    starTimeStamp = (int) (startTime/1000)+86400*2;
                    endTimeStamp = (int) (endTime/1000)+86400*2;
                }
                confimClickListener.onConfimListener(name,starTimeStamp,endTimeStamp);
                dismiss();
            }
        });
    }

    private void initData(){

        days = new ArrayList<>();
        days.add(new DistriDayBean(getOldDate(0),true));
        days.add(new DistriDayBean(getOldDate(1),false));
        days.add(new DistriDayBean(getOldDate(2),false));

        dayAdapter.setData(days);

        name= dayAdapter.getData().get(0).getName();
        cutTime(0);


    }


    /**
     * 获取前n天日期、后n天日期
     *
     * @param distanceDay 前几天 如获取前7天日期则传-7即可；如果后7天则传7
     * @return
     */
    public static String getOldDate(int distanceDay) {
        SimpleDateFormat dft = new SimpleDateFormat("MM-dd");
        Date beginDate = new Date();
        beginDate.setTime(Long.valueOf(timeBean.getNow_time())*1000);
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dft.format(endDate);
    }


    /**
     * 分割时间
     * @param day 0为今天 1为明天 2为后天
     */
    public void cutTime(int day){

        times = new ArrayList<>();

        String nowTime = TimeUtil.getSimpleDate(timeBean.getNow_time());//年月日
        String star = nowTime+" "+timeBean.getBegin_time();
        String end = nowTime+" "+timeBean.getEnd_start();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long startTime = format.parse(star.toString()).getTime();
            long endTime = format.parse(end.toString()).getTime();
            int num = (int) ((endTime-startTime)/1000/timeBean.getTimes());
            long tempstartTime =0;
            long tempendTime =0;
            times.clear();
            if (day == 0){
                for (int i=0;i<num;i++){
                    tempstartTime = startTime+timeBean.getTimes()*1000*i;
                    tempendTime = startTime+timeBean.getTimes()*1000*(i+1);
                    if (tempstartTime>Calendar.getInstance().getTimeInMillis()){
                        times.add(new DistriDayBean(tempstartTime,tempendTime));
                    }
                }
            }else {
                for (int i=0;i<num;i++){
                    tempstartTime = startTime+timeBean.getTimes()*1000*i;
                    tempendTime = startTime+timeBean.getTimes()*1000*(i+1);
                    times.add(new DistriDayBean(tempstartTime,tempendTime));
                }
            }
            timeAdapter.setData(times);


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private void setDayFalse(){
        for (int i =0;i<dayAdapter.getData().size();i++){
            dayAdapter.getData().get(i).setSelected(false);
        }
    }

    private void setTimeFalse(){
        for (int i =0;i<timeAdapter.getData().size();i++){
            timeAdapter.getData().get(i).setSelected(false);
        }
    }


    public interface OnConfimClickListener{
        public void onConfimListener(String name,long startTime,long endTime);
    }
}
