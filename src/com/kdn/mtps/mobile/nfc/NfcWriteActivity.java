package com.kdn.mtps.mobile.nfc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kdn.mtps.mobile.BaseActivity;
import com.kdn.mtps.mobile.MainActivity;
import com.kdn.mtps.mobile.R;
import com.kdn.mtps.mobile.TitleManager;
import com.kdn.mtps.mobile.aria.Cipher;
import com.kdn.mtps.mobile.constant.ConstVALUE;
import com.kdn.mtps.mobile.db.InspectResultMasterDao;
import com.kdn.mtps.mobile.info.CodeInfo;
import com.kdn.mtps.mobile.net.api.ApiManager;
import com.kdn.mtps.mobile.net.api.bean.NfcTagInfo;
import com.kdn.mtps.mobile.util.AnsemUtil;
import com.kdn.mtps.mobile.util.AppUtil;
import com.kdn.mtps.mobile.util.Logg;
import com.kdn.mtps.mobile.util.ToastUtil;
import com.kdn.mtps.mobile.util.thread.ATask;

public class NfcWriteActivity extends BaseActivity implements TitleManager, OnClickListener{

	private static final String TAG = "NFCWriteTag";
	private NfcAdapter mNfcAdapter;
    private IntentFilter[] mWriteTagFilters;
	private PendingIntent mNfcPendingIntent;
	private boolean silent=false;
	private boolean writeProtect = false;
	private Context context;
	
	Button btnName;
	Button btnEqpName;
	EditText editTagName;
	
	int selectedNameNo=-1;
	int selectedEqpNo=-1;
	
