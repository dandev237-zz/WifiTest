package dandev237.wifitest;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

/**
 * Clase dedicada a geolocalizar al usuario empleando GPS o la red.
 * <p>
 * Autor: Daniel Castro García
 * Email: dandev237@gmail.com
 * Fecha: 15/05/2016
 */
public class GeolocationGPS extends Service {

    public static final String NETWORK = LocationManager.NETWORK_PROVIDER;
    public static final String GPS = LocationManager.GPS_PROVIDER;

    //Minimum distance to update position (meters)
    private static final long MIN_DISTANCE_UPDATE = 10;

    //Minimum time between updates (milliseconds)
    private static final long MIN_TIME_UPDATE = 1000 * 60 * 1; //1 minute

    private final Context mContext;

    private boolean isGPSEnabled, isNetworkEnabled, canGetLocation;
    private Location location;
    private double latitude, longitude;
    private LocationManager locationManager;

    public GeolocationGPS(Context context) {
        mContext = context;
        location.setLatitude(0.0);
        location.setLongitude(0.0);
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            //Get provider status
            isGPSEnabled = locationManager.isProviderEnabled(GPS);
            isNetworkEnabled = locationManager.isProviderEnabled(NETWORK);

            if (isGPSEnabled || isNetworkEnabled) {
                canGetLocation = true;
                useProvider(selectProvider());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    private String selectProvider() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        return locationManager.getBestProvider(criteria, true);
    }

    private void useProvider(String provider) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider, MIN_TIME_UPDATE, MIN_DISTANCE_UPDATE, locationListener);

        if(locationManager != null){
            location = locationManager.getLastKnownLocation(provider);
            if(location != null){
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }
    }

    /**
     * Obtiene la latitud de la posición.
     * Devuelve 0.0 si no es posible obtener la posición.
     * @return
     */
    public double getLatitude() {
        double latitude = 0.0;
        if (location != null){
            latitude = location.getLatitude();
        }
        return latitude;
    }

    /**
     * Obtiene la longitud de la posición.
     * Devuelve 0.0 si no es posible obtener la posición.
     * @return
     */
    public double getLongitude() {
        double longitude = 0.0;
        if (location != null){
            longitude = location.getLongitude();
        }
        return longitude;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * LocationListener personalizado para responder ante distintos cambios en el sistema, como
     * un cambio en la localización.
     */
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //TODO
        }

        @Override
        public void onProviderEnabled(String provider) {
            //TODO
        }

        @Override
        public void onProviderDisabled(String provider) {
            //TODO
        }
    };
}
