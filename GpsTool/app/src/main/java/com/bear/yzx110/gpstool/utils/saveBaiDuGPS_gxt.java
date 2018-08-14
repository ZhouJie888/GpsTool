package com.bear.yzx110.gpstool.utils;

import android.app.Activity;
import android.os.Environment;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static android.R.attr.x;

/**
 * Created by ZHOUJIEDE on 2018/7/19.
 */

public class saveBaiDuGPS_gxt {
    private FileOutputStream fos;
    private Activity activity;

    public saveBaiDuGPS_gxt(){

    }
    public saveBaiDuGPS_gxt(Activity activity){
        if(activity != null)
            this.activity = activity;
        else
            System.err.println("保存数据类Activity空值错误！！！！！！！！！！！！");
    }
    // 判断SDcard状态
    private boolean getSDcard(){
        if(!Environment.getExternalStorageState().equals( Environment.MEDIA_MOUNTED)){
            //sdcard状态是没有挂载的情况
            Toast.makeText(activity, "sdcard不存在或接触不良，请重新插入", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    // 判断SDcard剩余空间
    public boolean getSDcardEMS(){
        //判断sdcard存储空间是否满足文件的存储
        File sdcard_filedir = Environment.getExternalStorageDirectory();//得到sdcard的目录作为一个文件对象
        long usableSpace = sdcard_filedir.getUsableSpace();//获取文件目录对象剩余空间
        long totalSpace = sdcard_filedir.getTotalSpace();
        //将一个long类型的文件大小格式化成用户可以看懂的M，G字符串
        String usableSpace_str = Formatter.formatFileSize(activity, usableSpace);
        String totalSpace_str = Formatter.formatFileSize(activity, totalSpace);
        if(usableSpace < 1024 * 1024 * 200){//判断剩余空间是否小于200M
            Toast.makeText(activity, "sdcard剩余空间不足,无法满足下载；剩余空间为："+usableSpace_str, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 开始保存
     * @param filename 文件名
     * @param content 保存内容
     */
    public void saveData_gxt(String filename, String file_gxt, String content){
        if(getSDcard() && getSDcardEMS()){
            File file=new File(Environment.getExternalStorageDirectory(), filename);
            System.out.println("获取SD卡的路径为1：" + Environment.getExternalStorageDirectory());
            System.out.println("获取SD卡的路径为2：" + file.toString());
            try {
                if(!file.exists()){
//                    boolean newFile = file.createNewFile();
                    System.out.println("文件为空，开始创建文件");
                    boolean mkdirs = file.mkdirs();

                    if (!mkdirs){
                        System.out.println("创建文件夹失败");
                    } else {
                        System.out.println("创建文件夹成功");
                        file = new File(file, file_gxt);
                        System.out.println("最终文件夹为：" + file.toString());
                        boolean newFile = file.createNewFile();
                        if (newFile)
                            System.out.println("创建文件成功");
                        else
                            System.out.println("创建文件失败");
                    }
                } else {
                    file = new File(file, file_gxt);
                    System.out.println("最终文件夹为：" + file.toString());
                    file.createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            OutputStream out= null;

            try {
                out = new FileOutputStream(file, true);
                out.write(content.getBytes());
            } catch (Exception e) {
                Log.i("保存文件类", "文件保存失败");
                e.printStackTrace();
            } finally {
                if (out != null){
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}
