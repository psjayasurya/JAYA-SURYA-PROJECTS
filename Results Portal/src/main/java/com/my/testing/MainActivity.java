package com.my.testing;

import android.content.Intent;
import android.os.AsyncTask;
import android.graphics.Typeface;

// Assuming keyTextView is your TextView instance

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TableLayout;
import android.widget.TableRow;

import android.app.Activity;

import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

public class MainActivity extends Activity {

	private static final String PHP_SCRIPT_URL = "https://surya00portfolio.000webhostapp.com/fetch_data.php?roll_number=";
	private EditText rollNumberEditText;
	
	private TextView textView;
	private TextView textView0;
	private TextView textView1;
	static String selectedItem;
	static String selectedItem0;
	static String selectedItem1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		rollNumberEditText = findViewById(R.id.rollNumberEditText);

		Spinner spinner = findViewById(R.id.spinner);
		textView = findViewById(R.id.textView);
		Spinner spinner1 = findViewById(R.id.spinner1);
		textView0 = findViewById(R.id.textView0);
		Spinner spinner2 = findViewById(R.id.spinner2);
		textView1 = findViewById(R.id.textView1);

		// Define an array of items
		String[] items = { "1st", "2nd", "3rd", "4th" };

		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);

		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);

		// Optional: Handle selection events
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				// Do something with the selected item
				 selectedItem = (String) parentView.getItemAtPosition(position);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// Do nothing
			}
		});
		
		String[] items0 = { "1st", "2nd" };

		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> adapter0 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items0);

		// Specify the layout to use when the list of choices appears
		adapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Apply the adapter to the spinner
		spinner1.setAdapter(adapter0);

		// Optional: Handle selection events
		spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				// Do something with the selected item
				selectedItem0 = (String) parentView.getItemAtPosition(position);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// Do nothing
			}
		});
		
		String[] items1= {"IT","MECH","CSE","ECE","CIVIL","EEE","CSM","CSD","CSC"};

		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> adapter1= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items1);

		// Specify the layout to use when the list of choices appears
		adapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Apply the adapter to the spinner
		spinner2.setAdapter(adapter1);

		// Optional: Handle selection events
		spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				// Do something with the selected item
				 selectedItem1= (String) parentView.getItemAtPosition(position);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// Do nothing
			}
		});

	}

	public void fetchData(View view) {
		String rollNumber = rollNumberEditText.getText().toString();
		String tablename=selectedItem+selectedItem0+selectedItem1;
		
		new FetchDataAsyncTask().execute(PHP_SCRIPT_URL + rollNumber+"&table_name="+tablename);
	}

	public class FetchDataAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			try {
				URL url = new URL(params[0]);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.connect();
				InputStream inputStream = conn.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				StringBuilder stringBuilder = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					stringBuilder.append(line);
				}
				bufferedReader.close();
				inputStream.close();
				return stringBuilder.toString();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Intent intent = new Intent(MainActivity.this, results.class);
			intent.putExtra("json_data", result);
			startActivity(intent);
		}
	}

}
