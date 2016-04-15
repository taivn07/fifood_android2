package GPSTracker;

/**
 * Created by USER on 15/4/2016.
 */

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;

public class GPSTracker extends Service
        implements LocationListener {
    private Context context;
    private boolean isGPSEnable = false;
    private boolean isGPSTrackingEnable = false;
    private boolean isNetworkEnable = false;
    private double lat;
    private double lng;
    private Location location;
    private LocationManager locationManager;

    public GPSTracker(Context paramContext) {
        this.context = paramContext;
        getLocation();
    }

    private void updateGPS() {
        if (this.location != null) {
            this.lat = this.location.getLatitude();
            this.lng = this.location.getLongitude();
        }
    }

    public boolean getIsGPSTrackingEnabled() {
        return this.isGPSTrackingEnable;
    }

    public double getLat() {
        if (this.location != null)
            this.lat = this.location.getLatitude();
        return this.lat;
    }

    public double getLng() {
        if (this.location != null)
            this.lng = this.location.getLongitude();
        return this.lng;
    }

    private void getLocation() {
        try {
            this.locationManager = ((LocationManager) this.context.getSystemService(LOCATION_SERVICE));
            this.isGPSEnable = this.locationManager.isProviderEnabled("gps");
            this.isNetworkEnable = this.locationManager.isProviderEnabled("network");
            if ((this.isGPSEnable) || (this.isNetworkEnable)) {
                if (this.isNetworkEnable) {

                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    this.locationManager.requestLocationUpdates("network", 1000L, 1000.0F, this);
                    if (this.locationManager != null) {
                        this.location = this.locationManager.getLastKnownLocation("network");
                        if (this.location != null) {
                            this.lat = this.location.getLatitude();
                            this.lng = this.location.getLongitude();
                        }
                    }
                }
                if ((this.isGPSEnable) && (this.location == null)) {

                    this.locationManager.requestLocationUpdates("gps", 1000L, 1000.0F, this);
                    if (this.locationManager != null) {
                        this.location = this.locationManager.getLastKnownLocation("gps");
                        if (this.location != null) {
                            this.lat = this.location.getLatitude();
                            this.lng = this.location.getLongitude();
                        }
                    }
                }
            }
            return;
        } catch (Exception localException) {
            while (true)
                localException.printStackTrace();
        }
    }

    public IBinder onBind(Intent paramIntent) {
        return null;
    }

    public void onLocationChanged(Location paramLocation) {
    }

    public void onProviderDisabled(String paramString) {
    }

    public void onProviderEnabled(String paramString) {
    }

    public void onStatusChanged(String paramString, int paramInt, Bundle paramBundle) {
    }
}
