package com.umich.cooties;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;


public class SQLiteAdapter {

	 public static final String MYDATABASE_NAME = "MY_DATABASE";
	 public static final String MYDATABASE_TABLE = "MY_TABLE";
	 public static final int MYDATABASE_VERSION = 1;
	 public static final String KEY_ID = "_id";
	 public static final String KEY_CONTENT1 = "Content1";
	 public static final String KEY_CONTENT2 = "Content2";
	
	 //for relationship table
	 public static final String RELATIONSHIP ="relationship";
	 public static final String ROUND="round";
	 public static final String id_me="id_me";
	 public static final String id_partner = "id_partner";
	 public static final String time = "time";
	 
	 //create table MY_DATABASE (ID integer primary key, Content text not null);
	 private static final String SCRIPT_CREATE_DATABASE =
	  "create table " + MYDATABASE_TABLE + " ("
	  + KEY_ID + " integer primary key autoincrement, "
	  + KEY_CONTENT1 + " text not null, "
	  + KEY_CONTENT2 + " text not null);";
	 
	 //create table RELATIONSHIP
	 private static final String CREATE_RELATIONSHIPS = 
			 "create table" + RELATIONSHIP + "("
			 + ROUND + "integer primary key autoincrement," +
					 id_me + "text not null," + id_partner + "text not null,"
					 +time + "text not null);";
	
	 private SQLiteHelper sqLiteHelper;
	 private SQLiteDatabase sqLiteDatabase;
	
	 private Context context;
 
	 public SQLiteAdapter(Context c){
	  context = c;
	 }

 

	 public SQLiteAdapter openToRead() throws android.database.SQLException {
	  sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);
	  sqLiteDatabase = sqLiteHelper.getReadableDatabase();
	  return this; 
	 }

 

	 public SQLiteAdapter openToWrite() throws android.database.SQLException {
	  sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);
	  sqLiteDatabase = sqLiteHelper.getWritableDatabase();
	  return this; 
	 }

 

 public void close(){
  sqLiteHelper.close();
 }

 

 public long insert(String content1, String content2){

  ContentValues contentValues = new ContentValues();
  contentValues.put(KEY_CONTENT1, content1);
  contentValues.put(KEY_CONTENT2, content2);
  return sqLiteDatabase.insert(MYDATABASE_TABLE, null, contentValues);
 }
 
 //insert into relationship table
 public long insertRelationship(String round, String id_me, String id_partner, String time){
	  ContentValues contentValues = new ContentValues();
	  contentValues.put(ROUND, round);
	  contentValues.put(id_me, id_me);
	  contentValues.put(id_partner, id_partner);
	  contentValues.put(time, time);
	 return sqLiteDatabase.insert(RELATIONSHIP,null, contentValues);
 }

 

 public int deleteAll(){
  return sqLiteDatabase.delete(MYDATABASE_TABLE, null, null);
 }

 

 public Cursor queueAll(){
  String[] columns = new String[]{KEY_ID, KEY_CONTENT1, KEY_CONTENT2};
  Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, columns,
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
