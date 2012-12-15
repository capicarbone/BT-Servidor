package personal.bt.server.backtasks;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.widget.TextView;

public class RecibirConexionTask extends AsyncTask<Void, String, String>{
	
	private final String NOMBRE_APP = "ServidorBT";
	private final UUID UUID_APP;
	private final String UUDI_STR = "f170ff30-421f-11e2-a25f-0800200c9a66";
	
	private BluetoothServerSocket servidorSocket;
		
	private BluetoothAdapter adapter;
	private TextView respuestaView;
	
	
	public RecibirConexionTask(BluetoothAdapter adapter, TextView view){
		
		this.respuestaView = view;
		this.adapter = adapter;
		
		UUID_APP = UUID.fromString(UUDI_STR);
		
		try{
			servidorSocket = adapter.listenUsingRfcommWithServiceRecord(NOMBRE_APP, UUID_APP);
		} catch (IOException e) {}
				
	}
	
	public void enviarMensaje( BluetoothSocket socket,  String mensaje){
			
			OutputStream output = null;
			byte[] buffer = mensaje.getBytes();		
			
			try {
				output = socket.getOutputStream();
			} catch (IOException e) {}
			
			try {
				output.write(buffer);
			} catch (IOException e) {	}
			
		}
	
	private String recibirMensajeLoop(BluetoothSocket socket){
		
		byte[] buffer = new byte[1024];
		InputStream input = null;
		String respuesta;
		int bytes_recibidos = 5;
		
		try {
			input = socket.getInputStream();
		} catch (IOException e) {
			respuesta = "No se pudo obtener el Stream de entrada";
		}
		
		try {
			bytes_recibidos = input.read(buffer);			
		} catch (IOException e) {
			respuesta = "No se pudo realizar la lectura. Error " + e.toString();
		
			return respuesta;
		}
						
		respuesta = new String(buffer);
		//respuesta = Integer.toString(bytes_recibidos);
		return respuesta;
				
	}

	@Override
	protected String doInBackground(Void... params) {
		
		BluetoothSocket entrante = null;
		String respuesta;
		
		publishProgress("Esperando conexión...");
		respuesta = "Conexión establecida";
		
		while(true){
			try {
				entrante = servidorSocket.accept();
				
			} catch (Exception e) {
				respuesta = "Recepción de conexión fallida";
				break;
			}
			
			if (entrante != null){
								
				respuesta = recibirMensajeLoop(entrante);
				enviarMensaje(entrante, "Hola, lo recibí");
				
				try {
					servidorSocket.close();					
				} catch (IOException e) {					
					respuesta = "Cierre del socket fallido";
				}
				break;
			}
		}			
		
		return respuesta;
	}
	
	protected void onProgressUpdate(String... respuesta){
		
		respuestaView.setText(respuesta[0]);
	}
	
	protected void onPostExecute(String resultado){
		
		respuestaView.setText(resultado);
		
		try {
			servidorSocket.close();
		} catch (IOException e) {
			
		}
	}

}
