package com.umich.cooties;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class Meet extends Activity{
	 private SQLiteAdapter mySQLiteAdapter;
	 ListView listContent;
	 SimpleCursorAdapter cursorAdapter;
	 Cursor cursor;
	public void onCreate(Bundle savedInstanceState){

 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		Button back=(Button) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		
	}
}
