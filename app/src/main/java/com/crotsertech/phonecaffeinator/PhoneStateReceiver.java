package com.crotsertech.phonecaffeinator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.telephony.TelephonyManager;

public class PhoneStateReceiver extends BroadcastReceiver {
    private static PowerManager.WakeLock wakeLock;

    @Override
    public void onReceive(Context context, Intent intent) {
        String stateStr = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if (stateStr == null) return;

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(stateStr)) {
            if (wakeLock == null || !wakeLock.isHeld()) {
                wakeLock = pm.newWakeLock(
                        PowerManager.PARTIAL_WAKE_LOCK,
                        "PhoneCaffeinator:WakeLock"
                );
                wakeLock.acquire();
            }
        } else if (TelephonyManager.EXTRA_STATE_IDLE.equals(stateStr)) {
            if (wakeLock != null && wakeLock.isHeld()) {
                wakeLock.release();
                wakeLock = null;
            }
        }
    }
}