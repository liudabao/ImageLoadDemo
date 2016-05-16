package com.example.lrucachedemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {
	Button btn_1;
	ImageView picture;
	LrucacheHelper lrucacheHelper;
	DiskLrucacheHelper diskLrucacheHelper;
	HttpHelper httpHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initLoader(MainActivity.this);
	}

	private void initView(){
		btn_1=(Button)findViewById(R.id.button1);
		picture=(ImageView)findViewById(R.id.imageView1);

		btn_1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new MyTask().execute("http://i-7.vcimg.com/trim/d1ffb217994bb0ba5d09cc4b263cf9f9123065/trim.jpg");
				
			}
		});
		
	}
	
	private void initLoader(Context context){
		LrucacheHelper.openLrucache();
		DiskLrucacheHelper.openCache(context, getAppVersion(context), 10 * 1024 * 1024);
	}
	
	private int getAppVersion(Context context) {
	    try {
	      PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
	      return info.versionCode;
	    } catch (NameNotFoundException e) {
	      e.printStackTrace();
	    }
	    return 1;
	  
	}
	
	class MyTask extends AsyncTask<String, Bitmap ,Boolean>{

		@Override
		protected Boolean doInBackground(String... url) {
			// TODO Auto-generated method stub
			
			String key=url[0];            
			Bitmap bitmap=lrucacheHelper.get(key);
			if(bitmap==null){
				Log.e("ImageLoader", "0");
				bitmap=diskLrucacheHelper.get(key);
				if(bitmap==null){
					Log.e("ImageLoader", "00");
					bitmap=HttpHelper.getBitmap(key);					
					//picture.setImageBitmap(bitmap);
					
					if(bitmap!=null){
						publishProgress(bitmap);
						lrucacheHelper.put(key, bitmap);
						diskLrucacheHelper.put(key, bitmap);
						Log.e("ImageLoader", "3");
					}
					else {
						Log.e("ImageLoader", "000");
					}
					
				}
				else{
					//picture.setImageBitmap(bitmap);
					Log.e("ImageLoader", "2");
					publishProgress(bitmap);
					lrucacheHelper.put(key, bitmap);
					
				}
			}
			else{
				//picture.setImageBitmap(bitmap);
				
				Log.e("ImageLoader", "1");
				publishProgress(bitmap);
			}
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Bitmap...values){
			Bitmap bitmap=values[0];
			if(bitmap!=null){
				picture.setImageBitmap(bitmap);
			}

		}
		
		@Override
		protected void onPostExecute(Boolean result){

		}
		
	}
}
