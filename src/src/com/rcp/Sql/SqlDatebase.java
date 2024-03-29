package src.com.rcp.Sql;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import src.com.rcp.day.R;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/***
 * 
 * 加载外部数据库到本地
 * 
 * @author toshiba
 * 
 */
public class SqlDatebase {

	/*** 上下文对象 */
	private Context context;

	/** 数据库操作对象 **/
	public SQLiteDatabase sqLiteDatabase_sms;

	public SqlDatebase(Context context) {
		this.context = context;
		// 第一次进去必须先建立数据库文件，如果先getWritableDatabase()就会建立数据库,
		// 使得fromOutCopyDate()读取不到数据,因为他们使用的是同一个数据库
		sqLiteDatabase_sms = fromOutCopyDate();
	
	}


	/***
	 * 从外部复制数据到本地
	 * 
	 * @return
	 */
	public SQLiteDatabase fromOutCopyDate() {
		String DATEBASE_PATH = "/data/data/src.com.rcp.day/databases";
		String DATEBASE_FILENAME = "rcpBrithday_sms.db";

		/** 数据的绝对路径 */
		String databasePath = DATEBASE_PATH + '/' + DATEBASE_FILENAME;

		File path = new File(DATEBASE_PATH);
		// 建立一个文件夹
		if (!path.exists()) {
			path.mkdir();
		}
		// 建立一个文件
		if (!(new File(databasePath)).exists()) {
			InputStream inputStream = context.getResources().openRawResource(
					R.raw.wishdata);

			try {
				FileOutputStream fileOutputStream = new FileOutputStream(
						databasePath);

				byte data[] = new byte[2048];

				int index = 0;
				while ((index = inputStream.read(data)) != -1) {
					fileOutputStream.write(data, 0, index);
					System.out.println("-======-------------");
				}
				inputStream.close();
				fileOutputStream.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(
				databasePath, null);
		
		return sqLiteDatabase;
	}

	public SQLiteDatabase getSqLiteDatabase() {
		return sqLiteDatabase_sms;
	}

	public void setSqLiteDatabase(SQLiteDatabase sqLiteDatabase) {
		this.sqLiteDatabase_sms = sqLiteDatabase;
	}

	public void closeSQl() {
		sqLiteDatabase_sms.close();
	}

}
