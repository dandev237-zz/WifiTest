package dandev237.wifitest;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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
 * Referencia: https://developers.google.com/maps/documentation/geolocation/intro?hl=es#overview
 * <p>
 * Autor: Daniel Castro García
 * Email: dandev237@gmail.com
 * Fecha: 15/05/2016
 */

public class GeolocationHTTP {

    /**
     * Creación de una petición HTTP con un objeto JSON, y manejo de la respuesta JSON
     * correspondiente.
     *
     * @param context
     * @param wifiDataList
     * @throws JSONException
     */
    public static void postRequest(Context context, List<WifiData> wifiDataList) throws JSONException {
        String url = "https://www.googleapis.com/geolocation/v1/geolocate?key=AIzaSyB994x5GfSkq7uH46UxrxMnYgBBlvpsASQ";

        JSONObject jsonObject = createJsonObject(wifiDataList);

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //TODO: Manejar respuesta JSON
            }
        }, null);
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
}
