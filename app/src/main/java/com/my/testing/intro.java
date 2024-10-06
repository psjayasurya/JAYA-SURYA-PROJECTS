package com.my.testing;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;

public class intro extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intr);

		Intent ihome = new Intent(intro.this, MainActivity.class);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(ihome);
				finish();

			}
		}, 2000);
	}
}
