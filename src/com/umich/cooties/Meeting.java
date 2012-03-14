package com.umich.cooties;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.database.Cursor;
import android.database.SQLException;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    static protected String item;
    static protected RelAdapter relAdapter;    
    static protected String body=null;
    static protected String my_first=null;
    static protected String my_last=null;
    static protected int my_sick=0;
    static protected int my_id=0;
    static protected int my_hand=0;
    static protected int my_nose=0;
    
    static protected SimpleCursorAdapter relCursorAdapter;
	static protected Cursor relCursor;

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
		/*
		Button next=(Button) findViewById(R.id.meetdone);
		next.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				Intent intent = new Intent(Meeting.this,FinishMeeting.class);
				Meeting.this.startActivity(intent);
			}
		});
		
		*/
		
		//rel db and cursor initialization
		relAdapter = new RelAdapter(this);
		relAdapter.openRelToWrite();
		relAdapter.openRelToRead();
		relCursor = relAdapter.queueAll();
		relCursor.moveToFirst();
		
		 
		//query the database
		//IT WORKS!!!!
		CootiesActivity.mySQLiteAdapter.openToRead();
		Cursor c = CootiesActivity.mySQLiteAdapter.queueAll();
		c.moveToLast();
		my_id = c.getInt(c.getColumnIndex(UserAdapter.KEY_ID));
		 my_first = c.getString(c.getColumnIndex(UserAdapter.FIRST_NAME));
		 my_last = c.getString(c.getColumnIndex(UserAdapter.LAST_NAME));
		 my_sick = c.getInt(c.getColumnIndex(UserAdapter.SICK));
		 my_hand = c.getInt(c.getColumnIndexOrThrow(UserAdapter.HAND_SICK));
		 my_nose = c.getInt(c.getColumnIndexOrThrow(UserAdapter.NOSE_SICK));
		 //the variable item will be packaged as an NFC datatype
		 item = String.valueOf(my_id) + " " + my_first +" " + my_last + " "+ String.valueOf(my_sick) + 
		 " " + String.valueOf(my_hand) + " "+ String.valueOf(my_nose);
      	toast(item);//debugging purposes
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
	        // Sticky notes received from Android
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

	     }
	    
	    //THIS IS WHERE THE MAGIC HAPPENS!
	    //this step is where you send the NEW notebody using info from the other phone
	    //store this information as an ArrayList string!
	    
	    private void promptForContent(final NdefMessage msg) {
	        new AlertDialog.Builder(this).setTitle("Complete the meeting?")
	            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface arg0, int arg1) {
	                	//want to get info from other phone
	                    body = new String(msg.getRecords()[0].getPayload());
	                    toast(body);//shows who you're meeting
	            		
	                    String[] inputList;
	                    inputList=body.split(" ");
	                    //partner info
	                    String partnerFirst=inputList[1]; // first
	                    String partnerLast=inputList[2]; //last
	                    String partnerHand=inputList[4];//hand sick
//	                    String partnerNose=inputList[5];//nose sick for potential sneezing infection while shaking hands?
	                    //check sickness functions	                    
	                    Boolean infect = VirusFunctions.willInfect(my_hand,Integer.parseInt(partnerHand));
	                    Boolean contract = VirusFunctions.willContract(my_hand, Integer.parseInt(partnerHand));                  
	                    if(infect){
	                    	VirusFunctions.exchangeVirus(my_hand,Integer.parseInt(partnerHand));
	                    }
	                    
	                    Integer infectInt = infect.hashCode();
	                    Integer contractInt = contract.hashCode();
	                    
	                    VirusFunctions.changeMe(infectInt);
	                    VirusFunctions.changeMe(contractInt);
	                    //write this shit into the relationship database
	                    relAdapter.insert(my_id,partnerFirst, partnerLast, infectInt, contractInt);
	                    
	                    CootiesActivity.mySQLiteAdapter.openToWrite();
	                    //need to determine random sickness transfers here
	                    
	                    //user wipes nose -- need to make this random event --
	                    VirusFunctions.increaseVirus(my_nose, my_hand);

	                    //re-inserting user with updated sickness? this is probably super bad
	                    CootiesActivity.mySQLiteAdapter.update(my_id, my_hand, CootiesActivity.time++, my_nose, CootiesActivity.time++);
	             	   Intent intent = new Intent(Meeting.this,HaveMet.class);
	           		   Meeting.this.startActivity(intent);
	                }
	            })
	            .setNegativeButton("No", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface arg0, int arg1) {
	                    
	                }
	            }).show();
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
	        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	    }

}
