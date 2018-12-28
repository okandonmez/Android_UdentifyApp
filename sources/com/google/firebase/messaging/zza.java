package com.google.firebase.messaging;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.Notification.BigTextStyle;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Process;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.util.CrashUtils.ErrorDialogData;
import com.google.android.gms.common.util.PlatformVersion;
import com.google.firebase.iid.zzap;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONArray;
import org.json.JSONException;

final class zza {
    private static zza zzdh;
    private Bundle zzdi;
    private Method zzdj;
    private Method zzdk;
    private final AtomicInteger zzdl = new AtomicInteger((int) SystemClock.elapsedRealtime());
    private final Context zzz;

    private zza(Context context) {
        this.zzz = context.getApplicationContext();
    }

    @TargetApi(26)
    private final Notification zza(CharSequence charSequence, String str, int i, Integer num, Uri uri, PendingIntent pendingIntent, PendingIntent pendingIntent2, String str2) {
        Builder smallIcon = new Builder(this.zzz).setAutoCancel(true).setSmallIcon(i);
        if (!TextUtils.isEmpty(charSequence)) {
            smallIcon.setContentTitle(charSequence);
        }
        if (!TextUtils.isEmpty(str)) {
            smallIcon.setContentText(str);
            smallIcon.setStyle(new BigTextStyle().bigText(str));
        }
        if (num != null) {
            smallIcon.setColor(num.intValue());
        }
        if (uri != null) {
            smallIcon.setSound(uri);
        }
        if (pendingIntent != null) {
            smallIcon.setContentIntent(pendingIntent);
        }
        if (pendingIntent2 != null) {
            smallIcon.setDeleteIntent(pendingIntent2);
        }
        if (str2 != null) {
            if (this.zzdj == null) {
                this.zzdj = zzl("setChannelId");
            }
            if (this.zzdj == null) {
                this.zzdj = zzl("setChannel");
            }
            if (this.zzdj == null) {
                Log.e("FirebaseMessaging", "Error while setting the notification channel");
            } else {
                try {
                    this.zzdj.invoke(smallIcon, new Object[]{str2});
                } catch (Throwable e) {
                    Log.e("FirebaseMessaging", "Error while setting the notification channel", e);
                } catch (Throwable e2) {
                    Log.e("FirebaseMessaging", "Error while setting the notification channel", e2);
                } catch (Throwable e22) {
                    Log.e("FirebaseMessaging", "Error while setting the notification channel", e22);
                } catch (Throwable e222) {
                    Log.e("FirebaseMessaging", "Error while setting the notification channel", e222);
                }
            }
        }
        return smallIcon.build();
    }

    static String zza(Bundle bundle, String str) {
        String string = bundle.getString(str);
        return string == null ? bundle.getString(str.replace("gcm.n.", "gcm.notification.")) : string;
    }

    private static void zza(Intent intent, Bundle bundle) {
        for (String str : bundle.keySet()) {
            if (str.startsWith("google.c.a.") || str.equals("from")) {
                intent.putExtra(str, bundle.getString(str));
            }
        }
    }

