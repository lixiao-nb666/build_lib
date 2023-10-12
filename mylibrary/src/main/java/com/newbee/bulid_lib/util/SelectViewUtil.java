package com.newbee.bulid_lib.util;

import android.view.View;

import com.newbee.bulid_lib.mybase.share.MyShare;

public class SelectViewUtil {
    private View[] views;
    private int index;
    private String shareKey;

    public SelectViewUtil(View... views){
       this.views=views;
       this.shareKey="SelectViewUtil:selectIndex";
    }


    public SelectViewUtil(String shareKey,View... views){
        this.shareKey=shareKey;
        this.views=views;
    }

    public int getShareIndex(){
        String str= MyShare.getInstance().getString(shareKey,"-1");
        int shareIndex=Integer.valueOf(str);
        return shareIndex;
    }


    public void recoveryView(int def){
        index=getShareIndex();
        setShowView();

    }


    private void setShowView(){
        if(null==views||views.length==0||index>=views.length){
            return;
        }
        this.index=index;
        int i=0;
        for(View v:views){
            v.setSelected(i==this.index);
            i++;
        }
    }

    public void setSelectViewByIndex(int index){
        setShowView();
        MyShare.getInstance().putString(shareKey,this.index+"");
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
}
