package com.newbee.bulid_lib.util;

import android.view.View;

public class SelectViewUtil {
    private View[] views;
    private int index;

    public SelectViewUtil(View... views){
       this.views=views;
    }

    public void setSelectViewByIndex(int index){
        if(null==views||views.length==0||index>=views.length){
            return;
        }
        View view=getNowShowView();
        if(null!=view){
            view.setSelected(false);
        }
        this.index=index;
        view=getNowShowView();
        if(null!=view){
            view.setSelected(true);
        }

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

    public int getIndex() {
        return index;
    }

    public View getNowShowView(){
        if(null==views||views.length==0){
            return null;
        }
        if(index<0||index>= views.length){
            return null;
        }
        return views[index];
    }

    public View[] getViews() {
        return views;
    }
}
