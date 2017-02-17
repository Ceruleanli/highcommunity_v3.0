package cn.hi028.android.highcommunity.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import net.duohuo.dhroid.activity.ActivityTack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.hi028.android.highcommunity.view.ECAlertDialog;

/**
 * Created by Lee_yting on 2016/11/17 0017.
 * 说明：UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.暂时没有将错误日志发送到服务器
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    static final String Tag = "CrashHandler--->";
    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mExceptionHandler;
    //CrashHandler实例
    private static CrashHandler INSTANCE = new CrashHandler();
    private Context mContext;
    // 用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();
    // 用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    //保证只有一个CrashHandler实例
    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        Log.e(Tag, "获取crash 单例");
        return INSTANCE;
    }
    public void init(Context context) {
        mContext = context;
        //获取系统默认的UncaughtException处理器
        mExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }


    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.e(Tag, "uncaughtException ");
        if (!handleException(ex) && mExceptionHandler != null) {
            Log.e(Tag, "!handleException(ex) && mExceptionHandler != null");
            //如果用户没有处理则让系统默认的异常处理器来处理
            mExceptionHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 退出程序
            ActivityTack.getInstanse().exit(mContext.getApplicationContext());
        }
    }

    ECAlertDialog dialog2;
    Throwable throwable;
    private boolean handleException(Throwable throwable) {
        Log.e(Tag, "handleException ");
        this.throwable=throwable;
        if (throwable == null) {
            return false;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Log.e(Tag, "Looper ");
                Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出,请重启再试", Toast.LENGTH_SHORT).show();
                Log.e(Tag, "Looper  2 ");
                Looper.loop();

            }
        }) {
        }.start();
        // 收集设备参数信息
        Log.e(Tag, "收集设备参数信息 ");
        collectDeviceInfo(mContext);
        // 保存日志文件
        Log.e(Tag, "保存日志文件");
        saveCrashInfo2File(throwable);
        return true;
    }
    /****
     * 重启app
     */
    public void restartApp() {


    }

    /****
     * crash弹窗
     */
    public void showCrashDialog() {


    }

    class GetDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                Log.e(Tag, "doInBackground  1s");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e(Tag, "onPostExecute ActivityTack  exit");
            ActivityTack.getInstanse().exit(mContext.getApplicationContext());
        }
    }
    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d(Tag, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
            }
        }
    }
    /**
     * 保存错误信息到文件中
     * @param ex
     * @return 返回文件名称,便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex) {
        if (ex==null){
            return null;}
        String device = "null";
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
            if (key.equals("DEVICE")) {
                device = value;
            }
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();

        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        Log.e(Tag,"sb--->"+sb);
        PostToWeb(device, sb);
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = "crach-" + time + "-" + timestamp + ".log";
            String readMeTxt="readme.txt";

            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/" + AppDir +"/"+ "/crach/";

                File dir = new File(path);
                File readMefile = new File(path + readMeTxt);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                if( !readMefile.exists()) {
                    Log.d("TestFile", "Create the file:" + fileName);
                    readMefile.createNewFile();
                    FileOutputStream stream = new FileOutputStream(readMefile);
                    String s = "该目录为记录奔溃日志目录。";
                    byte[] buf = s.getBytes();
                    stream.write(buf);
                    stream.close();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            Log.e(Tag,"RETURN fileName"+fileName);

            return fileName;
        } catch (Exception e) {
            Log.e(Tag, "an error occured while writing file...", e);
        }
        Log.e(Tag,"RETURN null");

        return null;
    }
    /**保存升级APK的目录以及奔溃日志保存目录**/
    public static final String AppDir = "hishequ";
    public void PostToWeb(String device, StringBuffer sb) {
    }


}
