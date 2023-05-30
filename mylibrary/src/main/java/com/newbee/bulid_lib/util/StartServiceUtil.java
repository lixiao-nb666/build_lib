package com.newbee.bulid_lib.util;

import android.content.Context;
import android.content.Intent;

public class StartServiceUtil {

    public static void startOtherService(Context context,String pckStr,String action){
        Intent intent = new Intent();
        intent.setAction(action);  //应用在清淡文件中注册的action
        intent.setPackage(pckStr);//应用程序的包名
        context.startService(intent);
    }


}
