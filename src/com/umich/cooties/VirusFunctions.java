package com.umich.cooties;

import java.io.IOException;
import java.util.Random;

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
import android.widget.Toast;
import android.app.Activity;

public class VirusFunctions extends Activity {
	//set variables for max virus saturation in nose and hand
	public static int virus_max=99;
	
	public static int hand_virus = 0;
	public static int nose_virus = 0;
	public static int timeMin=60;
	
	//generate a virus amount to add
	public static int increaseVirus(int infector){
		if(infector==0){
		    Random generator = new Random();
		    Integer num = generator.nextInt(10);
		    return infector+num;
		}
	    Random generator = new Random();
	    Integer num = generator.nextInt(infector);
	    return infector+num;
    }
   
    //generate a virus decrease via time decay (hand only!! for now) 
	public static int virusDecay(int infectee){
		/*virus inactivation will be static until
		 * able to model proper first order decay
		 */
		if(infectee==0){
			return 0;
		}
	    Random generator = new Random();
	    Integer num = generator.nextInt(infectee);
		int decreaseAmount = infectee%10;//this is based on 1% of the total infection percentage
		if((infectee-decreaseAmount)>0){
			return infectee-decreaseAmount;
		}
		return infectee;
	}
       
    //wash hands
    public static int wash(int handvirus){
    	int afterwash=0;
    	afterwash=handvirus/2;//reduce infection concentration by 50%
    	return afterwash;
    }
    
    //determine if you will infect partner
    public static boolean willInfect(int my_sick, int partner_sick){
    	boolean result=false;
    	if(my_sick>partner_sick){
    		result=true;
    	}
    	return result;
    }
    
    //determine if partner will infect you
    public static boolean willContract(int my_sick, int partner_sick){
    	boolean result=false;
    	if(partner_sick>my_sick){
    		result=true;
    	}
    	return result;
    }
    
    //change from bool hash to 1 or 0
    public static int changeMe(int code){
    	if(code==1231){
    		return code= 1;
    	}
    	else{
    		return code = 0;
    		
    	}
    }
    
    //actual hand_sick exchange
    public static int exchangeVirus(int my_hand, int partner_hand){
    	int totalVirus=my_hand+partner_hand;
		return totalVirus/2;//splits the total virus amount and equally distributes it
    	
    }
    

}
