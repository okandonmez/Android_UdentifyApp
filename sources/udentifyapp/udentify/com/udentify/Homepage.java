package udentifyapp.udentify.com.udentify;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import java.util.HashMap;
import java.util.Map;

public class Homepage extends AppCompatActivity implements OnClickListener {
    String access_token;
    Button btnLogout;
    public final String deleteFCMIdUrl = "https://admin.udentify.co/api/Notification/DeleteFCMId";

    /* renamed from: udentifyapp.udentify.com.udentify.Homepage$1 */
    class C04201 implements Listener<String> {
        C04201() {
        }

        public void onResponse(String response) {
            Log.e("DeleteFCMResponse", response);
        }
    }

    /* renamed from: udentifyapp.udentify.com.udentify.Homepage$2 */
    class C04212 implements ErrorListener {
        C04212() {
        }

        public void onErrorResponse(VolleyError error) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0332R.layout.activity_homepage);
        this.btnLogout = (Button) findViewById(C0332R.id.btnLogout);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.access_token = extras.getString("access_token");
        }
        this.btnLogout.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (view.getId() == C0332R.id.btnLogout) {
            logoutRequest();
        }
    }

    public void logoutRequest() {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean("isSuccess", false);
        editor.apply();
        MySingleton.getInstance(getApplicationContext()).addToRequestqueue(new StringRequest(1, "https://admin.udentify.co/api/Notification/DeleteFCMId", new C04201(), new C04212()) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap();
                params.put("Id", FirebaseInstanceId.getInstance().getToken());
                return params;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap();
                params.put("Authorization", "Bearer " + Homepage.this.access_token);
                return params;
            }
        });
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}
