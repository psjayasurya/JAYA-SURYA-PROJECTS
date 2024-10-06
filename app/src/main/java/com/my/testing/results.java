package com.my.testing;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.graphics.Typeface;

// Assuming keyTextView is your TextView instance

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TableLayout;
import android.widget.TableRow;

import android.app.Activity;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class results extends Activity {

	private TableLayout tableLayout;
	private TextView textview1;
	static float cigi = 0.0f;
	static float ci = 0.0f;
	static int k=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.results);
		tableLayout = findViewById(R.id.tableLayout);
		textview1 = findViewById(R.id.textview1);
		// Assuming you have a TableLayout in your XML layout file
		// Get the data passed from MainActivity
		Intent intent = getIntent();
		String result = intent.getStringExtra("json_data");

		TableRow row1 = new TableRow(this);
		TextView keyTextView1 = new TextView(this);
		TextView valueTextView1 = new TextView(this);
		TextView valueTextView2 = new TextView(this);
		TextView valueTextView3 = new TextView(this);
		TextView valueTextView4 = new TextView(this);
		TextView valueTextView5 = new TextView(this);
		TextView valueTextView6 = new TextView(this);
		TextView valueTextView7 = new TextView(this);
		valueTextView7.setText("Subject Code");

		row1.addView(valueTextView7);

		keyTextView1.setText("Subject Title");
		// Set other attributes for keyTextView as needed
		row1.addView(keyTextView1);
		valueTextView1.setText("Internal Marks(40)");
		// Set other attributes for valueTextView as needed
		row1.addView(valueTextView1);
		valueTextView2.setText("External Marks(60)");

		row1.addView(valueTextView2);

		valueTextView3.setText("Total marks(100)");

		row1.addView(valueTextView3);
		valueTextView4.setText("Grade Points");

		row1.addView(valueTextView4);
		valueTextView5.setText("Grade");

		row1.addView(valueTextView5);
		valueTextView6.setText("Credits");

		row1.addView(valueTextView6);
		valueTextView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
		valueTextView1.setPadding(15, 15, 15, 15);
		valueTextView1.setTypeface(null, Typeface.BOLD);

		valueTextView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
		valueTextView2.setPadding(15, 15, 15, 15);
		valueTextView2.setTypeface(null, Typeface.BOLD);
		keyTextView1.setPadding(15, 15, 15, 15);
		keyTextView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
		keyTextView1.setTypeface(null, Typeface.BOLD);
		keyTextView1.setTextColor(Color.BLACK);
		valueTextView1.setTextColor(Color.BLACK);
		valueTextView2.setTextColor(Color.BLACK);
		valueTextView3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
		valueTextView3.setPadding(15, 15, 15, 15);
		valueTextView3.setTypeface(null, Typeface.BOLD);
		valueTextView3.setTextColor(Color.BLACK);
		valueTextView4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
		valueTextView4.setPadding(15, 15, 15, 15);
		valueTextView4.setTypeface(null, Typeface.BOLD);
		valueTextView4.setTextColor(Color.BLACK);
		valueTextView5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
		valueTextView5.setPadding(15, 15, 15, 15);
		valueTextView5.setTypeface(null, Typeface.BOLD);
		valueTextView5.setTextColor(Color.BLACK);
		valueTextView6.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
		valueTextView6.setPadding(15, 15, 15, 15);
		valueTextView6.setTypeface(null, Typeface.BOLD);
		valueTextView6.setTextColor(Color.BLACK);
		valueTextView7.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
		valueTextView7.setPadding(15, 15, 15, 15);
		valueTextView7.setTypeface(null, Typeface.BOLD);
		valueTextView7.setTextColor(Color.BLACK);
		tableLayout.addView(row1);

		String jsonArrayString = "[" + result.replaceAll("\\}\\{", "},{") + "]";
		try {
			// Create JSONArray from the string
			JSONArray jsonArray = new JSONArray(jsonArrayString);

			// Now you can use the JSONArray as needed
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if(jsonObject.getString("grade").equals("F")){
					k++;
				}

				TableRow row = new TableRow(this);
				TextView keyTextView = new TextView(this);
				TextView valueTextView = new TextView(this);
				TextView valueTextView10 = new TextView(this);
				TextView valueTextView11 = new TextView(this);
				TextView valueTextView12 = new TextView(this);
				TextView valueTextView13 = new TextView(this);
				TextView valueTextView14 = new TextView(this);
				TextView valueTextView15 = new TextView(this);
				valueTextView11.setText(jsonObject.getString("subject_code"));
				row.addView(valueTextView11);

				keyTextView.setText(jsonObject.getString("subject_title"));
				// Set other attributes for keyTextView as needed
				row.addView(keyTextView);
				valueTextView.setText(jsonObject.getString("internal_marks"));

				row.addView(valueTextView);
				valueTextView10.setText(jsonObject.getString("external_marks"));
				//et other attributes for valueTextView as needed
				row.addView(valueTextView10);
				if (!jsonObject.getString("external_marks").equals("--")) {
					int sub_total = Integer.parseInt(jsonObject.getString("internal_marks"))
							+ Integer.parseInt(jsonObject.getString("external_marks"));
					valueTextView12.setText(Integer.toString(sub_total));
					row.addView(valueTextView12);
				} else {
					valueTextView12.setText(jsonObject.getString("internal_marks"));
					row.addView(valueTextView12);

				}

				if (!jsonObject.getString("grade").equals("F")) {
					valueTextView13.setText(jsonObject.getString("grade_points"));

					row.addView(valueTextView13);
				} else {
					valueTextView13.setText("0");
					row.addView(valueTextView13);
				}

				valueTextView14.setText(jsonObject.getString("grade"));

				row.addView(valueTextView14);

				if (!jsonObject.getString("grade").equals("F")) {
					valueTextView15.setText(jsonObject.getString("credits"));

					row.addView(valueTextView15);
				} else {
					valueTextView15.setText("0");

					row.addView(valueTextView15);

				}

				if (!jsonObject.getString("grade_points").equals("--") ) {

					cigi = cigi + (Float.parseFloat(valueTextView13.getText().toString())
							* Float.parseFloat(valueTextView15.getText().toString()));
					ci = ci + Float.parseFloat(valueTextView15.getText().toString());
				}

				valueTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
				valueTextView.setPadding(15, 15, 15, 15);
				valueTextView.setTypeface(null, Typeface.BOLD);
				valueTextView10.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
				valueTextView10.setPadding(15, 15, 15, 15);
				valueTextView10.setTypeface(null, Typeface.BOLD);
				keyTextView.setPadding(15, 15, 15, 15);
				keyTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
				keyTextView.setTypeface(null, Typeface.BOLD);
				keyTextView.setTextColor(Color.BLACK);
				valueTextView10.setTextColor(Color.BLACK);
				valueTextView.setTextColor(Color.BLACK);
				valueTextView11.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
				valueTextView11.setPadding(15, 15, 15, 15);
				valueTextView11.setTypeface(null, Typeface.BOLD);
				valueTextView11.setTextColor(Color.BLACK);
				valueTextView12.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
				valueTextView12.setPadding(15, 15, 15, 15);
				valueTextView12.setTypeface(null, Typeface.BOLD);
				valueTextView12.setTextColor(Color.BLACK);
				valueTextView13.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
				valueTextView13.setPadding(15, 15, 15, 15);
				valueTextView13.setTypeface(null, Typeface.BOLD);
				valueTextView13.setTextColor(Color.BLACK);
				valueTextView14.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
				valueTextView14.setPadding(15, 15, 15, 15);
				valueTextView14.setTypeface(null, Typeface.BOLD);
				valueTextView14.setTextColor(Color.BLACK);
				valueTextView15.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
				valueTextView15.setPadding(15, 15, 15, 15);
				valueTextView15.setTypeface(null, Typeface.BOLD);
				valueTextView15.setTextColor(Color.BLACK);
				tableLayout.addView(row);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		float sgp = cigi / ci;
		TableRow row2 = new TableRow(this);
		TextView sgpa = new TextView(this);
		sgpa.setText("SGPA:  " + sgp);
		row2.addView(sgpa);
		sgpa.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
		sgpa.setPadding(15, 15, 15, 15);
		sgpa.setTypeface(null, Typeface.BOLD);
		sgpa.setTextColor(Color.BLACK);
		tableLayout.addView(row2);

		if (k == 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Congrats 👏! You cleared all subjects");
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// Handle OK button click
				}
			});
			AlertDialog alertDialog = builder.create();
			alertDialog.show();
		} else if (k > 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("You failed in " + k + " subjects");
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// Handle OK button clic
					k=0;
				}
			});
			AlertDialog alertDialog = builder.create();
			alertDialog.show();
		}

	}
}