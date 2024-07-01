package com.example.httpsrequests;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private ImageView imageViewCat;
    private View main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textViewResult);
        imageViewCat = findViewById(R.id.imageViewCat);
        main = findViewById(R.id.main);
        Button btn = findViewById(R.id.buttonValidate);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCatImage();
                setCatFact();
            }
        });
    }

    private void setCatImage() {
        OkHttpClient client = new OkHttpClient();
        String url = "https://api.thecatapi.com/v1/images/search";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> Snackbar.make(main, e.toString(), Snackbar.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> Snackbar.make(main, "API request failed", Snackbar.LENGTH_LONG).show());
                } else {
                    try {
                        assert response.body() != null;
                        String jsonData = response.body().string();
                        JSONArray jsonArray = new JSONArray(jsonData);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String imageUrl = jsonObject.getString("url");

                        runOnUiThread(() -> Picasso.get().load(imageUrl).into(imageViewCat));
                    } catch (JSONException e) {
                        runOnUiThread(() -> Snackbar.make(main, "Failed to parse response", Snackbar.LENGTH_LONG).show());
                    }
                }
            }
        });
    }

    private void setCatFact() {
        OkHttpClient client = new OkHttpClient();
        String url = "https://catfact.ninja/fact";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(()->textView.setText(e.toString()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()){
                    runOnUiThread(()->textView.setText("Api request failed"));
                }else{
                    try {
                        assert response.body() != null;
                        String jsonData = response.body().string();
                        JSONObject jsonObject = new JSONObject(jsonData);
                        String fact = jsonObject.getString("fact");
                        runOnUiThread(()->textView.setText(fact));
                    } catch (JSONException e) {
                        runOnUiThread(()->textView.setText("Failed to parse response"));

                    }
                }
            }
        });
    }
}