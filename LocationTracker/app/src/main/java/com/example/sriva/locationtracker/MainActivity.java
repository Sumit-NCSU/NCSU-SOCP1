package com.example.sriva.locationtracker;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    static final int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.INTERNET, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private TextView hostField;
    private TextView nameView;
    private TextView resultView;
    private ToggleButton trackingButton;
    private LocationManager locationManager;
    private String provider;
    private LocationListener locationListener;
    private static final String appLogName = "LocationTracker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hostField = (TextView) findViewById(R.id.editText);
        nameView = (TextView) findViewById(R.id.editText2);
        resultView = (TextView) findViewById(R.id.textView);
        trackingButton = (ToggleButton) findViewById(R.id.toggleButton);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = LocationManager.NETWORK_PROVIDER;
        //This criteria below is needed to get the best provider while running on Emulator.
        //For Real Device, just use NETWORK_PROVIDER, as it provides accurate location.
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        provider = locationManager.getBestProvider(criteria, false);
        //tracking button is disabled until user enters name.
        trackingButton.setEnabled(false);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                updateLocation(location);
            }

            @Override
            public void onStatusChanged(final String provider, final int status, final Bundle extras) {
            }

            @Override
            public void onProviderEnabled(final String provider) {
                Log.i(appLogName, "provider enabled: " + provider);
            }

            @Override
            public void onProviderDisabled(final String provider) {
                Log.i(appLogName, "provider disabled: " + provider);
            }
        };
        nameView.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (String.valueOf(nameView.getText()).length() == 0) {
                    trackingButton.setEnabled(false);
                } else {
                    trackingButton.setEnabled(true);
                }
                resultView.setText("");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        resultView.setMovementMethod(new ScrollingMovementMethod());
        trackingButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String oldResult = String.valueOf(resultView.getText());
                if (isChecked) {
                    resultView.setText(oldResult.concat("\nStarted Tracking"));
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        //register the Location Listener with Location Manager
                        //Location change min 1 mt. requests every 1000 milliseconds.
                        locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);
                    }
                } else {
                    resultView.setText(oldResult.concat("\nStopped Tracking"));
                    //Un-registering Location Listener.
                    locationManager.removeUpdates(locationListener);
                }
            }
        });
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && trackingButton.isChecked()) {
            //register the Location Listener with Location Manager
            locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Un-registering Location Listener.
        locationManager.removeUpdates(locationListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Un-registering Location Listener.
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_ALL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(appLogName, "resuming...");
                    onResume();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission(s) Denied!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    public void resetResults(View view) {
        resultView.setText("");
    }

    public void updateLocation(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Log.i(appLogName, "location Changed New Location is: " + "Latitude: " + latitude + " Longitude: " + longitude);
        if (trackingButton.isChecked()) {
            String username = String.valueOf(nameView.getText());
            Log.i(appLogName,"Posting Request for: " + username + ", at Location: " + location.toString());
            //sending data to the server.
            postRequestToServer(location, username);
        }
    }

    private void postRequestToServer(Location location, String username) {
        String host = String.valueOf(hostField.getText());
        //This is used when connected with Real Device on same Wifi as the laptop.
//        host = "192.168.42.34:9000";
        // This is the server deployed on Heroku
//        host = "location-server-9x.herokuapp.com";
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + host + "/locationupdate";
        Log.i(appLogName, "The server URL is: " + url);

        final String oldResult = String.valueOf(resultView.getText());

        try {
            JSONObject request = new JSONObject();
            request.put("username", username);
            request.put("timestamp", System.currentTimeMillis());
            request.put("latitude", location.getLatitude());
            request.put("longitude", location.getLongitude());
            Log.i(appLogName, "The JSON data is: " + request.toString());
            //Json Request
            JsonObjectRequest jsonRequest = new JsonObjectRequest
                    (Request.Method.POST, url, request, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i(appLogName, "Found response: " + response);
                            try {
                                String responseUsername = response.getString("name");
                                double distance = response.getDouble("distance");
                                DecimalFormat df = new DecimalFormat("#.###");
                                resultView.setText(oldResult + "\nHello " + responseUsername + ", your total distance is: " + df.format(distance) + " Km.");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(appLogName, "Error: " + error.toString());
                            resultView.setText(oldResult + "\nCould not connect to server!");
                        }
                    });
            // Add the request to the RequestQueue.
            queue.add(jsonRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
