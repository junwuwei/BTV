package src.com.rcp.day;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import rcp.com.other.ActivityMeg;
import rcp.com.src.brithUtil.imageUtil;
import rcp.com.src.brithUtil.otherUtil;
import rcp.com.src.volues.activityVolues;
import rcp.com.src.volues.sqlVolue;
import src.com.rcp.Sql.sql_brith;
import src.com.rcp.photo.Rcp_IndexSearchActivity;
import src.com.rcp.wheelview.ScreenInfo;
import src.com.rcp.wheelview.WheelMain;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/***
 * ������ս���
 * 
 * @author toshiba
 * 
 */
public class rcp_addBrithday extends Activity {

	/*** �Զ���Dialog */
	private AlertDialog dialog;

	private WheelMain wheelMain;
	/** ��Ҫ���صĲ��� */
	private LinearLayout layout;

	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private AlertDialog alertDialog;

	/** ������ϵ���������� **/
	brith_ListItem data1 = new brith_ListItem();

	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// ����
	private static final int PHOTO_REQUEST_GALLERY = 2;// �������ѡ��
	private static final int PHOTO_REQUEST_CUT = 3;// ���

	public static final int SEND_SEARCH_NAME = 4;// ����ϵ��
	public static final int SEND_SEARCH_PHONE = 5;// ����ϵ��
	// ����һ���Ե�ǰʱ��Ϊ��Ƶ��ļ�
	File tempFile = new File(Environment.getExternalStorageDirectory()
			+ "/brithPhoto/", getPhotoFileName());

	String path = Environment.getExternalStorageDirectory() + "/brithPhoto/";

	private TextView tv_title;

	/** �������հ�ť */
	private Button btn_setBrith;
	/** ������ϵ�˰�ť **/
	private Button btn_lianxiren_name;

	/** ������ϵ�˰�ť **/
	private Button btn_lianxiren_phone;
	/** ���水ť **/
	private Button btn_save;
	/** ���Ͷ��Ű�ť */
	private Button btn_sendSMS;
	/** ��ݿ�������� **/
	private sql_brith db_brith;
	/** add�������EditText */
	private EditText add_edit[] = new EditText[3];
	/** �Ƿ�ɹ����� **/
	private boolean isOK;

	/** add�������EditTextID */
	private int EditTextID[] = { R.id.add_brith_name,
			R.id.add_brith_zhufuduanxin, R.id.add_brith_beizhu };
	private RadioButton nan, nv;

	private RadioGroup group;
	/** �޸�ͷ�� */
	private ImageView ib_upphoto;

	/** �޸�ͷ���Զ���Dialog�еİ�ť **/
	private RadioButton rb_dialog[] = new RadioButton[3];

	private int RadioButtonID[] = { R.id.rb_setPhoto1, R.id.rb_setPhoto2,
			R.id.rb_setPhoto3 };

	/** ͨ��centerIndex�������������ִ洢��ʽ **/
	private int centerIndex;

	// ��ñ༭Ȩ��
	SharedPreferences.Editor editor;

