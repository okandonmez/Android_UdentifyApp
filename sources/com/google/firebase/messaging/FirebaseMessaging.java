package com.google.firebase.messaging;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import java.util.regex.Pattern;

public class FirebaseMessaging {
    public static final String INSTANCE_ID_SCOPE = "FCM";
    private static final Pattern zzdm = Pattern.compile("[a-zA-Z0-9-_.~%]{1,900}");
    private static FirebaseMessaging zzdn;
    private final FirebaseInstanceId zzdd;

    private FirebaseMessaging(FirebaseInstanceId firebaseInstanceId) {
        this.zzdd = firebaseInstanceId;
    }

    public static synchronized FirebaseMessaging getInstance() {
        FirebaseMessaging firebaseMessaging;
        synchronized (FirebaseMessaging.class) {
            if (zzdn == null) {
                zzdn = new FirebaseMessaging(FirebaseInstanceId.getInstance());
            }
            firebaseMessaging = zzdn;
        }
        return firebaseMessaging;
    }

    public boolean isAutoInitEnabled() {
        return this.zzdd.zzn();
    }

    public void send(RemoteMessage remoteMessage) {
        if (TextUtils.isEmpty(remoteMessage.getTo())) {
            throw new IllegalArgumentException("Missing 'to'");
        }
        Context applicationContext = FirebaseApp.getInstance().getApplicationContext();
        Intent intent = new Intent("com.google.android.gcm.intent.SEND");
        Intent intent2 = new Intent();
        intent2.setPackage("com.google.example.invalidpackage");
        intent.putExtra("app", PendingIntent.getBroadcast(applicationContext, 0, intent2, 0));
        intent.setPackage("com.google.android.gms");
        intent.putExtras(remoteMessage.zzdp);
        applicationContext.sendOrderedBroadcast(intent, "com.google.android.gtalkservice.permission.GTALK_SERVICE");
    }

    public void setAutoInitEnabled(boolean z) {
        this.zzdd.zzb(z);
    }

    public Task<Void> subscribeToTopic(String str) {
        if (str != null && str.startsWith("/topics/")) {
            Log.w("FirebaseMessaging", "Format /topics/topic-name is deprecated. Only 'topic-name' should be used in subscribeToTopic.");
            Object substring = str.substring(8);
        }
        if (substring == null || !zzdm.matcher(substring).matches()) {
            throw new IllegalArgumentException(new StringBuilder(String.valueOf(substring).length() + 78).append("Invalid topic name: ").append(substring).append(" does not match the allowed format [a-zA-Z0-9-_.~%]{1,900}").toString());
        }
        FirebaseInstanceId firebaseInstanceId = this.zzdd;
        String valueOf = String.valueOf("S!");
        String valueOf2 = String.valueOf(substring);
        return firebaseInstanceId.zza(valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf));
    }

    public Task<Void> unsubscribeFromTopic(String str) {
        if (str != null && str.startsWith("/topics/")) {
            Log.w("FirebaseMessaging", "Format /topics/topic-name is deprecated. Only 'topic-name' should be used in unsubscribeFromTopic.");
            Object substring = str.substring(8);
        }
        if (substring == null || !zzdm.matcher(substring).matches()) {
            throw new IllegalArgumentException(new StringBuilder(String.valueOf(substring).length() + 78).append("Invalid topic name: ").append(substring).append(" does not match the allowed format [a-zA-Z0-9-_.~%]{1,900}").toString());
        }
        FirebaseInstanceId firebaseInstanceId = this.zzdd;
        String valueOf = String.valueOf("U!");
        String valueOf2 = String.valueOf(substring);
        return firebaseInstanceId.zza(valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf));
    }
}
