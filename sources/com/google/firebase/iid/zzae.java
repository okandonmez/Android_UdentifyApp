package com.google.firebase.iid;

import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.tasks.TaskCompletionSource;

abstract class zzae<T> {
    final int what;
    final int zzby;
    final TaskCompletionSource<T> zzbz = new TaskCompletionSource();
    final Bundle zzca;

    zzae(int i, int i2, Bundle bundle) {
        this.zzby = i;
        this.what = i2;
        this.zzca = bundle;
    }

    final void finish(T t) {
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            String valueOf = String.valueOf(this);
            String valueOf2 = String.valueOf(t);
            Log.d("MessengerIpcClient", new StringBuilder((String.valueOf(valueOf).length() + 16) + String.valueOf(valueOf2).length()).append("Finishing ").append(valueOf).append(" with ").append(valueOf2).toString());
        }
        this.zzbz.setResult(t);
    }

    public String toString() {
        int i = this.what;
        int i2 = this.zzby;
        return "Request { what=" + i + " id=" + i2 + " oneWay=" + zzv() + "}";
    }

    final void zza(zzaf zzaf) {
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            String valueOf = String.valueOf(this);
            String valueOf2 = String.valueOf(zzaf);
            Log.d("MessengerIpcClient", new StringBuilder((String.valueOf(valueOf).length() + 14) + String.valueOf(valueOf2).length()).append("Failing ").append(valueOf).append(" with ").append(valueOf2).toString());
        }
        this.zzbz.setException(zzaf);
    }

    abstract void zzb(Bundle bundle);

    abstract boolean zzv();
}
