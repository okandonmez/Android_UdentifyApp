package udentifyapp.udentify.com.udentify;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat.Builder;
import com.google.android.gms.common.util.CrashUtils.ErrorDialogData;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, SplashScreen.class);
        intent.setFlags(67108864);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, ErrorDialogData.SUPPRESSED);
        Builder notificationBuilder = new Builder(this);
        notificationBuilder.setContentTitle("FCM NOTIFICATION");
        notificationBuilder.setContentText(remoteMessage.getNotification().getBody());
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSmallIcon(C0332R.drawable.appicon);
        notificationBuilder.setContentIntent(pendingIntent);
        ((NotificationManager) getSystemService("notification")).notify(0, notificationBuilder.build());
    }
}
