package com.newbee.build;

import android.Manifest;
import android.view.View;
import android.widget.TextView;

import com.newbee.bulid_lib.mybase.activity.welcome.BaseWelcomeActivity;
import com.newbee.bulid_lib.mybase.activity.welcome.bean.WelcomeInfoBean;
import com.newbee.bulid_lib.util.AudioUtil;

import java.util.ArrayList;
import java.util.List;

public class DemoActivity extends BaseWelcomeActivity {
    private TextView tv;

    @Override
    public int getWelcomeLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initWelcomeView() {
        tv=findViewById(R.id.tvTitle);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nowNumb=AudioUtil.getInstance().getVolume();

                AudioUtil.getInstance().setVolume(nowNumb+1);
            }
        });
    }

    @Override
    public void initWelcomeData() {

    }

    @Override
    public void initWelcomeControl() {

    }

    @Override
    public WelcomeInfoBean getWelcomeInfoBean() {
        WelcomeInfoBean welcomeInfoBean=new WelcomeInfoBean();
        List<String> permissions=new ArrayList<>();
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        welcomeInfoBean.setPermissionList(permissions);
        welcomeInfoBean.setNeedFilePermission(true);
        return welcomeInfoBean;
    }

    @Override
    public void userNoPermission() {

    }

    @Override
    public void userGetAllPermission() {
        showToast("get all permission");
        AudioUtil.getInstance().init(context);

//        finish();
    }
}
