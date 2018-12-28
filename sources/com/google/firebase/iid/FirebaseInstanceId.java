package com.google.firebase.iid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Build.VERSION;
import android.os.Looper;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.measurement.AppMeasurement;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.concurrent.GuardedBy;

public class FirebaseInstanceId {
    static final Executor zzah = zzn.zzba;
    private static final long zzai = TimeUnit.HOURS.toSeconds(8);
    private static zzaq zzaj;
    private static final Executor zzak = Executors.newCachedThreadPool();
    @GuardedBy("FirebaseInstanceId.class")
    @VisibleForTesting
    private static ScheduledThreadPoolExecutor zzal;
    private static final Executor zzam = new ThreadPoolExecutor(0, 1, 30, TimeUnit.SECONDS, new LinkedBlockingQueue());
    private final FirebaseApp zzan;
    private final zzah zzao;
    private IRpc zzap;
    private final zzak zzaq;
    private final zzau zzar;
    @GuardedBy("this")
    private boolean zzas;
    @GuardedBy("this")
    private boolean zzat;

    FirebaseInstanceId(FirebaseApp firebaseApp) {
        this(firebaseApp, new zzah(firebaseApp.getApplicationContext()));
    }

    private FirebaseInstanceId(FirebaseApp firebaseApp, zzah zzah) {
        this.zzaq = new zzak();
        this.zzas = false;
        if (zzah.zza(firebaseApp) == null) {
            throw new IllegalStateException("FirebaseInstanceId failed to initialize, FirebaseApp is missing project ID");
        }
        synchronized (FirebaseInstanceId.class) {
            if (zzaj == null) {
                zzaj = new zzaq(firebaseApp.getApplicationContext());
            }
        }
        this.zzan = firebaseApp;
        this.zzao = zzah;
        if (this.zzap == null) {
            IRpc iRpc = (IRpc) firebaseApp.get(IRpc.class);
            if (iRpc != null) {
                this.zzap = iRpc;
            } else {
                this.zzap = new zzo(firebaseApp, zzah, zzam);
            }
        }
        this.zzap = this.zzap;
        this.zzar = new zzau(zzaj);
        this.zzat = zzl();
        if (zzn()) {
            zzd();
        }
    }

    public static FirebaseInstanceId getInstance() {
        return getInstance(FirebaseApp.getInstance());
    }

    @Keep
    public static synchronized FirebaseInstanceId getInstance(@NonNull FirebaseApp firebaseApp) {
        FirebaseInstanceId firebaseInstanceId;
        synchronized (FirebaseInstanceId.class) {
            firebaseInstanceId = (FirebaseInstanceId) firebaseApp.get(FirebaseInstanceId.class);
        }
        return firebaseInstanceId;
    }

    private final synchronized void startSync() {
        if (!this.zzas) {
            zza(0);
        }
    }

    private final <T> T zza(Task<T> task) throws IOException {
        try {
            return Tasks.await(task, 30000, TimeUnit.MILLISECONDS);
        } catch (Throwable e) {
            Throwable th = e;
            Throwable e2 = th.getCause();
            if (e2 instanceof IOException) {
                if ("INSTANCE_ID_RESET".equals(e2.getMessage())) {
                    zzj();
                }
                throw ((IOException) e2);
            } else if (e2 instanceof RuntimeException) {
                throw ((RuntimeException) e2);
            } else {
                throw new IOException(th);
            }
        } catch (InterruptedException e3) {
            throw new IOException("SERVICE_NOT_AVAILABLE");
        } catch (TimeoutException e4) {
            throw new IOException("SERVICE_NOT_AVAILABLE");
        }
    }

    static void zza(Runnable runnable, long j) {
        synchronized (FirebaseInstanceId.class) {
            if (zzal == null) {
                zzal = new ScheduledThreadPoolExecutor(1);
            }
            zzal.schedule(runnable, j, TimeUnit.SECONDS);
        }
    }

    private static String zzd(String str) {
        return (str.isEmpty() || str.equalsIgnoreCase(AppMeasurement.FCM_ORIGIN) || str.equalsIgnoreCase("gcm")) ? "*" : str;
    }

    private final void zzd() {
        zzar zzg = zzg();
        if (zzg == null || zzg.zzj(this.zzao.zzx()) || this.zzar.zzaj()) {
            startSync();
        }
    }

    private static String zzf() {
        return zzah.zza(zzaj.zzg("").getKeyPair());
    }

    static boolean zzi() {
        return Log.isLoggable("FirebaseInstanceId", 3) || (VERSION.SDK_INT == 23 && Log.isLoggable("FirebaseInstanceId", 3));
    }

