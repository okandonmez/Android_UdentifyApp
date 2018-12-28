package com.android.volley.toolbox;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyLog;
import java.io.UnsupportedEncodingException;

public abstract class JsonRequest<T> extends Request<T> {
    protected static final String PROTOCOL_CHARSET = "utf-8";
    private static final String PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s", new Object[]{PROTOCOL_CHARSET});
    private Listener<T> mListener;
    private final Object mLock;
    private final String mRequestBody;

    protected abstract Response<T> parseNetworkResponse(NetworkResponse networkResponse);

    @Deprecated
    public JsonRequest(String url, String requestBody, Listener<T> listener, ErrorListener errorListener) {
        this(-1, url, requestBody, listener, errorListener);
    }

    public JsonRequest(int method, String url, String requestBody, Listener<T> listener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mLock = new Object();
        this.mListener = listener;
        this.mRequestBody = requestBody;
    }

    public void cancel() {
        super.cancel();
        synchronized (this.mLock) {
            this.mListener = null;
        }
    }

    protected void deliverResponse(T response) {
        synchronized (this.mLock) {
            Listener<T> listener = this.mListener;
        }
        if (listener != null) {
            listener.onResponse(response);
        }
    }

    @Deprecated
    public String getPostBodyContentType() {
        return getBodyContentType();
    }

    @Deprecated
    public byte[] getPostBody() {
        return getBody();
    }

    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    public byte[] getBody() {
        byte[] bArr = null;
        try {
            if (this.mRequestBody != null) {
                bArr = this.mRequestBody.getBytes(PROTOCOL_CHARSET);
            }
        } catch (UnsupportedEncodingException e) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", this.mRequestBody, PROTOCOL_CHARSET);
        }
        return bArr;
    }
}
