package personal.bt.server.threads;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

import android.R;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.widget.ListView;
import android.widget.TextView;

public class AcceptThread extends Thread{
	
	private final BluetoothServerSocket btServerSocket;
	
	private final String NOMBRE_APP = "ServidorBT";
	private final UUID UUID_APP;
	private final String UUDI_STR = "f170ff30-421f-11e2-a25f-0800200c9a66";
	private final BluetoothAdapter btAdapter;
	private TextView respuesta;
	

	public AcceptThread(BluetoothAdapter btAd, TextView res) {
		
		this.btAdapter = btAd;
		this.respuesta = res;
		
		UUID_APP = UUID.fromString(UUDI_STR);
		
		BluetoothServerSocket tmp = null;
		
		try{
			tmp = btAdapter.listenUsingRfcommWithServiceRecord(NOMBRE_APP, UUID_APP);
		} catch (IOException e) {}
		
		btServerSocket = tmp;
		respuesta.setText("Servidor funcionando");
		
	}
	
	public void run(){
		BluetoothSocket entrante = null;
		
		while(true){
			try {
				entrante = btServerSocket.accept();
				
			} catch (Exception e) {
				break;
			}
			
			if (entrante != null){
				
				respuesta.post(new Runnable() {
					
					public void run() {
						respuesta.setText("Conexión establecida");						
					}									
					
				});
				
				try {
					btServerSocket.close();					
				} catch (IOException e) {					
					
				}
				break;
			}
		}
	
	}
	
}
