package dandev237.wifitest;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Clase que tiene como objetivo la geolocalización del usuario empleando la Google Maps Geolocation
 * API (geolocalización mediante petición HTTP empleando información sobre los puntos de
 * acceso detectados).
 * Esta clase hace uso del framework Volley.
 *
 * Referencia: https://developers.google.com/maps/documentation/geolocation/intro?hl=es#overview
 * <p>
 * Autor: Daniel Castro García
 * Email: dandev237@gmail.com
 * Fecha: 15/05/2016
 */

public class GeolocationHTTP {

    private static double latitude, longitude;

    //No haremos una clase singleton puesto que es una prueba, pero para cuando se espera que esta funcionalidad
    //esté activa durante toda la ejecución de la app, lo mejor es hacer una clase singleton que maneje la cola.
    private static RequestQueue requestQueue;
    private static Cache cache;
    private static Network network;
    private static Context appContext;

    public GeolocationHTTP(Context appContext){
        latitude = 0.0;
        longitude = 0.0;
        GeolocationHTTP.appContext = appContext;
    }

    /**
     * Creación de una petición HTTP con un objeto JSON, y manejo de la respuesta JSON
     * correspondiente.
     *
     * @param context
     * @param wifiDataList
     * @throws JSONException
     */
    public static void postRequest(Context context, List<WifiData> wifiDataList) throws JSONException {
        prepareRequestQueue();
        String url = "https://www.googleapis.com/geolocation/v1/geolocate?key=AIzaSyB994x5GfSkq7uH46UxrxMnYgBBlvpsASQ";

        JSONObject jsonObject = createJsonObject(wifiDataList);

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Solo extraemos latitud y longitud
                try {
                    JSONObject location = response.getJSONObject("location");
                    latitude = location.getDouble("lat");
                    longitude = location.getDouble("lng");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);

        requestQueue.add(jsonRequest);
    }

    /**
     * Método para preparar la cola de peticiones.
     * Referencia: https://developer.android.com/training/volley/requestqueue.html
     */
    private static void prepareRequestQueue() {
        //Instanciar la caché
        cache = new DiskBasedCache(appContext.getCacheDir(), 1024 * 1024); // 1 MB capacidad

        //Preparar la red para usar HttpUrlConnection como el cliente HTTP
        network = new BasicNetwork(new HurlStack());

        //Instanciar la cola con la caché y la red
        requestQueue = new RequestQueue(cache, network);

        requestQueue.start();
    }

    /**
     * Método para crear un objeto JSON con el formato apropiado para la petición HTTP.
     * (Consultar referencia)
     * @param wifiDataList
     * @return
     * @throws JSONException
     */
    @NonNull
    private static JSONObject createJsonObject(List<WifiData> wifiDataList) throws JSONException {
        JSONArray wifiAccessPoints = new JSONArray();
        for(WifiData wd: wifiDataList){
            //Crear un objeto JSON por cada WifiData
            wifiAccessPoints.put(DataToJSONArray(wd));
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("homeMobileCountryCode", 214);
        jsonObject.put("homeMibleNetworkCode", 03);
        jsonObject.put("radioType", "lte");
        jsonObject.put("carrier", "Orange");
        jsonObject.put("considerIp", "true");
        jsonObject.put("wifiAccessPoints", wifiAccessPoints);
        return jsonObject;
    }

    /**
     * Método auxiliar para convertir un objeto WifiData a un array JSON que incluir en el
     * objeto JSON de la petición HTTP.
     * @param wifiData
     * @return
     * @throws JSONException
     */
    private static JSONObject DataToJSONArray(WifiData wifiData) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("macAddress", wifiData.getBssid());
        object.put("signalStrength", wifiData.getLevel());

        return object;
    }

    //Getters

    public static double getLatitude() {
        return latitude;
    }

    public static double getLongitude() {
        return longitude;
    }
}
