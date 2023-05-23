package com.newbee.bulid_lib.util;

import android.view.View;

public class SelectViewUtil {
    private View[] views;
    private View nowSelectView;

    public SelectViewUtil(View... views){
       this.views=views;
    }

    public void setSelectViewByIndex(int index){
        if(null==views||views.length==0||index>=views.length){
            return;
        }

        if(null!=nowSelectView){
            nowSelectView.setSelected(false);
            nowSelectView=null;
        }
        nowSelectView=views[index];
        nowSelectView.setSelected(true);
    }
}
