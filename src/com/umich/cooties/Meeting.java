package com.umich.cooties;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Meeting extends Activity{
	public void onCreate(Bundle savedInstanceState){

		 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finish_meeting);
		
		Button back=(Button) findViewById(R.id.meetdone);
		back.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				Intent intent = new Intent(Meeting.this,FinishMeeting.class);
				startActivity(intent);
				finish();
			}
		});
	}

}
