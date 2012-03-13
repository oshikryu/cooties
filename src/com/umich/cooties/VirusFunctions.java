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
	public static void increaseVirus(int infector,int infectee){
	    Random generator = new Random();
	    Integer num = generator.nextInt(infector);
	    infector= infectee+num;
    }
   
    //generate a virus decrease via time decay (hand only!! for now) 
	public static void virusDecay(int infectee){
		/*virus inactivation will be static until
		 * able to model proper first order decay
		 */
		int decreaseAmount = 1;//this is based on 1% of the total infection percentage
		infectee= infectee-decreaseAmount;
	}
   
	//touch time randomizer
    public static int touch(){
	    Random generator = new Random();
	    Integer num = timeMin + generator.nextInt(180);
	    return num;
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
    public static void changeMe(int code){
    	if(code==1231){
    		code= 1;
    	}
    	else{
    		code = 0;
    		
    	}
    }
    
    //actual hand_sick exchange
    public static void exchangeVirus(int my_hand, int partner_hand){
    	int totalVirus=my_hand+partner_hand;
    	if(willInfect(my_hand, partner_hand)){
    		my_hand=(totalVirus)/2;//splits the total virus amount and equally distributes it
    		partner_hand=(totalVirus)/2;//same thing but for your partner
    	}   	
    }
    
    //nose to hand a.k.a. excretion
    public static void noseToHand(int nose, int hand){
    	//increase nose to hand
    	increaseVirus(nose, hand);
    }
    
    //hand to nose a.k.a. infection
    public static void handToNose(int hand, int nose){
    	increaseVirus(hand,nose);
    	
    }
	
}
