package com.google.firebase.iid;

import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Parcelable;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Base64;
import android.util.Log;
import com.google.android.gms.common.util.PlatformVersion;
import javax.annotation.concurrent.GuardedBy;

public final class FirebaseInstanceIdReceiver extends WakefulBroadcastReceiver {
    private static boolean zzbb = false;
    @GuardedBy("FirebaseInstanceIdReceiver.class")
    private static zzh zzbc;
    @GuardedBy("FirebaseInstanceIdReceiver.class")
    private static zzh zzbd;

    private static synchronized zzh zza(Context context, String str) {
        zzh zzh;
        synchronized (FirebaseInstanceIdReceiver.class) {
            if ("com.google.firebase.MESSAGING_EVENT".equals(str)) {
                if (zzbd == null) {
                    zzbd = new zzh(context, str);
                }
                zzh = zzbd;
            } else {
                if (zzbc == null) {
                    zzbc = new zzh(context, str);
                }
                zzh = zzbc;
            }
        }
        return zzh;
    }

    private final void zza(Context context, Intent intent, String str) {
        String str2 = null;
        int i = 0;
        int i2 = -1;
        intent.setComponent(null);
        intent.setPackage(context.getPackageName());
        if (VERSION.SDK_INT <= 18) {
            intent.removeCategory(context.getPackageName());
        }
        String stringExtra = intent.getStringExtra("gcm.rawData64");
        if (stringExtra != null) {
            intent.putExtra("rawData", Base64.decode(stringExtra, 0));
            intent.removeExtra("gcm.rawData64");
        }
        if ("google.com/iid".equals(intent.getStringExtra("from")) || "com.google.firebase.INSTANCE_ID_EVENT".equals(str)) {
            str2 = "com.google.firebase.INSTANCE_ID_EVENT";
        } else if ("com.google.android.c2dm.intent.RECEIVE".equals(str) || "com.google.firebase.MESSAGING_EVENT".equals(str)) {
            str2 = "com.google.firebase.MESSAGING_EVENT";
        } else {
            Log.d("FirebaseInstanceId", "Unexpected intent");
        }
        if (str2 != null) {
            if (PlatformVersion.isAtLeastO() && context.getApplicationInfo().targetSdkVersion >= 26) {
                i = 1;
            }
            if (i != 0) {
                if (isOrderedBroadcast()) {
                    setResultCode(-1);
                }
                zza(context, str2).zza(intent, goAsync());
            } else {
                i2 = zzap.zzac().zza(context, str2, intent);
            }
        }
        if (isOrderedBroadcast()) {
            setResultCode(i2);
        }
    }

    public final void onReceive(Context context, Intent intent) {
        if (intent != null) {
            Parcelable parcelableExtra = intent.getParcelableExtra("wrapped_intent");
            if (parcelableExtra instanceof Intent) {
                zza(context, (Intent) parcelableExtra, intent.getAction());
            } else {
                zza(context, intent, intent.getAction());
            }
        }
    }
}
