package com.mygdx.game;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.TestGame;

public class AndroidLauncher extends AndroidApplication implements AndroidHelper{
	Context context;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		context = getApplicationContext();
		initialize(new TestGame(this), config);
	}

	@Override
	public void printMessage(String title, String text) {

	}

	@Override
	public void makeToast(final String text) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
			}
		});
	}
}
