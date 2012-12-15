package personal.bt.server;

import personal.bt.server.backtasks.RecibirConexionTask;
import personal.bt.server.threads.AcceptThread;
import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class ServidorAct extends Activity {
	
	private BluetoothAdapter btAdapter;
	private static final int PETICION_ACTION_BT = 45;
	
	//private AcceptThread hiloServidor;
	private RecibirConexionTask servidorTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_servidor);			
		
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		
		
		TextView respuesta = (TextView) findViewById(R.id.respuesta);
		
		// Se verifica que se haya encontrado un adaptador
		if (btAdapter != null ){
			
			// Si el adaptador no está activado se solicita su activación
			
			if (!btAdapter.isEnabled()){
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, PETICION_ACTION_BT);
			}
			
			servidorTask = new RecibirConexionTask(btAdapter, respuesta);
			servidorTask.execute();
			
			//hiloServidor = new AcceptThread(btAdapter, respuesta);	
			//hiloServidor.start();
			
		}
				
	}
	
	@Override
	protected void onResume(){
		
		super.onResume();			
				
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_servidor, menu);
		return true;
	}

}
