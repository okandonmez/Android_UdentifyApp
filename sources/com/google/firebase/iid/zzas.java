package com.google.firebase.iid;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import com.google.android.gms.common.util.VisibleForTesting;
import java.io.IOException;

final class zzas implements Runnable {
    private final zzah zzao;
    private final zzau zzar;
    private final long zzdb;
    private final WakeLock zzdc = ((PowerManager) getContext().getSystemService("power")).newWakeLock(1, "fiid-sync");
    private final FirebaseInstanceId zzdd;

    @VisibleForTesting
    zzas(FirebaseInstanceId firebaseInstanceId, zzah zzah, zzau zzau, long j) {
        this.zzdd = firebaseInstanceId;
        this.zzao = zzah;
        this.zzar = zzau;
        this.zzdb = j;
        this.zzdc.setReferenceCounted(false);
    }

    @VisibleForTesting
    private final boolean zzag() {
        String zzh;
        Exception e;
        String str;
        String valueOf;
        zzar zzg = this.zzdd.zzg();
        if (zzg != null && !zzg.zzj(this.zzao.zzx())) {
            return true;
        }
        try {
            zzh = this.zzdd.zzh();
            if (zzh == null) {
                Log.e("FirebaseInstanceId", "Token retrieval failed: null");
                return false;
            }
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                Log.d("FirebaseInstanceId", "Token successfully retrieved");
            }
            if (zzg != null && (zzg == null || zzh.equals(zzg.zzcz))) {
                return true;
            }
            Context context = getContext();
            Parcelable intent = new Intent("com.google.firebase.iid.TOKEN_REFRESH");
            Intent intent2 = new Intent("com.google.firebase.INSTANCE_ID_EVENT");
            intent2.setClass(context, FirebaseInstanceIdReceiver.class);
            intent2.putExtra("wrapped_intent", intent);
            context.sendBroadcast(intent2);
            return true;
        } catch (IOException e2) {
            e = e2;
            str = "FirebaseInstanceId";
            zzh = "Token retrieval failed: ";
            valueOf = String.valueOf(e.getMessage());
            Log.e(str, valueOf.length() == 0 ? zzh.concat(valueOf) : new String(zzh));
            return false;
        } catch (SecurityException e3) {
            e = e3;
            str = "FirebaseInstanceId";
            zzh = "Token retrieval failed: ";
            valueOf = String.valueOf(e.getMessage());
            if (valueOf.length() == 0) {
            }
            Log.e(str, valueOf.length() == 0 ? zzh.concat(valueOf) : new String(zzh));
            return false;
        }
    }

    final Context getContext() {
        return this.zzdd.zze().getApplicationContext();
    }

    public final void run() {
        Object obj = 1;
        this.zzdc.acquire();
        try {
            this.zzdd.zza(true);
            if (this.zzao.zzw() == 0) {
                obj = null;
            }
            if (obj == null) {
                this.zzdd.zza(false);
            } else if (zzah()) {
                if (zzag() && this.zzar.zza(this.zzdd)) {
                    this.zzdd.zza(false);
                } else {
                    this.zzdd.zza(this.zzdb);
                }
                this.zzdc.release();
            } else {
                new zzat(this).zzai();
                this.zzdc.release();
            }
        } finally {
            this.zzdc.release();
        }
    }

    final boolean zzah() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService("connectivity");
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
