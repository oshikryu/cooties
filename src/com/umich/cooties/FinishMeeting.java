package com.umich.cooties;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FinishMeeting extends Activity{
	public void onCreate(Bundle savedInstanceState){

		 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finish_meeting);
		
		Button back=(Button) findViewById(R.id.next);
		back.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				Intent intent = new Intent(FinishMeeting.this,HaveMet.class);
				startActivity(intent);
				finish();
			}
		});
		
	}
}
