package udentifyapp.udentify.com.udentify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class SplashScreen extends AppCompatActivity {
    private static final String loginRequestURL = "https://admin.udentify.co/Token";

    /* renamed from: udentifyapp.udentify.com.udentify.SplashScreen$1 */
    class C03331 implements Runnable {
        C03331() {
        }

        public void run() {
            SplashScreen.this.startActivity(new Intent(SplashScreen.this, LoginActivity.class));
            SplashScreen.this.finish();
        }
    }

    /* renamed from: udentifyapp.udentify.com.udentify.SplashScreen$2 */
    class C04262 implements Listener<String> {
        C04262() {
        }

        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String access_token = jsonObject.getString("access_token");
                if (jsonObject.getString("access_token") != null) {
                    Log.e("Response", "Login Succesful");
                    Log.e("Login Token", access_token);
                    Intent intent = new Intent(SplashScreen.this.getApplicationContext(), Homepage.class);
                    intent.putExtra("access_token", access_token);
                    SplashScreen.this.startActivity(intent);
                    SplashScreen.this.finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: udentifyapp.udentify.com.udentify.SplashScreen$3 */
    class C04273 implements ErrorListener {
        C04273() {
        }

        public void onErrorResponse(VolleyError error) {
            Toast.makeText(SplashScreen.this.getApplicationContext(), error.toString(), 1).show();
        }
    }

    @RequiresApi(api = 21)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0332R.layout.activity_splash_screen);
        if (!getSettings()) {
            setSplashing(ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED);
        }
    }

    public void setSplashing(int time) {
        new Handler().postDelayed(new C03331(), (long) time);
    }

    public boolean getSettings() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!preferences.getBoolean("isSuccess", false)) {
            return false;
        }
        loginRequest(preferences.getString("username", "username"), preferences.getString("password", "password"));
        return true;
    }

    public void loginRequest(String username, String password) {
        final String str = username;
        final String str2 = password;
        Volley.newRequestQueue(this).add(new StringRequest(1, loginRequestURL, new C04262(), new C04273()) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap();
                params.put("username", str);
                params.put("password", str2);
                params.put("grant_type", "password");
                return params;
            }
        });
    }
}
