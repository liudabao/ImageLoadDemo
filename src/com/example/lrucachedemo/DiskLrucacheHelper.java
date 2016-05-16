package com.example.lrucachedemo;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.R.bool;
import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.jakewharton.disklrucache.DiskLruCache;

public class DiskLrucacheHelper {
	
	private static DiskLruCache diskLrucache;
	
	public static void openCache(Context context, int appVersion, int size){
		try {
			
			if(!Environment.isExternalStorageRemovable()||
					Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
				Log.e("DiskLrucache", context.getExternalCacheDir()+"");
				diskLrucache=DiskLruCache.open(context.getExternalCacheDir(), appVersion, 1, size);
				
			}
			else{
				diskLrucache=DiskLruCache.open(context.getCacheDir(), appVersion, 1, size);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	public static void put(String key, Bitmap values){
		try {
			
			if(diskLrucache==null){
				throw new IllegalStateException("缓存不存在");
			}
			DiskLruCache.Editor editor=diskLrucache.edit(hashKeyForDisk(key));
			OutputStream out=editor.newOutputStream(0);
			boolean success=values.compress(Bitmap.CompressFormat.PNG, 100, out);
			if (success) {
				editor.commit();
			    diskLrucache.flush();
			}
			else{
				editor.abort();
			}
		    
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static Bitmap get(String key){

		try {
			
			if(diskLrucache==null){
				throw new IllegalStateException("缓存不存在");
			}
			DiskLruCache.Snapshot snapshot=diskLrucache.get(hashKeyForDisk(key));
			if(snapshot!=null){
				InputStream in=snapshot.getInputStream(0);
				Bitmap bitmap=BitmapFactory.decodeStream(in);
				return bitmap;
			}
		    
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	
	public static String hashKeyForDisk(String key) {
		  
		String cacheKey;
		try
		{		    
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
		    mDigest.update(key.getBytes());
		    cacheKey = bytesToHexString(mDigest.digest());
		  
		}
		catch (NoSuchAlgorithmException e) {		   			
			cacheKey = String.valueOf(key.hashCode());
		  
		}
		return cacheKey;
	}

	private static String bytesToHexString(byte[] bytes) {
		  
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
		    
			String hex = Integer.toHexString(0xFF & bytes[i]);
		    if (hex.length() == 1) {
		      sb.append('0');
		    }
		    sb.append(hex);
		  }
		  return sb.toString();
	}

}
