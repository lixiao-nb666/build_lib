package com.newbee.bulid_lib.util;

import android.content.Context;
import android.media.AudioManager;



/**
 * Created by Administrator on 2018/7/6 0006.
 */

public class AudioUtil {

    private AudioManager mAudioManager;
    private static AudioUtil mInstance;

    private AudioUtil() {
    }

    public synchronized static AudioUtil getInstance() {
        if (mInstance == null) {
            mInstance = new AudioUtil();
        }
        return mInstance;
    }

    public void init(Context context){
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }



    //AudioManager.STREAM_VOICE_CALL
    private int audioType=AudioManager.STREAM_MUSIC;


    public void setAudioType(int setType){
        this.audioType=setType;
    }

    //获取多媒体最大音量
    public int getMaxVolume() {
        try {
            return mAudioManager.getStreamMaxVolume(audioType);
        }catch (Exception e){
            return -1;
        }

    }

    //获取多媒体音量
    public int getVolume() {
        try {
            return mAudioManager.getStreamVolume(audioType);
        }catch (Exception e){
            return -1;
        }
    }




    /**
     * 设置多媒体音量
     * 这里我只写了多媒体和通话的音量调节，其他的只是参数不同，大家可仿照
     */
    public void setVolume(int volume) {
        try {

            mAudioManager.setStreamVolume(audioType, //音量类型
                    volume,
                    AudioManager.FLAG_PLAY_SOUND
                            | AudioManager.FLAG_SHOW_UI);
        } catch (Exception e) {
        }
    }


    /**
     * 设置多媒体音量
     * 这里我只写了多媒体和通话的音量调节，其他的只是参数不同，大家可仿照
     */
    public void setVolumeByPercentage(int volume) {
        try {
            int needVolume = getMaxVolume();
            if (volume <= 0) {
                needVolume = 0;
            } else if (volume > 0 && volume < 100) {

                needVolume = needVolume * volume / 100;
            }
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, //音量类型
                    needVolume,
                    AudioManager.FLAG_PLAY_SOUND
                            | AudioManager.FLAG_SHOW_UI);
        } catch (Exception e) {
        }
    }




    // 关闭/打开扬声器播放
    public void setSpeakerStatus(boolean on) {
        try {
            if (on) { //扬声器
                mAudioManager.setSpeakerphoneOn(true);
                mAudioManager.setMode(AudioManager.MODE_NORMAL);
            } else {
                // 设置最大音量
                int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
                mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, max, AudioManager.STREAM_VOICE_CALL);
                // 设置成听筒模式
                mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                mAudioManager.setSpeakerphoneOn(false);// 关闭扬声器
                mAudioManager.setRouting(AudioManager.MODE_NORMAL, AudioManager.ROUTE_EARPIECE, AudioManager.ROUTE_ALL);
            }
        }catch (Exception e){

        }

    }




}
