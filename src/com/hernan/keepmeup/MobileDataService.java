package com.hernan.keepmeup;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;

/*
 * Servicio de programacion de conexion 3g
 * 
 * La idea es que el usuario escoja el tiempo
 * a esperar para volver a conectar y desconectar
 * el 3G. El primer timer depende del tiempo seleccionado,
 * segundo timer sera el tiempo en que la conexion 3G
 * se mantendra activada por lo que seria un timer
 * dentro de un timer.
 * 
 * timerScheduler = 30 minutos
 * timerConnection = 30 segundos
 * 
 * el tiempo de conexion podria variar de acuerdo a lo
 * necesitado.
 */
public class MobileDataService extends Service {

	int delayMain = 1200000; // tiempo para conectar (20 minutos)
    int periodMain = delayMain; // cada cuanto tiempo volvera a conectar    
    final Timer timerMain = new Timer();
	
    final int delay = 120000; // tiempo antes de desconectar (2 minutos)
    final Timer timer = new Timer();
	
    ConnectivityManager comManager;
    
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void onCreate() {
		super.onCreate();
		
		comManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		
		// Make sure data connection is OFF
		if(isDataConnectionOn())
			setMobileDataEnabled(false);
		
		startScheduler();
	}
	
	public void onDestroy() {
		super.onDestroy();
		
		stopSheduler();
	}

	// testing only
	public void testerMethod(){
		if(isDataConnectionOn())
			setMobileDataEnabled(false);
		
		setMobileDataEnabled(true);
	}
	
	// arrancar timer para ejecutar el ativar/desactivar de conexion 3g
	private void startScheduler() {
		timerMain.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println( "ON" );
				setMobileDataEnabled(true);
				
				timer.schedule(new TimerTask() {
		            public void run()   
		            {
		            	System.out.println("OFF\n");
		            	setMobileDataEnabled(false);
		            }

		        }, delay);  
				
			}
		}, delayMain, periodMain);
	}
	
	private void stopSheduler() {
		System.out.println("OFF\n");
		
		if(isDataConnectionOn())
			setMobileDataEnabled(false);
		
		timer.cancel();
		timer.purge();
		timerMain.cancel();
		timerMain.purge();
		
		if(isDataConnectionOn())
			setMobileDataEnabled(false);
	}
	
	public boolean isDataConnectionOn() {
    	try {
    		if (comManager.getActiveNetworkInfo().isConnected()) {
    			Log.d("KMU", "Data Connection On");
    			return true;
    		} else {
    			Log.d("KMU", "Data Connection off");
    			return false;
    		}
    	} catch (NullPointerException e) {
    		// No Active Connection
    		Log.d("KMU", "No Active Connection");
    		return false;
    	}
	}
	
	private void setMobileDataEnabled(boolean enabled) {
        Class comManagerClass;
		try {
			comManagerClass = Class.forName(comManager.getClass().getName());
			final Field iConnectivityManagerField = comManagerClass.getDeclaredField("mService");
	        iConnectivityManagerField.setAccessible(true);
	        final Object iConnectivityManager = iConnectivityManagerField.get(comManager);
	        final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
	        final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
	        setMobileDataEnabledMethod.setAccessible(true);
	        
	        setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
	        
	        
	        Log.d("KMU", "*** ACCESS MODIFIER! ***");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}