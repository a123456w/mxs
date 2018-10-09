package com.guo.qlzx.maxiansheng.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.fragment.CouponCenterFragment;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.widget.LoadingLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 李 on 2018/4/27.
 * 非新人进入-领券中心
 */

public class CouponCenterActivity extends BaseActivity {

    @BindView(R.id.tl_title)
    TabLayout tlTitle;
    @BindView(R.id.vp_content)
    ViewPager vpContent;

    private CouponCenterFragment one=new CouponCenterFragment(0);
    private CouponCenterFragment two=new CouponCenterFragment(1);
    private CouponCenterFragment three=new CouponCenterFragment(2);
    private List<String> list = new ArrayList<>();
    private MyAdapter adapter;
    private List<CouponCenterFragment> fragments = new ArrayList<>();
    @Override
    public int getContentView() {
        return R.layout.activity_coupon_center;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("领券中心");

        initData();

    }

    private void initData() {
        fragments.add(one);
        fragments.add(two);
        fragments.add(three);
        //标题数据
        list.add("新人优惠券");
        list.add("普通优惠券");
        list.add("PLUS会员优惠券");
        adapter = new MyAdapter(getSupportFragmentManager(), list, fragments);
        vpContent.setAdapter(adapter);
        /*Tab与ViewPager绑定*/
        tlTitle.setupWithViewPager(vpContent);
    }

    @Override
    public void getData() {

    }

     class MyAdapter extends FragmentPagerAdapter{
         private List<String> list;
         private List<CouponCenterFragment> fragments = new ArrayList<>();
         public MyAdapter(FragmentManager fm,List<String > list,List<CouponCenterFragment> fragments) {
             super(fm);
             this.fragments=fragments;
             this.list=list;
         }
         @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

         @Override
         public CharSequence getPageTitle(int position) {
             return list.get(position);
         }
     }
}
