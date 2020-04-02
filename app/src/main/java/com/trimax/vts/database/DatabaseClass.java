package com.trimax.vts.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.trimax.vts.utils.CommonClass;
import com.trimax.vts.model.MenuModel;

import java.util.ArrayList;

public class DatabaseClass {
	SQLiteDatabase myDB = null;
	Context ctx;
	//DataBaseHelper dbh;

	private final static String DB_PATH = "/data/data/com.trimax.vts.mobile/databases/";
	private final static String DB_NAME = "VTS.sqlite";
	public int NewDBVersion = 1;


	public DatabaseClass(Context c) {
		this.ctx = c;
	}

	public void copyDb(Context c) {

		CommonClass cm = new CommonClass(c);

		ArrayList<String> retVal = cm.copyDB(ctx, DB_PATH, DB_NAME, Integer.toString(NewDBVersion));
		if (retVal.get(0).toString().equals("yes")) {
			try {
				myDB = ctx.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
				ContentValues args = new ContentValues();
				args.put("Version", NewDBVersion);
				myDB.update("DatabaseVersion", args, null, null);
			} catch (Exception e) {
				// Log.e("DBHelper", "Cannot open database" + e.toString());
				new CommonClass(c).alertComman(ctx, "Sorry",
						"Something Went Wrong!!");
			} finally {
				AppDatabase.getDbInstance(c);
				if (myDB != null) {
					myDB.close();
				}
			}
		}

	}


	public void DatabaseVersion_CreateTable() {
		try {
			myDB = ctx.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
			myDB.execSQL("CREATE TABLE IF NOT EXISTS DatabaseVersion(Version int NOT NULL);");

			String strQ = "SELECT count(*) FROM DatabaseVersion;";
			Cursor c = myDB.rawQuery(strQ, null);
			c.moveToFirst();
			int count = c.getInt(0);
			if (count == 0) {
				ContentValues args = new ContentValues();
				args.put("Version", 1);
				myDB.update("DatabaseVersion", args, null, null);
				//System.out.println("DatabaseVersion table created ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		}

	public void RulesPermission_CreateTable() {
		try {
			myDB = ctx.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
			myDB.execSQL("CREATE TABLE IF NOT EXISTS Permissions(menu_seq TEXT, menu_status TEXT);");

			System.out.println("DatabaseVersion table created ");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

     public ArrayList<MenuModel> getMenuPermission(){
		ArrayList<MenuModel> mm = new ArrayList<>();
		 myDB = ctx.openOrCreateDatabase(DB_NAME,Context.MODE_PRIVATE,null);
		 String query = "select distinct * from Permissions where menu_seq!=0 order by menu_seq";
		 Cursor cursor = myDB.rawQuery(query, null);

		 if (cursor.moveToFirst()){
			 do {
				String postion =(cursor.getString(0));
				int status =Integer.parseInt(cursor.getString(1));
				mm.add(new MenuModel(status,postion));
			 }
			 while (cursor.moveToNext());
		 }
		 return mm;
	 }


	public void insertMenus(Context ctx, String query) {
		try {
			myDB = ctx.openOrCreateDatabase(DB_NAME,Context.MODE_PRIVATE,null);
			myDB.execSQL(query);
			Log.e("DBHelper", "insertion completed");
		} catch (Exception e) {
		Log.e("DBHelper", "Cannot open database" + e.toString());
		} finally {
			if (myDB != null) {
				myDB.close();
			}
		}
	}

	  public  void deletedata(){
		  SQLiteDatabase db = null;
		  db = ctx.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
		  db.execSQL("delete from permissions");
		  db.close();
	  }



	}
