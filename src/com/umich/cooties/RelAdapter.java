package com.umich.cooties;

import java.util.ArrayList;

import com.umich.cooties.UserAdapter.SQLiteHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;


public class RelAdapter {
	
	 protected static RelHelper sqLiteHelper;
	 protected static  SQLiteDatabase relrelsqLiteDatabase;
	 private Context context;	 
	 
	 public RelAdapter(Context c){
	  context = c;
	 }
	 
	 //for relationship table
	 public static final String REL_DB ="rel_db";
	 public static final String REL_TABLE = "rel_table";
	 public static final int RELATIONSHIP_VERSION = 1;
	 public static final String ID_ME="id_me";
	 public static final String PARTNER_FIRST = "partner_first";
	 public static final String PARTNER_LAST = "partner_last";
	 public static String SPREAD = "spread";
	 public static String CONTRACT = "contract";
	 public static String ROUND="_id";

	 
	 //create table RELATIONSHIP
	 /*FOREIGN KEY(" +ID_ME+" ) REFERENCES*/
	 
	 private static final String CREATE_RELATIONSHIPS = 
			 "create table " + REL_TABLE + "("
			 + ROUND + " integer primary key autoincrement, " +
			 ID_ME + " INTEGER, "+ PARTNER_FIRST + " text not null,"
			 + PARTNER_LAST + " text not null," + SPREAD +" integer, " +
			 CONTRACT+ " integer);";
	 
	 //for relationship helper and database;
	 
	 // relationship constructors
	 public RelAdapter openRelToWrite() throws android.database.SQLException{
		 sqLiteHelper = new RelHelper(context, REL_DB, null, RELATIONSHIP_VERSION);
		 relrelsqLiteDatabase = sqLiteHelper.getWritableDatabase();
		 return this;
	 }

	 public RelAdapter openRelToRead() throws android.database.SQLException {
		 sqLiteHelper = new RelHelper(context, REL_DB, null, RELATIONSHIP_VERSION);
		 relrelsqLiteDatabase = sqLiteHelper.getReadableDatabase();
		  return this; 
		 }
 

	 public void close(){
		 sqLiteHelper.close();
	 }

	 
	public static int getLast(){
		int last=0;
		Cursor cur = relrelsqLiteDatabase.rawQuery("SELECT * FROM " + REL_TABLE, null);
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
	 
	 //insert into relationship table
	 public long insert(Integer id_me, String partner_first, String partner_last, Integer spread, Integer contract){
		  ContentValues contentValues = new ContentValues();
		  contentValues.put(ID_ME, id_me);
		  contentValues.put(PARTNER_FIRST, partner_first);
		  contentValues.put(PARTNER_LAST, partner_last);
		  contentValues.put(SPREAD, spread);
		  contentValues.put(CONTRACT, contract);
		  
		 return relrelsqLiteDatabase.insert(REL_TABLE,null, contentValues);
	 }
	
	 
	
	public int deleteAll(){
		return relrelsqLiteDatabase.delete(REL_TABLE, null, null);
	 }
	
	 
	
	 public Cursor queueAll(){
	  String[] columns = new String[]{ROUND, ID_ME, PARTNER_FIRST, PARTNER_LAST, SPREAD, CONTRACT};
	  Cursor cursor = relrelsqLiteDatabase.query(REL_TABLE, columns,
	    null, null, null, null, null);
	  return cursor;
	
	 }
 

	 public class RelHelper extends SQLiteOpenHelper {
	
	  public RelHelper(Context context, String name,
	  CursorFactory factory, int version) {
		  super(context, name, factory, version);
	  }	
	  @Override
	  public void onCreate(SQLiteDatabase db) {
		   // TODO Auto-generated method stub
		   db.execSQL(CREATE_RELATIONSHIPS);
	  }	  
	  @Override

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	  	// TODO Auto-generated method stub
  		}
	 }

}
