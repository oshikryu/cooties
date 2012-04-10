package com.umich.cooties;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.drawable.AnimationDrawable;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Parcelable;


import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class Meeting extends Activity{
	/*
	 * These are the NFC initialized variables implemented
	 * from stickynotes
	 */
	
    private static final String TAG = "meeting";
    private boolean mResumed = false;
    private boolean mWriteMode = false;
    NfcAdapter mNfcAdapter;
    PendingIntent mNfcPendingIntent;
    IntentFilter[] mWriteTagFilters;
    IntentFilter[] mNdefExchangeFilters;
//	static protected SimpleCursorAdapter relCursorAdapter;
//	static protected RelAdapter relAdapter;
//	static protected Cursor relCursor;
    static protected String item;    
    static protected String body = null;
    static protected String my_first = null;
    static protected String my_last = null;
    static protected int my_sick = 0;
    static protected int my_id = 0;
    static protected double my_hand = 0;
    static protected double my_source = 0;
    static protected double my_hiv_sick = 0;
    static protected int my_has_hiv = 0;
    
    
    //this number is between 0 and 1 and will be
    //used for determining probabilistic infection
    static protected Double my_probability = 0.0;
   	public static Double sharedProbability = 0.0;
   	


	AnimationDrawable animation;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		setContentView(R.layout.meeting);
		Button back=(Button) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		//this adds animation to the meeting screen
		 animation = new AnimationDrawable();
        animation.addFrame(getResources().getDrawable(R.drawable.page4_meeting1), 500);
        animation.addFrame(getResources().getDrawable(R.drawable.page4_meeting2), 500);
        animation.addFrame(getResources().getDrawable(R.drawable.page4_meeting3), 500);
        animation.addFrame(getResources().getDrawable(R.drawable.page4_meeting4), 500);
        animation.addFrame(getResources().getDrawable(R.drawable.page4_meeting5), 500);
        animation.setOneShot(false);
        
        ImageView imageAnim =  (ImageView) findViewById(R.id.anim);
        imageAnim.setBackgroundDrawable(animation);
        
        // run the start() method later on the UI thread
        imageAnim.post(new Starter());
		

		
		 
		//query the database
		//IT WORKS!!!!
		//package info from database into string
		CootiesActivity.mySQLiteAdapter.openToRead();
		Cursor c = CootiesActivity.mySQLiteAdapter.queueAll();
		c.moveToLast();
		my_id = c.getInt(c.getColumnIndex(UserAdapter.KEY_ID));
		my_first = c.getString(c.getColumnIndex(UserAdapter.FIRST_NAME));
		my_last = c.getString(c.getColumnIndex(UserAdapter.LAST_NAME));
		my_sick = c.getInt(c.getColumnIndex(UserAdapter.SICK));
		my_hiv_sick = c.getDouble(c.getColumnIndexOrThrow(UserAdapter.HIV_SICK));
		my_has_hiv = c.getInt(c.getColumnIndexOrThrow(UserAdapter.HAS_HIV));
		my_hand = c.getDouble(c.getColumnIndexOrThrow(UserAdapter.HAND_SICK));
		my_source = c.getDouble(c.getColumnIndexOrThrow(UserAdapter.SOURCE_SICK));
		
	   	Random generator = new Random();
	   	my_probability = generator.nextDouble();	
	   	
		//the string item will be packaged as an NFC datatype
		item = String.valueOf(my_id) + " " + my_first +" " + my_last + " "+ String.valueOf(my_sick) + 
		" " + String.valueOf(my_hand) + " "+ String.valueOf(my_source) + " "+ String.valueOf(my_has_hiv)+  
		" "+ String.valueOf(my_hiv_sick) + " " + my_probability;
//      	toast(item);//debugging purposes
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        // Handle all of our received NFC intents in this activity.
        mNfcPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // Intent filters for reading a note from a tag or exchanging over p2p.
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefDetected.addDataType("text/plain");
        } catch (MalformedMimeTypeException e) { }
        mNdefExchangeFilters = new IntentFilter[] { ndefDetected };

        // Intent filters for writing to a tag
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        mWriteTagFilters = new IntentFilter[] { tagDetected };
	}
	
	//for animation purposes
    class Starter implements Runnable {
        public void run() {
            animation.start();        
        }
    }
    
	  //get last user 
	  public Cursor fetchRecord(long ID) throws SQLException {
		    Cursor mCursor = UserAdapter.sqLiteDatabase.query(true, UserAdapter.USER_TABLE, new String[] {
		    				UserAdapter.KEY_ID, UserAdapter.FIRST_NAME, UserAdapter.LAST_NAME, UserAdapter.SICK}, UserAdapter.KEY_ID + "=" + ID, null,
		          null, null, null, null);
		    mCursor.moveToLast();
		    return mCursor;
	  }
	
	
	  @Override
	    protected void onResume() {
	        super.onResume();
	        mResumed = true;
	        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
	            NdefMessage[] messages = getNdefMessages(getIntent());
	            byte[] payload = messages[0].getRecords()[0].getPayload();
	            setIntent(new Intent()); // Consume this intent.
	        }
	        enableNdefExchangeMode();
	    }

	    @Override
	    protected void onPause() {
	        super.onPause();
	        mResumed = false;
	        mNfcAdapter.disableForegroundNdefPush(this);
	    }

	    @Override
	    //step1
	    protected void onNewIntent(Intent intent) {
	        // NDEF exchange mode
            NdefMessage[] msgs = getNdefMessages(intent);
            //this figures out what to pull from the other device
            promptForContent(msgs[0]);
	    	
	        if (!mWriteMode && NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
//	            NdefMessage[] msgs = getNdefMessages(intent);
	            //this figures out what to pull from the other device
//	            promptForContent(msgs[0]);
	        }
	        //washing hands randomly
		    Intent myIntent = new Intent(getBaseContext(),
		    		    RandomReceiver.class);
		    PendingIntent pendingIntent
		     = PendingIntent.getBroadcast(getBaseContext(),
		       0, myIntent, 0);
		  
		    AlarmManager alarmManager
		      = (AlarmManager)getSystemService(ALARM_SERVICE);
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTimeInMillis(System.currentTimeMillis());
		    Random generator = new Random();
		    Integer num = generator.nextInt(20)+10;
		    calendar.add(Calendar.SECOND, num);

		    num=num*1000;//this sets num into milliseconds

		    alarmManager.setRepeating(AlarmManager.RTC,
		      calendar.getTimeInMillis(), num, pendingIntent);
		    finish();
	     }
  
	    //THIS IS WHERE THE MAGIC HAPPENS!
	    //this function determines the exchanges of infection
	    //store this information as an ArrayList string!
	    private void promptForContent(final NdefMessage msg) {
        	//want to get info from other phone
            body = new String(msg.getRecords()[0].getPayload());
            String[] inputList;
            inputList=body.split(" ");
            //partner info
            String partnerFirst = inputList[1]; // first
            String partnerLast = inputList[2]; //last
            String partnerHand = inputList[4];//hand sick
            String partnerHIV = inputList[7];//hiv sick
            String partnerProb = inputList[8];//shared probability
            
          //determining which phone's probability to use
            sharedProbability = my_probability;
            if(my_hand == 0){
            	if(my_hiv_sick < Double.parseDouble(partnerHIV)){
            		sharedProbability = Double.parseDouble(partnerProb);
            	}
            }
            if(my_hand != 0){
	            if(my_hand <= Double.parseDouble(partnerHand)){
	            	sharedProbability = Double.parseDouble(partnerProb);
	            }
            }            
            toast("You are meeting: "+ partnerFirst + " " + partnerLast + " " );//shows who you're meeting
            
            /* this is all gastro stuff*/
            //check sickness functions	          

            Boolean infect = VirusFunctions.willInfect(my_hand,Double.parseDouble(partnerHand));
            Boolean contract = VirusFunctions.willContract(my_hand, Double.parseDouble(partnerHand));	                    
            Integer infectInt = infect.hashCode();
            Integer contractInt = contract.hashCode();	                    
            infectInt = VirusFunctions.changeMe(infectInt);
            contractInt =VirusFunctions.changeMe(contractInt);
            
            //determine timing for pathogens
    	    long new_hand_time = System.currentTimeMillis() - CootiesActivity.initial_time;
    	    long new_source_time = System.currentTimeMillis() - CootiesActivity.initial_time;
            
            
            //decay on the hand
            if((new_hand_time - CootiesActivity.current_hand_time) > 60*1000){
            	my_hand = VirusFunctions.virusDecay(my_hand);
                CootiesActivity.current_hand_time = System.currentTimeMillis() - CootiesActivity.initial_time;           
            }
            
            //shedding AND inoculation every ~276.92 seconds within a range of 15 seconds
            //this works out to roughly 13 times per hour
            //10% transfer efficiency of pathogen
	    	if((new_source_time - CootiesActivity.current_source_time) > 276.92*1000){
	    		double shedding = my_source/10;
	    		my_hand += shedding;
	    		if(my_hand != 0){
	    			my_source = VirusFunctions.exchangeVirus(my_source, my_hand);
	   				my_sick = 1;
	   				CootiesActivity.current_source_time = System.currentTimeMillis() - CootiesActivity.initial_time;
	    		}
	   			
	   			CootiesActivity.current_hand_time = System.currentTimeMillis() - CootiesActivity.initial_time;   
	    	}
	    	
	    	
	    	//getting well instantly after 1 hour 
	    	if((new_source_time - CootiesActivity.initial_time) > 3600*1000){
	    		my_sick = 0;
	    		my_source = 0;
	    	}
	    	
            if(contract | infect){
            	my_hand = VirusFunctions.exchangeVirus(my_hand,Double.parseDouble(partnerHand));
            	my_sick = 1;
            }
 
            /*this is all HIV stuff*/
            Boolean spread_hiv = VirusFunctions.willInfect(my_hiv_sick, Double.parseDouble(partnerHIV));
            Boolean contract_hiv = VirusFunctions.willContract(my_hiv_sick, Double.parseDouble(partnerHIV));
            Integer infectHIVInt = spread_hiv.hashCode();
            Integer contractHIVInt = contract_hiv.hashCode();	                    
            infectHIVInt= VirusFunctions.changeMe(infectHIVInt);
            contractHIVInt=VirusFunctions.changeMe(contractHIVInt);
            
            if(contract_hiv | spread_hiv){    
            	my_hiv_sick = VirusFunctions.exchangeVirus(my_hiv_sick,Double.parseDouble(partnerHIV));
            	my_has_hiv = 1;
            }
            
            //write this shit into the relationship database
            CootiesActivity.relAdapter.insert(my_id,partnerFirst, partnerLast, infectInt, contractInt, infectHIVInt, contractHIVInt);
            
            CootiesActivity.mySQLiteAdapter.openToWrite();
            
            
            //updating user with updated sickness? this is probably super bad
            CootiesActivity.mySQLiteAdapter.update(my_id, my_sick, my_has_hiv, my_hiv_sick, my_hand, CootiesActivity.current_hand_time, my_source, CootiesActivity.current_source_time);
            
     	   Intent intent = new Intent(Meeting.this,HaveMet.class);
   		   Meeting.this.startActivity(intent);
	    }
	    
	    //This changes the string within the note
	    //into a Ndef sendable file
	    private NdefMessage getNoteAsNdef() {
	    	//changed to access the static string item;
	        byte[] textBytes = item.getBytes();
	        NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, "text/plain".getBytes(),
	                new byte[] {}, textBytes);
	        return new NdefMessage(new NdefRecord[] {
	            textRecord
	        });
	    }
	    //get ndef messages
	    NdefMessage[] getNdefMessages(Intent intent) {
	        // Parse the intent
	        NdefMessage[] msgs = null;
	        String action = intent.getAction();
	        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
	                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
	            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
	            if (rawMsgs != null) {
	                msgs = new NdefMessage[rawMsgs.length];
	                for (int i = 0; i < rawMsgs.length; i++) {
	                    msgs[i] = (NdefMessage) rawMsgs[i];
	                }
	            } else {
	                // Unknown tag type
	                byte[] empty = new byte[] {};
	                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
	                NdefMessage msg = new NdefMessage(new NdefRecord[] {
	                    record
	                });
	                msgs = new NdefMessage[] {
	                    msg
	                };
	            }
	        } else {
	            Log.d(TAG, "Unknown intent.");
	            finish();
	        }
	        return msgs;
	    }
	    //turn on ndef exchange after writing
	    //called from prompt for content
	    //leads to get note from ndef
	    private void enableNdefExchangeMode() {
	        mNfcAdapter.enableForegroundNdefPush(Meeting.this, getNoteAsNdef());
	        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mNdefExchangeFilters, null);
	    }
	    //turn off ndef exchange before writing
	    private void disableNdefExchangeMode() {
	        mNfcAdapter.disableForegroundNdefPush(this);
	        mNfcAdapter.disableForegroundDispatch(this);
	    }
	    //turn on tag writing after ndef exchange off
	    private void enableTagWriteMode() {
	        mWriteMode = true;
	        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
	        mWriteTagFilters = new IntentFilter[] {
	            tagDetected
	        };
	        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mWriteTagFilters, null);
	    }
	    //turn off tag writing before ndef exchange turned on
	    private void disableTagWriteMode() {
	        mWriteMode = false;
	        mNfcAdapter.disableForegroundDispatch(this);
	    }	    

	    private void toast(String text) {
	        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	    }

}
