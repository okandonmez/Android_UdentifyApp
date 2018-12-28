package udentifyapp.udentify.com.udentify;

import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String REG_TOKEN = "REG_TOKEN";

    public void onTokenRefresh() {
        Log.e(REG_TOKEN, FirebaseInstanceId.getInstance().getToken());
    }
}
