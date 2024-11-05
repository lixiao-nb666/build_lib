package com.newbee.bulid_lib.util.phone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.Display;

import com.newbee.bulid_lib.mybase.LG;
import com.newbee.bulid_lib.mybase.appliction.BaseApplication;
import com.newbee.bulid_lib.mybase.share.MyShare;
import com.newbee.bulid_lib.util.Md5Util;
import com.newbee.bulid_lib.util.RandomUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by xiefuning on 2017/4/13.
 * about:
 */

public class PhoneUtil {
    private final String tag = getClass().getName() + ">>>>";
    private TelephonyManager manager;
    private Context context;
    private PackageInfo packageInfo;
    private static PhoneUtil phoneUtil;

    private PhoneUtil() {
        this.context = BaseApplication.getContext();
        manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            PackageManager packageManager = context.getPackageManager();
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static PhoneUtil getInstance() {
        if (phoneUtil == null) {
            synchronized (PhoneUtil.class) {
                if (phoneUtil == null) {
                    phoneUtil = new PhoneUtil();
                }
            }
        }
        return phoneUtil;
    }


    public String getImei() {
        String imei = manager.getDeviceId();
        return TextUtils.isEmpty(imei) ? "" : imei;
    }

    public String getImsi() {
        String imsi = manager.getSubscriberId();
        return TextUtils.isEmpty(imsi) ? "" : imsi;
    }


    public String loadFileAsString(String fileName) throws Exception {
        File file = new File(fileName);
        if (!file.exists()) return "";
        FileReader reader = new FileReader(fileName);
        String text = loadReaderAsString(reader);
        reader.close();
        return text;
    }

    public String loadReaderAsString(Reader reader) throws Exception {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[4096];
        int readLength = reader.read(buffer);
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength);
            readLength = reader.read(buffer);
        }
        return builder.toString();
    }

    /**
     * VersionCode升级App版本时用
     *
     * @return
     */
    public String getVersionCode() {
        if (packageInfo != null) return packageInfo.versionCode + "";
        return "";
    }
    //VersionCode升级App版本时用，VersionName显示给用户

    /**
     * VersionName显示给用户
     *
     * @return
     */
    public String getVersionName() {
        if (packageInfo != null) return packageInfo.versionName;
        return "";
    }

    public String getAppName() {
        if (packageInfo != null) {
            //获取albelRes
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } else {
            return "";
        }

    }

    public String getCountryId() {
        String CountryID = manager.getSimCountryIso().toUpperCase();
        if (TextUtils.isEmpty(CountryID)) {
            if (CountryID.length() > 0) return CountryID;
        }
        return manager.getNetworkCountryIso();
    }

    public String getDeviceId() {
        return manager.getDeviceId();
    }

    public String getDeviceName() {
        return Build.MODEL;
    }

    public String getLang() {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        return language;
    }

    public String getSDK_INT() {
        return Build.VERSION.SDK_INT + "";
    }

    public String getOsVersion() {
        return Build.VERSION.RELEASE;
    }


    public String getRatio(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay(); //Activity#getWindowManager()
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        StringBuilder sb = new StringBuilder();
        sb.append(width);
        sb.append("*");
        sb.append(height);
        return sb.toString();
    }

    /**
     * 屏幕是否横屏
     */
    public boolean screenIsLand(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay(); //Activity#getWindowManager()
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        if (width > height) {
            return true;
        }
        return false;
    }

    public String getAndroidId() {
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return TextUtils.isEmpty(androidId) ? "" : androidId;
    }


    public String[] getVersion() {
        String[] version = {"null", "null", "null", "null"};
        String str1 = "/proc/version";
        String str2;
        String[] arrayOfString;
        FileReader localFileReader = null;
        BufferedReader localBufferedReader = null;
        try {
            localFileReader = new FileReader(str1);
            localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            version[0] = arrayOfString[2];//KernelVersion

        } catch (IOException e) {
        } finally {
            try {
                if (localFileReader != null) {
                    localFileReader.close();
                }
                if (localBufferedReader != null) {
                    localBufferedReader.close();
                }
            } catch (Exception e) {
            }
        }
        version[1] = Build.VERSION.RELEASE;// firmware version
        version[2] = Build.MODEL;//model
        version[3] = Build.DISPLAY;//system version
        return version;
    }


    public String getSdTotalSize(Context context) {
        StatFs sf = new StatFs("/mnt/sdcard");
        long blockSize = sf.getBlockSize();
        long totalBlocks = sf.getBlockCount();
        return Formatter.formatFileSize(context, blockSize * totalBlocks);
    }

    public String getSdAvailableSize(Context context) {
        StatFs sf = new StatFs("/mnt/sdcard");
        long blockSize = sf.getBlockSize();
        long availableBlocks = sf.getAvailableBlocks();
        return Formatter.formatFileSize(context, blockSize * availableBlocks);
    }

    public int readSystem() {
        try {
            StatFs sf = new StatFs("/mnt/sdcard");
            long blockSize = sf.getBlockSize();

            //SD卡的总量
            long totalBlocks = sf.getBlockCount();
            long sdCount = blockSize * totalBlocks;
            //SD卡还能使用的
            long availableBlocks = sf.getAvailableBlocks();
            long sdCanUse = blockSize * availableBlocks;
            int percent = (int) (sdCanUse * 100 / sdCount);
            return percent;
        } catch (Exception e) {
            return 0;
        }


    }

    public String[] getSdInfo(Context context) {
        String[] strs = new String[3];
        try {
            StatFs sf = new StatFs("/mnt/sdcard");
            long blockSize = sf.getBlockSize();

            //SD卡的总量
            long totalBlocks = sf.getBlockCount();
            long sdCount = blockSize * totalBlocks;
            //SD卡还能使用的
            long availableBlocks = sf.getAvailableBlocks();
            long sdCanUse = blockSize * availableBlocks;
            int percent = (int) (sdCanUse * 100 / sdCount);
            strs[0] = Formatter.formatFileSize(context, sdCount);
            strs[1] = Formatter.formatFileSize(context, sdCanUse);
            strs[2] = percent + "";
        } catch (Exception e) {
            strs[0] = "";
            strs[1] = "";
            strs[2] = "";
        }
        return strs;
    }


    public String getPhoneType() {
        String[] strs = getVersion();
        if (strs != null && strs.length > 0) {
            return strs[strs.length - 1];
        }
        return "";
    }

    // 获取设备信息
    public void getDeviceInfo() {
        LG.i(tag, "系统参数------------------------------begin");
        //主板
        String board = Build.BOARD;
        LG.i(tag, "主板：" + board);
        //系统定制商
        String brand = Build.BRAND;
        LG.i(tag, "系统定制商：" + brand);
        //设备信息
        String device = Build.DEVICE;
        LG.i(tag, "系统定制商：" + device);
        //显示屏参数
        String display = Build.DISPLAY;
        LG.i(tag, "显示屏参数：" + display);
        //唯一编号
        String fingderprint = Build.FINGERPRINT;
        LG.i(tag, "唯一编号：" + fingderprint);
        //硬件序列号
        String serial = Build.SERIAL;
        LG.i(tag, "硬件序列号：" + serial);
        //修订版本列表
        String id = Build.ID;
        LG.i(tag, "修订版本列表：" + id);
        //硬件制造商
        String manufacturer = Build.MANUFACTURER;
        LG.i(tag, "硬件制造商：" + manufacturer);
        //版本
        String model = Build.MODEL;
        LG.i(tag, "版本：" + model);
        //硬件名
        String hardware = Build.HARDWARE;
        LG.i(tag, "硬件名：" + hardware);
        //手机产品名
        String product = Build.PRODUCT;
        LG.i(tag, "手机产品名：" + product);
        //描述build的标签
        String tags = Build.TAGS;
        LG.i(tag, "描述build的标签：" + tags);
        //Builder类型
        String type = Build.TYPE;
        LG.i(tag, "Builder类型：" + type);
        //当前开发代号
        String vcodename = Build.VERSION.CODENAME;
        LG.i(tag, "当前开发代号：" + vcodename);
        //源码控制版本号
        String vincremental = Build.VERSION.INCREMENTAL;
        LG.i(tag, "源码控制版本号：" + vincremental);
        //版本字符串
        String vrelease = Build.VERSION.RELEASE;
        LG.i(tag, "版本字符串：" + vrelease);
        //版本号
        int vsdkint = Build.VERSION.SDK_INT;
        LG.i(tag, "版本号：" + vsdkint);
        //HOST值
        String host = Build.HOST;
        LG.i(tag, "HOST值：" + host);
        //User名
        String user = Build.USER;
        LG.i(tag, "User名：" + user);
        //编译时间
        long time = Build.TIME;
        LG.i(tag, "编译时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date(time)));
        //OS版本号
        String osVersion = System.getProperty("os.version");
        LG.i(tag, "OS版本号：" + osVersion);
        // OS名称
        String osName = System.getProperty("os.name");
        LG.i(tag, "OS名称：" + osName);
        //OS架构
        String osArch = System.getProperty("os.arch");
        LG.i(tag, "OS架构：" + osArch);
        //home属性
        String osUserHome = System.getProperty("os.home");
        LG.i(tag, "home属性：" + osUserHome);
        //name属性
        String osUserName = System.getProperty("os.name");
        LG.i(tag, "name属性 ：" + osUserName);
        //dir属性
        String osUserDir = System.getProperty("os.dir");
        LG.i(tag, "dir属性：" + osUserDir);
        //时区
        String osUserTimeZone = System.getProperty("os.timezone");
        LG.i(tag, "时区：" + osUserTimeZone);
//        //电话号
//        String phoneNum = teleohonyManager.getLine1Number();
//        LG.i(tag,"手机号：" + phoneNum);
//        //集成电路卡标识
//        String iccid = teleohonyManager.getSimSerialNumber();
//        LG.i(tag,"集成电路卡标识：" + iccid);
//        //手机电量
//        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
//        MyApplication1.getInstance().registerReceiver(receiver, filter);//注册BroadcastReceiver
//        //设备id
//        String mDeviceId = teleohonyManager.getDeviceId();`1
//        LG.i(tag,"设备id：" + mDeviceId);
//        getPhoneType();
//        getLocalMacAddress();
//        getLocalIpAddress();
//        getCpuInfo();
//        getTotalMemory();
//        getAvailMemory();
//        getWeithAndHeight();
        //TODO 连接wifi名字
    }

    public String getCanUseBS() {
        //先从数据库拿MAC数据
        String macSerial = MyShare.getInstance().getString("mac");
        if (checkIsMac(macSerial)) {
            LG.i(tag, "....dudao maca is 111111111111111111111111111");
            return macSerial;
        }
        //再优先使用ADB获取MAC地址
        macSerial = adbGetMac();
        if (checkIsMac(macSerial)) {
            LG.i(tag, "....dudao maca is 33333333333333333333");
            MyShare.getInstance().putString("mac", macSerial);
            return macSerial;
        }
        //还为空直接读文件夹
        macSerial = readFileGetMac();
        if (checkIsMac(macSerial)) {
            LG.i(tag, "....dudao maca is 4444444444444444444444");
            MyShare.getInstance().putString("mac", macSerial);
            return macSerial;
        }
        macSerial = getWifiMac(BaseApplication.getContext());
        if (checkIsMac(macSerial)) {
            LG.i(tag, "....dudao maca is 222222222222222222");
            MyShare.getInstance().putString("mac", macSerial);
            return macSerial;
        }

        String ANDROID_ID = Settings.System.getString(BaseApplication.getContext().getContentResolver(), Settings.System.ANDROID_ID);
        String dateDeviceId=useDateGetDeviceId();
        macSerial=useAndoridIdAndDateDeviceIdGetStr(ANDROID_ID,dateDeviceId);
        if (checkIsMac(macSerial)) {
            LG.i(tag, "....dudao maca is..444444");
            MyShare.getInstance().putString("mac", macSerial);
            return macSerial;
        }

        macSerial = Md5Util.MD5(Build.FINGERPRINT) ;
        if (checkIsMac(macSerial)) {
            LG.i(tag, "....dudao maca is 555555555555555555555555");
            MyShare.getInstance().putString("mac", macSerial);
            return macSerial;
        }
        macSerial=getImsi();
        if (checkIsMac(macSerial)) {
            LG.i(tag, "....dudao maca is..33333333333");
            MyShare.getInstance().putString("mac", macSerial);
            return macSerial;
        }
        UUID uuid = new UUID(RandomUtil.getInstance().getRandomString(10).hashCode(), RandomUtil.getInstance().getRandomString(10).hashCode());
        macSerial = uuid.toString();
        MyShare.getInstance().putString("mac", macSerial);
        LG.i(tag, "....dudao maca is 666666666666666666");
        return macSerial;
    }

    private boolean checkIsMac(String checkMac) {
        if (TextUtils.isEmpty(checkMac)) {
            return false;
        } else {
            if ("02:00:00:00:00:00".equals(checkMac) || "00:00:00:00:00:00".equals(checkMac)) {
                return false;
            } else {
                return true;
            }
        }
    }

    private String useAndoridIdAndDateDeviceIdGetStr(String a,String b){
        if(TextUtils.isEmpty(a)){
            if(TextUtils.isEmpty(b)){
                return "";
            }else {
                return Md5Util.MD5(b);
            }
        }else {
            if(TextUtils.isEmpty(b)){
                return Md5Util.MD5(a);
            }else {
                return Md5Util.MD5(a+b);
            }

        }

    }

    public  String useDateGetDeviceId()
    {
        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 位

        try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                serial = Build.getSerial();
            }
            else
            {
                serial = Build.SERIAL;
            }
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        }
        catch (Exception exception)
        {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }

        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }


    /**
     * \* 获取Wifi Mac 默认值空字符串
     * <p>
     * <p>
     * <p>
     * \* @param paramContext
     * <p>
     * \* @return
     */

    @SuppressLint("MissingPermission")
    public static String getWifiMac(Context paramContext) {
        String result = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                while (interfaces != null && interfaces.hasMoreElements()) {
                    NetworkInterface iF = interfaces.nextElement();

                    byte[] addr = iF.getHardwareAddress();
                    if (addr == null || addr.length == 0) {
                        continue;
                    }
                    //其他网卡（如rmnet0）的MAC，跳过
                    if ("wlan0".equalsIgnoreCase(iF.getName()) || "eth0".equalsIgnoreCase(iF.getName())) {
                        StringBuilder buf = new StringBuilder();
                        for (byte b : addr) {
                            buf.append(String.format("%02X:", b));
                        }
                        if (buf.length() > 0) {
                            buf.deleteCharAt(buf.length() - 1);
                        }
                        String mac = buf.toString();
                        if (mac.length() > 0) {
                            result = mac;
                            return result;
                        }
                    }
                }
            } catch (Exception e) {
            }
        } else {
            try {
                // MAC地址
                WifiManager wifi = (WifiManager) paramContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (wifi != null) {
                    WifiInfo wiinfo = wifi.getConnectionInfo();
                    result = wiinfo.getMacAddress();
                }
            } catch (Throwable e) {
            }
        }
        return result;
    }


    /**
     * 通过WiFiManager获取mac地址
     *
     * @return
     */
    private String wifiGetMac2() {
        WifiManager wm = (WifiManager) BaseApplication.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wi = wm.getConnectionInfo();
        if (wi == null || wi.getMacAddress() == null) {
            return null;
        }
        if ("02:00:00:00:00:00".equals(wi.getMacAddress().trim())) {
            return null;
        } else {
            return wi.getMacAddress().trim();
        }
    }

    private String adbGetMac() {
        String macSerial = "";
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (Exception ex) {
            macSerial = "";
        }
        return macSerial;
    }


    private String readFileGetMac() {
        try {
            return loadFileAsString("/sys/class/net/eth0/address").toUpperCase().substring(0, 17);
        } catch (Exception e) {
            return "";
        }
    }


    public PhoneSystemInfoBean getPhoneSystemInfoBean() {
        PhoneSystemInfoBean phoneSystemInfoBean = new PhoneSystemInfoBean();
//                        LG.i(tag,"系统参数------------------------------begin");
        //主板
        String board = Build.BOARD;
        phoneSystemInfoBean.setBoard(board);
//        LG.i(tag,"主板：" + board);
//        //系统定制商
        String brand = Build.BRAND;
        phoneSystemInfoBean.setBrand(brand);
//        LG.i(tag,"系统定制商：" + brand);
//        //设备信息
//        String device = Build.DEVICE;
//        LG.i(tag,"系统定制商：" + device);
//        //显示屏参数
        String display = Build.DISPLAY;
        phoneSystemInfoBean.setDisplayInfo(display);
//        LG.i(tag,"显示屏参数：" + display);
//        //唯一编号
        String fingderprint = Build.FINGERPRINT;
        phoneSystemInfoBean.setFingderprint(fingderprint);
//        LG.i(tag,"唯一编号：" + fingderprint);
//        //硬件序列号
        String serial = Build.SERIAL;
        phoneSystemInfoBean.setSerial(serial);
//        LG.i(tag,"硬件序列号：" + serial);
//        //修订版本列表
//        String id = Build.ID;
//        LG.i(tag,"修订版本列表：" + id);
//        //硬件制造商
        String manufacturer = Build.MANUFACTURER;
        phoneSystemInfoBean.setManufacturer(manufacturer);
//        LG.i(tag,"硬件制造商：" + manufacturer);
//        //版本
        String model = Build.MODEL;
        phoneSystemInfoBean.setSystemModel(model);
//        LG.i(tag,"版本：" + model);
//        //硬件名
//        String hardware = Build.HARDWARE;

//        LG.i(tag,"硬件名：" + hardware);
//        //手机产品名
        String product = Build.PRODUCT;
        phoneSystemInfoBean.setProductName(product);
//        LG.i(tag,"手机产品名：" + product);
//        //描述build的标签
//        String tags = Build.TAGS;
//        LG.i(tag,"描述build的标签：" + tags);
//        //Builder类型
//        String type = Build.TYPE;
//        LG.i(tag,"Builder类型：" + type);
//        //当前开发代号
//        String vcodename = Build.VERSION.CODENAME;
//        LG.i(tag,"当前开发代号：" + vcodename);
//        //源码控制版本号
//        String vincremental = Build.VERSION.INCREMENTAL;
//        LG.i(tag,"源码控制版本号：" + vincremental);
//        //版本字符串
//        String vrelease = Build.VERSION.RELEASE;
//        LG.i(tag,"版本字符串：" + vrelease);
//        //版本号
        int vsdkint = Build.VERSION.SDK_INT;
        phoneSystemInfoBean.setVsdkint(vsdkint);
//        LG.i(tag,"版本号：" + vsdkint);
//        //HOST值
//        String host = Build.HOST;
//        LG.i(tag,"HOST值：" + host);
//        //User名
//        String user = Build.USER;
//        LG.i(tag,"User名：" + user);
//        //编译时间
//        long time = Build.TIME;
//        LG.i(tag,"编译时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date(time)));
//        //OS版本号
//        String osVersion = System.getProperty("os.version");
//        LG.i(tag,"OS版本号：" + osVersion);
//        // OS名称
//        String osName = System.getProperty("os.name");
//        LG.i(tag,"OS名称：" + osName);
//        //OS架构
//        String osArch = System.getProperty("os.arch");
//        LG.i(tag,"OS架构：" + osArch);
//        //home属性
//        String osUserHome = System.getProperty("os.home");
//        LG.i(tag,"home属性：" + osUserHome);
//        //name属性
//        String osUserName = System.getProperty("os.name");
//        LG.i(tag,"name属性 ：" + osUserName);
//        //dir属性
//        String osUserDir = System.getProperty("os.dir");
//        LG.i(tag,"dir属性：" + osUserDir);
//        //时区
//        String osUserTimeZone = System.getProperty("os.timezone");
//        LG.i(tag,"时区：" + osUserTimeZone);
        String svBuildName = "ChannelStr";
        phoneSystemInfoBean.setSvBuildName(svBuildName);
        String mac = "mac";
        phoneSystemInfoBean.setMac(mac);

        phoneSystemInfoBean.setReadSystem(PhoneUtil.getInstance().readSystem());
        phoneSystemInfoBean.setVersionStr(PhoneUtil.getInstance().getVersionName());
        LG.i(tag, "---kankanphonedevice:" + phoneSystemInfoBean);
        return phoneSystemInfoBean;
    }


}
