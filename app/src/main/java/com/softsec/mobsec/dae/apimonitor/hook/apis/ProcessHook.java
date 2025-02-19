package com.softsec.mobsec.dae.apimonitor.hook.apis;

import com.softsec.mobsec.dae.apimonitor.hook.hookUtils.Hook;
import com.softsec.mobsec.dae.apimonitor.hook.hookUtils.MethodHookCallBack;
import com.softsec.mobsec.dae.apimonitor.hook.hookUtils.Reflector;

import java.lang.reflect.Method;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class ProcessHook extends Hook {

    public static final String TAG = "DAEAM_Process";

    @Override
    public void initAllHooks(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        logger.setTag(TAG);

        try {
            Method startMethod = Reflector.findMethod(android.os.Process.class, "start",
                    String.class, String.class, int.class, int.class, int[].class, int.class, int.class, int.class,
                    String.class, String.class, String.class, String.class, String[].class);
            methodHookImpl.hookMethod(startMethod, new MethodHookCallBack() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    int uid = (Integer) param.args[2];
                    if (uid == 10066) {
                        int debugFlags = (Integer) param.args[5];
                        param.args[5] = (debugFlags | 0x1);
                        logger.recordAPICalling(param, "开启新进程", "debugFlags", String.valueOf(param.args[5]));
                    }
                }
            });
        }catch (Error e){
            XposedBridge.log("ERROR_PROCESS: " + e.getMessage());
        }
    }
}
