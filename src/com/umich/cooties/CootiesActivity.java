package com.umich.cooties;

import java.util.Calendar;
import java.util.Random;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class CootiesActivity extends Activity {


	 EditText inputContent1, inputContent2;
	 Button buttonAdd, buttonDeleteAll, back,buttonMeet;
	 
     static protected UserAdapter mySQLiteAdapter;
	 static protected SimpleCursorAdapter cursorAdapter;
	 static protected Cursor cursor;
	 public static  Integer time;

	   /** Called when the activity is first created. */

	   @Override

	   public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setUpForm();
	   }
	   
	   public void setUpForm() {
		   setContentView(R.layout.main);
	       inputContent1 = (EditText)findViewById(R.id.first);
	       inputContent2 = (EditText)findViewById(R.id.last);
	       buttonAdd = (Button)findViewById(R.id.add);
	       mySQLiteAdapter = new UserAdapter(this);
	       mySQLiteAdapter.openToWrite();
	       cursor = mySQLiteAdapter.queueAll();
	       buttonAdd.setOnClickListener(buttonAddOnClickListener);
	       
	   }
	   
	   //initialize user sickness boolean
	   public static int determineSick(){
		   Random generator = new Random();
		   Integer num = generator.nextInt(6);
		   if(num.equals(0)){
			   //switch these values for testing
			   return 1;
		   }
		   else{
			   return 0 /*+ num.toString()*/;
		   }
	   }
	   
	   //initialize user hand sickness
	   public  int determineHand(Integer sick){
		   Integer hand_sick=0;
		   Random generator = new Random();
		   Integer num = generator.nextInt(100);
		   if(sick.equals(1)){
			   hand_sick=num;
		   } 
		   return hand_sick;
	   }   
	   
	   //initialize and SYNCHRONIZE hand_sick and nose_sick time
	   public  int determineSickTime(Integer sick){
		   Integer time=0;
		   if(sick.equals(1)){
			   time=1;
		   }
		   return time;
	   }
	   
	   
	   //initialize user nose sickness
	   public  int determineNose(Integer sick){
		   Integer nose_sick=0;
		   Random generator = new Random();
		   Integer num = generator.nextInt(100);
		   if(sick.equals(1)){
			   nose_sick=num;
		   } 
		   return nose_sick;
	   }


	   
	   Button.OnClickListener buttonAddOnClickListener = new Button.OnClickListener(){

		  public void onClick(View arg0) {
			  // TODO Auto-generated method stub
			  String first_name = inputContent1.getText().toString();
			  String last_name = inputContent2.getText().toString();
			  Integer sickness = determineSick();
			  Integer hand_sick=determineHand(sickness);
			  Integer nose_sick=determineNose(sickness);
			  time=determineSickTime(sickness);
			  mySQLiteAdapter.insert(first_name, last_name, sickness,hand_sick, time, nose_sick, time);
			  setContentView(R.layout.gofind);
			  buttonMeet=(Button)findViewById(R.id.meeting);
			  buttonMeet.setOnClickListener(buttonMeetOnClickListener);
			  back = (Button)findViewById(R.id.back);
			  back.setOnClickListener(buttonBackOnClickListener);
			  //this will close the keyboard, but you will have to restart the app to enter a new name. 
			  InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			  imm.hideSoftInputFromWindow(inputContent1.getWindowToken(), 0);

		  	  } 
	   };

	   Button.OnClickListener buttonDeleteAllOnClickListener  = new Button.OnClickListener(){

		  public void onClick(View arg0) {
		   // TODO Auto-generated method stub
		   mySQLiteAdapter.deleteAll();
		  }

	   };
	     
	   Button.OnClickListener buttonMeetOnClickListener = new View.OnClickListener(){
		   public void onClick(View v){  
    	   Intent intent = new Intent(CootiesActivity.this,Meeting.class);
   		   CootiesActivity.this.startActivity(intent);
	   }};

	   
	   Button.OnClickListener buttonBackOnClickListener  = new Button.OnClickListener(){
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



}