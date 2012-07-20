package com.hernan.keepmeup;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

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

	int delayMain = 20000; // tiempo espera inicial
    int periodMain = delayMain; // cada cuanto tiempo volvera a conectar    
    final Timer timerMain = new Timer();
	
    final int delay = 5000; // tiempo hasta desconectar
    final Timer timer = new Timer();
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void onCreate() {
		super.onCreate();
		startservice();
	}
	
	public void onDestroy() {
		super.onDestroy();
		stopSheduler();
	}

	// arrancar timer para ejecutar el ativar/desactivar de conexion 3g
	private void startservice() {
		timerMain.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println( "ON" );
				
				timer.schedule(new TimerTask() {
		            public void run()   
		            {
		            	System.out.println("OFF\n");
		            }

		        }, delay);  
				
			}
		}, delayMain, periodMain);
	}
	
	private void stopSheduler() {
		System.out.println("OFF\n");
		
		timer.cancel();
		timer.purge();
		timerMain.cancel();
		timerMain.purge();
	}
}