	private int brithID;

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.add_brithday);
		ActivityMeg.getInstance().addActivity(this);
		SharedPreferences sharedPreferences = getSharedPreferences(
				activityVolues.shape_name, MODE_PRIVATE);
		editor = sharedPreferences.edit();
		Intent center = getIntent();
		centerIndex = center.getIntExtra("center", -100);
		brithID = center.getIntExtra("brithID", 0);
		dataInit();
		viewInit();
		newCreateFile();
		if (brithID != 0) {
			isOK = true;
			setInfo();
		}
	}

	public void newCreateFile() {
		File file1 = new File(path);
		// û��Ŀ¼�Ƚ���Ŀ¼
		if (!file1.exists()) {

			System.out.println("============" + file1.mkdirs());
		}

	}

	public void dataInit() {
		db_brith = new sql_brith(this);
	}

	/**
	 * View�ĳ�ʼ��
	 * 
	 */
	public void viewInit() {

		layout = (LinearLayout) this.findViewById(R.id.add_bottom);

		btn_setBrith = (Button) this.findViewById(R.id.add_brith_brith);
		btn_setBrith.setOnClickListener(onClickListener);
		btn_lianxiren_name = (Button) this
				.findViewById(R.id.add_brith_name_tolianxiren);
		btn_lianxiren_name.setOnClickListener(onClickListener);
		btn_lianxiren_phone = (Button) this
				.findViewById(R.id.add_brith_phone_tolianxiren);
		btn_lianxiren_phone.setOnClickListener(onClickListener);
		btn_save = (Button) this.findViewById(R.id.add_brith_baocun);
		btn_save.setOnClickListener(onClickListener);
		btn_sendSMS = (Button) this
				.findViewById(R.id.add_brith_brith_toduanxin);
		btn_sendSMS.setOnClickListener(onClickListener);
		for (int i = 0; i < add_edit.length; i++) {
			add_edit[i] = (EditText) this.findViewById(EditTextID[i]);
		}
		group = (RadioGroup) this.findViewById(R.id.add_radioGourp);
		nan = (RadioButton) this.findViewById(R.id.add_brith_nan);
		nv = (RadioButton) this.findViewById(R.id.add_brith_nv);
		nan.setChecked(true);
		ib_upphoto = (ImageView) this.findViewById(R.id.reviseHead);
		ib_upphoto.setOnClickListener(onClickListener);
		tv_title = (TextView) this.findViewById(R.id.add_title);
		if (centerIndex == 100) {
			tv_title.setText("��������Ļ���Ϣ");
			layout.setVisibility(View.GONE);
			btn_sendSMS.setVisibility(View.GONE);
		} else {
			layout.setVisibility(View.VISIBLE);
		}
	}

	OnClickListener onClickListener = new OnClickListener() {

		public void onClick(View v) {
			int id = v.getId();
			if (id == R.id.add_brith_brith) {
				showDateTimePicker();
			} else if (id == R.id.add_brith_name_tolianxiren) {
				// �뿪��ǰActivityҪ�ر���ݿ�
				// sqlDatebase.closeSQl();
				Intent intent = new Intent();
				intent.putExtra("add_index_name", 1);
				intent.setClass(rcp_addBrithday.this,
						Rcp_IndexSearchActivity.class);
				startActivityForResult(intent, SEND_SEARCH_NAME);
			} else if (id == R.id.add_brith_phone_tolianxiren) {
				Intent intent = new Intent();
				intent.putExtra("add_index_phone", 2);
				intent.setClass(rcp_addBrithday.this,
						Rcp_IndexSearchActivity.class);
				startActivityForResult(intent, SEND_SEARCH_PHONE);
			} else if (id == R.id.add_brith_brith_toduanxin) {
			} else if (id == R.id.add_brith_baocun) {
				if (isOK && (add_edit[0].getText().toString().length() > 0)) {
					System.out.println("kkkkkkkkkkkkkkk" + centerIndex);
					if (brithID != 0) {
						update();
						Intent intent4 = new Intent(rcp_addBrithday.this,
								brithInfo.class);
						rcp_addBrithday.this.startActivity(intent4);
					} else {
						if (centerIndex == 100) {
							saveToShape();

						} else {
							savetTodb();
						}
					}
				} else {
					Toast.makeText(rcp_addBrithday.this, "��,��������Ϣ�ٱ���", 0)
							.show();
				}
				Intent intent4 = new Intent(rcp_addBrithday.this,
						Rcp_birthdayActivity.class);
				rcp_addBrithday.this.startActivity(intent4);
				finish();
			} else if (id == R.id.reviseHead) {
				showSetPhotoDialog();
			} else if (id == R.id.rb_setPhoto1) {
				alertDialog.dismiss();
				// ����ϵͳ�����չ���
				Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// ָ������������պ���Ƭ�Ĵ���·��
				intent1.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(tempFile));
				startActivityForResult(intent1, PHOTO_REQUEST_TAKEPHOTO);
			} else if (id == R.id.rb_setPhoto2) {
				alertDialog.dismiss();
				Intent intent2 = new Intent(Intent.ACTION_PICK, null);
				intent2.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent2, PHOTO_REQUEST_GALLERY);
			} else if (id == R.id.rb_setPhoto3) {
			}

		}

	};

	/***
	 * 
	 * ��ȡ��������
	 * 
	 */
	public Cursor readInfo(String ID) {

		Cursor cursor = new sql_brith(this).db.query(sqlVolue.TABLE_brith_name,
				null, "_id" + "=?", new String[] { ID }, null, null, null);

		return cursor;
	}

	public void setInfo() {
		Cursor cursor = null;
		try {
			cursor = readInfo(brithID + "");
			if (cursor != null) {
				// �α��±����
				cursor.moveToPosition(0);
				// ȡ�α��е�ֵ
				while (true) {
					// �ж��Ƿ������
					if (cursor.isAfterLast()) {
						break;
					}
					String name = cursor.getString(1);
					add_edit[0].setText(name);
					String sex = cursor.getString(3);
					String photo = cursor.getString(4);
					data1.setBrithPer_photo(photo);
					Bitmap map = new imageUtil().getBitmapTodifferencePath(
							photo + "", this);
					if (map != null) {
						ib_upphoto.setImageBitmap(map);
					} else {
						ib_upphoto
								.setBackgroundResource(sex.equals("��") ? R.drawable.defaultboy
										: R.drawable.defaultgirl);
					}
					int year = cursor.getInt(5);
					int month = cursor.getInt(6);
					int date = cursor.getInt(7);
					data1.setGregorianYear(year);
					data1.setGregorianDate(date);
					data1.setGregorianMouth(month);
					String gongli = year + "-" + month + "-" + date;
					btn_setBrith.setText(gongli);
					// tv_birth_1stline.setText(gongli);
					// String year_ = otherUtil.cyclicalm(year);
					//
					// String day_ = calendarUtil.getChineseDay(year, month,
					// date);
					// String month_ = calendarUtil.getChineseMonth(year, month,
					// date);
					// tv_birth_2ndline.setText(year_ + "��(" + year + ")" +
					// month_ +
					// day_);
					int ind = sex.equals("��") ? 0 : 1;
					if (ind == 1) {
						nv.setChecked(true);
					} else {
						nan.setChecked(true);
					}
					add_edit[1].setText(cursor.getString(8));
					add_edit[2].setText(cursor.getString(11));

					data1.setBrithPer_name(add_edit[0].getText().toString());
					// data1.setBrithPer_age(otherUtil.getCurYear() -
					// data1.gregorianYear);
					// data1.setBrithPer_sex((group.getCheckedRadioButtonId() ==
					// R.id.add_brith_nan) ? "��"
					// : "Ů");

					// data1.setBrithPer_phone(add_edit[1].getText().toString());
					// data1.setBrithPer_animals(otherUtil.getAnimals(data1.gregorianYear));
					// data1.setBrithPer_constellation(otherUtil.getconstellation(
					// data1.gregorianMouth, data1.gregorianDate));
					data1.setBrithPer_beizhuInfo(add_edit[2].getText()
							.toString());
					cursor.moveToNext();
				}
			}
		} finally {
			if (cursor != null) {

				try {

					cursor.close();

				} catch (Exception e) {

				}

			}

		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	/***
	 * 
	 * �����û����
	 * 
	 */
	public void saveToShape() {
		isOK = false;
		editor.putString("name", add_edit[0].getText().toString());
		editor.putString("sex",
				(group.getCheckedRadioButtonId() == R.id.add_brith_nan) ? "��"
						: "Ů");
		editor.putString("email", activityVolues.loadName);
		boolean id = editor.commit();
		Log.e("huhuhuhu", id + "");
	}

	/**
	 * 
	 * ������ݵ�DB
	 * 
	 */
	public void savetTodb() {
		// ÿ��ֻ�ܱ���һ��
		isOK = false;
		db_brith.insert(set());
		// δ���ݵ�����++
		activityVolues.backupCount++;
		Log.e(".............>>>", activityVolues.backupCount + "");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		switch (requestCode) {

		case SEND_SEARCH_NAME: {
			if (data == null) {
				System.out.println("oooooooooooooooooooooooooooo");
				break;
			}
			Bundle b = data.getExtras(); // dataΪB�лش���Intent
			String str = b.getString("add_name");
			add_edit[0].setText(str);
		}
			break;
		case SEND_SEARCH_PHONE: {
			if (data == null) {
				System.out.println("oooooooooooooooooooooooooooo");
				break;
			}
			Bundle b = data.getExtras(); // dataΪB�лش���Intent
			String str = b.getString("add_phone");
			add_edit[1].setText(str);
		}
			break;
		case PHOTO_REQUEST_TAKEPHOTO:
			startPhotoZoom(Uri.fromFile(tempFile), 150);
			if (centerIndex == 100) {
				editor.putString("photo", tempFile.getPath());
			}
			data1.setBrithPer_photo(tempFile.getPath());
			break;

		case PHOTO_REQUEST_GALLERY:
			if (data != null) {

				startPhotoZoom(data.getData(), 150);
				// ��һ���ڲ���ַ

				if (centerIndex == 100) {
					editor.putString("photo", data.getDataString());
				}
				data1.setBrithPer_photo(data.getDataString());
				System.out.println("----2222222----------------->>"
						+ data.getDataString());

			}
			break;

		case PHOTO_REQUEST_CUT:
			if (data != null)
				setPicToView(data);
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	public void update() {
		isOK = false;
		db_brith.update(set(), brithID + "");
		Log.e("oooooooooo", data1.getBrithPer_name());
	}

	public brith_ListItem set() {
		data1.setBrithPer_name(add_edit[0].getText().toString());
		data1.setBrithPer_age(otherUtil.getCurYear() - data1.gregorianYear);
		data1.setBrithPer_sex((group.getCheckedRadioButtonId() == R.id.add_brith_nan) ? "��"
				: "Ů");

		data1.setBrithPer_phone(add_edit[1].getText().toString());
		data1.setBrithPer_animals(otherUtil.getAnimals(data1.gregorianYear));
		data1.setBrithPer_constellation(otherUtil.getconstellation(
				data1.gregorianMouth, data1.gregorianDate));
		data1.setBrithPer_beizhuInfo(add_edit[2].getText().toString());

		return data1;

	}

	/***
	 * 
	 * ʱ�������
	 * 
	 */
	public void showDateTimePicker() {
		LayoutInflater inflater = LayoutInflater.from(rcp_addBrithday.this);
		View timepickerview = inflater.inflate(R.layout.selectbirthday, null);
		timepickerview.setMinimumWidth(getWindowManager().getDefaultDisplay()
				.getWidth());
		ScreenInfo screenInfo = new ScreenInfo(rcp_addBrithday.this);
		wheelMain = new WheelMain(timepickerview);
		wheelMain.screenheight = screenInfo.getHeight();
		Calendar calendar = Calendar.getInstance();
		// calendar.setTime(dateFormat.parse(time));����ָ��ʱ��
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		wheelMain.setTime(year, month, day);
		dialog = new AlertDialog.Builder(this).setView(timepickerview).show();

		Window window = dialog.getWindow();
		window.setGravity(Gravity.BOTTOM); // �˴���������dialog��ʾ��λ��
		window.setWindowAnimations(R.style.mystyle); // ��Ӷ���

		Button btn = (Button) timepickerview
				.findViewById(R.id.btn_datetime_sure);
		btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				isOK = true;
				btn_setBrith.setText(wheelMain.getTime());
				data1.setGregorianYear(wheelMain.getYear());
				data1.setGregorianMouth(wheelMain.getMonth());
				data1.setGregorianDate(wheelMain.getDay());

				editor.putString("year", wheelMain.getYear() + "");
				editor.putString("month", wheelMain.getMonth() + "");
				editor.putString("date", wheelMain.getDay() + "");
				btn_setBrith.setHint(wheelMain.getTime());
				
				
				 Intent intent1 = new Intent("android.alarm.demo.action");
		            PendingIntent sender = PendingIntent.getBroadcast(rcp_addBrithday.this,
		                    0, intent1, 0);

		            // We want the alarm to go off 30 seconds from now.
		            Calendar calendar = Calendar.getInstance();
		            calendar.setTimeInMillis(System.currentTimeMillis());
//		            calendar.add(Calendar.SECOND, 15);
		            calendar.add( data1.getGregorianMouth(), data1.getGregorianDate());
		            // Schedule the alarm!
		            AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
//		            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
		            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
				Toast.makeText(rcp_addBrithday.this, "15秒后提醒", Toast.LENGTH_LONG).show();
				
				
				
				dialog.dismiss();
			}
		});

	}

	/**
	 * ��ʾ����ͷ���Dialog
	 * 
	 */
	private void showSetPhotoDialog() {
		// ��ʼ���Զ��岼�ֲ���
		LayoutInflater layoutInflater = getLayoutInflater();
		// Ϊ�����������OnClickListener�л�ȡ�������������ݣ����붨��Ϊfinal����.
		View customLayout = layoutInflater.inflate(
				R.layout.showsetphototdialog,
				(ViewGroup) findViewById(R.id.customDialog));

		for (int i = 0; i < rb_dialog.length; i++) {
			rb_dialog[i] = (RadioButton) customLayout
					.findViewById(RadioButtonID[i]);
			rb_dialog[i].setOnClickListener(onClickListener);
		}

		alertDialog = new AlertDialog.Builder(this).setView(customLayout)
				.show();

		Window window = alertDialog.getWindow();
		window.setGravity(Gravity.BOTTOM); // �˴���������dialog��ʾ��λ��
		window.setWindowAnimations(R.style.mystyle); // ��Ӷ���
	}

	private void startPhotoZoom(Uri uri, int size) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// cropΪtrue�������ڿ�����intent��������ʾ��view���Լ���
		intent.putExtra("crop", "true");

		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		// outputX,outputY �Ǽ���ͼƬ�Ŀ��
		intent.putExtra("outputX", size);
		intent.putExtra("outputY", size);
		intent.putExtra("return-data", true);

		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	// �����м��ú��ͼƬ��ʾ��UI������
	private void setPicToView(Intent picdata) {
		Bundle bundle = picdata.getExtras();

		if (bundle != null) {
			Bitmap photo = bundle.getParcelable("data");
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);//
			// Drawable drawable = new BitmapDrawable(photo);
			ib_upphoto.setImageBitmap(photo);
		}
	}

	// ʹ��ϵͳ��ǰ���ڼ��Ե�����Ϊ��Ƭ�����
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";

	}
}