	NfcTagInfo tagInfo;
	String strEqpsKey;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nfc_write);
		
		setTitle();
		
		setInit();
		
	}

	@Override
	public void setTitle() {
		Button btnHeaderTitle = (Button)findViewById(R.id.btnHeaderTitle);
		Button btnHeaderLeft = (Button)findViewById(R.id.btnHeaderLeft);
		Button btnHeaderRight = (Button)findViewById(R.id.btnHeaderRight);
		
		btnHeaderTitle.setText("NFC 태그 쓰기");
		
		btnHeaderLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btnHeaderRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppUtil.startActivity(NfcWriteActivity.this, new Intent(NfcWriteActivity.this, MainActivity.class));
			}
		});
	}

	public void setInit() {
		btnName = (Button)findViewById(R.id.btnName);
		btnEqpName = (Button)findViewById(R.id.btnEqpName);
		editTagName = (EditText)findViewById(R.id.editTagName);
		
		btnName.setOnClickListener(this);
		btnEqpName.setOnClickListener(this);
		
		context = getApplicationContext();
        
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

		mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP), 0);
        
        IntentFilter discovery=new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);        
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);

        // Intent filters for writing to a tag
        mWriteTagFilters = new IntentFilter[] { discovery };
	}
	
	public void showNameDialog() {
		
		InspectResultMasterDao inputInsRetDao = InspectResultMasterDao.getInstance(this);
		
		final String strItems[] = CodeInfo.getInstance(this).getTracksNames();
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		dialog.setTitle(getString(R.string.facility_name));
		dialog.setSingleChoiceItems(strItems, selectedNameNo, new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectedNameNo = which;
                btnName.setText(strItems[which]);
                
                //지지물 초기화
                selectedEqpNo=-1;
                btnEqpName.setText(getString(R.string.eqp_no));
                
//                String strTracksKey = CodeInfo.getInstance(NfcWriteActivity.this).getKey(ConstVALUE.CODE_TYPE_TRACKS, strItems[which]);
                editTagName.setText("");
                dialog.dismiss();
			}
		}).show();
	}
	
	public void showEqpNameDialog() {
		String strName = btnName.getText().toString();
				
		if (selectedNameNo <= 0) {
			ToastUtil.show(this, "선로를 선택해 주세요.");
			return;
		}
		
		InspectResultMasterDao inputInsRetDao = InspectResultMasterDao.getInstance(this);
		inputInsRetDao.selectEqpNames(this, strName);
		
		final String strItems[] = CodeInfo.getInstance(this).getEqpsNames();
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		dialog.setTitle(getString(R.string.eqp_no));
		dialog.setSingleChoiceItems(strItems, selectedEqpNo, new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectedEqpNo = which;
                btnEqpName.setText(strItems[which]);
                
//                String strTracksName = btnName.getText().toString();
//                String strTracksKey = CodeInfo.getInstance(NfcWriteActivity.this).getKey(ConstVALUE.CODE_TYPE_TRACKS, strTracksName);
                strEqpsKey = CodeInfo.getInstance(NfcWriteActivity.this).getKey(ConstVALUE.CODE_TYPE_EQPS, strItems[which]);
//                editTagName.setText(strTracksKey + "_" + strEqpsKey);
                
                if (!AnsemUtil.isConnected(NfcWriteActivity.this))
    				AnsemUtil.showAnsemLogin(NfcWriteActivity.this);
    			else {
    				apiTagInfo(strEqpsKey);
    			}
                
                dialog.dismiss();
			}
		}).show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnName:
			showNameDialog();
			break;
		case R.id.btnEqpName:
			showEqpNameDialog();
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		if(mNfcAdapter != null) {
			mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mWriteTagFilters, null);
		} else {
			Toast.makeText(context, "Sorry, No NFC Adapter found.", Toast.LENGTH_SHORT).show();
		}
	}
    
	@Override
	protected void onPause() {
		super.onPause();
		if(mNfcAdapter != null) mNfcAdapter.disableForegroundDispatch(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		if (selectedNameNo < 0 || selectedEqpNo < 0) {
			ToastUtil.show(this, "선로 및 지지물을 선택해 주세요.");
			return;
		}
		
		if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
        	// validate that this tag can be written....
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//            if(supportedTechs(detectedTag.getTechList())) {
	            // check if tag is writable (to the extent that we can
	            if(writableTag(detectedTag)) {
	            	//writeTag here
	            	WriteResponse wr = writeTag(getTagAsNdef(), detectedTag);
	            	String message = (wr.getStatus() == 1? "태그 쓰기가 완료되었습니다. " : "태그 쓰기가 실패하였습니다. "); //+ wr.getMessage();
	            	Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
	            } else {
	            	Toast.makeText(context,"This tag is not writable",Toast.LENGTH_SHORT).show();
	            	
	            }	            
//            } else {
//            	Toast.makeText(context,"This tag type is not supported",Toast.LENGTH_SHORT).show();
//            	Sounds.PlayFailed(context, silent);
//            }
        }
    
	}
	
	
    public WriteResponse writeTag(NdefMessage message, Tag tag) {
        int size = message.toByteArray().length;
        String mess = "";

        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();

                if (!ndef.isWritable()) {
                    return new WriteResponse(0,"Tag is read-only");

                }
                if (ndef.getMaxSize() < size) {
                    mess = "Tag capacity is " + ndef.getMaxSize() + " bytes, message is " + size
                            + " bytes.";
                    return new WriteResponse(0,mess);
                }

                ndef.writeNdefMessage(message);
                if(writeProtect)  ndef.makeReadOnly();
                mess = "Wrote message to pre-formatted tag.";
                return new WriteResponse(1,mess);
            } else {
                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        format.format(message);
                        mess = "Formatted tag and wrote message";
                        return new WriteResponse(1,mess);
                    } catch (IOException e) {
                        mess = "Failed to format tag.";
                        return new WriteResponse(0,mess);
                    }
                } else {
                    mess = "Tag doesn't support NDEF.";
                    return new WriteResponse(0,mess);
                }
            }
        } catch (Exception e) {
            mess = "Failed to write tag";
            return new WriteResponse(0,mess);
        }
    }
    
    private class WriteResponse {
    	int status;
    	String message;
    	WriteResponse(int Status, String Message) {
    		this.status = Status;
    		this.message = Message;
    	}
    	public int getStatus() {
    		return status;
    	}
    	public String getMessage() {
    		return message;
    	}
    }
    
	public static boolean supportedTechs(String[] techs) {
	    boolean ultralight=false;
	    boolean nfcA=false;
	    boolean ndef=false;
	    for(String tech:techs) {
	    	Log.d("Test", "tech :  " + tech);
	    	if(tech.equals("android.nfc.tech.MifareUltralight")) {
	    		ultralight=true;
	    	}else if(tech.equals("android.nfc.tech.NfcA")) { 
	    		nfcA=true;
	    	} else if(tech.equals("android.nfc.tech.Ndef") || tech.equals("android.nfc.tech.NdefFormatable")) {
	    		ndef=true;
	   		
	    	}
	    }
        if(ultralight && nfcA && ndef) {
        	return true;
        } else {
        	return false;
        }
	}
	
    private boolean writableTag(Tag tag) {

        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    Toast.makeText(context,"Tag is read-only.",Toast.LENGTH_SHORT).show();
                    ndef.close(); 
                    return false;
                }
                ndef.close();
                return true;
            } 
        } catch (Exception e) {
            Toast.makeText(context,"Failed to read tag",Toast.LENGTH_SHORT).show();
        }

        return false;
    }
    
    private NdefMessage getTagAsNdef() {
//    	boolean addAAR = false;
//    	String uniqueId = "BTA0040299";       
//        byte[] uriField = uniqueId.getBytes(Charset.forName("US-ASCII"));
//        byte[] payload = new byte[uriField.length + 1];              //add 1 for the URI Prefix
//        payload[0] = 0x01;                                      	//prefixes http://www. to the URI
//        
//        System.arraycopy(uriField, 0, payload, 1, uriField.length);  //appends URI to payload
//        NdefRecord rtdUriRecord = new NdefRecord(
//            NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_URI, new byte[0], payload);
//        
//        NdefRecord rtdUriRecord2 = new NdefRecord(
//                NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], "BTA0040299".getBytes());
//        
//        
//        
//        if(addAAR) {
//        	// note:  returns AAR for different app (nfcreadtag)
//        	return new NdefMessage(new NdefRecord[] {
//            rtdUriRecord, NdefRecord.createApplicationRecord("com.tapwise.nfcreadtag")
//        }); 
//        } else {
//        	return new NdefMessage(new NdefRecord[] {
//        			rtdUriRecord2});
    	
    	String strTag = editTagName.getText().toString();
    	Logg.d("strTag : " + strTag);
    	
		try {
			strTag = Cipher.encrypt(strTag, Cipher.createNfcMasterKey());
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Logg.d("strTag 22: " + strTag);
    	
    	return fromText(strTag);
       }
    
    public static NdefMessage fromText(String text) {
        try {
            byte[] textBytes = text.getBytes();
            byte[] textPayload = new byte[textBytes.length + 3];
            textPayload[0] = 0x02; // Status byte; UTF-8 and "en" encoding.
            textPayload[1] = 'e';
            textPayload[2] = 'n';
            System.arraycopy(textBytes, 0, textPayload, 3, textBytes.length);
            NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                    NdefRecord.RTD_TEXT, new byte[0], textPayload);
            NdefRecord[] records = new NdefRecord[] { record };
            return new NdefMessage(records);
        } catch (NoClassDefFoundError e) {
            return null;
        }
    }
    
    
    public void apiTagInfo(final String eqpNo) {
		
		ATask.executeVoidProgress(this, R.string.get_nfc_tag_list, true, new ATask.OnTaskProgress() {
			public void onPre() {
			}

			public void onBG() {
				tagInfo = ApiManager.tagInfo(eqpNo);
			}

			@Override
			public void onPost() {
				if (tagInfo != null && ApiManager.RESULT_OK.equals(tagInfo.code)) {
					editTagName.setText(tagInfo.nfcTagInfo.TAG);
				} else {
					ToastUtil.show(NfcWriteActivity.this, "태그정보를 가져올 수 없습니다.");
				}
			}
			
			@Override
			public void onCancel() {
			}
			
		});
		
	}
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	
		if (requestCode == AnsemUtil.REQUEST_ANSEM_CODE) {
			if (AnsemUtil.isConnected(this)) {
				apiTagInfo(strEqpsKey);
			}
		}
		
	}
}

	