    private final Bundle zzal() {
        if (this.zzdi != null) {
            return this.zzdi;
        }
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = this.zzz.getPackageManager().getApplicationInfo(this.zzz.getPackageName(), 128);
        } catch (NameNotFoundException e) {
        }
        if (applicationInfo == null || applicationInfo.metaData == null) {
            return Bundle.EMPTY;
        }
        this.zzdi = applicationInfo.metaData;
        return this.zzdi;
    }

    static String zzb(Bundle bundle, String str) {
        String valueOf = String.valueOf(str);
        String valueOf2 = String.valueOf("_loc_key");
        return zza(bundle, valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf));
    }

    @TargetApi(26)
    private final boolean zzb(int i) {
        if (VERSION.SDK_INT != 26) {
            return true;
        }
        try {
            if (!(this.zzz.getResources().getDrawable(i, null) instanceof AdaptiveIconDrawable)) {
                return true;
            }
            Log.e("FirebaseMessaging", "Adaptive icons cannot be used in notifications. Ignoring icon id: " + i);
            return false;
        } catch (NotFoundException e) {
            return false;
        }
    }

    static Object[] zzc(Bundle bundle, String str) {
        String valueOf = String.valueOf(str);
        String valueOf2 = String.valueOf("_loc_args");
        String zza = zza(bundle, valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf));
        if (TextUtils.isEmpty(zza)) {
            return null;
        }
        try {
            JSONArray jSONArray = new JSONArray(zza);
            String[] strArr = new String[jSONArray.length()];
            for (int i = 0; i < strArr.length; i++) {
                strArr[i] = jSONArray.opt(i);
            }
            return strArr;
        } catch (JSONException e) {
            valueOf = "FirebaseMessaging";
            String valueOf3 = String.valueOf(str);
            valueOf2 = String.valueOf("_loc_args");
            valueOf2 = (valueOf2.length() != 0 ? valueOf3.concat(valueOf2) : new String(valueOf3)).substring(6);
            Log.w(valueOf, new StringBuilder((String.valueOf(valueOf2).length() + 41) + String.valueOf(zza).length()).append("Malformed ").append(valueOf2).append(": ").append(zza).append("  Default value will be used.").toString());
            return null;
        }
    }

    static synchronized zza zzd(Context context) {
        zza zza;
        synchronized (zza.class) {
            if (zzdh == null) {
                zzdh = new zza(context);
            }
            zza = zzdh;
        }
        return zza;
    }

    private final String zzd(Bundle bundle, String str) {
        Object zza = zza(bundle, str);
        if (!TextUtils.isEmpty(zza)) {
            return zza;
        }
        String zzb = zzb(bundle, str);
        if (TextUtils.isEmpty(zzb)) {
            return null;
        }
        Resources resources = this.zzz.getResources();
        int identifier = resources.getIdentifier(zzb, "string", this.zzz.getPackageName());
        if (identifier == 0) {
            String str2 = "FirebaseMessaging";
            String valueOf = String.valueOf(str);
            String valueOf2 = String.valueOf("_loc_key");
            valueOf2 = (valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf)).substring(6);
            Log.w(str2, new StringBuilder((String.valueOf(valueOf2).length() + 49) + String.valueOf(zzb).length()).append(valueOf2).append(" resource not found: ").append(zzb).append(" Default value will be used.").toString());
            return null;
        }
        Object[] zzc = zzc(bundle, str);
        if (zzc == null) {
            return resources.getString(identifier);
        }
        try {
            return resources.getString(identifier, zzc);
        } catch (Throwable e) {
            valueOf = Arrays.toString(zzc);
            Log.w("FirebaseMessaging", new StringBuilder((String.valueOf(zzb).length() + 58) + String.valueOf(valueOf).length()).append("Missing format argument for ").append(zzb).append(": ").append(valueOf).append(" Default value will be used.").toString(), e);
            return null;
        }
    }

    static boolean zzf(Bundle bundle) {
        return "1".equals(zza(bundle, "gcm.n.e")) || zza(bundle, "gcm.n.icon") != null;
    }

    @Nullable
    static Uri zzg(@NonNull Bundle bundle) {
        Object zza = zza(bundle, "gcm.n.link_android");
        if (TextUtils.isEmpty(zza)) {
            zza = zza(bundle, "gcm.n.link");
        }
        return !TextUtils.isEmpty(zza) ? Uri.parse(zza) : null;
    }

    static String zzi(Bundle bundle) {
        Object zza = zza(bundle, "gcm.n.sound2");
        return TextUtils.isEmpty(zza) ? zza(bundle, "gcm.n.sound") : zza;
    }

    @TargetApi(26)
    private static Method zzl(String str) {
        try {
            return Builder.class.getMethod(str, new Class[]{String.class});
        } catch (NoSuchMethodException e) {
            return null;
        } catch (SecurityException e2) {
            return null;
        }
    }

    private final Integer zzm(String str) {
        Integer num = null;
        if (VERSION.SDK_INT >= 21) {
            if (!TextUtils.isEmpty(str)) {
                try {
                    num = Integer.valueOf(Color.parseColor(str));
                } catch (IllegalArgumentException e) {
                    Log.w("FirebaseMessaging", new StringBuilder(String.valueOf(str).length() + 54).append("Color ").append(str).append(" not valid. Notification will use default color.").toString());
                }
            }
            int i = zzal().getInt("com.google.firebase.messaging.default_notification_color", 0);
            if (i != 0) {
                try {
                    num = Integer.valueOf(ContextCompat.getColor(this.zzz, i));
                } catch (NotFoundException e2) {
                    Log.w("FirebaseMessaging", "Cannot find the color resource referenced in AndroidManifest.");
                }
            }
        }
        return num;
    }

    @TargetApi(26)
    private final String zzn(String str) {
        if (!PlatformVersion.isAtLeastO()) {
            return null;
        }
        NotificationManager notificationManager = (NotificationManager) this.zzz.getSystemService(NotificationManager.class);
        try {
            if (this.zzdk == null) {
                this.zzdk = notificationManager.getClass().getMethod("getNotificationChannel", new Class[]{String.class});
            }
            if (!TextUtils.isEmpty(str)) {
                if (this.zzdk.invoke(notificationManager, new Object[]{str}) != null) {
                    return str;
                }
                Log.w("FirebaseMessaging", new StringBuilder(String.valueOf(str).length() + 122).append("Notification Channel requested (").append(str).append(") has not been created by the app. Manifest configuration, or default, value will be used.").toString());
            }
            Object string = zzal().getString("com.google.firebase.messaging.default_notification_channel_id");
            if (TextUtils.isEmpty(string)) {
                Log.w("FirebaseMessaging", "Missing Default Notification Channel metadata in AndroidManifest. Default value will be used.");
            } else {
                if (this.zzdk.invoke(notificationManager, new Object[]{string}) != null) {
                    return string;
                }
                Log.w("FirebaseMessaging", "Notification Channel set in AndroidManifest.xml has not been created by the app. Default value will be used.");
            }
            if (this.zzdk.invoke(notificationManager, new Object[]{"fcm_fallback_notification_channel"}) == null) {
                Object newInstance = Class.forName("android.app.NotificationChannel").getConstructor(new Class[]{String.class, CharSequence.class, Integer.TYPE}).newInstance(new Object[]{"fcm_fallback_notification_channel", this.zzz.getString(C0331R.string.fcm_fallback_notification_channel_label), Integer.valueOf(3)});
                notificationManager.getClass().getMethod("createNotificationChannel", new Class[]{r2}).invoke(notificationManager, new Object[]{newInstance});
            }
            return "fcm_fallback_notification_channel";
        } catch (Throwable e) {
            Log.e("FirebaseMessaging", "Error while setting the notification channel", e);
            return null;
        } catch (Throwable e2) {
            Log.e("FirebaseMessaging", "Error while setting the notification channel", e2);
            return null;
        } catch (Throwable e22) {
            Log.e("FirebaseMessaging", "Error while setting the notification channel", e22);
            return null;
        } catch (Throwable e222) {
            Log.e("FirebaseMessaging", "Error while setting the notification channel", e222);
            return null;
        } catch (Throwable e2222) {
            Log.e("FirebaseMessaging", "Error while setting the notification channel", e2222);
            return null;
        } catch (Throwable e22222) {
            Log.e("FirebaseMessaging", "Error while setting the notification channel", e22222);
            return null;
        } catch (Throwable e222222) {
            Log.e("FirebaseMessaging", "Error while setting the notification channel", e222222);
            return null;
        } catch (Throwable e2222222) {
            Log.e("FirebaseMessaging", "Error while setting the notification channel", e2222222);
            return null;
        }
    }

    final boolean zzh(Bundle bundle) {
        if ("1".equals(zza(bundle, "gcm.n.noui"))) {
            return true;
        }
        boolean z;
        CharSequence zzd;
        CharSequence zzd2;
        String zza;
        Resources resources;
        int identifier;
        Integer zzm;
        Uri uri;
        Object zza2;
        Uri zzg;
        Intent intent;
        Intent intent2;
        Parcelable parcelable;
        Bundle bundle2;
        Intent intent3;
        PendingIntent zza3;
        PendingIntent zza4;
        Parcelable parcelable2;
        NotificationCompat.Builder smallIcon;
        Notification build;
        String zza5;
        NotificationManager notificationManager;
        int i;
        if (!((KeyguardManager) this.zzz.getSystemService("keyguard")).inKeyguardRestrictedInputMode()) {
            if (!PlatformVersion.isAtLeastLollipop()) {
                SystemClock.sleep(10);
            }
            int myPid = Process.myPid();
            List<RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) this.zzz.getSystemService("activity")).getRunningAppProcesses();
            if (runningAppProcesses != null) {
                for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                    if (runningAppProcessInfo.pid == myPid) {
                        z = runningAppProcessInfo.importance == 100;
                        if (z) {
                            return false;
                        }
                        zzd = zzd(bundle, "gcm.n.title");
                        if (TextUtils.isEmpty(zzd)) {
                            zzd = this.zzz.getApplicationInfo().loadLabel(this.zzz.getPackageManager());
                        }
                        zzd2 = zzd(bundle, "gcm.n.body");
                        zza = zza(bundle, "gcm.n.icon");
                        if (!TextUtils.isEmpty(zza)) {
                            resources = this.zzz.getResources();
                            identifier = resources.getIdentifier(zza, "drawable", this.zzz.getPackageName());
                            if (identifier == 0 || !zzb(identifier)) {
                                identifier = resources.getIdentifier(zza, "mipmap", this.zzz.getPackageName());
                                if (identifier == 0 || !zzb(identifier)) {
                                    Log.w("FirebaseMessaging", new StringBuilder(String.valueOf(zza).length() + 61).append("Icon resource ").append(zza).append(" not found. Notification will use default icon.").toString());
                                }
                            }
                            zzm = zzm(zza(bundle, "gcm.n.color"));
                            zza = zzi(bundle);
                            if (TextUtils.isEmpty(zza)) {
                                uri = null;
                            } else if (!"default".equals(zza) || this.zzz.getResources().getIdentifier(zza, "raw", this.zzz.getPackageName()) == 0) {
                                uri = RingtoneManager.getDefaultUri(2);
                            } else {
                                String packageName = this.zzz.getPackageName();
                                uri = Uri.parse(new StringBuilder((String.valueOf(packageName).length() + 24) + String.valueOf(zza).length()).append("android.resource://").append(packageName).append("/raw/").append(zza).toString());
                            }
                            zza2 = zza(bundle, "gcm.n.click_action");
                            if (TextUtils.isEmpty(zza2)) {
                                zzg = zzg(bundle);
                                if (zzg != null) {
                                    intent = new Intent("android.intent.action.VIEW");
                                    intent.setPackage(this.zzz.getPackageName());
                                    intent.setData(zzg);
                                    intent2 = intent;
                                } else {
                                    intent = this.zzz.getPackageManager().getLaunchIntentForPackage(this.zzz.getPackageName());
                                    if (intent == null) {
                                        Log.w("FirebaseMessaging", "No activity found to launch app");
                                    }
                                    intent2 = intent;
                                }
                            } else {
                                intent = new Intent(zza2);
                                intent.setPackage(this.zzz.getPackageName());
                                intent.setFlags(ErrorDialogData.BINDER_CRASH);
                                intent2 = intent;
                            }
                            if (intent2 == null) {
                                parcelable = null;
                            } else {
                                intent2.addFlags(67108864);
                                bundle2 = new Bundle(bundle);
                                FirebaseMessagingService.zzj(bundle2);
                                intent2.putExtras(bundle2);
                                for (String zza6 : bundle2.keySet()) {
                                    if (!zza6.startsWith("gcm.n.") || zza6.startsWith("gcm.notification.")) {
                                        intent2.removeExtra(zza6);
                                    }
                                }
                                parcelable = PendingIntent.getActivity(this.zzz, this.zzdl.incrementAndGet(), intent2, ErrorDialogData.SUPPRESSED);
                            }
                            if (FirebaseMessagingService.zzk(bundle)) {
                                intent3 = new Intent("com.google.firebase.messaging.NOTIFICATION_OPEN");
                                zza(intent3, bundle);
                                intent3.putExtra("pending_intent", parcelable);
                                zza3 = zzap.zza(this.zzz, this.zzdl.incrementAndGet(), intent3, ErrorDialogData.SUPPRESSED);
                                intent = new Intent("com.google.firebase.messaging.NOTIFICATION_DISMISS");
                                zza(intent, bundle);
                                zza4 = zzap.zza(this.zzz, this.zzdl.incrementAndGet(), intent, ErrorDialogData.SUPPRESSED);
                            } else {
                                zza4 = null;
                                parcelable2 = parcelable;
                            }
                            if (PlatformVersion.isAtLeastO() || this.zzz.getApplicationInfo().targetSdkVersion <= 25) {
                                smallIcon = new NotificationCompat.Builder(this.zzz).setAutoCancel(true).setSmallIcon(identifier);
                                if (!TextUtils.isEmpty(zzd)) {
                                    smallIcon.setContentTitle(zzd);
                                }
                                if (!TextUtils.isEmpty(zzd2)) {
                                    smallIcon.setContentText(zzd2);
                                    smallIcon.setStyle(new NotificationCompat.BigTextStyle().bigText(zzd2));
                                }
                                if (zzm != null) {
                                    smallIcon.setColor(zzm.intValue());
                                }
                                if (uri != null) {
                                    smallIcon.setSound(uri);
                                }
                                if (zza3 != null) {
                                    smallIcon.setContentIntent(zza3);
                                }
                                if (zza4 != null) {
                                    smallIcon.setDeleteIntent(zza4);
                                }
                                build = smallIcon.build();
                            } else {
                                build = zza(zzd, zzd2, identifier, zzm, uri, zza3, zza4, zzn(zza(bundle, "gcm.n.android_channel_id")));
                            }
                            zza5 = zza(bundle, "gcm.n.tag");
                            if (Log.isLoggable("FirebaseMessaging", 3)) {
                                Log.d("FirebaseMessaging", "Showing notification");
                            }
                            notificationManager = (NotificationManager) this.zzz.getSystemService("notification");
                            if (TextUtils.isEmpty(zza5)) {
                                zza5 = "FCM-Notification:" + SystemClock.uptimeMillis();
                            }
                            notificationManager.notify(zza5, 0, build);
                            return true;
                        }
                        i = zzal().getInt("com.google.firebase.messaging.default_notification_icon", 0);
                        if (i == 0 || !zzb(i)) {
                            i = this.zzz.getApplicationInfo().icon;
                        }
                        if (i == 0 || !zzb(i)) {
                            i = 17301651;
                        }
                        identifier = i;
                        zzm = zzm(zza(bundle, "gcm.n.color"));
                        zza6 = zzi(bundle);
                        if (TextUtils.isEmpty(zza6)) {
                            if ("default".equals(zza6)) {
                            }
                            uri = RingtoneManager.getDefaultUri(2);
                        } else {
                            uri = null;
                        }
                        zza2 = zza(bundle, "gcm.n.click_action");
                        if (TextUtils.isEmpty(zza2)) {
                            zzg = zzg(bundle);
                            if (zzg != null) {
                                intent = this.zzz.getPackageManager().getLaunchIntentForPackage(this.zzz.getPackageName());
                                if (intent == null) {
                                    Log.w("FirebaseMessaging", "No activity found to launch app");
                                }
                                intent2 = intent;
                            } else {
                                intent = new Intent("android.intent.action.VIEW");
                                intent.setPackage(this.zzz.getPackageName());
                                intent.setData(zzg);
                                intent2 = intent;
                            }
                        } else {
                            intent = new Intent(zza2);
                            intent.setPackage(this.zzz.getPackageName());
                            intent.setFlags(ErrorDialogData.BINDER_CRASH);
                            intent2 = intent;
                        }
                        if (intent2 == null) {
                            intent2.addFlags(67108864);
                            bundle2 = new Bundle(bundle);
                            FirebaseMessagingService.zzj(bundle2);
                            intent2.putExtras(bundle2);
                            for (String zza62 : bundle2.keySet()) {
                                if (zza62.startsWith("gcm.n.")) {
                                }
                                intent2.removeExtra(zza62);
                            }
                            parcelable = PendingIntent.getActivity(this.zzz, this.zzdl.incrementAndGet(), intent2, ErrorDialogData.SUPPRESSED);
                        } else {
                            parcelable = null;
                        }
                        if (FirebaseMessagingService.zzk(bundle)) {
                            zza4 = null;
                            parcelable2 = parcelable;
                        } else {
                            intent3 = new Intent("com.google.firebase.messaging.NOTIFICATION_OPEN");
                            zza(intent3, bundle);
                            intent3.putExtra("pending_intent", parcelable);
                            zza3 = zzap.zza(this.zzz, this.zzdl.incrementAndGet(), intent3, ErrorDialogData.SUPPRESSED);
                            intent = new Intent("com.google.firebase.messaging.NOTIFICATION_DISMISS");
                            zza(intent, bundle);
                            zza4 = zzap.zza(this.zzz, this.zzdl.incrementAndGet(), intent, ErrorDialogData.SUPPRESSED);
                        }
                        if (PlatformVersion.isAtLeastO()) {
                        }
                        smallIcon = new NotificationCompat.Builder(this.zzz).setAutoCancel(true).setSmallIcon(identifier);
                        if (TextUtils.isEmpty(zzd)) {
                            smallIcon.setContentTitle(zzd);
                        }
                        if (TextUtils.isEmpty(zzd2)) {
                            smallIcon.setContentText(zzd2);
                            smallIcon.setStyle(new NotificationCompat.BigTextStyle().bigText(zzd2));
                        }
                        if (zzm != null) {
                            smallIcon.setColor(zzm.intValue());
                        }
                        if (uri != null) {
                            smallIcon.setSound(uri);
                        }
                        if (zza3 != null) {
                            smallIcon.setContentIntent(zza3);
                        }
                        if (zza4 != null) {
                            smallIcon.setDeleteIntent(zza4);
                        }
                        build = smallIcon.build();
                        zza5 = zza(bundle, "gcm.n.tag");
                        if (Log.isLoggable("FirebaseMessaging", 3)) {
                            Log.d("FirebaseMessaging", "Showing notification");
                        }
                        notificationManager = (NotificationManager) this.zzz.getSystemService("notification");
                        if (TextUtils.isEmpty(zza5)) {
                            zza5 = "FCM-Notification:" + SystemClock.uptimeMillis();
                        }
                        notificationManager.notify(zza5, 0, build);
                        return true;
                    }
                }
            }
        }
        z = false;
        if (z) {
            return false;
        }
        zzd = zzd(bundle, "gcm.n.title");
        if (TextUtils.isEmpty(zzd)) {
            zzd = this.zzz.getApplicationInfo().loadLabel(this.zzz.getPackageManager());
        }
        zzd2 = zzd(bundle, "gcm.n.body");
        zza62 = zza(bundle, "gcm.n.icon");
        if (TextUtils.isEmpty(zza62)) {
            resources = this.zzz.getResources();
            identifier = resources.getIdentifier(zza62, "drawable", this.zzz.getPackageName());
            identifier = resources.getIdentifier(zza62, "mipmap", this.zzz.getPackageName());
            Log.w("FirebaseMessaging", new StringBuilder(String.valueOf(zza62).length() + 61).append("Icon resource ").append(zza62).append(" not found. Notification will use default icon.").toString());
        }
        i = zzal().getInt("com.google.firebase.messaging.default_notification_icon", 0);
        i = this.zzz.getApplicationInfo().icon;
        i = 17301651;
        identifier = i;
        zzm = zzm(zza(bundle, "gcm.n.color"));
        zza62 = zzi(bundle);
        if (TextUtils.isEmpty(zza62)) {
            uri = null;
        } else {
            if ("default".equals(zza62)) {
            }
            uri = RingtoneManager.getDefaultUri(2);
        }
        zza2 = zza(bundle, "gcm.n.click_action");
        if (TextUtils.isEmpty(zza2)) {
            intent = new Intent(zza2);
            intent.setPackage(this.zzz.getPackageName());
            intent.setFlags(ErrorDialogData.BINDER_CRASH);
            intent2 = intent;
        } else {
            zzg = zzg(bundle);
            if (zzg != null) {
                intent = new Intent("android.intent.action.VIEW");
                intent.setPackage(this.zzz.getPackageName());
                intent.setData(zzg);
                intent2 = intent;
            } else {
                intent = this.zzz.getPackageManager().getLaunchIntentForPackage(this.zzz.getPackageName());
                if (intent == null) {
                    Log.w("FirebaseMessaging", "No activity found to launch app");
                }
                intent2 = intent;
            }
        }
        if (intent2 == null) {
            parcelable = null;
        } else {
            intent2.addFlags(67108864);
            bundle2 = new Bundle(bundle);
            FirebaseMessagingService.zzj(bundle2);
            intent2.putExtras(bundle2);
            for (String zza622 : bundle2.keySet()) {
                if (zza622.startsWith("gcm.n.")) {
                }
                intent2.removeExtra(zza622);
            }
            parcelable = PendingIntent.getActivity(this.zzz, this.zzdl.incrementAndGet(), intent2, ErrorDialogData.SUPPRESSED);
        }
        if (FirebaseMessagingService.zzk(bundle)) {
            intent3 = new Intent("com.google.firebase.messaging.NOTIFICATION_OPEN");
            zza(intent3, bundle);
            intent3.putExtra("pending_intent", parcelable);
            zza3 = zzap.zza(this.zzz, this.zzdl.incrementAndGet(), intent3, ErrorDialogData.SUPPRESSED);
            intent = new Intent("com.google.firebase.messaging.NOTIFICATION_DISMISS");
            zza(intent, bundle);
            zza4 = zzap.zza(this.zzz, this.zzdl.incrementAndGet(), intent, ErrorDialogData.SUPPRESSED);
        } else {
            zza4 = null;
            parcelable2 = parcelable;
        }
        if (PlatformVersion.isAtLeastO()) {
        }
        smallIcon = new NotificationCompat.Builder(this.zzz).setAutoCancel(true).setSmallIcon(identifier);
        if (TextUtils.isEmpty(zzd)) {
            smallIcon.setContentTitle(zzd);
        }
        if (TextUtils.isEmpty(zzd2)) {
            smallIcon.setContentText(zzd2);
            smallIcon.setStyle(new NotificationCompat.BigTextStyle().bigText(zzd2));
        }
        if (zzm != null) {
            smallIcon.setColor(zzm.intValue());
        }
        if (uri != null) {
            smallIcon.setSound(uri);
        }
        if (zza3 != null) {
            smallIcon.setContentIntent(zza3);
        }
        if (zza4 != null) {
            smallIcon.setDeleteIntent(zza4);
        }
        build = smallIcon.build();
        zza5 = zza(bundle, "gcm.n.tag");
        if (Log.isLoggable("FirebaseMessaging", 3)) {
            Log.d("FirebaseMessaging", "Showing notification");
        }
        notificationManager = (NotificationManager) this.zzz.getSystemService("notification");
        if (TextUtils.isEmpty(zza5)) {
            zza5 = "FCM-Notification:" + SystemClock.uptimeMillis();
        }
        notificationManager.notify(zza5, 0, build);
        return true;
    }
}
