package com.google.android.gms.internal.measurement;

import android.support.annotation.WorkerThread;

final class zzjk extends zzeo {
    private final /* synthetic */ zzji zzaqg;

    zzjk(zzji zzji, zzhj zzhj) {
        this.zzaqg = zzji;
        super(zzhj);
    }

    @WorkerThread
    public final void run() {
        this.zzaqg.zzkt();
    }
}
