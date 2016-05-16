package com.example.lrucachedemo;

import android.R.integer;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

public class LrucacheHelper{
	
	private static LruCache<String, Bitmap> lruCache;
	
	public static void openLrucache(){
		
		lruCache=new LruCache<String, Bitmap>((int)Runtime.getRuntime().maxMemory()/8){
			@Override
			protected int sizeOf(String key, Bitmap value){
				
				return value.getRowBytes()*value.getHeight();
			}
		};
	}
	
	public static void put(String key, Bitmap value) {
		lruCache.put(key, value);
	}
	
	public static Bitmap get(String key) {
		//Log.e("Lrucache", key);
		return lruCache.get(key);
	}

}
