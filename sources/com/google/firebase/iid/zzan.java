package com.google.firebase.iid;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.iid.zzi.zza;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.concurrent.GuardedBy;

final class zzan {
    private static int zzby = 0;
    private static PendingIntent zzck;
    private final zzah zzao;
    @GuardedBy("responseCallbacks")
    private final SimpleArrayMap<String, TaskCompletionSource<Bundle>> zzcl = new SimpleArrayMap();
    private Messenger zzcm;
    private Messenger zzcn;
    private zzi zzco;
    private final Context zzz;

    public zzan(Context context, zzah zzah) {
        this.zzz = context;
        this.zzao = zzah;
        this.zzcm = new Messenger(new zzao(this, Looper.getMainLooper()));
    }

    private static synchronized void zza(Context context, Intent intent) {
        synchronized (zzan.class) {
            if (zzck == null) {
                Intent intent2 = new Intent();
                intent2.setPackage("com.google.example.invalidpackage");
                zzck = PendingIntent.getBroadcast(context, 0, intent2, 0);
            }
            intent.putExtra("app", zzck);
        }
    }

    private final void zza(String str, Bundle bundle) {
        synchronized (this.zzcl) {
            TaskCompletionSource taskCompletionSource = (TaskCompletionSource) this.zzcl.remove(str);
            if (taskCompletionSource == null) {
                String str2 = "FirebaseInstanceId";
                String str3 = "Missing callback for ";
                String valueOf = String.valueOf(str);
                Log.w(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3));
                return;
            }
            taskCompletionSource.setResult(bundle);
        }
    }

    private static synchronized String zzab() {
        String num;
        synchronized (zzan.class) {
            int i = zzby;
            zzby = i + 1;
            num = Integer.toString(i);
        }
        return num;
    }

    private final void zzb(Message message) {
        if (message == null || !(message.obj instanceof Intent)) {
            Log.w("FirebaseInstanceId", "Dropping invalid message");
            return;
        }
        Intent intent = (Intent) message.obj;
        intent.setExtrasClassLoader(new zza());
        if (intent.hasExtra("google.messenger")) {
            Parcelable parcelableExtra = intent.getParcelableExtra("google.messenger");
            if (parcelableExtra instanceof zzi) {
                this.zzco = (zzi) parcelableExtra;
            }
            if (parcelableExtra instanceof Messenger) {
                this.zzcn = (Messenger) parcelableExtra;
            }
        }
        intent = (Intent) message.obj;
        String action = intent.getAction();
        String stringExtra;
        String valueOf;
        String str;
        if ("com.google.android.c2dm.intent.REGISTRATION".equals(action)) {
            CharSequence stringExtra2 = intent.getStringExtra("registration_id");
            if (stringExtra2 == null) {
                stringExtra2 = intent.getStringExtra("unregistered");
            }
            if (stringExtra2 == null) {
                stringExtra = intent.getStringExtra("error");
                if (stringExtra == null) {
                    valueOf = String.valueOf(intent.getExtras());
                    Log.w("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf).length() + 49).append("Unexpected response, no error or registration id ").append(valueOf).toString());
                    return;
                }
                if (Log.isLoggable("FirebaseInstanceId", 3)) {
                    str = "FirebaseInstanceId";
                    String str2 = "Received InstanceID error ";
                    action = String.valueOf(stringExtra);
                    Log.d(str, action.length() != 0 ? str2.concat(action) : new String(str2));
                }
                if (stringExtra.startsWith("|")) {
                    String[] split = stringExtra.split("\\|");
                    if (split.length <= 2 || !"ID".equals(split[1])) {
                        action = "FirebaseInstanceId";
                        str = "Unexpected structured response ";
                        valueOf = String.valueOf(stringExtra);
                        Log.w(action, valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
                        return;
                    }
                    stringExtra = split[2];
                    action = split[3];
                    if (action.startsWith(":")) {
                        action = action.substring(1);
                    }
                    zza(stringExtra, intent.putExtra("error", action).getExtras());
                    return;
                }
                synchronized (this.zzcl) {
                    for (int i = 0; i < this.zzcl.size(); i++) {
                        zza((String) this.zzcl.keyAt(i), intent.getExtras());
                    }
                }
                return;
            }
            Matcher matcher = Pattern.compile("\\|ID\\|([^|]+)\\|:?+(.*)").matcher(stringExtra2);
            if (matcher.matches()) {
                action = matcher.group(1);
                stringExtra = matcher.group(2);
                Bundle extras = intent.getExtras();
                extras.putString("registration_id", stringExtra);
                zza(action, extras);
            } else if (Log.isLoggable("FirebaseInstanceId", 3)) {
                stringExtra = "FirebaseInstanceId";
                str = "Unexpected response string: ";
                valueOf = String.valueOf(stringExtra2);
                Log.d(stringExtra, valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
            }
        } else if (Log.isLoggable("FirebaseInstanceId", 3)) {
            stringExtra = "FirebaseInstanceId";
            str = "Unexpected response action: ";
            valueOf = String.valueOf(action);
            Log.d(stringExtra, valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
        }
    }

    private final Bundle zzd(Bundle bundle) throws IOException {
        Bundle zze = zze(bundle);
        if (zze == null || !zze.containsKey("google.messenger")) {
            return zze;
        }
        zze = zze(bundle);
        return (zze == null || !zze.containsKey("google.messenger")) ? zze : null;
    }

    private final Bundle zze(Bundle bundle) throws IOException {
        String zzab = zzab();
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        synchronized (this.zzcl) {
            this.zzcl.put(zzab, taskCompletionSource);
        }
        if (this.zzao.zzw() == 0) {
            throw new IOException("MISSING_INSTANCEID_SERVICE");
        }
        Intent intent = new Intent();
        intent.setPackage("com.google.android.gms");
        if (this.zzao.zzw() == 2) {
            intent.setAction("com.google.iid.TOKEN_REQUEST");
        } else {
            intent.setAction("com.google.android.c2dm.intent.REGISTER");
        }
        intent.putExtras(bundle);
        zza(this.zzz, intent);
        intent.putExtra("kid", new StringBuilder(String.valueOf(zzab).length() + 5).append("|ID|").append(zzab).append("|").toString());
        if (Log.isLoggable("FirebaseInstanceId", 3)) {
            String valueOf = String.valueOf(intent.getExtras());
            Log.d("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf).length() + 8).append("Sending ").append(valueOf).toString());
        }
        intent.putExtra("google.messenger", this.zzcm);
        if (!(this.zzcn == null && this.zzco == null)) {
            Message obtain = Message.obtain();
            obtain.obj = intent;
            try {
                if (this.zzcn != null) {
                    this.zzcn.send(obtain);
                } else {
                    this.zzco.send(obtain);
                }
            } catch (RemoteException e) {
                if (Log.isLoggable("FirebaseInstanceId", 3)) {
                    Log.d("FirebaseInstanceId", "Messenger failed, fallback to startService");
                }
            }
            Bundle bundle2 = (Bundle) Tasks.await(taskCompletionSource.getTask(), 30000, TimeUnit.MILLISECONDS);
            synchronized (this.zzcl) {
                this.zzcl.remove(zzab);
            }
            return bundle2;
        }
        if (this.zzao.zzw() == 2) {
            this.zzz.sendBroadcast(intent);
        } else {
            this.zzz.startService(intent);
        }
        try {
            Bundle bundle22 = (Bundle) Tasks.await(taskCompletionSource.getTask(), 30000, TimeUnit.MILLISECONDS);
            synchronized (this.zzcl) {
                this.zzcl.remove(zzab);
            }
            return bundle22;
        } catch (InterruptedException e2) {
            Log.w("FirebaseInstanceId", "No response");
            throw new IOException("TIMEOUT");
        } catch (TimeoutException e3) {
            Log.w("FirebaseInstanceId", "No response");
            throw new IOException("TIMEOUT");
        } catch (Throwable e4) {
            throw new IOException(e4);
        } catch (Throwable th) {
            synchronized (this.zzcl) {
                this.zzcl.remove(zzab);
            }
        }
    }

    final Bundle zzc(Bundle bundle) throws IOException {
        Exception e;
        if (this.zzao.zzz() < 12000000) {
            return zzd(bundle);
        }
        try {
            return (Bundle) Tasks.await(zzv.zzc(this.zzz).zzb(1, bundle));
        } catch (InterruptedException e2) {
            e = e2;
        } catch (ExecutionException e3) {
            e = e3;
        }
        if (Log.isLoggable("FirebaseInstanceId", 3)) {
            String valueOf = String.valueOf(e);
            Log.d("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf).length() + 22).append("Error making request: ").append(valueOf).toString());
        }
        return ((e.getCause() instanceof zzaf) && ((zzaf) e.getCause()).getErrorCode() == 4) ? zzd(bundle) : null;
    }
}
