package com.trimax.vts.database;

import android.provider.BaseColumns;

public class TableData
{
  public TableData()
  {
	  
  }
	
	public static abstract class TableInfo 
	{
		
		
	public static final String CODE_TYPE="code_type";
	public static final String CODE="code";
	public static final String SHORT_DESC="short_desc";
	public static final String REF_DATA="ref_data";
	
	
	public static final String DATABASE_NAME="VTS.sqlite";
	public static final String TABLE_NAME="system_codes";
	
	
		
		
		
	}
	
	public static abstract class MapTableInfo implements BaseColumns {
		public static final String LAT_COLUMN = "Lat";
		public static final String LONG_COLUMN = "Long";
		public static final String DATABASE_NAME = "VTS";
		public static final String TABLE_NAME = "LatLongTable";
		
	}
  
  
  
  
}
