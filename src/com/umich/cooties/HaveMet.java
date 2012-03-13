package com.umich.cooties;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

public class HaveMet extends Activity{
	 ListView listContent, relContent;
	 Button buttonAdd, buttonDeleteAll, buttonViewData, buttonViewForm,buttonMeet;
	 	 
	public void onCreate(Bundle savedInstanceState){

		 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.have_met);
		listContent = (ListView)findViewById(R.id.contentlist);
		relContent = (ListView)findViewById(R.id.reltable);
		
		//eventually want to show relationship table interactions
	    String[] from = new String[]{UserAdapter.KEY_ID, UserAdapter.FIRST_NAME, UserAdapter.LAST_NAME, 
	    		UserAdapter.SICK, UserAdapter.HAND_SICK, UserAdapter.NOSE_SICK};
	    int[] to = new int[]{R.id.id, R.id.text1, R.id.text2, R.id.text3, R.id.text4, R.id.text5};
	    CootiesActivity.cursorAdapter = new SimpleCursorAdapter(this, R.layout.row, CootiesActivity.cursor, from, to);
	    listContent.setAdapter(CootiesActivity.cursorAdapter);
		updateList();
		
		String[] start = new String[]{RelAdapter.ROUND, RelAdapter.ID_ME, RelAdapter.PARTNER_FIRST, RelAdapter.PARTNER_LAST, 
				RelAdapter.SPREAD, RelAdapter.CONTRACT};
		int[] end = new int[]{R.id.id, R.id.text1, R.id.text2, R.id.text3, R.id.text4, R.id.text5};
		Meeting.relCursorAdapter = new SimpleCursorAdapter(this, R.layout.row, Meeting.relCursor, start,  end);
		relContent.setAdapter(Meeting.relCursorAdapter);
		updateRel();
				
		Button next=(Button) findViewById(R.id.meetdone);
		next.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				   Intent intent = new Intent(HaveMet.this,Meeting.class);
				   HaveMet.this.startActivity(intent);

			}
		});
		
		
	}
	
	private void updateRel(){
		Meeting.relCursor.requery();
	}
	
	 private void updateList(){
		  CootiesActivity.cursor.requery();
		 }

}
