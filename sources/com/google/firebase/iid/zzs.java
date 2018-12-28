package com.google.firebase.iid;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.google.android.gms.internal.firebase_messaging.zzh;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

final class zzs {
    zzs() {
    }

    @Nullable
    private static zzt zza(SharedPreferences sharedPreferences, String str) throws zzu {
        String string = sharedPreferences.getString(zzaq.zzb(str, "|P|"), null);
        String string2 = sharedPreferences.getString(zzaq.zzb(str, "|K|"), null);
        return (string == null || string2 == null) ? null : new zzt(zza(string, string2), zzb(sharedPreferences, str));
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @android.support.annotation.Nullable
    private static com.google.firebase.iid.zzt zza(java.io.File r7) throws com.google.firebase.iid.zzu, java.io.IOException {
        /*
        r1 = 0;
        r2 = new java.io.FileInputStream;
        r2.<init>(r7);
        r0 = new java.util.Properties;	 Catch:{ Throwable -> 0x0044, all -> 0x004e }
        r0.<init>();	 Catch:{ Throwable -> 0x0044, all -> 0x004e }
        r0.load(r2);	 Catch:{ Throwable -> 0x0044, all -> 0x004e }
        r3 = "pub";
        r3 = r0.getProperty(r3);	 Catch:{ Throwable -> 0x0044, all -> 0x004e }
        r4 = "pri";
        r4 = r0.getProperty(r4);	 Catch:{ Throwable -> 0x0044, all -> 0x004e }
        if (r3 == 0) goto L_0x0020;
    L_0x001e:
        if (r4 != 0) goto L_0x0025;
    L_0x0020:
        zza(r1, r2);
        r0 = r1;
    L_0x0024:
        return r0;
    L_0x0025:
        r3 = zza(r3, r4);	 Catch:{ Throwable -> 0x0044, all -> 0x004e }
        r4 = "cre";
        r0 = r0.getProperty(r4);	 Catch:{ NumberFormatException -> 0x003d }
        r4 = java.lang.Long.parseLong(r0);	 Catch:{ NumberFormatException -> 0x003d }
        r0 = new com.google.firebase.iid.zzt;	 Catch:{ Throwable -> 0x0044, all -> 0x004e }
        r0.<init>(r3, r4);	 Catch:{ Throwable -> 0x0044, all -> 0x004e }
        zza(r1, r2);
        goto L_0x0024;
    L_0x003d:
        r0 = move-exception;
        r3 = new com.google.firebase.iid.zzu;	 Catch:{ Throwable -> 0x0044, all -> 0x004e }
        r3.<init>(r0);	 Catch:{ Throwable -> 0x0044, all -> 0x004e }
        throw r3;	 Catch:{ Throwable -> 0x0044, all -> 0x004e }
    L_0x0044:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x0046 }
    L_0x0046:
        r1 = move-exception;
        r6 = r1;
        r1 = r0;
        r0 = r6;
    L_0x004a:
        zza(r1, r2);
        throw r0;
    L_0x004e:
        r0 = move-exception;
        goto L_0x004a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.iid.zzs.zza(java.io.File):com.google.firebase.iid.zzt");
    }

    private static KeyPair zza(String str, String str2) throws zzu {
        Exception e;
        String valueOf;
        try {
            byte[] decode = Base64.decode(str, 8);
            byte[] decode2 = Base64.decode(str2, 8);
            try {
                KeyFactory instance = KeyFactory.getInstance("RSA");
                return new KeyPair(instance.generatePublic(new X509EncodedKeySpec(decode)), instance.generatePrivate(new PKCS8EncodedKeySpec(decode2)));
            } catch (InvalidKeySpecException e2) {
                e = e2;
                valueOf = String.valueOf(e);
                Log.w("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf).length() + 19).append("Invalid key stored ").append(valueOf).toString());
                throw new zzu(e);
            } catch (NoSuchAlgorithmException e3) {
                e = e3;
                valueOf = String.valueOf(e);
                Log.w("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf).length() + 19).append("Invalid key stored ").append(valueOf).toString());
                throw new zzu(e);
            }
        } catch (Exception e4) {
            throw new zzu(e4);
        }
    }

    static void zza(Context context) {
        for (File file : zzb(context).listFiles()) {
            if (file.getName().startsWith("com.google.InstanceId")) {
                file.delete();
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void zza(android.content.Context r6, java.lang.String r7, com.google.firebase.iid.zzt r8) {
        /*
        r1 = 0;
        r0 = "FirebaseInstanceId";
        r2 = 3;
        r0 = android.util.Log.isLoggable(r0, r2);	 Catch:{ IOException -> 0x0057 }
        if (r0 == 0) goto L_0x0014;
    L_0x000b:
        r0 = "FirebaseInstanceId";
        r2 = "Writing key to properties file";
        android.util.Log.d(r0, r2);	 Catch:{ IOException -> 0x0057 }
    L_0x0014:
        r0 = zzf(r6, r7);	 Catch:{ IOException -> 0x0057 }
        r0.createNewFile();	 Catch:{ IOException -> 0x0057 }
        r2 = new java.util.Properties;	 Catch:{ IOException -> 0x0057 }
        r2.<init>();	 Catch:{ IOException -> 0x0057 }
        r3 = "pub";
        r4 = r8.zzp();	 Catch:{ IOException -> 0x0057 }
        r2.setProperty(r3, r4);	 Catch:{ IOException -> 0x0057 }
        r3 = "pri";
        r4 = r8.zzq();	 Catch:{ IOException -> 0x0057 }
        r2.setProperty(r3, r4);	 Catch:{ IOException -> 0x0057 }
        r3 = "cre";
        r4 = r8.zzbl;	 Catch:{ IOException -> 0x0057 }
        r4 = java.lang.String.valueOf(r4);	 Catch:{ IOException -> 0x0057 }
        r2.setProperty(r3, r4);	 Catch:{ IOException -> 0x0057 }
        r3 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x0057 }
        r3.<init>(r0);	 Catch:{ IOException -> 0x0057 }
        r0 = 0;
        r2.store(r3, r0);	 Catch:{ Throwable -> 0x0050 }
        r0 = 0;
        zza(r0, r3);	 Catch:{ IOException -> 0x0057 }
    L_0x004f:
        return;
    L_0x0050:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x0052 }
    L_0x0052:
        r0 = move-exception;
        zza(r1, r3);	 Catch:{ IOException -> 0x0057 }
        throw r0;	 Catch:{ IOException -> 0x0057 }
    L_0x0057:
        r0 = move-exception;
        r1 = "FirebaseInstanceId";
        r0 = java.lang.String.valueOf(r0);
        r2 = java.lang.String.valueOf(r0);
        r2 = r2.length();
        r2 = r2 + 21;
        r3 = new java.lang.StringBuilder;
        r3.<init>(r2);
        r2 = "Failed to write key: ";
        r2 = r3.append(r2);
        r0 = r2.append(r0);
        r0 = r0.toString();
        android.util.Log.w(r1, r0);
        goto L_0x004f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.iid.zzs.zza(android.content.Context, java.lang.String, com.google.firebase.iid.zzt):void");
    }

    private static /* synthetic */ void zza(Throwable th, FileInputStream fileInputStream) {
        if (th != null) {
            try {
                fileInputStream.close();
                return;
            } catch (Throwable th2) {
                zzh.zza(th, th2);
                return;
            }
        }
        fileInputStream.close();
    }

    private static /* synthetic */ void zza(Throwable th, FileOutputStream fileOutputStream) {
        if (th != null) {
            try {
                fileOutputStream.close();
                return;
            } catch (Throwable th2) {
                zzh.zza(th, th2);
                return;
            }
        }
        fileOutputStream.close();
    }

    private static long zzb(SharedPreferences sharedPreferences, String str) {
        String string = sharedPreferences.getString(zzaq.zzb(str, "cre"), null);
        if (string != null) {
            try {
                return Long.parseLong(string);
            } catch (NumberFormatException e) {
            }
        }
        return 0;
    }

    private static File zzb(Context context) {
        File noBackupFilesDir = ContextCompat.getNoBackupFilesDir(context);
        if (noBackupFilesDir != null && noBackupFilesDir.isDirectory()) {
            return noBackupFilesDir;
        }
        Log.w("FirebaseInstanceId", "noBackupFilesDir doesn't exist, using regular files directory instead");
        return context.getFilesDir();
    }

    private final void zzb(Context context, String str, zzt zzt) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.google.android.gms.appid", 0);
        try {
            if (zzt.equals(zza(sharedPreferences, str))) {
                return;
            }
        } catch (zzu e) {
        }
        if (Log.isLoggable("FirebaseInstanceId", 3)) {
            Log.d("FirebaseInstanceId", "Writing key to shared preferences");
        }
        Editor edit = sharedPreferences.edit();
        edit.putString(zzaq.zzb(str, "|P|"), zzt.zzp());
        edit.putString(zzaq.zzb(str, "|K|"), zzt.zzq());
        edit.putString(zzaq.zzb(str, "cre"), String.valueOf(zzt.zzbl));
        edit.commit();
    }

    @Nullable
    private final zzt zzd(Context context, String str) throws zzu {
        zzu zzu;
        zzu zzu2;
        try {
            zzt zze = zze(context, str);
            if (zze != null) {
                zzb(context, str, zze);
                return zze;
            }
            zzu = null;
            try {
                zze = zza(context.getSharedPreferences("com.google.android.gms.appid", 0), str);
                if (zze != null) {
                    zza(context, str, zze);
                    return zze;
                }
                zzu2 = zzu;
                if (zzu2 == null) {
                    return null;
                }
                throw zzu2;
            } catch (zzu e) {
                zzu2 = e;
            }
        } catch (zzu zzu22) {
            zzu = zzu22;
        }
    }

    @Nullable
    private final zzt zze(Context context, String str) throws zzu {
        File zzf = zzf(context, str);
        if (!zzf.exists()) {
            return null;
        }
        try {
            return zza(zzf);
        } catch (IOException e) {
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                String valueOf = String.valueOf(e);
                Log.d("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf).length() + 40).append("Failed to read key from file, retrying: ").append(valueOf).toString());
            }
            try {
                return zza(zzf);
            } catch (Exception e2) {
                String valueOf2 = String.valueOf(e2);
                Log.w("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf2).length() + 45).append("IID file exists, but failed to read from it: ").append(valueOf2).toString());
                throw new zzu(e2);
            }
        }
    }

    private static File zzf(Context context, String str) {
        String str2;
        if (TextUtils.isEmpty(str)) {
            str2 = "com.google.InstanceId.properties";
        } else {
            try {
                str2 = Base64.encodeToString(str.getBytes("UTF-8"), 11);
                str2 = new StringBuilder(String.valueOf(str2).length() + 33).append("com.google.InstanceId_").append(str2).append(".properties").toString();
            } catch (UnsupportedEncodingException e) {
                throw new AssertionError(e);
            }
        }
        return new File(zzb(context), str2);
    }

    @WorkerThread
    final zzt zzb(Context context, String str) throws zzu {
        zzt zzd = zzd(context, str);
        return zzd != null ? zzd : zzc(context, str);
    }

    @WorkerThread
    final zzt zzc(Context context, String str) {
        zzt zzt = new zzt(zza.zzb(), System.currentTimeMillis());
        try {
            zzt zzd = zzd(context, str);
            if (zzd != null) {
                if (!Log.isLoggable("FirebaseInstanceId", 3)) {
                    return zzd;
                }
                Log.d("FirebaseInstanceId", "Loaded key after generating new one, using loaded one");
                return zzd;
            }
        } catch (zzu e) {
        }
        if (Log.isLoggable("FirebaseInstanceId", 3)) {
            Log.d("FirebaseInstanceId", "Generated new key");
        }
        zza(context, str, zzt);
        zzb(context, str, zzt);
        return zzt;
    }
}
