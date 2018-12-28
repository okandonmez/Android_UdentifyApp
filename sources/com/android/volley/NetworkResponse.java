package com.android.volley;

import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class NetworkResponse {
    public final List<Header> allHeaders;
    public final byte[] data;
    public final Map<String, String> headers;
    public final long networkTimeMs;
    public final boolean notModified;
    public final int statusCode;

    @Deprecated
    public NetworkResponse(int statusCode, byte[] data, Map<String, String> headers, boolean notModified, long networkTimeMs) {
        this(statusCode, data, headers, toAllHeaderList(headers), notModified, networkTimeMs);
    }

    public NetworkResponse(int statusCode, byte[] data, boolean notModified, long networkTimeMs, List<Header> allHeaders) {
        this(statusCode, data, toHeaderMap(allHeaders), allHeaders, notModified, networkTimeMs);
    }

    @Deprecated
    public NetworkResponse(int statusCode, byte[] data, Map<String, String> headers, boolean notModified) {
        this(statusCode, data, (Map) headers, notModified, 0);
    }

    public NetworkResponse(byte[] data) {
        this((int) Callback.DEFAULT_DRAG_ANIMATION_DURATION, data, false, 0, Collections.emptyList());
    }

    @Deprecated
    public NetworkResponse(byte[] data, Map<String, String> headers) {
        this((int) Callback.DEFAULT_DRAG_ANIMATION_DURATION, data, (Map) headers, false, 0);
    }

    private NetworkResponse(int statusCode, byte[] data, Map<String, String> headers, List<Header> allHeaders, boolean notModified, long networkTimeMs) {
        this.statusCode = statusCode;
        this.data = data;
        this.headers = headers;
        if (allHeaders == null) {
            this.allHeaders = null;
        } else {
            this.allHeaders = Collections.unmodifiableList(allHeaders);
        }
        this.notModified = notModified;
        this.networkTimeMs = networkTimeMs;
    }

    private static Map<String, String> toHeaderMap(List<Header> allHeaders) {
        if (allHeaders == null) {
            return null;
        }
        if (allHeaders.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, String> headers = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        for (Header header : allHeaders) {
            headers.put(header.getName(), header.getValue());
        }
        return headers;
    }

    private static List<Header> toAllHeaderList(Map<String, String> headers) {
        if (headers == null) {
            return null;
        }
        if (headers.isEmpty()) {
            return Collections.emptyList();
        }
        List<Header> allHeaders = new ArrayList(headers.size());
        for (Entry<String, String> header : headers.entrySet()) {
            allHeaders.add(new Header((String) header.getKey(), (String) header.getValue()));
        }
        return allHeaders;
    }
}
