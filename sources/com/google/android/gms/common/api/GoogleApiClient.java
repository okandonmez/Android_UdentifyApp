package com.google.android.gms.common.api;

import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.ArrayMap;
import android.view.View;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.api.Api.AbstractClientBuilder;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.Api.AnyClientKey;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.Api.ApiOptions.HasOptions;
import com.google.android.gms.common.api.Api.ApiOptions.NotRequiredOptions;
import com.google.android.gms.common.api.Api.BaseClientBuilder;
import com.google.android.gms.common.api.Api.Client;
import com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl;
import com.google.android.gms.common.api.internal.LifecycleActivity;
import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.common.api.internal.SignInConnectionListener;
import com.google.android.gms.common.api.internal.zzav;
import com.google.android.gms.common.api.internal.zzch;
import com.google.android.gms.common.api.internal.zzi;
import com.google.android.gms.common.api.internal.zzp;
import com.google.android.gms.common.internal.AccountType;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.ClientSettings.OptionalApiSettings;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.signin.SignIn;
import com.google.android.gms.signin.SignInClient;
import com.google.android.gms.signin.SignInOptions;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.concurrent.GuardedBy;

@KeepForSdk
public abstract class GoogleApiClient {
    @KeepForSdk
    public static final String DEFAULT_ACCOUNT = "<<default account>>";
    public static final int SIGN_IN_MODE_OPTIONAL = 2;
    public static final int SIGN_IN_MODE_REQUIRED = 1;
    @GuardedBy("sAllClients")
    private static final Set<GoogleApiClient> zzcu = Collections.newSetFromMap(new WeakHashMap());

    @KeepForSdk
    public static final class Builder {
        private final Context mContext;
        private Looper zzcn;
        private final Set<Scope> zzcv;
        private final Set<Scope> zzcw;
        private int zzcx;
        private View zzcy;
        private String zzcz;
        private String zzda;
        private final Map<Api<?>, OptionalApiSettings> zzdb;
        private final Map<Api<?>, ApiOptions> zzdc;
        private LifecycleActivity zzdd;
        private int zzde;
        private OnConnectionFailedListener zzdf;
        private GoogleApiAvailability zzdg;
        private AbstractClientBuilder<? extends SignInClient, SignInOptions> zzdh;
        private final ArrayList<ConnectionCallbacks> zzdi;
        private final ArrayList<OnConnectionFailedListener> zzdj;
        private boolean zzdk;
        private Account zzs;

        @KeepForSdk
        public Builder(@NonNull Context context) {
            this.zzcv = new HashSet();
            this.zzcw = new HashSet();
            this.zzdb = new ArrayMap();
            this.zzdc = new ArrayMap();
            this.zzde = -1;
            this.zzdg = GoogleApiAvailability.getInstance();
            this.zzdh = SignIn.CLIENT_BUILDER;
            this.zzdi = new ArrayList();
            this.zzdj = new ArrayList();
            this.zzdk = false;
            this.mContext = context;
            this.zzcn = context.getMainLooper();
            this.zzcz = context.getPackageName();
            this.zzda = context.getClass().getName();
        }

        @KeepForSdk
        public Builder(@NonNull Context context, @NonNull ConnectionCallbacks connectionCallbacks, @NonNull OnConnectionFailedListener onConnectionFailedListener) {
            this(context);
            Preconditions.checkNotNull(connectionCallbacks, "Must provide a connected listener");
            this.zzdi.add(connectionCallbacks);
            Preconditions.checkNotNull(onConnectionFailedListener, "Must provide a connection failed listener");
            this.zzdj.add(onConnectionFailedListener);
        }

        private final <O extends ApiOptions> void zza(Api<O> api, O o, Scope... scopeArr) {
            Set hashSet = new HashSet(api.zzj().getImpliedScopes(o));
            for (Object add : scopeArr) {
                hashSet.add(add);
            }
            this.zzdb.put(api, new OptionalApiSettings(hashSet));
        }

        public final Builder addApi(@NonNull Api<? extends NotRequiredOptions> api) {
            Preconditions.checkNotNull(api, "Api must not be null");
            this.zzdc.put(api, null);
            Collection impliedScopes = api.zzj().getImpliedScopes(null);
            this.zzcw.addAll(impliedScopes);
            this.zzcv.addAll(impliedScopes);
            return this;
        }

        public final <O extends HasOptions> Builder addApi(@NonNull Api<O> api, @NonNull O o) {
            Preconditions.checkNotNull(api, "Api must not be null");
            Preconditions.checkNotNull(o, "Null options are not permitted for this Api");
            this.zzdc.put(api, o);
            Collection impliedScopes = api.zzj().getImpliedScopes(o);
            this.zzcw.addAll(impliedScopes);
            this.zzcv.addAll(impliedScopes);
            return this;
        }

        public final <O extends HasOptions> Builder addApiIfAvailable(@NonNull Api<O> api, @NonNull O o, Scope... scopeArr) {
            Preconditions.checkNotNull(api, "Api must not be null");
            Preconditions.checkNotNull(o, "Null options are not permitted for this Api");
            this.zzdc.put(api, o);
            zza(api, o, scopeArr);
            return this;
        }

