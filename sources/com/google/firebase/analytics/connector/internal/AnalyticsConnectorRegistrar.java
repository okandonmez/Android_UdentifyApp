package com.google.firebase.analytics.connector.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Keep;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.connector.AnalyticsConnector;
import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentRegistrar;
import com.google.firebase.components.Dependency;
import java.util.Collections;
import java.util.List;

@Keep
@KeepForSdk
public class AnalyticsConnectorRegistrar implements ComponentRegistrar {
    @Keep
    @SuppressLint({"MissingPermission"})
    @KeepForSdk
    public List<Component<?>> getComponents() {
        return Collections.singletonList(Component.builder(AnalyticsConnector.class).add(Dependency.required(FirebaseApp.class)).add(Dependency.required(Context.class)).factory(zza.zzbos).build());
    }
}
