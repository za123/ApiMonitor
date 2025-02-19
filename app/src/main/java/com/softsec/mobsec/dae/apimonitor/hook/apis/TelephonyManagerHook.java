package com.softsec.mobsec.dae.apimonitor.hook.apis;

import android.os.Build;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import com.softsec.mobsec.dae.apimonitor.hook.hookUtils.Hook;
import com.softsec.mobsec.dae.apimonitor.hook.hookUtils.MethodHookCallBack;
import com.softsec.mobsec.dae.apimonitor.hook.hookUtils.Reflector;

import java.lang.reflect.Method;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class TelephonyManagerHook extends Hook {

	public static final String TAG = "DAEAM_TelephonyManager";

	@Override
	public void initAllHooks(XC_LoadPackage.LoadPackageParam packageParam) {
		logger.setTag(TAG);

		Method getLine1Numbermethod = Reflector.findMethod(TelephonyManager.class, "getLine1Number");
		methodHookImpl.hookMethod(getLine1Numbermethod, new MethodHookCallBack() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				super.afterHookedMethod(param);
				String[] callingInfo = getCallingInfo();
				logger.setCallingInfo(callingInfo[0]);
				logger.addRelatedAttrs("xrefFrom", callingInfo[1]);
				logger.addRelatedAttrs("result", (String)param.getResult());
				logger.recordAPICalling(param, "获取本机号码");
			}
		});

		Method listenMethod = Reflector.findMethod(TelephonyManager.class,
				"listen", PhoneStateListener.class,int.class);
		methodHookImpl.hookMethod(listenMethod, new MethodHookCallBack() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				super.afterHookedMethod(param);
				int eventNo =  (Integer) param.args[1];
				String eventStr = "null";
				if((eventNo & PhoneStateListener.LISTEN_CELL_LOCATION) != 0){
					eventStr = "LISTEN_CELL_LOCATION";
				}
				if((eventNo & PhoneStateListener.LISTEN_SIGNAL_STRENGTHS) != 0){
					eventStr = "LISTEN_SIGNAL_STRENGTHS";
				}
				if((eventNo & PhoneStateListener.LISTEN_CALL_STATE) != 0){
					eventStr = "LISTEN_CALL_STATE";
				}
				if((eventNo & PhoneStateListener.LISTEN_DATA_CONNECTION_STATE) != 0){
					eventStr = "LISTEN_DATA_CONNECTION_STATE";
				}
				if((eventNo & PhoneStateListener.LISTEN_CELL_LOCATION) != 0){
					eventStr = "LISTEN_SERVICE_STATE";
				}
				String[] callingInfo = getCallingInfo();
				logger.setCallingInfo(callingInfo[0]);
				logger.addRelatedAttrs("xrefFrom", callingInfo[1]);
				logger.recordAPICalling(param, "监听手机信息",
						"PhoneStateListener", param.args[0].getClass().getName(),
						"ListeningEvent", eventStr);
			}
		});

		//　获取设备id
		Method telphonyManager_getDeviceIdMethod = Reflector.findMethod(TelephonyManager.class, "getDeviceId");
		methodHookImpl.hookMethod(telphonyManager_getDeviceIdMethod, new MethodHookCallBack() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				super.afterHookedMethod(param);
				String[] callingInfo = getCallingInfo();
				logger.setCallingInfo(callingInfo[0]);
				logger.addRelatedAttrs("xrefFrom", callingInfo[1]);
				logger.addRelatedAttrs("result", (String)param.getResult());
				logger.recordAPICalling(param, "获取设备ID");
			}
		});


		// 获取IMSI
		Method telphonyManager_getSubscriberIdMethod = Reflector.findMethod(TelephonyManager.class, "getSubscriberId");
		methodHookImpl.hookMethod(telphonyManager_getSubscriberIdMethod, new MethodHookCallBack() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				super.afterHookedMethod(param);
				String[] callingInfo = getCallingInfo();
				logger.setCallingInfo(callingInfo[0]);
				logger.addRelatedAttrs("xrefFrom", callingInfo[1]);
				logger.addRelatedAttrs("result", (String)param.getResult());
				logger.recordAPICalling(param, "获取Sim卡MSI");
			}
		});

		// 获取手机位置
		Method telphonyManager_getCellLocationMethod = Reflector.findMethod(TelephonyManager.class, "getCellLocation");
		methodHookImpl.hookMethod(telphonyManager_getCellLocationMethod, new MethodHookCallBack() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				super.afterHookedMethod(param);
				String[] callingInfo = getCallingInfo();
				CellLocation location = (CellLocation)param.getResult();
				logger.setCallingInfo(callingInfo[0]);
				logger.addRelatedAttrs("xrefFrom", callingInfo[1]);
				logger.recordAPICalling(param, "获取基站位置");
			}
		});

		// 获取系统ID
		Method cdmaLocation_getSystemId = Reflector.findMethod(CdmaCellLocation.class, "getSystemId");
		methodHookImpl.hookMethod(cdmaLocation_getSystemId, new MethodHookCallBack() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				super.afterHookedMethod(param);
				String[] callingInfo = getCallingInfo();
				logger.setCallingInfo(callingInfo[0]);
				logger.addRelatedAttrs("result", String.valueOf((int)param.getResult()));
				logger.addRelatedAttrs("xrefFrom", callingInfo[1]);
				logger.recordAPICalling(param, "获取系统ID");
			}
		});


		// 获取网络ID
		Method cdmaLocation_getNetworkId = Reflector.findMethod(CdmaCellLocation.class, "getNetworkId");
		methodHookImpl.hookMethod(cdmaLocation_getNetworkId, new MethodHookCallBack() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				super.afterHookedMethod(param);
				String[] callingInfo = getCallingInfo();
				logger.setCallingInfo(callingInfo[0]);
				logger.addRelatedAttrs("result", String.valueOf((int)param.getResult()));
				logger.addRelatedAttrs("xrefFrom", callingInfo[1]);
				logger.recordAPICalling(param, "获取网络服务种类");
			}
		});

		// 获取基站ID
		Method cdmaLocation_getBaseStationId = Reflector.findMethod(CdmaCellLocation.class, "getBaseStationId");
		methodHookImpl.hookMethod(cdmaLocation_getBaseStationId, new MethodHookCallBack() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				super.afterHookedMethod(param);
				String[] callingInfo = getCallingInfo();
				logger.setCallingInfo(callingInfo[0]);
				logger.addRelatedAttrs("result", String.valueOf((int)param.getResult()));
				logger.addRelatedAttrs("xrefFrom", callingInfo[1]);
				logger.recordAPICalling(param, "获取基站ID");
			}
		});

		// 获得cell id
		Method gsmLocation_getCidMethod = Reflector.findMethod(GsmCellLocation.class, "getCid");
		methodHookImpl.hookMethod(gsmLocation_getCidMethod, new MethodHookCallBack() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				super.afterHookedMethod(param);
				String[] callingInfo = getCallingInfo();
				logger.setCallingInfo(callingInfo[0]);
				logger.addRelatedAttrs("result", String.valueOf((int)param.getResult()));
				logger.addRelatedAttrs("xrefFrom", callingInfo[1]);
				logger.recordAPICalling(param, "获取GSM原件ID");
			}
		});


		//获得gsm地区代号
		Method gsmLocation_getLacMethod = Reflector.findMethod(GsmCellLocation.class, "getLac");
		methodHookImpl.hookMethod(gsmLocation_getLacMethod, new MethodHookCallBack() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				super.afterHookedMethod(param);
				String[] callingInfo = getCallingInfo();
				logger.setCallingInfo(callingInfo[0]);
				logger.addRelatedAttrs("result", String.valueOf((int)param.getResult()));
				logger.addRelatedAttrs("xrefFrom", callingInfo[1]);
				logger.recordAPICalling(param, "获取地理位置");
			}
		});

		// 获得运营商信息
		Method getSimCarrierIdMethod = Reflector.findMethod(TelephonyManager.class, "getSimCarrierId");
		if(getSimCarrierIdMethod != null) {
			methodHookImpl.hookMethod(getSimCarrierIdMethod, new MethodHookCallBack() {
				@Override
				protected void afterHookedMethod(MethodHookParam param) throws Throwable {
					super.afterHookedMethod(param);
					String[] callingInfo = getCallingInfo();
					logger.setCallingInfo(callingInfo[0]);
					logger.addRelatedAttrs("result", String.valueOf((int)param.getResult()));
					logger.addRelatedAttrs("xrefFrom", callingInfo[1]);
					logger.recordAPICalling(param, "获取运营商");
				}
			});
		}

		// 获得运营商信息
		if(Build.VERSION.SDK_INT >= 28) {
			Method getSimCarrierIdNameMethod = Reflector.findMethod(TelephonyManager.class, "getSimCarrierIdName");
			if(getSimCarrierIdNameMethod != null) {
				methodHookImpl.hookMethod(getSimCarrierIdNameMethod, new MethodHookCallBack() {
					@Override
					protected void afterHookedMethod(MethodHookParam param) throws Throwable {
						super.afterHookedMethod(param);
						String[] callingInfo = getCallingInfo();
						logger.setCallingInfo(callingInfo[0]);
						logger.addRelatedAttrs("xrefFrom", callingInfo[1]);
						logger.recordAPICalling(param, "获取运营商");
					}
				});
			}
		}

	}
}
