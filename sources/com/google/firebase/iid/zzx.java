package com.google.firebase.iid;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.stats.ConnectionTracker;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import javax.annotation.concurrent.GuardedBy;

final class zzx implements ServiceConnection {
    @GuardedBy("this")
    int state;
    final Messenger zzbq;
    zzac zzbr;
    @GuardedBy("this")
    final Queue<zzae<?>> zzbs;
    @GuardedBy("this")
    final SparseArray<zzae<?>> zzbt;
    final /* synthetic */ zzv zzbu;

    private zzx(zzv zzv) {
        this.zzbu = zzv;
        this.state = 0;
        this.zzbq = new Messenger(new Handler(Looper.getMainLooper(), new zzy(this)));
        this.zzbs = new ArrayDeque();
        this.zzbt = new SparseArray();
    }

    private final void zzs() {
        this.zzbu.zzbn.execute(new zzaa(this));
    }

    public final synchronized void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (Log.isLoggable("MessengerIpcClient", 2)) {
            Log.v("MessengerIpcClient", "Service connected");
        }
        if (iBinder == null) {
            zza(0, "Null service connection");
        } else {
            try {
                this.zzbr = new zzac(iBinder);
                this.state = 2;
                zzs();
            } catch (RemoteException e) {
                zza(0, e.getMessage());
            }
        }
    }

    public final synchronized void onServiceDisconnected(ComponentName componentName) {
        if (Log.isLoggable("MessengerIpcClient", 2)) {
            Log.v("MessengerIpcClient", "Service disconnected");
        }
        zza(2, "Service disconnected");
    }

    final synchronized void zza(int i) {
        zzae zzae = (zzae) this.zzbt.get(i);
        if (zzae != null) {
            Log.w("MessengerIpcClient", "Timing out request: " + i);
            this.zzbt.remove(i);
            zzae.zza(new zzaf(3, "Timed out waiting for response"));
            zzt();
        }
    }

    final synchronized void zza(int i, String str) {
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            String str2 = "MessengerIpcClient";
            String str3 = "Disconnected: ";
            String valueOf = String.valueOf(str);
            Log.d(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3));
        }
        switch (this.state) {
            case 0:
                throw new IllegalStateException();
            case 1:
            case 2:
                if (Log.isLoggable("MessengerIpcClient", 2)) {
                    Log.v("MessengerIpcClient", "Unbinding service");
                }
                this.state = 4;
                ConnectionTracker.getInstance().unbindService(this.zzbu.zzz, this);
                zzaf zzaf = new zzaf(i, str);
                for (zzae zza : this.zzbs) {
                    zza.zza(zzaf);
                }
                this.zzbs.clear();
                for (int i2 = 0; i2 < this.zzbt.size(); i2++) {
                    ((zzae) this.zzbt.valueAt(i2)).zza(zzaf);
                }
                this.zzbt.clear();
                break;
            case 3:
                this.state = 4;
                break;
            case 4:
                break;
            default:
                throw new IllegalStateException("Unknown state: " + this.state);
        }
    }

    final boolean zza(Message message) {
        int i = message.arg1;
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            Log.d("MessengerIpcClient", "Received response to request: " + i);
        }
        synchronized (this) {
            zzae zzae = (zzae) this.zzbt.get(i);
            if (zzae == null) {
                Log.w("MessengerIpcClient", "Received response for unknown request: " + i);
            } else {
                this.zzbt.remove(i);
                zzt();
                Bundle data = message.getData();
                if (data.getBoolean("unsupported", false)) {
                    zzae.zza(new zzaf(4, "Not supported by GmsCore"));
                } else {
                    zzae.zzb(data);
                }
            }
        }
        return true;
    }

    final synchronized boolean zzb(zzae zzae) {
        boolean z = false;
        boolean z2 = true;
        synchronized (this) {
            switch (this.state) {
                case 0:
                    this.zzbs.add(zzae);
                    if (this.state == 0) {
                        z = true;
                    }
                    Preconditions.checkState(z);
                    if (Log.isLoggable("MessengerIpcClient", 2)) {
                        Log.v("MessengerIpcClient", "Starting bind to GmsCore");
                    }
                    this.state = 1;
                    Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
                    intent.setPackage("com.google.android.gms");
                    if (!ConnectionTracker.getInstance().bindService(this.zzbu.zzz, intent, this, 1)) {
                        zza(0, "Unable to bind to service");
                        break;
                    }
                    this.zzbu.zzbn.schedule(new zzz(this), 30, TimeUnit.SECONDS);
                    break;
                case 1:
                    this.zzbs.add(zzae);
                    break;
                case 2:
                    this.zzbs.add(zzae);
                    zzs();
                    break;
                case 3:
                case 4:
                    z2 = false;
                    break;
                default:
                    throw new IllegalStateException("Unknown state: " + this.state);
            }
        }
        return z2;
    }

    final synchronized void zzt() {
        if (this.state == 2 && this.zzbs.isEmpty() && this.zzbt.size() == 0) {
            if (Log.isLoggable("MessengerIpcClient", 2)) {
                Log.v("MessengerIpcClient", "Finished handling requests, unbinding");
            }
            this.state = 3;
            ConnectionTracker.getInstance().unbindService(this.zzbu.zzz, this);
        }
    }

    final synchronized void zzu() {
        if (this.state == 1) {
            zza(1, "Timed out while binding");
        }
    }
}
