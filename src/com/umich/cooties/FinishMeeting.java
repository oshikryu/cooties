package com.umich.cooties;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.umich.cooties.*;

public class FinishMeeting extends Activity{
	public void onCreate(Bundle savedInstanceState){

		/*
		 *This code cancels the alarm so it won't bother you 
		 *once you complete the activity
		 */
		Intent intent = new Intent(this, RandomReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(this,
		               0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.cancel(sender);
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finish_meeting);
		
		Button back=(Button) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		Button next=(Button) findViewById(R.id.meetdone);
		next.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				Intent intent = new Intent(FinishMeeting.this,CootiesActivity.class);
				FinishMeeting.this.startActivity(intent);
			}
		});
		
		Button home=(Button) findViewById(R.id.home);
		home.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				Intent intent = new Intent(FinishMeeting.this,CootiesActivity.class);
				FinishMeeting.this.startActivity(intent);
			}
		});
	}
}