        public final Builder addApiIfAvailable(@NonNull Api<? extends NotRequiredOptions> api, Scope... scopeArr) {
            Preconditions.checkNotNull(api, "Api must not be null");
            this.zzdc.put(api, null);
            zza(api, null, scopeArr);
            return this;
        }

        public final Builder addConnectionCallbacks(@NonNull ConnectionCallbacks connectionCallbacks) {
            Preconditions.checkNotNull(connectionCallbacks, "Listener must not be null");
            this.zzdi.add(connectionCallbacks);
            return this;
        }

        public final Builder addOnConnectionFailedListener(@NonNull OnConnectionFailedListener onConnectionFailedListener) {
            Preconditions.checkNotNull(onConnectionFailedListener, "Listener must not be null");
            this.zzdj.add(onConnectionFailedListener);
            return this;
        }

        public final Builder addScope(@NonNull Scope scope) {
            Preconditions.checkNotNull(scope, "Scope must not be null");
            this.zzcv.add(scope);
            return this;
        }

        @KeepForSdk
        public final Builder addScopeNames(String[] strArr) {
            for (String scope : strArr) {
                this.zzcv.add(new Scope(scope));
            }
            return this;
        }

        public final GoogleApiClient build() {
            Preconditions.checkArgument(!this.zzdc.isEmpty(), "must call addApi() to add at least one API");
            ClientSettings buildClientSettings = buildClientSettings();
            Api api = null;
            Map optionalApiSettings = buildClientSettings.getOptionalApiSettings();
            Map arrayMap = new ArrayMap();
            Map arrayMap2 = new ArrayMap();
            ArrayList arrayList = new ArrayList();
            Object obj = null;
            for (Api api2 : this.zzdc.keySet()) {
                Api api22;
                Object obj2 = this.zzdc.get(api22);
                boolean z = optionalApiSettings.get(api22) != null;
                arrayMap.put(api22, Boolean.valueOf(z));
                ConnectionCallbacks zzp = new zzp(api22, z);
                arrayList.add(zzp);
                BaseClientBuilder zzk = api22.zzk();
                Client buildClient = zzk.buildClient(this.mContext, this.zzcn, buildClientSettings, obj2, zzp, zzp);
                arrayMap2.put(api22.getClientKey(), buildClient);
                Object obj3 = zzk.getPriority() == 1 ? obj2 != null ? 1 : null : obj;
                if (!buildClient.providesSignIn()) {
                    api22 = api;
                } else if (api != null) {
                    String name = api22.getName();
                    String name2 = api.getName();
                    throw new IllegalStateException(new StringBuilder((String.valueOf(name).length() + 21) + String.valueOf(name2).length()).append(name).append(" cannot be used with ").append(name2).toString());
                }
                obj = obj3;
                api = api22;
            }
            if (api != null) {
                if (obj != null) {
                    name = api.getName();
                    throw new IllegalStateException(new StringBuilder(String.valueOf(name).length() + 82).append("With using ").append(name).append(", GamesOptions can only be specified within GoogleSignInOptions.Builder").toString());
                }
                Preconditions.checkState(this.zzs == null, "Must not set an account in GoogleApiClient.Builder when using %s. Set account in GoogleSignInOptions.Builder instead", api.getName());
                Preconditions.checkState(this.zzcv.equals(this.zzcw), "Must not set scopes in GoogleApiClient.Builder when using %s. Set account in GoogleSignInOptions.Builder instead.", api.getName());
            }
            GoogleApiClient zzav = new zzav(this.mContext, new ReentrantLock(), this.zzcn, buildClientSettings, this.zzdg, this.zzdh, arrayMap, this.zzdi, this.zzdj, arrayMap2, this.zzde, zzav.zza(arrayMap2.values(), true), arrayList, false);
            synchronized (GoogleApiClient.zzcu) {
                GoogleApiClient.zzcu.add(zzav);
            }
            if (this.zzde >= 0) {
                zzi.zza(this.zzdd).zza(this.zzde, zzav, this.zzdf);
            }
            return zzav;
        }

        @KeepForSdk
        @VisibleForTesting
        public final ClientSettings buildClientSettings() {
            SignInOptions signInOptions = SignInOptions.DEFAULT;
            if (this.zzdc.containsKey(SignIn.API)) {
                signInOptions = (SignInOptions) this.zzdc.get(SignIn.API);
            }
            return new ClientSettings(this.zzs, this.zzcv, this.zzdb, this.zzcx, this.zzcy, this.zzcz, this.zzda, signInOptions);
        }

        public final Builder enableAutoManage(@NonNull FragmentActivity fragmentActivity, int i, @Nullable OnConnectionFailedListener onConnectionFailedListener) {
            LifecycleActivity lifecycleActivity = new LifecycleActivity(fragmentActivity);
            Preconditions.checkArgument(i >= 0, "clientId must be non-negative");
            this.zzde = i;
            this.zzdf = onConnectionFailedListener;
            this.zzdd = lifecycleActivity;
            return this;
        }

