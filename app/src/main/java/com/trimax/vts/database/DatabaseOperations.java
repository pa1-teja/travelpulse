package com.trimax.vts.database;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.trimax.vts.database.TableData.MapTableInfo;
import com.trimax.vts.database.TableData.TableInfo;

public class DatabaseOperations extends SQLiteOpenHelper
{
     public static final int database_version=1;
    private SQLiteDatabase SQ=null;
	
   /*  public String CREATE_QUERY="CREATE TABLE "+TableInfo.TABLE_NAME+"("+
    		                      TableInfo.CODE_TYPE+" TEXT,"+
    		                      TableInfo.CODE+" TEXT,"+
    		                      TableInfo.SHORT_DESC+" TEXT,"+
    		                      TableInfo.REF_DATA+" TEXT);";
    		                      
*/

    		                    
	
	public DatabaseOperations(Context context) {
		super(context, TableInfo.DATABASE_NAME, null, database_version);
		Log.i("DATABASE OPERATIONS","DATABASE CREATED");
		
	}

	@Override
	public void onCreate(SQLiteDatabase sdb)
	{
		/*sdb.execSQL(CREATE_QUERY);
		Log.i("DATABASE OPERATIONS","TABLE CREATED");
		ContentValues cv=new ContentValues(2);
		cv.put(TableInfo.MAKE,"BMW");
		cv.put(TableInfo.MODEL,"BMW 5");
		sdb.insert(TableInfo.TABLE_NAME,null,cv);
	    Log.i("DATABASE OPERATIONS","ONE ROW INSERTED");*/
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	/*public void putInformation(DatabaseOperations dop,String code, String code_type,String short_desc,String ref_data)
			{
		
		   SQLiteDatabase SQ=dop.getWritableDatabase();
		   ContentValues cv=new ContentValues();
		   cv.put(TableInfo.CODE_TYPE,code);
		   cv.put(TableInfo.CODE,code_type); 
		   cv.put(TableInfo.SHORT_DESC, short_desc);
		   cv.put(TableInfo.REF_DATA, ref_data);
		   SQ.insert(TableInfo.TABLE_NAME, null, cv);
		   Log.i("DATABASE OPERATIONS","ONE ROW INSERTED");
			}   */

	public ArrayList<String[]> getSystemCodeData(Context ctx, String CodeType, String SpinnerData, String extraWhereClause) {
		SQLiteDatabase myDB = null;
		ArrayList<String[]> result = new ArrayList<String[]>();
		String[] code = null;
		String[] shortdesc = null;
		String[] refdata = null;
		try {
			myDB = ctx.openOrCreateDatabase("VTS.sqlite", Context.MODE_PRIVATE, null);
			String q = "SELECT code, short_desc, ref_data FROM System_codes where code_type = '" + CodeType + "'";
			
			if(extraWhereClause != null) {
				q = q + " " + extraWhereClause;
			}
			
			q = q + " " + "ORDER BY short_desc";
			
			Cursor c = myDB.rawQuery(q, null);

			int cursorcount = c.getCount();
			int j = 0;

			if (SpinnerData == "Y") {
				j = 1;
				cursorcount = cursorcount + 1;
				code = new String[cursorcount];
				shortdesc = new String[cursorcount];
				refdata = new String[cursorcount];
				code[0] = "0";
				shortdesc[0] = "--Select--";
				refdata[0] = "";
			} else {
				code = new String[cursorcount];
				shortdesc = new String[cursorcount];
				refdata = new String[cursorcount];
			}

			if (c.getCount() > 0) {
				c.moveToFirst();
				for (int i = j; i < cursorcount; i++) {
					String sid = c.getString(c.getColumnIndex("code"));
					String sname = c.getString(c.getColumnIndex("short_desc"));
					String srefdata = c.getString(c.getColumnIndex("ref_data"));
					code[i] = sid;
					shortdesc[i] = sname;
					refdata[i] = srefdata;
					c.moveToNext();
				}
			}
			result.add(code);
			result.add(shortdesc);
			result.add(refdata);
			c.close();

		} catch (Exception e) {
			Log.e("DBHelper", "Cannot open database" + e.toString());
		} finally {
			if (myDB != null) {
				myDB.close();
			}
		}
		return result;
	}

	public Cursor getInformationForMap(DatabaseOperations dop) {
		SQLiteDatabase SQ =dop.getWritableDatabase();
		String[] columns={MapTableInfo.LAT_COLUMN,MapTableInfo.LONG_COLUMN};
		Cursor CR = SQ.query(MapTableInfo.TABLE_NAME, columns, null, null, null, null, null);
		return CR;
	}
	
	public Cursor getInformationWithCount(DatabaseOperations dop, int count) {
		SQLiteDatabase SQ =dop.getWritableDatabase();
		String[] columns={MapTableInfo.LAT_COLUMN,MapTableInfo.LONG_COLUMN};
		String test = "5";
		//Cursor CR = SQ.query(TableInfo.TABLE_NAME, columns, null, null, null, null, null);
		Cursor CR = SQ.rawQuery("select " + MapTableInfo.LAT_COLUMN+","+MapTableInfo.LONG_COLUMN+" from " +MapTableInfo.TABLE_NAME+ " LIMIT "+count+", 1", null);
		return CR;
	}
	



}