    private final boolean zzl() {
        Context applicationContext = this.zzan.getApplicationContext();
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences("com.google.firebase.messaging", 0);
        if (sharedPreferences.contains("auto_init")) {
            return sharedPreferences.getBoolean("auto_init", true);
        }
        try {
            PackageManager packageManager = applicationContext.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(applicationContext.getPackageName(), 128);
                if (!(applicationInfo == null || applicationInfo.metaData == null || !applicationInfo.metaData.containsKey("firebase_messaging_auto_init_enabled"))) {
                    return applicationInfo.metaData.getBoolean("firebase_messaging_auto_init_enabled");
                }
            }
        } catch (NameNotFoundException e) {
        }
        return zzm();
    }

    private final boolean zzm() {
        try {
            Class.forName("com.google.firebase.messaging.FirebaseMessaging");
            return true;
        } catch (ClassNotFoundException e) {
            Context applicationContext = this.zzan.getApplicationContext();
            Intent intent = new Intent("com.google.firebase.MESSAGING_EVENT");
            intent.setPackage(applicationContext.getPackageName());
            ResolveInfo resolveService = applicationContext.getPackageManager().resolveService(intent, 0);
            return (resolveService == null || resolveService.serviceInfo == null) ? false : true;
        }
    }

    @WorkerThread
    public void deleteInstanceId() throws IOException {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            throw new IOException("MAIN_THREAD");
        }
        zza(this.zzap.deleteInstanceId(zzf()));
        zzj();
    }

    @WorkerThread
    public void deleteToken(String str, String str2) throws IOException {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            throw new IOException("MAIN_THREAD");
        }
        String zzd = zzd(str2);
        zza(this.zzap.deleteToken(zzf(), str, zzd));
        zzaj.zzd("", str, zzd);
    }

    public long getCreationTime() {
        return zzaj.zzg("").getCreationTime();
    }

    @WorkerThread
    public String getId() {
        zzd();
        return zzf();
    }

    @Nullable
    public String getToken() {
        zzar zzg = zzg();
        if (zzg == null || zzg.zzj(this.zzao.zzx())) {
            startSync();
        }
        return zzg != null ? zzg.zzcz : null;
    }

    @WorkerThread
    public String getToken(String str, String str2) throws IOException {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            throw new IOException("MAIN_THREAD");
        }
        String zzd = zzd(str2);
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        zzak.execute(new zzk(this, str, str2, taskCompletionSource, zzd));
        return (String) zza(taskCompletionSource.getTask());
    }

    public final synchronized Task<Void> zza(String str) {
        Task<Void> zza;
        zza = this.zzar.zza(str);
        startSync();
        return zza;
    }

    final /* synthetic */ Task zza(String str, String str2, String str3) {
        return this.zzap.getToken(str, str2, str3);
    }

    final synchronized void zza(long j) {
        zza(new zzas(this, this.zzao, this.zzar, Math.min(Math.max(30, j << 1), zzai)), j);
        this.zzas = true;
    }

    final /* synthetic */ void zza(String str, String str2, TaskCompletionSource taskCompletionSource, Task task) {
        if (task.isSuccessful()) {
            String str3 = (String) task.getResult();
            zzaj.zza("", str, str2, str3, this.zzao.zzx());
            taskCompletionSource.setResult(str3);
            return;
        }
        taskCompletionSource.setException(task.getException());
    }

    final /* synthetic */ void zza(String str, String str2, TaskCompletionSource taskCompletionSource, String str3) {
        zzar zzc = zzaj.zzc("", str, str2);
        if (zzc == null || zzc.zzj(this.zzao.zzx())) {
            this.zzaq.zza(str, str3, new zzl(this, zzf(), str, str3)).addOnCompleteListener(zzak, new zzm(this, str, str3, taskCompletionSource));
            return;
        }
        taskCompletionSource.setResult(zzc.zzcz);
    }

    final synchronized void zza(boolean z) {
        this.zzas = z;
    }

    final void zzb(String str) throws IOException {
        zzar zzg = zzg();
        if (zzg == null || zzg.zzj(this.zzao.zzx())) {
            throw new IOException("token not available");
        }
        zza(this.zzap.subscribeToTopic(zzf(), zzg.zzcz, str));
    }

    @VisibleForTesting
    public final synchronized void zzb(boolean z) {
        Editor edit = this.zzan.getApplicationContext().getSharedPreferences("com.google.firebase.messaging", 0).edit();
        edit.putBoolean("auto_init", z);
        edit.apply();
        if (!this.zzat && z) {
            zzd();
        }
        this.zzat = z;
    }

    final void zzc(String str) throws IOException {
        zzar zzg = zzg();
        if (zzg == null || zzg.zzj(this.zzao.zzx())) {
            throw new IOException("token not available");
        }
        zza(this.zzap.unsubscribeFromTopic(zzf(), zzg.zzcz, str));
    }

    final FirebaseApp zze() {
        return this.zzan;
    }

    @Nullable
    final zzar zzg() {
        return zzaj.zzc("", zzah.zza(this.zzan), "*");
    }

    final String zzh() throws IOException {
        return getToken(zzah.zza(this.zzan), "*");
    }

    final synchronized void zzj() {
        zzaj.zzaf();
        if (zzn()) {
            startSync();
        }
    }

    final void zzk() {
        zzaj.zzh("");
        startSync();
    }

    @VisibleForTesting
    public final synchronized boolean zzn() {
        return this.zzat;
    }
}
