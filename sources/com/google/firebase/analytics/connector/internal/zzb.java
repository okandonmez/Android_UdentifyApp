package com.google.firebase.analytics.connector.internal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.internal.measurement.zzkc;
import com.google.android.gms.measurement.AppMeasurement;
import com.google.android.gms.measurement.AppMeasurement.UserProperty;
import com.google.firebase.analytics.FirebaseAnalytics.Event;
import com.google.firebase.analytics.connector.AnalyticsConnector.ConditionalUserProperty;
import java.util.Arrays;
import java.util.List;

public final class zzb {
    private static final List<String> zzbot = Arrays.asList(new String[]{"_e", "_f", "_iap", "_s", "_au", "_ui", "_cd", Event.APP_OPEN});
    private static final List<String> zzbou = Arrays.asList(new String[]{"auto", "app", "am"});
    private static final List<String> zzbov = Arrays.asList(new String[]{"_r", "_dbg"});
    private static final List<String> zzbow = Arrays.asList((String[]) ArrayUtils.concat(UserProperty.zzadb, UserProperty.zzadc));
    private static final List<String> zzbox = Arrays.asList(new String[]{"^_ltv_[A-Z]{3}$", "^_cc[1-5]{1}$"});

    public static boolean zza(ConditionalUserProperty conditionalUserProperty) {
        if (conditionalUserProperty == null) {
            return false;
        }
        String str = conditionalUserProperty.origin;
        return (str == null || str.isEmpty()) ? false : ((conditionalUserProperty.value == null || zzkc.zzf(conditionalUserProperty.value) != null) && zzfb(str) && zzfc(conditionalUserProperty.name)) ? ((!conditionalUserProperty.name.equals("_ce1") && !conditionalUserProperty.name.equals("_ce2")) || str.equals(AppMeasurement.FCM_ORIGIN) || str.equals("frc")) ? (conditionalUserProperty.expiredEventName == null || (zza(conditionalUserProperty.expiredEventName, conditionalUserProperty.expiredEventParams) && zzb(str, conditionalUserProperty.expiredEventName, conditionalUserProperty.expiredEventParams))) ? (conditionalUserProperty.triggeredEventName == null || (zza(conditionalUserProperty.triggeredEventName, conditionalUserProperty.triggeredEventParams) && zzb(str, conditionalUserProperty.triggeredEventName, conditionalUserProperty.triggeredEventParams))) ? conditionalUserProperty.timedOutEventName == null || (zza(conditionalUserProperty.timedOutEventName, conditionalUserProperty.timedOutEventParams) && zzb(str, conditionalUserProperty.timedOutEventName, conditionalUserProperty.timedOutEventParams)) : false : false : false : false;
    }

