package com.umich.cooties;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
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
	 Button buttonAdd, buttonDeleteAll, buttonViewData, buttonViewForm,buttonMeet, buttonFinish;
	 	 
	public void onCreate(Bundle savedInstanceState){ 
		 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.have_met);
		listContent = (ListView)findViewById(R.id.contentlist);
		relContent = (ListView)findViewById(R.id.reltable);
		
		//shows the user states for each round of interactions
	    String[] from = new String[]{UserAdapter.KEY_ID, UserAdapter.FIRST_NAME, UserAdapter.LAST_NAME, 
	    		UserAdapter.SICK, UserAdapter.HAND_SICK, UserAdapter.SOURCE_SICK, UserAdapter.HAS_HIV, UserAdapter.HIV_SICK,};
	    int[] to = new int[]{R.id.id, R.id.text1, R.id.text2, R.id.text3, R.id.text4, R.id.text5, R.id.text6, R.id.text7};
	    CootiesActivity.cursorAdapter = new SimpleCursorAdapter(this, R.layout.row, CootiesActivity.cursor, from, to);
	    listContent.setAdapter(CootiesActivity.cursorAdapter);
		updateList();
		
		//shows the effects of interacting with others (relationships)
		String[] start = new String[]{RelAdapter.ROUND, RelAdapter.ID_ME, RelAdapter.PARTNER_FIRST, RelAdapter.PARTNER_LAST, 
				RelAdapter.SPREAD, RelAdapter.CONTRACT, RelAdapter.SPREAD_HIV, RelAdapter.CONTRACT_HIV};
		int[] end = new int[]{R.id.id, R.id.text1, R.id.text2, R.id.text3, R.id.text4, R.id.text5, R.id.text6, R.id.text7};
		CootiesActivity.relCursorAdapter = new SimpleCursorAdapter(this, R.layout.row, CootiesActivity.relCursor, start,  end);
		relContent.setAdapter(CootiesActivity.relCursorAdapter);
		updateRel();
		
//		//shows the effects of interacting with others (relationships)
//		String[] start = new String[]{RelAdapter.ROUND, RelAdapter.PARTNER_FIRST, RelAdapter.PARTNER_LAST};
//		int[] end = new int[]{R.id.id, R.id.text1, R.id.text2};
//		CootiesActivity.relCursorAdapter = new SimpleCursorAdapter(this, R.layout.minimal_row, CootiesActivity.relCursor, start,  end);
//		relContent.setAdapter(CootiesActivity.relCursorAdapter);
//		updateRel();
		
		
		//home button
		Button home=(Button) findViewById(R.id.home);
		home.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				   Intent intent = new Intent(HaveMet.this,CootiesActivity.class);
				   HaveMet.this.startActivity(intent);

			}
		});
		
		
		Button next=(Button) findViewById(R.id.meetdone);
		next.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				   Intent intent = new Intent(HaveMet.this,Meeting.class);
				   HaveMet.this.startActivity(intent);

			}
		});
		Button finish=(Button) findViewById(R.id.finish);
		finish.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				   Intent intent = new Intent(HaveMet.this,FinishMeeting.class);
				   HaveMet.this.startActivity(intent);

			}
		});		
		
	}
	
	private void updateRel(){
		CootiesActivity.relCursor.requery();
	}
	
	 private void updateList(){
		  CootiesActivity.cursor.requery();
		 }

}
