package udentifyapp.udentify.com.udentify;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements OnClickListener {
    private static final String loginRequestURL = "https://admin.udentify.co/Token";
    Button btnLogin;
    EditText edtPassword;
    EditText edtUsername;
    public String fcmIdRequestUrl = "https://admin.udentify.co/api/Notification/AddFCMId";
    String fcm_token;
    ProgressBar pbLogin;
    TextInputLayout txtUsernameLayout;
    TextView txtWebLink;

    /* renamed from: udentifyapp.udentify.com.udentify.LoginActivity$2 */
    class C04232 implements ErrorListener {
        C04232() {
        }

        public void onErrorResponse(VolleyError error) {
            LoginActivity.this.pbLogin.setVisibility(4);
            Toast.makeText(LoginActivity.this.getApplicationContext(), error.toString(), 1).show();
        }
    }

    /* renamed from: udentifyapp.udentify.com.udentify.LoginActivity$5 */
    class C04255 implements ErrorListener {
        C04255() {
        }

        public void onErrorResponse(VolleyError error) {
            LoginActivity.this.pbLogin.setVisibility(4);
        }
    }

    @RequiresApi(api = 21)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0332R.layout.activity_login);
        setStatusBarColor();
        this.txtWebLink = (TextView) findViewById(C0332R.id.txtWebLink);
        this.txtWebLink.setOnClickListener(this);
        this.txtUsernameLayout = (TextInputLayout) findViewById(C0332R.id.UsernameLayout);
        this.pbLogin = (ProgressBar) findViewById(C0332R.id.pbLogin);
        setProgressBar();
        this.edtUsername = (EditText) findViewById(C0332R.id.edtUsername);
        this.edtPassword = (EditText) findViewById(C0332R.id.edtPassword);
        this.btnLogin = (Button) findViewById(C0332R.id.btnLogin);
        this.btnLogin.setOnClickListener(this);
        this.fcm_token = FirebaseInstanceId.getInstance().getToken();
    }

    @SuppressLint({"ResourceAsColor"})
    public void setProgressBar() {
    }

    @RequiresApi(api = 21)
    private void setStatusBarColor() {
        Window window = getWindow();
        window.clearFlags(67108864);
        window.addFlags(Integer.MIN_VALUE);
        window.setStatusBarColor(ContextCompat.getColor(this, C0332R.color.loginStatusBarColor));
    }

    public void onClick(View view) {
        if (view.getId() == C0332R.id.btnLogin) {
            loginRequest();
        }
    }

    private void loginRequest() {
        Log.e("btnClick", "Login Request");
        final String username = this.edtUsername.getText().toString();
        final String password = this.edtPassword.getText().toString();
        if (username.length() == 0 || password.length() == 0) {
            if (username.length() == 0) {
                this.edtUsername.setError("Bu alan boş bırakılamaz");
            }
            if (password.length() == 0) {
                this.edtPassword.setError("Bu alan boş bırakılamaz");
                return;
            }
            return;
        }
        this.pbLogin.setVisibility(0);
        Volley.newRequestQueue(this).add(new StringRequest(1, loginRequestURL, new Listener<String>() {
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String access_token = jsonObject.getString("access_token");
                    if (jsonObject.getString("access_token") != null) {
                        Log.e("Response", "Login Succesful");
                        Log.e("Login Token", access_token);
                        LoginActivity.this.sendFCMId(access_token, username, password);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new C04232()) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap();
                params.put("username", username);
                params.put("password", password);
                params.put("grant_type", "password");
                return params;
            }
        });
    }

    public void sendFCMId(final String access_token, final String username, final String password) throws JSONException {
        final String bearer = "Bearer " + access_token;
        Log.e("bearer", bearer);
        MySingleton.getInstance(this).addToRequestqueue(new StringRequest(1, this.fcmIdRequestUrl, new Listener<String>() {
            public void onResponse(String response) {
                try {
                    if (new JSONObject(response).getString("Success").equals("true")) {
                        LoginActivity.this.pbLogin.setVisibility(4);
                        Intent intent = new Intent(LoginActivity.this.getApplicationContext(), Homepage.class);
                        intent.putExtra("access_token", access_token);
                        LoginActivity.this.startActivity(intent);
                        LoginActivity.this.saveSettings(username, password);
                        LoginActivity.this.finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new C04255()) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap();
                params.put("Id", LoginActivity.this.fcm_token);
                return params;
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap();
                params.put("Authorization", bearer);
                return params;
            }
        });
    }

    public void saveSettings(String username, String password) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean("isSuccess", true);
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
    }
}
