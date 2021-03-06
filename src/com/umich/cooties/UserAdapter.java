package com.umich.cooties;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;


public class UserAdapter extends Activity {

	public static final String USER_DB = "MY_DATABASE";
	public static final String USER_TABLE = "MY_TABLE";
	public static final int MYDATABASE_VERSION = 1;
	public static final String KEY_ID = "_id";
	public static final String FIRST_NAME = "first";
	public static final String LAST_NAME = "last";
	public static final String SICK = "sickness";
	public static final String HAS_HIV = "has_hiv";
	public static final String HIV_SICK = "hiv_sick";
	public static final String HAND_SICK="Hand_Sick";
	public static final String SOURCE_SICK="Source_Sick";
	public static final String HAND_SICK_TIME="Hand_Sick_Time";
	public static final String SOURCE_SICK_TIME="Nose_Sick_Time";
	



	//create table MY_DATABASE (ID integer primary key, Content text not null);
	private static final String SCRIPT_CREATE_DATABASE =
	"create table " + USER_TABLE + " ("	
	+ KEY_ID + " integer primary key autoincrement, "
	+ FIRST_NAME + " text not null, "
	+ LAST_NAME + " text not null, "
	+ SICK + " integer NOT NULL, "
	+ HAS_HIV + " integer NOT NULL, "
	+ HIV_SICK + " REAL NOT NULL, "
	+ HAND_SICK + " REAL NOT NULL, "
	+ HAND_SICK_TIME + " real NOT NULL, "
	+ SOURCE_SICK + " REAL NOT NULL, "
	+ SOURCE_SICK_TIME + " real NOT NULL);";


	protected static SQLiteHelper sqLiteHelper;
	static SQLiteDatabase sqLiteDatabase;

	private Context context;

	public UserAdapter(Context c){
		context = c;
	}

 

	public UserAdapter openToRead() throws android.database.SQLException {
		sqLiteHelper = new SQLiteHelper(context, USER_DB, null, MYDATABASE_VERSION);
		sqLiteDatabase = sqLiteHelper.getReadableDatabase();
		return this; 
	}

 

	 public UserAdapter openToWrite() throws android.database.SQLException {
		sqLiteHelper = new SQLiteHelper(context, USER_DB, null, MYDATABASE_VERSION);
		sqLiteDatabase = sqLiteHelper.getWritableDatabase();
		return this; 
	 }

 

	public void close(){
		sqLiteHelper.close();
	}

	
	
	public static int getLast(){
		int last=0;
		Cursor cur = sqLiteDatabase.rawQuery("SELECT * FROM " + USER_TABLE, null);
		ArrayList<String> temp = new ArrayList<String>();
		if (cur != null) {
		    if (cur.moveToFirst()) {
		        do {
		            last++; // "Title" is the field name(column) of the Table                 
		        } while (cur.moveToNext());
		    }
		}
		return last;
	}
/*	
	public int updateSickToYes(int item_id, String name){
	   ContentValues cv=new ContentValues();
	   cv.put(SICK, "yes");
	   cv.put(FIRST_NAME,"");
	   
	   return sqLiteDatabase.update(USER_TABLE, cv, KEY_ID+"=?", 
	    new String []{String.valueOf(item_id)});   
	}
*/
	public long insert(String first, String last, int sick, int has_hiv, double hiv_sick, double hand_sick, long hand_sick_time,
			double source_sick, long nose_sick_time){

		ContentValues contentValues = new ContentValues();
		contentValues.put(FIRST_NAME, first);
		contentValues.put(LAST_NAME, last);
		contentValues.put(SICK, sick);
		contentValues.put(HAS_HIV, has_hiv);
		contentValues.put(HIV_SICK, hiv_sick);
		contentValues.put(HAND_SICK, hand_sick);
		contentValues.put(HAND_SICK_TIME, hand_sick_time);
		contentValues.put(SOURCE_SICK, source_sick);
		contentValues.put(SOURCE_SICK_TIME, nose_sick_time);
		return sqLiteDatabase.insert(USER_TABLE, null, contentValues);
	}

	public int update(int id,int sick, int has_hiv, double hiv_sick, double hand_sick, long hand_sick_time,
			double source_sick, long source_sick_time){
		    ContentValues cv=new ContentValues();
		    cv.put(SICK, sick);
			cv.put(HAS_HIV, has_hiv);
			cv.put(HIV_SICK, hiv_sick);
		    cv.put(HAND_SICK, hand_sick);
		    cv.put(SOURCE_SICK, source_sick);
		    cv.put(HAND_SICK_TIME, hand_sick_time);
		    cv.put(SOURCE_SICK_TIME, source_sick_time);
		    return sqLiteDatabase.update(USER_TABLE, cv, KEY_ID+"="+id, 
		    		null);   
	  }
	
	
	public int deleteAll(){
		return sqLiteDatabase.delete(USER_TABLE, null, null);
	}

	public Cursor queueAll(){
		String[] columns = new String[]{KEY_ID, FIRST_NAME, LAST_NAME, SICK, HAS_HIV, HIV_SICK, HAND_SICK, HAND_SICK_TIME, SOURCE_SICK, SOURCE_SICK_TIME};
		Cursor cursor = sqLiteDatabase.query(USER_TABLE, columns,
		null, null, null, null, null);
		return cursor;
	}
	
	public class SQLiteHelper extends SQLiteOpenHelper {

		public SQLiteHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		}



	@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(SCRIPT_CREATE_DATABASE);
	}			
	@Override

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		}
	}


	
	

}