        public final Builder enableAutoManage(@NonNull FragmentActivity fragmentActivity, @Nullable OnConnectionFailedListener onConnectionFailedListener) {
            return enableAutoManage(fragmentActivity, 0, onConnectionFailedListener);
        }

        public final Builder setAccountName(String str) {
            this.zzs = str == null ? null : new Account(str, AccountType.GOOGLE);
            return this;
        }

        public final Builder setGravityForPopups(int i) {
            this.zzcx = i;
            return this;
        }

        public final Builder setHandler(@NonNull Handler handler) {
            Preconditions.checkNotNull(handler, "Handler must not be null");
            this.zzcn = handler.getLooper();
            return this;
        }

        public final Builder setViewForPopups(@NonNull View view) {
            Preconditions.checkNotNull(view, "View must not be null");
            this.zzcy = view;
            return this;
        }

        public final Builder useDefaultAccount() {
            return setAccountName("<<default account>>");
        }
    }

    public interface ConnectionCallbacks {
        public static final int CAUSE_NETWORK_LOST = 2;
        public static final int CAUSE_SERVICE_DISCONNECTED = 1;

        void onConnected(@Nullable Bundle bundle);

        void onConnectionSuspended(int i);
    }

    public interface OnConnectionFailedListener {
        void onConnectionFailed(@NonNull ConnectionResult connectionResult);
    }

    public static void dumpAll(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        synchronized (zzcu) {
            String concat = String.valueOf(str).concat("  ");
            int i = 0;
            for (GoogleApiClient googleApiClient : zzcu) {
                int i2 = i + 1;
                printWriter.append(str).append("GoogleApiClient#").println(i);
                googleApiClient.dump(concat, fileDescriptor, printWriter, strArr);
                i = i2;
            }
        }
    }

    @KeepForSdk
    public static Set<GoogleApiClient> getAllClients() {
        Set<GoogleApiClient> set;
        synchronized (zzcu) {
            set = zzcu;
        }
        return set;
    }

    public abstract ConnectionResult blockingConnect();

    public abstract ConnectionResult blockingConnect(long j, @NonNull TimeUnit timeUnit);

    public abstract PendingResult<Status> clearDefaultAccountAndReconnect();

    public abstract void connect();

    public void connect(int i) {
        throw new UnsupportedOperationException();
    }

    public abstract void disconnect();

    public abstract void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr);

    @KeepForSdk
    public <A extends AnyClient, R extends Result, T extends ApiMethodImpl<R, A>> T enqueue(@NonNull T t) {
        throw new UnsupportedOperationException();
    }

    @KeepForSdk
    public <A extends AnyClient, T extends ApiMethodImpl<? extends Result, A>> T execute(@NonNull T t) {
        throw new UnsupportedOperationException();
    }

    @KeepForSdk
    @NonNull
    public <C extends Client> C getClient(@NonNull AnyClientKey<C> anyClientKey) {
        throw new UnsupportedOperationException();
    }

    @NonNull
    public abstract ConnectionResult getConnectionResult(@NonNull Api<?> api);

    @KeepForSdk
    public Context getContext() {
        throw new UnsupportedOperationException();
    }

    @KeepForSdk
    public Looper getLooper() {
        throw new UnsupportedOperationException();
    }

    @KeepForSdk
    public boolean hasApi(@NonNull Api<?> api) {
        throw new UnsupportedOperationException();
    }

    public abstract boolean hasConnectedApi(@NonNull Api<?> api);

    public abstract boolean isConnected();

    public abstract boolean isConnecting();

    public abstract boolean isConnectionCallbacksRegistered(@NonNull ConnectionCallbacks connectionCallbacks);

    public abstract boolean isConnectionFailedListenerRegistered(@NonNull OnConnectionFailedListener onConnectionFailedListener);

    @KeepForSdk
    public boolean maybeSignIn(SignInConnectionListener signInConnectionListener) {
        throw new UnsupportedOperationException();
    }

    @KeepForSdk
    public void maybeSignOut() {
        throw new UnsupportedOperationException();
    }

    public abstract void reconnect();

    public abstract void registerConnectionCallbacks(@NonNull ConnectionCallbacks connectionCallbacks);

    public abstract void registerConnectionFailedListener(@NonNull OnConnectionFailedListener onConnectionFailedListener);

    @KeepForSdk
    public <L> ListenerHolder<L> registerListener(@NonNull L l) {
        throw new UnsupportedOperationException();
    }

    public abstract void stopAutoManage(@NonNull FragmentActivity fragmentActivity);

    public abstract void unregisterConnectionCallbacks(@NonNull ConnectionCallbacks connectionCallbacks);

    public abstract void unregisterConnectionFailedListener(@NonNull OnConnectionFailedListener onConnectionFailedListener);

    public void zza(zzch zzch) {
        throw new UnsupportedOperationException();
    }

    public void zzb(zzch zzch) {
        throw new UnsupportedOperationException();
    }
}
