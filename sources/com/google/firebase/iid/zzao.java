package com.google.firebase.iid;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

final class zzao extends Handler {
    private final /* synthetic */ zzan zzcp;

    zzao(zzan zzan, Looper looper) {
        this.zzcp = zzan;
        super(looper);
    }

    public final void handleMessage(Message message) {
        this.zzcp.zzb(message);
    }
}
