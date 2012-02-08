package com.umich.cooties;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class Meet extends Activity implements OnClickListener{
	 private SQLiteAdapter mySQLiteAdapter;
	 ListView listContent;
	 SimpleCursorAdapter cursorAdapter;
	 Cursor cursor;
	public void onCreate(Bundle savedInstanceState){

 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		Button back=(Button) findViewById(R.id.back);
	    Button Meeter=(Button) findViewById(R.id.meeting);
	    Meeter.setOnClickListener(this);
//	    back.setOnClickListener(new View.OnClickListener(){
//			public void onClick(View view){
//				Intent intent = new Intent();
//				setResult(RESULT_OK, intent);
//			}
//		});
//		Meeter.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent();
//			    intent.setClass(Meet.this,Meeting.class);
//				startActivity(intent);
//				finish();
//			}
//		});
		
	}
	public void onClick(View v) {
		
		Intent intent = new Intent(this,Meeting.class);
		startActivity(intent);
		finish();	
	}
}
