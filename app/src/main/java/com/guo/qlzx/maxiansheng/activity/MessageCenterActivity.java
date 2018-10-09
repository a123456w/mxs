package com.guo.qlzx.maxiansheng.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.MessageCenterBean;
import com.guo.qlzx.maxiansheng.bean.MessageCenterListBean;
import com.guo.qlzx.maxiansheng.event.ChangeHeaderEvent;
import com.guo.qlzx.maxiansheng.event.HomeNumEvent;
import com.guo.qlzx.maxiansheng.event.OrderNumberEvent;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/4/27.
 * 消息中心
 */

public class MessageCenterActivity extends BaseActivity {

    @BindView(R.id.tv_sever)
    TextView tvSever;
    @BindView(R.id.tv_people)
    TextView tvPeople;
    private PreferencesHelper helper;

    @Override
    public int getContentView() {
        return R.layout.activity_message_center;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("消息中心");
        helper = new PreferencesHelper(this);
    }

    @Override
    public void getData() {
        getMessageData(helper.getToken());
    }
    private void getMessageData(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).getMessageData(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<MessageCenterBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<MessageCenterBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            String system=baseBean.data.getUnread_system_num();
                            String person=baseBean.data.getUnread_person_num();
                            if (!TextUtils.isEmpty(system)&&!"0".equals(system)){
                                tvSever.setVisibility(View.VISIBLE);
                                tvSever.setText(system);
                            }else {
                                tvSever.setVisibility(View.GONE);
                            }
                            if (!TextUtils.isEmpty(person)&&!"0".equals(person)){
                                tvPeople.setVisibility(View.VISIBLE);
                                tvPeople.setText(person);
                            }else {
                                tvPeople.setVisibility(View.GONE);
                            }

                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(MessageCenterActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }
    @OnClick({R.id.rl_system, R.id.rl_inform})
    public void onViewClicked(View view) {
        Intent intent = new Intent(MessageCenterActivity.this, MessageCenterListActivity.class);
        switch (view.getId()) {
            case R.id.rl_system:
                //系统消息
                intent.putExtra("MESSAGETYPE", 0);
                startActivity(intent);
                break;
            case R.id.rl_inform:
                //通知消息
                intent.putExtra("MESSAGETYPE", 1);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getMessageData(helper.getToken());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.post(new ChangeHeaderEvent());
        EventBusUtil.post(new HomeNumEvent());
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBusUtil.post(new ChangeHeaderEvent());
        EventBusUtil.post(new HomeNumEvent());
    }
}
