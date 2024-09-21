package com.my.nutricalc ;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.TextView;
import android.os.AsyncTask;




import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {
ImageView sendbtn;
private EditText editText;
private TextView textview;
private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		sendbtn=findViewById(R.id.share);
		editText = findViewById(R.id.editText);
		button = findViewById(R.id.button);
		textview = findViewById(R.id.textview);
		
		sendbtn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				Intent sendintent=new Intent();
				sendintent.setAction(Intent.ACTION_SEND);
				sendintent.setType("text/plain");
				sendintent.putExtra(Intent.EXTRA_TEXT,"Download NutriCalc APP");
				startActivity(Intent.createChooser(sendintent,"choose one"));
				
				
				
			}
		});
		button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				String food = editText.getText().toString();
		
                // Call the function or execute code when the button is clicked
                new FetchFoodDetails().execute("https://api.api-ninjas.com/v1/nutrition?query="+food);
            }
        });
    
    }
	private class FetchFoodDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return makeHttpRequest(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            displayDetails(result);
        }
    }

    private String makeHttpRequest(String urlString) {
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("accept", "application/json");
            connection.setRequestProperty("X-Api-Key", "XJjs8iO02oDchHZkeFIO5A==gENOIrMg4SDVe58Q"); // Specify JSON response

            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            reader.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    private void displayDetails(String json) {
        try {
            JSONArray detailsArray = new JSONArray(json);
            StringBuilder detailsText = new StringBuilder();

            for (int i = 0; i < detailsArray.length(); i++) {
                JSONObject jokeObject = detailsArray.getJSONObject(i);
                String name = jokeObject.getString("name");
			    detailsText.append(String.valueOf(i+1)+"."+"Name :"+name).append("\n\n");
				String calorie = jokeObject.getString("calories");
			    detailsText.append(String.valueOf(i+2)+"."+"Calories :"+calorie).append("\n\n");
				String toalfat= jokeObject.getString("fat_total_g");
			    detailsText.append(String.valueOf(i+3)+"."+"Total fat in grams :"+toalfat).append("\n\n");
				String fatsat = jokeObject.getString("fat_saturated_g");
				detailsText.append(String.valueOf(i+4)+"."+"Saturated fat in grams:"+fatsat).append("\n\n");
				String protein = jokeObject.getString("protein_g");
				detailsText.append(String.valueOf(i+5)+"."+"protein in grams :"+protein).append("\n\n");
				String sodium = jokeObject.getString("sodium_mg");
				detailsText.append(String.valueOf(i+6)+"."+"Sodium in mg :"+sodium).append("\n\n");
				String fiber = jokeObject.getString("fiber_g");
				detailsText.append(String.valueOf(i+7)+"."+"fiber in grams :"+fiber).append("\n\n");
				String sugar = jokeObject.getString("sugar_g");
				detailsText.append(String.valueOf(i+8)+"."+"Sugar in g :"+sugar).append("\n\n");
				String chol = jokeObject.getString("cholesterol_mg");
				detailsText.append(String.valueOf(i+9)+"."+"Cholestrol in mg :"+chol). append("\n\n");
				String carbo= jokeObject.getString("carbohydrates_total_g");
				detailsText.append(String.valueOf(i+10)+"."+"Carbohydrates in g :"+carbo).append("\n\n");
				String pottasium = jokeObject.getString("potassium_mg");
				detailsText.append(String.valueOf(i+11)+"."+"Pottasium in mg :"+pottasium).append("\n\n");
				
				
				
				}

            textview.setText(detailsText.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}



