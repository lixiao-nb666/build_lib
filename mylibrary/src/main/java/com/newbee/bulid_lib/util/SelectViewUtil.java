package com.newbee.bulid_lib.util;

import android.view.View;

public class SelectViewUtil {
    private View[] views;
    private View nowSelectView;
    private int index;

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
        this.index=index;
        nowSelectView=views[index];
        nowSelectView.setSelected(true);
    }

    public int getCount(){
        if(null==views){
            return 0;
        }
        return views.length;
    }

    public void toLast(){
        if(null==views||views.length==0){
            return ;
        }
        index--;
        if(index<0){
            index=views.length-1;
        }
        setSelectViewByIndex(index);
    }

    public void toNext(){
        if(null==views||views.length==0){
            return ;
        }
        index++;
        if(index>=views.length){
            index=0;
        }
        setSelectViewByIndex(index);
    }

    public View getNowSelectView(){
        return nowSelectView;
    }
}
