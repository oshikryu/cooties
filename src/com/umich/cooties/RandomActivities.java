package com.umich.cooties;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RandomActivities extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.washhands);
        new AlertDialog.Builder(this).setTitle("Do you want to wash your hands?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            	double washed_my_hand=VirusFunctions.wash(Meeting.my_hand);
        		CootiesActivity.mySQLiteAdapter.openToRead();
            	CootiesActivity.mySQLiteAdapter.update(Meeting.my_id, Meeting.my_sick, Meeting.my_has_hiv, Meeting.my_hiv_sick, washed_my_hand, CootiesActivity.time++, Meeting.my_source, CootiesActivity.time++);
  			  	Handler myHandler = new Handler();
  			  	myHandler.postDelayed(mMyRunnable, 10*1000);
  			  	
//           	   	Intent intent = new Intent(RandomActivities.this,Meeting.class);
//           	   	RandomActivities.this.startActivity(intent);
        }
	    })
	    .setNegativeButton("No", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface arg0, int arg1) {
	      	   Intent intent = new Intent(RandomActivities.this,HaveMet.class);
			   RandomActivities.this.startActivity(intent);
	        }
	    }).show();
	        
		Button home=(Button) findViewById(R.id.home);
		home.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				Intent intent = new Intent(RandomActivities.this,CootiesActivity.class);
				RandomActivities.this.startActivity(intent);
			}
		});
        
	}
	
//    @Override
//    protected void onPause() {
//		/*
//		 *This code cancels the alarm so it won't bother you 
//		 *once you complete the activity
//		 */
//		Intent intent = new Intent(this, RandomReceiver.class);
//		PendingIntent sender = PendingIntent.getBroadcast(this,
//		               0, intent, 0);
//		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//		alarmManager.cancel(sender);;
//    }
	   
	   private Runnable mMyRunnable = new Runnable(){
	       public void run(){
	      	   Intent intent = new Intent(RandomActivities.this,HaveMet.class);
			   RandomActivities.this.startActivity(intent);
	       }
	    };


}