package src.com.rcp.day;

import org.json.JSONObject;

import rcp.com.other.ActivityMeg;
import rcp.com.other.MyHorizontalScrollView;
import rcp.com.other.SizeCallbackForMenu;
import rcp.com.src.brithUtil.CalendarUtil;
import rcp.com.src.brithUtil.imageUtil;
import rcp.com.src.brithUtil.otherUtil;
import rcp.com.src.volues.activityVolues;
import rcp.com.src.volues.sqlVolue;
import src.com.rcp.Sql.sql_brith;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/***
 * 
 * 生日详情
 * 
 * @author toshiba
 * 
 */
public class brithInfo extends Activity {
	/** 跳转过来ItemID **/
	private int ItemID;
	/** 头像 */
	private ImageView iv_photo;
	/** 性别 */
	private ImageView iv_gender;

	private TextView tv_name;
	private TextView tv_birth_1stline;
	private TextView tv_birth_2ndline;
	private TextView tv_zodiac;
	private TextView tv_astra;
	private TextView tv_nextage;
	private TextView tv_lefttime;
	private TextView tv_nextdate;

	/** 修改按钮 */
	private Button btn_updata;

	private Button btn_sms;

	private Button btn_phone;

	private CalendarUtil calendarUtil;

	private String phone;

	private Button back;

	/** 百科 */
	private MyHorizontalScrollView scrollView;

	private View birthinfo;

	private View baikeView;

