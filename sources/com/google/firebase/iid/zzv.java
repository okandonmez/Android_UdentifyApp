package com.google.firebase.iid;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import com.google.android.gms.tasks.Task;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.annotation.concurrent.GuardedBy;

public final class zzv {
    @GuardedBy("MessengerIpcClient.class")
    private static zzv zzbm;
    private final ScheduledExecutorService zzbn;
    @GuardedBy("this")
    private zzx zzbo = new zzx();
    @GuardedBy("this")
    private int zzbp = 1;
    private final Context zzz;

    @VisibleForTesting
    private zzv(Context context, ScheduledExecutorService scheduledExecutorService) {
        this.zzbn = scheduledExecutorService;
        this.zzz = context.getApplicationContext();
    }

    private final synchronized <T> Task<T> zza(zzae<T> zzae) {
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            String valueOf = String.valueOf(zzae);
            Log.d("MessengerIpcClient", new StringBuilder(String.valueOf(valueOf).length() + 9).append("Queueing ").append(valueOf).toString());
        }
        if (!this.zzbo.zzb(zzae)) {
            this.zzbo = new zzx();
            this.zzbo.zzb(zzae);
        }
        return zzae.zzbz.getTask();
    }

    public static synchronized zzv zzc(Context context) {
        zzv zzv;
        synchronized (zzv.class) {
            if (zzbm == null) {
                zzbm = new zzv(context, Executors.newSingleThreadScheduledExecutor());
            }
            zzv = zzbm;
        }
        return zzv;
    }

    private final synchronized int zzr() {
        int i;
        i = this.zzbp;
        this.zzbp = i + 1;
        return i;
    }

    public final Task<Void> zza(int i, Bundle bundle) {
        return zza(new zzad(zzr(), 2, bundle));
    }

    public final Task<Bundle> zzb(int i, Bundle bundle) {
        return zza(new zzag(zzr(), 1, bundle));
    }
}