    public static boolean zza(@NonNull String str, @Nullable Bundle bundle) {
        if (zzbot.contains(str)) {
            return false;
        }
        if (bundle != null) {
            for (String containsKey : zzbov) {
                if (bundle.containsKey(containsKey)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static AppMeasurement.ConditionalUserProperty zzb(ConditionalUserProperty conditionalUserProperty) {
        AppMeasurement.ConditionalUserProperty conditionalUserProperty2 = new AppMeasurement.ConditionalUserProperty();
        conditionalUserProperty2.mOrigin = conditionalUserProperty.origin;
        conditionalUserProperty2.mActive = conditionalUserProperty.active;
        conditionalUserProperty2.mCreationTimestamp = conditionalUserProperty.creationTimestamp;
        conditionalUserProperty2.mExpiredEventName = conditionalUserProperty.expiredEventName;
        if (conditionalUserProperty.expiredEventParams != null) {
            conditionalUserProperty2.mExpiredEventParams = new Bundle(conditionalUserProperty.expiredEventParams);
        }
        conditionalUserProperty2.mName = conditionalUserProperty.name;
        conditionalUserProperty2.mTimedOutEventName = conditionalUserProperty.timedOutEventName;
        if (conditionalUserProperty.timedOutEventParams != null) {
            conditionalUserProperty2.mTimedOutEventParams = new Bundle(conditionalUserProperty.timedOutEventParams);
        }
        conditionalUserProperty2.mTimeToLive = conditionalUserProperty.timeToLive;
        conditionalUserProperty2.mTriggeredEventName = conditionalUserProperty.triggeredEventName;
        if (conditionalUserProperty.triggeredEventParams != null) {
            conditionalUserProperty2.mTriggeredEventParams = new Bundle(conditionalUserProperty.triggeredEventParams);
        }
        conditionalUserProperty2.mTriggeredTimestamp = conditionalUserProperty.triggeredTimestamp;
        conditionalUserProperty2.mTriggerEventName = conditionalUserProperty.triggerEventName;
        conditionalUserProperty2.mTriggerTimeout = conditionalUserProperty.triggerTimeout;
        if (conditionalUserProperty.value != null) {
            conditionalUserProperty2.mValue = zzkc.zzf(conditionalUserProperty.value);
        }
        return conditionalUserProperty2;
    }

    public static boolean zzb(@NonNull String str, @NonNull String str2, @Nullable Bundle bundle) {
        if (!"_cmp".equals(str2)) {
            return true;
        }
        if (!zzfb(str)) {
            return false;
        }
        if (bundle == null) {
            return false;
        }
        for (String containsKey : zzbov) {
            if (bundle.containsKey(containsKey)) {
                return false;
            }
        }
        Object obj = -1;
        switch (str.hashCode()) {
            case 101200:
                if (str.equals(AppMeasurement.FCM_ORIGIN)) {
                    obj = null;
                    break;
                }
                break;
            case 101230:
                if (str.equals("fdl")) {
                    int i = 1;
                    break;
                }
                break;
        }
        switch (obj) {
            case null:
                bundle.putString("_cis", "fcm_integration");
                return true;
            case 1:
                bundle.putString("_cis", "fdl_integration");
                return true;
            default:
                return false;
        }
    }

    public static ConditionalUserProperty zzd(AppMeasurement.ConditionalUserProperty conditionalUserProperty) {
        ConditionalUserProperty conditionalUserProperty2 = new ConditionalUserProperty();
        conditionalUserProperty2.origin = conditionalUserProperty.mOrigin;
        conditionalUserProperty2.active = conditionalUserProperty.mActive;
        conditionalUserProperty2.creationTimestamp = conditionalUserProperty.mCreationTimestamp;
        conditionalUserProperty2.expiredEventName = conditionalUserProperty.mExpiredEventName;
        if (conditionalUserProperty.mExpiredEventParams != null) {
            conditionalUserProperty2.expiredEventParams = new Bundle(conditionalUserProperty.mExpiredEventParams);
        }
        conditionalUserProperty2.name = conditionalUserProperty.mName;
        conditionalUserProperty2.timedOutEventName = conditionalUserProperty.mTimedOutEventName;
        if (conditionalUserProperty.mTimedOutEventParams != null) {
            conditionalUserProperty2.timedOutEventParams = new Bundle(conditionalUserProperty.mTimedOutEventParams);
        }
        conditionalUserProperty2.timeToLive = conditionalUserProperty.mTimeToLive;
        conditionalUserProperty2.triggeredEventName = conditionalUserProperty.mTriggeredEventName;
        if (conditionalUserProperty.mTriggeredEventParams != null) {
            conditionalUserProperty2.triggeredEventParams = new Bundle(conditionalUserProperty.mTriggeredEventParams);
        }
        conditionalUserProperty2.triggeredTimestamp = conditionalUserProperty.mTriggeredTimestamp;
        conditionalUserProperty2.triggerEventName = conditionalUserProperty.mTriggerEventName;
        conditionalUserProperty2.triggerTimeout = conditionalUserProperty.mTriggerTimeout;
        if (conditionalUserProperty.mValue != null) {
            conditionalUserProperty2.value = zzkc.zzf(conditionalUserProperty.mValue);
        }
        return conditionalUserProperty2;
    }

    public static boolean zzfb(@NonNull String str) {
        return !zzbou.contains(str);
    }

    public static boolean zzfc(@NonNull String str) {
        if (zzbow.contains(str)) {
            return false;
        }
        for (String matches : zzbox) {
            if (str.matches(matches)) {
                return false;
            }
        }
        return true;
    }
}
