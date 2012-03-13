package com.umich.cooties;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.umich.cooties.*;

public class FinishMeeting extends Activity{
	public void onCreate(Bundle savedInstanceState){

		 
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
				Intent intent = new Intent(FinishMeeting.this,HaveMet.class);
				FinishMeeting.this.startActivity(intent);
			}
		});
	}
}
