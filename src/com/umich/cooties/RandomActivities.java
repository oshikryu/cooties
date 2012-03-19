package com.umich.cooties;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class RandomActivities extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.washhands);
        new AlertDialog.Builder(this).setTitle("Do you want to wash your hands?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            	VirusFunctions.wash(Meeting.my_hand);
  			  	Handler myHandler = new Handler();
  			  	myHandler.postDelayed(mMyRunnable, 10*1000);
  			  	
//           	   	Intent intent = new Intent(RandomActivities.this,Meeting.class);
//           	   	RandomActivities.this.startActivity(intent);
        }
    })
    .setNegativeButton("No", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface arg0, int arg1) {
      	   Intent intent = new Intent(RandomActivities.this,Meeting.class);
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
	   
	   private Runnable mMyRunnable = new Runnable(){
	       public void run(){
	      	   Intent intent = new Intent(RandomActivities.this,Meeting.class);
			   RandomActivities.this.startActivity(intent);
	       }
	    };


}