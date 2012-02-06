package com.umich.cooties;

import android.app.Activity;

import com.umich.cooties.SQLiteAdapter;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.content.Intent;
import android.widget.Toast;
import java.util.Random;

public class CootiesActivity extends Activity {


	 EditText inputContent1, inputContent2;
	 Button buttonAdd, buttonDeleteAll, buttonViewData, buttonViewForm,buttonMeet;
	 
	 private SQLiteAdapter mySQLiteAdapter;
	 ListView listContent;

	 SimpleCursorAdapter cursorAdapter;
	 Cursor cursor;

	   /** Called when the activity is first created. */

	   @Override

	   public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setUpForm();
	   }
	   
	   public void setUpForm() {
		   setContentView(R.layout.main);
	       inputContent1 = (EditText)findViewById(R.id.content1);
//	       inputContent2 = (EditText)findViewById(R.id.content2);
	       buttonAdd = (Button)findViewById(R.id.add);
	       buttonDeleteAll = (Button)findViewById(R.id.deleteall);
	       buttonViewData = (Button)findViewById(R.id.viewdata);
//	       buttonMeet = (Button)findViewById(R.id.meet);

	       mySQLiteAdapter = new SQLiteAdapter(this);
	       mySQLiteAdapter.openToWrite();
	       cursor = mySQLiteAdapter.queueAll();

	       buttonAdd.setOnClickListener(buttonAddOnClickListener);
	       buttonDeleteAll.setOnClickListener(buttonDeleteAllOnClickListener);
	       buttonViewData.setOnClickListener(buttonViewDataOnClickListener);
	       /*
	       buttonMeet.setOnClickListener(new View.OnClickListener(){
	    	   public void onClick(View view){
	    		   Intent request = new Intent(view.getContext(), Meet.class);
	    		   startActivityForResult(request,0);
	    	   }});   
	       */
	   }
	   public static String determineSick(){
		   Random generator = new Random();
		   Integer num = generator.nextInt(6);
		   if(num==0){
			   return "yes";
		   }
		   else{
			   return "no " /*+ num.toString()*/;
		   }
	   }

	   Button.OnClickListener buttonAddOnClickListener = new Button.OnClickListener(){

		  public void onClick(View arg0) {
			  
		   // TODO Auto-generated method stub
		   String data1 = inputContent1.getText().toString();
//		   String data2 = inputContent2.getText().toString();
		   String data2 = determineSick();
		   mySQLiteAdapter.insert(data1, data2);
		   setContentView(R.layout.list);
		      buttonViewForm = (Button)findViewById(R.id.back);
			  buttonViewForm.setOnClickListener(buttonViewFormOnClickListener);

		  	  } 
	   };

	   Button.OnClickListener buttonDeleteAllOnClickListener  = new Button.OnClickListener(){

		  public void onClick(View arg0) {
		   // TODO Auto-generated method stub
		   mySQLiteAdapter.deleteAll();
		  }

	   };

	   Button.OnClickListener buttonViewDataOnClickListener  = new Button.OnClickListener(){
		  public void onClick(View arg0) {
		   // TODO Auto-generated method stub
		  setContentView(R.layout.dataview);
		  listContent = (ListView)findViewById(R.id.contentlist);
	      String[] from = new String[]{SQLiteAdapter.KEY_ID, SQLiteAdapter.KEY_CONTENT1, SQLiteAdapter.KEY_CONTENT2};
	      int[] to = new int[]{R.id.id, R.id.text1, R.id.text2};
	      cursorAdapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.row, cursor, from, to);
	      listContent.setAdapter(cursorAdapter);
		  updateList();
	      buttonViewForm = (Button)findViewById(R.id.viewform);
		  buttonViewForm.setOnClickListener(buttonViewFormOnClickListener);
		  }

	   };
	   
	   Button.OnClickListener buttonViewFormOnClickListener  = new Button.OnClickListener(){
	  
	  public void onClick(View arg0) {
		   // TODO Auto-generated method stub
		  setUpForm();
		  }

	   };
	   
	 @Override

	 protected void onDestroy() {
	  // TODO Auto-generated method stub
	  super.onDestroy();
	  mySQLiteAdapter.close();
	 }


	 private void updateList(){
	  cursor.requery();
	   }
}