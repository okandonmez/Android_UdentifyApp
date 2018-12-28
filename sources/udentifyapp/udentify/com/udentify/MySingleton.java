package udentifyapp.udentify.com.udentify;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {
    private static Context mCtx;
    private static MySingleton mInstance;
    private RequestQueue requestQueue = getRequestQueue();

    private MySingleton(Context context) {
        mCtx = context;
    }

    public RequestQueue getRequestQueue() {
        if (this.requestQueue == null) {
            this.requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return this.requestQueue;
    }

    public static synchronized MySingleton getInstance(Context context) {
        MySingleton mySingleton;
        synchronized (MySingleton.class) {
            if (mInstance == null) {
                mInstance = new MySingleton(context);
            }
            mySingleton = mInstance;
        }
        return mySingleton;
    }

    public <T> void addToRequestqueue(Request<T> request) {
        this.requestQueue.add(request);
    }
}
