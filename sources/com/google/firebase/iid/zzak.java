package com.google.firebase.iid;

import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import com.google.android.gms.tasks.Task;
import java.util.Map;
import javax.annotation.concurrent.GuardedBy;

final class zzak {
    @GuardedBy("this")
    private final Map<Pair<String, String>, Task<String>> zzch = new ArrayMap();

    zzak() {
    }

    final /* synthetic */ Task zza(Pair pair, Task task) throws Exception {
        synchronized (this) {
            this.zzch.remove(pair);
        }
        return task;
    }

    final synchronized Task<String> zza(String str, String str2, zzam zzam) {
        Task<String> task;
        Pair pair = new Pair(str, str2);
        task = (Task) this.zzch.get(pair);
        if (task == null) {
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                String valueOf = String.valueOf(pair);
                Log.d("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf).length() + 24).append("Making new request for: ").append(valueOf).toString());
            }
            task = zzam.zzo().continueWithTask(FirebaseInstanceId.zzah, new zzal(this, pair));
            this.zzch.put(pair, task);
        } else if (Log.isLoggable("FirebaseInstanceId", 3)) {
            String valueOf2 = String.valueOf(pair);
            Log.d("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf2).length() + 29).append("Joining ongoing request for: ").append(valueOf2).toString());
        }
        return task;
    }
}
