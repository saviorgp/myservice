package com.myservice.model.transaction;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.myservice.utils.AndroidUtils;


/**
 * Classe para controlar a thread
 * 
 * @author Jos� Carlos
 * 
 */
public class TransactionTask extends AsyncTask<Void, Void, Boolean> {
	
	private static final String TAG = "TransacaoTask";
	private final Context context;
	private final ITransaction transacao;
	private ProgressDialog progresso;
	private Throwable exceptionErro;
	private int aguardeMsg;
	
	public TransactionTask(Context context, ITransaction transacao, int aguardeMsg) {
		
		this.context = context;
		this.transacao = transacao;
		this.aguardeMsg = aguardeMsg;
	}
	
	@Override
	protected void onPreExecute() {
		
		super.onPreExecute();
		abrirProgress();
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		
		try {
			
			transacao.execute();
			
		} catch (Throwable e) {
			
			Log.e(TAG, e.getMessage(), e);
			// Salva o erro e retorna false
			this.exceptionErro = e;
			
			return false;
			
		} finally {
			
			try {
				
				fecharProgress();
				
			} catch (Exception e) {
				
				Log.e(TAG, e.getMessage(), e);
			}
		}
		// OK
		return true;
	}
	
	@Override
	protected void onPostExecute(Boolean ok) {
		
		if (ok) {
			transacao.updateView();
			
		} else {
			AndroidUtils.alertDialog(context, "Erro: " + exceptionErro.getMessage());
		}
	}
	
	public void abrirProgress() {
		
		try {
			
			progresso = ProgressDialog.show(context, "", context.getString(aguardeMsg));
			
		} catch (Throwable e) {
			
			Log.e(TAG, e.getMessage(), e);
		}
	}
	
	public void fecharProgress() {
		
		try {
			
			if (progresso != null) {
				
				progresso.dismiss();
			}
			
		} catch (Throwable e) {
			
			Log.e(TAG, e.getMessage(), e);
		}
	}
}
