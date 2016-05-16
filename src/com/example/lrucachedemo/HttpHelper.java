package com.example.lrucachedemo;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils.TruncateAt;
import android.util.Log;

public class HttpHelper {

	public static Bitmap getBitmap(String s){
		HttpURLConnection connection;
		URL url;
		try {
			url=new URL(s);
			Log.e("HttpHelp", s);
			connection=(HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(3000);
			connection.setReadTimeout(3000);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			Log.e("HttpHelp", "2 "+connection.getResponseCode());
			if(connection.getResponseCode()==200){
				Log.e("HttpHelp", "connect success");
				InputStream in=connection.getInputStream();
				Bitmap bitmap=BitmapFactory.decodeStream(in);
				
				return bitmap;
			}
			else {
				Log.e("HttpHelp", "connect failed");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
		
	}
}
