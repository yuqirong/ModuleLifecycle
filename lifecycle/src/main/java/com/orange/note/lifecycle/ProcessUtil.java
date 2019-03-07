package com.orange.note.lifecycle;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import android.text.TextUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author maomao
 * @date 2018/11/5
 */
class ProcessUtil {

    private ProcessUtil() {
        //no instance
    }

    /**
     * 当前进程是否是主进程
     *
     * @param context
     * @return
     */
    public static boolean isMainProcess(Context context) {
        int myPid = Process.myPid();
        String procName = readProcName(context, myPid);
        if (TextUtils.isEmpty(procName)) {
            procName = readProcName(myPid);
        }
        return context.getPackageName().equals(procName);
    }


    /**
     * 从android runtime中读出进程名称
     *
     * @param context
     * @param myPid
     * @return
     */
    public static String readProcName(Context context, int myPid) {
        ActivityManager.RunningAppProcessInfo myProcess = null;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            List<ActivityManager.RunningAppProcessInfo> list = activityManager.getRunningAppProcesses();
            if (list != null) {
                try {
                    for (ActivityManager.RunningAppProcessInfo info : list) {
                        if (info.pid == myPid) {
                            myProcess = info;
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (myProcess != null) {
                    return myProcess.processName;
                }
            }
        }
        return null;
    }

    /**
     * 从内核中读取进程名称
     *
     * @param myPid
     * @return
     */
    public static String readProcName(int myPid) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("/proc/" + myPid + "/cmdline");
            byte[] buffer = new byte[128];
            int len = fileInputStream.read(buffer);
            if (len <= 0) {
                return null;
            }
            int index = 0;
            for (; index < buffer.length; index++) {
                if (buffer[index] > 128 || buffer[index] <= 0) {
                    break;
                }
            }
            return new String(buffer, 0, index);
        } catch (Exception ignore) {
            ignore.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException ignore) {
            }
        }
        return null;
    }
}

