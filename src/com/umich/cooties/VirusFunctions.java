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
	public static double increaseVirus(Integer infector){
		if(infector.equals(0)){
		    Random generator = new Random();
		    Integer num = generator.nextInt(10);
		    return infector+num;
		}
	    Random generator = new Random();
	    Integer num = generator.nextInt(infector);
	    return infector+num;
    }
   
    //generate a virus decrease via time decay (hand only!! for now) 
	public static double virusDecay(Double infectee){
		/*virus inactivation will be static until
		 * able to model proper first order decay
		 */
		if(infectee.equals(0)){
			return 0;
		}
		Double decreaseAmount = infectee/100;//this is based on 1% of influenza decay per minute 
		if((infectee-decreaseAmount)>0){
			return infectee-decreaseAmount;
		}
		return infectee;
	}
       
    //wash hands
    public static double wash(double handvirus){
    	double afterwash=0;
    	afterwash=handvirus/2;//reduce infection concentration by 50%
    	return afterwash;
    }
    
    //determine if you will contract from your partner
    public static boolean willContract(double my_sick, double partner_sick){
    	boolean result=false;
    	if(partner_sick > my_sick ){
    		double partner_prob=partner_sick/100;
 		   	if(partner_prob > Meeting.sharedProbability){
 		   		return true;
 		   	}
    	}
    	return result;
    }
    
    //determine if you will infect your partner
    public static boolean willInfect(double my_sick, double partner_sick){
    	boolean result=false;
    	if(my_sick > partner_sick){
    		double my_prob=my_sick/100;
 		   	if(my_prob > Meeting.sharedProbability){
 		   		return true;
 		   	}
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
    
    /*
    *actual hand_sick pathogen exchange
    *this function calculates the amount of pathogen exchanged
    *using a transfer efficiency of 10% 
    */
    public static double exchangeVirus(double me, double partner){
    	double my_prob=me/10;
    	double partner_prob=partner/10;
    	if(willInfect(me, partner)){//if you have more pathogen than your partner
    		me-=(my_prob-partner_prob);
    	}
    	if(willContract(me,partner)){//if you have less pathogen than your partner
    		me+=(partner_prob-my_prob);
    	}
		return me;//splits the total virus amount and equally distributes it
    }
    
    
}