	private Button Btn_baike;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.birth_info);
		ActivityMeg.getInstance().addActivity(this);
		ItemID = getIntent().getIntExtra("ItemID", 0);
		calendarUtil = new CalendarUtil();
		ViewInit();
		if (ItemID != 0) {
			DataInit();
		} else {
			myCenterDataInit();
		}
	}

	public void ViewInit() {
		back = (Button) this.findViewById(R.id.brithInfo_back);
		back.setOnClickListener(onClickListener);
		iv_photo = (ImageView) this.findViewById(R.id.avatar);
		iv_gender = (ImageView) this.findViewById(R.id.gender);
		tv_name = (TextView) this.findViewById(R.id.infoname);
		tv_birth_1stline = (TextView) this.findViewById(R.id.birth_1stline);
		tv_birth_2ndline = (TextView) this.findViewById(R.id.birth_2ndline);

		tv_zodiac = (TextView) this.findViewById(R.id.zodiac);
		tv_astra = (TextView) this.findViewById(R.id.astra);
		tv_nextage = (TextView) this.findViewById(R.id.nextage);
		tv_lefttime = (TextView) this.findViewById(R.id.lefttime);
		tv_nextdate = (TextView) this.findViewById(R.id.nextdate);

		btn_updata = (Button) this.findViewById(R.id.title_xiugai);
		btn_updata.setOnClickListener(onClickListener);
		btn_sms = (Button) this.findViewById(R.id.birth_info_sendmsg);
		btn_sms.setOnClickListener(onClickListener);
		btn_phone = (Button) this.findViewById(R.id.birth_info_phone);
		btn_phone.setOnClickListener(onClickListener);

		// scrollView = (MyHorizontalScrollView)
		// findViewById(R.id.myScrollView);
		// birthinfo = LayoutInflater.from(this)
		// .inflate(R.layout.birth_info, null);
		// Btn_baike = (Button) birthinfo.findViewById(R.id.baike_handler);
		// Btn_baike.setOnClickListener(onClickListener);
		// baikeView = LayoutInflater.from(this).inflate(R.layout.baike_new,
		// null);
		// View rightView = new View(this);// 右边透明视图
		// leftView.setBackgroundColor(Color.TRANSPARENT);
		// rightView.setBackgroundColor(Color.TRANSPARENT);
		// final View[] children = new View[] { birthinfo, baikeView };
		// 初始化滚动布局
		// scrollView.initViews(children, new SizeCallbackForMenu(Btn_baike),
		// baikeView);
		// // scrollView.setLeftBtn(leftButton);
		// scrollView.setRightBtn(Btn_baike);

	}

	OnClickListener onClickListener = new OnClickListener() {

		public void onClick(View v) {
			int id = v.getId();
			if (id == R.id.title_xiugai) {
				Intent intent = new Intent(brithInfo.this,
						rcp_addBrithday.class);
				intent.putExtra("brithID", ItemID);
				brithInfo.this.startActivity(intent);
				finish();
			} else if (id == R.id.birth_info_sendmsg) {
				Intent intent = new Intent(brithInfo.this, rcp_msg_wish.class);
				intent.putExtra("phone", phone);
				intent.putExtra("brithID", ItemID);
				brithInfo.this.startActivity(intent);
			} else if (id == R.id.birth_info_phone) {
				// 初始化信使并设置启动应用动作
				Intent intent = new Intent(Intent.ACTION_VIEW);
				// 数据初始化
				Uri uri = Uri.parse("tel:" + phone);
				intent.setData(uri);
				startActivity(intent);
			} else if (id == R.id.brithInfo_back) {
				Intent intent = new Intent(brithInfo.this,
						Rcp_birthdayActivity.class);
				brithInfo.this.startActivity(intent);
			} else if (id == R.id.baike_handler) {
				scrollView.clickRightButton(Btn_baike.getMeasuredWidth());
			}
		}
	};

	/**
	 * 个人中心数据的初始化
	 * 
	 */
	public void myCenterDataInit() {
		SharedPreferences sharedPreferences = getSharedPreferences(
				activityVolues.shape_name, MODE_PRIVATE);

		String img1 = sharedPreferences.getString("photo", "null");

		String sex1 = sharedPreferences.getString("sex", "男");
		Bitmap map = new imageUtil().getBitmapTodifferencePath(img1, this);
		if (map != null) {
			iv_photo.setImageBitmap(map);
		} else {
			iv_photo.setBackgroundResource(sex1.equals("男") ? R.drawable.defaultboy
					: R.drawable.defaultgirl);
		}
		String name1 = sharedPreferences.getString("name", "我");
		tv_name.setText(name1);
		String year1 = sharedPreferences.getString("year", "1992");
		String month1 = sharedPreferences.getString("month", "02");
		String date1 = sharedPreferences.getString("date", "26");
		int iyear = Integer.parseInt(year1);
		int imonth = Integer.parseInt(month1);
		int idate = Integer.parseInt(date1);
		String gongli = iyear + "-" + imonth + "-" + idate;
		tv_birth_1stline.setText(gongli);
		String year_ = otherUtil.cyclicalm(iyear);

		String day_ = calendarUtil.getChineseDay(iyear, imonth, idate);
		String month_ = calendarUtil.getChineseMonth(iyear, imonth, idate);
		tv_birth_2ndline.setText(year_ + "年(" + iyear + ")" + month_ + day_);
		int age = otherUtil.getCurYear() - iyear;
		String ta = sex1.equals("男") ? "他" : "她";

		tv_nextage.setText("距离" + ta + (age + 1) + "岁生日还有");
		long num = otherUtil.getForMyBrithday(iyear + "", imonth + "", idate
				+ "");

		tv_lefttime.setText(num + "");

		tv_nextdate.setText(otherUtil.getForMyBrithdayToDate(num));
		iv_gender.setBackgroundResource(sex1.equals("男") ? R.drawable.boy
				: R.drawable.girl);

		tv_zodiac.setText(otherUtil.getAnimals(iyear));

		tv_astra.setText(otherUtil.getconstellation(imonth, idate));
	}

	public void DataInit() {

		Cursor cursor = null;

		try {
			cursor = readInfo(ItemID + "");
			if (cursor != null) {
				// 游标下标归零
				cursor.moveToPosition(0);
				// 取游标中的值
				while (true) {
					// 判断是否在最后
					if (cursor.isAfterLast()) {
						break;
					}
					int id = cursor.getInt(0);
					String name = cursor.getString(1);
					tv_name.setText(name);
					String sex = cursor.getString(3);
					String photo = cursor.getString(4);
					Bitmap map = new imageUtil().getBitmapTodifferencePath(
							photo, this);
					if (map != null) {
						iv_photo.setImageBitmap(map);
					} else {
						iv_photo.setBackgroundResource(sex.equals("男") ? R.drawable.defaultboy
								: R.drawable.defaultgirl);
					}
					int year = cursor.getInt(5);
					int month = cursor.getInt(6);
					int date = cursor.getInt(7);
					String gongli = year + "-" + month + "-" + date;
					tv_birth_1stline.setText(gongli);
					String year_ = otherUtil.cyclicalm(year);

					String day_ = calendarUtil.getChineseDay(year, month, date);
					String month_ = calendarUtil.getChineseMonth(year, month,
							date);
					tv_birth_2ndline.setText(year_ + "年(" + year + ")" + month_
							+ day_);
					int age = cursor.getInt(2);
					String ta = sex.equals("男") ? "他" : "她";

					tv_nextage.setText("距离" + ta + (age + 1) + "岁生日还有");
					long num = otherUtil.getForMyBrithday(year + "",
							month + "", date + "");

					tv_lefttime.setText(num + "");

					tv_nextdate.setText(otherUtil.getForMyBrithdayToDate(num)
							+ "");
					iv_gender
							.setBackgroundResource(sex.equals("男") ? R.drawable.boy
									: R.drawable.girl);
					phone = cursor.getString(8);
					String animals = cursor.getString(9);
					tv_zodiac.setText(animals);
					String lation = cursor.getString(10);
					tv_astra.setText(lation);
					String beizhu = cursor.getString(11);

					System.out.println(id + "  " + name + "  " + age + "  "
							+ sex + "  " + photo + "  " + year + "  " + month
							+ "  " + date + "  " + phone + "  " + animals
							+ "  " + lation + "  " + beizhu + "  ");
					cursor.moveToNext();
				}
			}
		} finally {
			if (cursor != null) {

				try {

					cursor.close();

				} catch (Exception e) {

					// ignore this

				}
			}
		}
	}

	/***
	 * 
	 * 读取生日内容
	 * 
	 */
	public Cursor readInfo(String ID) {

		Cursor cursor = new sql_brith(this).db.query(sqlVolue.TABLE_brith_name,
				null, "_id" + "=?", new String[] { ID }, null, null, null);

		return cursor;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		finish();
		Log.e("----------uuu", "myCenter_消亡");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.e("----------uuu", "myCenter_暂停");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e("----------uuu", "myCenter_回复");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.e("----------uuu", "myCenter_start");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		Log.e("----------uuu", "myCenter_stop");
	}

}
