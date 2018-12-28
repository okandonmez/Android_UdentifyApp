package com.google.firebase.iid;

import android.support.annotation.GuardedBy;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.io.IOException;
import java.util.Map;

final class zzau {
    @GuardedBy("itself")
    private final zzaq zzaj;
    @GuardedBy("this")
    private int zzdf = 0;
    @GuardedBy("this")
    private final Map<Integer, TaskCompletionSource<Void>> zzdg = new ArrayMap();

    zzau(zzaq zzaq) {
        this.zzaj = zzaq;
    }

    @WorkerThread
    private static boolean zza(FirebaseInstanceId firebaseInstanceId, String str) {
        String str2;
        String valueOf;
        String[] split = str.split("!");
        if (split.length != 2) {
            return true;
        }
        String str3 = split[0];
        String str4 = split[1];
        int i = -1;
        try {
            switch (str3.hashCode()) {
                case 83:
                    if (str3.equals("S")) {
                        i = 0;
                        break;
                    }
                    break;
                case 85:
                    if (str3.equals("U")) {
                        boolean z = true;
                        break;
                    }
                    break;
            }
            switch (i) {
                case 0:
                    firebaseInstanceId.zzb(str4);
                    if (!FirebaseInstanceId.zzi()) {
                        return true;
                    }
                    Log.d("FirebaseInstanceId", "subscribe operation succeeded");
                    return true;
                case 1:
                    firebaseInstanceId.zzc(str4);
                    if (!FirebaseInstanceId.zzi()) {
                        return true;
                    }
                    Log.d("FirebaseInstanceId", "unsubscribe operation succeeded");
                    return true;
                default:
                    return true;
            }
        } catch (IOException e) {
            str2 = "FirebaseInstanceId";
            str3 = "Topic sync failed: ";
            valueOf = String.valueOf(e.getMessage());
            Log.e(str2, valueOf.length() == 0 ? new String(str3) : str3.concat(valueOf));
            return false;
        }
        str2 = "FirebaseInstanceId";
        str3 = "Topic sync failed: ";
        valueOf = String.valueOf(e.getMessage());
        if (valueOf.length() == 0) {
        }
        Log.e(str2, valueOf.length() == 0 ? new String(str3) : str3.concat(valueOf));
        return false;
    }

    @Nullable
    @GuardedBy("this")
    private final String zzak() {
        synchronized (this.zzaj) {
            Object zzae = this.zzaj.zzae();
        }
        if (!TextUtils.isEmpty(zzae)) {
            String[] split = zzae.split(",");
            if (split.length > 1 && !TextUtils.isEmpty(split[1])) {
                return split[1];
            }
        }
        return null;
    }

    private final synchronized boolean zzk(String str) {
        boolean z;
        synchronized (this.zzaj) {
            String zzae = this.zzaj.zzae();
            String valueOf = String.valueOf(",");
            String valueOf2 = String.valueOf(str);
            if (zzae.startsWith(valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf))) {
                valueOf = String.valueOf(",");
                valueOf2 = String.valueOf(str);
                this.zzaj.zzf(zzae.substring((valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf)).length()));
                z = true;
            } else {
                z = false;
            }
        }
        return z;
    }

    final synchronized Task<Void> zza(String str) {
        TaskCompletionSource taskCompletionSource;
        Object zzae;
        synchronized (this.zzaj) {
            zzae = this.zzaj.zzae();
            this.zzaj.zzf(new StringBuilder((String.valueOf(zzae).length() + 1) + String.valueOf(str).length()).append(zzae).append(",").append(str).toString());
        }
        taskCompletionSource = new TaskCompletionSource();
        this.zzdg.put(Integer.valueOf((TextUtils.isEmpty(zzae) ? 0 : zzae.split(",").length - 1) + this.zzdf), taskCompletionSource);
        return taskCompletionSource.getTask();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @android.support.annotation.WorkerThread
    final boolean zza(com.google.firebase.iid.FirebaseInstanceId r4) {
        /*
        r3 = this;
    L_0x0000:
        monitor-enter(r3);
        r1 = r3.zzak();	 Catch:{ all -> 0x001c }
        if (r1 != 0) goto L_0x0013;
    L_0x0007:
        r0 = "FirebaseInstanceId";
        r1 = "topic sync succeeded";
        android.util.Log.d(r0, r1);	 Catch:{ all -> 0x001c }
        r0 = 1;
        monitor-exit(r3);	 Catch:{ all -> 0x001c }
    L_0x0012:
        return r0;
    L_0x0013:
        monitor-exit(r3);	 Catch:{ all -> 0x001c }
        r0 = zza(r4, r1);
        if (r0 != 0) goto L_0x001f;
    L_0x001a:
        r0 = 0;
        goto L_0x0012;
    L_0x001c:
        r0 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x001c }
        throw r0;
    L_0x001f:
        monitor-enter(r3);
        r0 = r3.zzdg;	 Catch:{ all -> 0x003f }
        r2 = r3.zzdf;	 Catch:{ all -> 0x003f }
        r2 = java.lang.Integer.valueOf(r2);	 Catch:{ all -> 0x003f }
        r0 = r0.remove(r2);	 Catch:{ all -> 0x003f }
        r0 = (com.google.android.gms.tasks.TaskCompletionSource) r0;	 Catch:{ all -> 0x003f }
        r3.zzk(r1);	 Catch:{ all -> 0x003f }
        r1 = r3.zzdf;	 Catch:{ all -> 0x003f }
        r1 = r1 + 1;
        r3.zzdf = r1;	 Catch:{ all -> 0x003f }
        monitor-exit(r3);	 Catch:{ all -> 0x003f }
        if (r0 == 0) goto L_0x0000;
    L_0x003a:
        r1 = 0;
        r0.setResult(r1);
        goto L_0x0000;
    L_0x003f:
        r0 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x003f }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.iid.zzau.zza(com.google.firebase.iid.FirebaseInstanceId):boolean");
    }

    final synchronized boolean zzaj() {
        return zzak() != null;
    }
}
