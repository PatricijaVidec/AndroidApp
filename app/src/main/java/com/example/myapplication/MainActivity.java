package com.example.myapplication;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private TextView users; //uporabniki
    private String url = "https://smartpark-ezdre8cwche5dxhr.swedencentral-01.azurewebsites.net/api/v1/user" ; // spremeni na svoje

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        users = (TextView) findViewById(R.id.users); // pri textview

    }

    public void showUsers(View view) {
        if (view != null) {
            JsonArrayRequest request = new JsonArrayRequest(url, jsonArrayListener, errorListener)
            {
              @Override
              public Map<String, String> getHeaders() throws AuthFailureError
              {
                  Map<String, String> params = new HashMap<String, String>();
                  params.put("ApiKey", "SecretKey");
                  return params;
              }
            };
            requestQueue.add(request);
        }
    }
    private Response.Listener<JSONArray> jsonArrayListener = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response){
            ArrayList<String> data = new ArrayList<>();

            for (int i = 0; i < response.length(); i++){
                try {
                    JSONObject object =response.getJSONObject(i);
                    String name = object.getString("firstName");
                    String surname = object.getString("lastName");
                    String reservation = object.getString("reservations");

                    if (reservation.equals("null")) {
                        reservation = "doesn't have any reservations yet";
                    }

                    data.add(name + " " + surname + "  Reservations: " + reservation);

                } catch (JSONException e){
                    e.printStackTrace();
                    return;

                }
            }

            users.setText("");


            for (String row: data){
                String currentText = users.getText().toString();
                users.setText(currentText + "\n\n" + row);
            }

        }

    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("REST error", error.getMessage());
        }
    };
    public static final String EXTRA_MESSAGE = "com.example.myapplication.MESSAGE";

    public void addUserActivity (View view) {
        Intent intent = new Intent(this,addUser.class);
        String message = "Add a user.";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

}