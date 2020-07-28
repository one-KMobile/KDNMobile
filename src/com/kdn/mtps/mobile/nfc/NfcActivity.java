package com.kdn.mtps.mobile.nfc;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.kdn.mtps.mobile.BaseActivity;
import com.kdn.mtps.mobile.MainActivity;
import com.kdn.mtps.mobile.R;
import com.kdn.mtps.mobile.TitleManager;
import com.kdn.mtps.mobile.aria.Cipher;
import com.kdn.mtps.mobile.db.InspectResultMasterDao;
import com.kdn.mtps.mobile.inspect.InspectInfo;
import com.kdn.mtps.mobile.inspect.InspectResultDetailActivity;
import com.kdn.mtps.mobile.nfc.record.NdefMessageParser;
import com.kdn.mtps.mobile.nfc.record.ParsedNdefRecord;
import com.kdn.mtps.mobile.util.AppUtil;
import com.kdn.mtps.mobile.util.Logg;
import com.kdn.mtps.mobile.util.ToastUtil;

public class NfcActivity extends BaseActivity implements TitleManager {

	public static final String TAG = "NfcDemo";
	public static final String MIME_TEXT_PLAIN = "text/plain";
			
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private NdefMessage mNdefPushMessage;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nfc_main);
		
		setTitle();
		
		setInit();
		
		resolveIntent(getIntent());
		
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            ToastUtil.show(this, "This device doesn't support NFC.");
            finish();
            return;
 
        }
     
		mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mNdefPushMessage = new NdefMessage(new NdefRecord[] { newTextRecord(
                "Message from NFC Reader :-)", Locale.ENGLISH, true) });
        
	}
	
	@Override
	public void setTitle() {
		Button btnHeaderTitle = (Button)findViewById(R.id.btnHeaderTitle);
		Button btnHeaderLeft = (Button)findViewById(R.id.btnHeaderLeft);
		Button btnHeaderRight = (Button)findViewById(R.id.btnHeaderRight);
		
		btnHeaderTitle.setText("NFC 태그 리딩");
		
		btnHeaderLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btnHeaderRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppUtil.startActivity(NfcActivity.this, new Intent(NfcActivity.this, MainActivity.class));
			}
		});
	}
	
	public void setInit() {
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
		mNfcAdapter.enableForegroundNdefPush(this, mNdefPushMessage);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mNfcAdapter != null) {
			mNfcAdapter.disableForegroundDispatch(this);
            mNfcAdapter.disableForegroundNdefPush(this);
        }

	}

	@Override
	protected void onNewIntent(Intent intent) {
		resolveIntent(intent);
	}

	private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Parcelable tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                byte[] payload = dumpTagData(tag).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
                msgs = new NdefMessage[] { msg };
            }
           
            List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);
            final int size = records.size();
            for (int i = 0; i < size; i++) {
                ParsedNdefRecord record = records.get(i);
                Logg.d("ret : " + record.getText());
                
                String strTag = record.getText();
                
                try {
                	strTag = Cipher.decrypt(strTag, Cipher.createNfcMasterKey());
					
					Logg.d("ret 22: " + strTag);
				} catch (InvalidKeyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
                goDetailActivity(strTag);
                
            }
        }
    }

	private NdefRecord newTextRecord(String text, Locale locale, boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));

        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        byte[] textBytes = text.getBytes(utfEncoding);

        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);

        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }
	
	private String dumpTagData(Parcelable p) {
        StringBuilder sb = new StringBuilder();
        Tag tag = (Tag) p;
        byte[] id = tag.getId();
        sb.append("Tag ID (hex): ").append(getHex(id)).append("\n");
        sb.append("Tag ID (dec): ").append(getDec(id)).append("\n");
        sb.append("ID (reversed): ").append(getReversed(id)).append("\n");

        String prefix = "android.nfc.tech.";
        sb.append("Technologies: ");
        for (String tech : tag.getTechList()) {
            sb.append(tech.substring(prefix.length()));
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                sb.append('\n');
                MifareClassic mifareTag = MifareClassic.get(tag);
                String type = "Unknown";
                switch (mifareTag.getType()) {
                case MifareClassic.TYPE_CLASSIC:
                    type = "Classic";
                    break;
                case MifareClassic.TYPE_PLUS:
                    type = "Plus";
                    break;
                case MifareClassic.TYPE_PRO:
                    type = "Pro";
                    break;
                }
                sb.append("Mifare Classic type: ");
                sb.append(type);
                sb.append('\n');

                sb.append("Mifare size: ");
                sb.append(mifareTag.getSize() + " bytes");
                sb.append('\n');

                sb.append("Mifare sectors: ");
                sb.append(mifareTag.getSectorCount());
                sb.append('\n');

                sb.append("Mifare blocks: ");
                sb.append(mifareTag.getBlockCount());
            }

            if (tech.equals(MifareUltralight.class.getName())) {
                sb.append('\n');
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                String type = "Unknown";
                switch (mifareUlTag.getType()) {
                case MifareUltralight.TYPE_ULTRALIGHT:
                    type = "Ultralight";
                    break;
                case MifareUltralight.TYPE_ULTRALIGHT_C:
                    type = "Ultralight C";
                    break;
                }
                sb.append("Mifare Ultralight type: ");
                sb.append(type);
            }
        }

        return sb.toString();
    }
	
	private String getHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private long getDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    private long getReversed(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }
    
    public static final int MENU_WRITE_NFC = Menu.FIRST;
	public static final int MENU_READ_QRCODE = Menu.FIRST + 1;
	public static final int MENU_TEST = Menu.FIRST + 2;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		super.onCreateOptionsMenu(menu);
		
	    menu.add(Menu.NONE, MENU_READ_QRCODE, Menu.NONE, "QR코드 읽기");
	    menu.add(Menu.NONE, MENU_WRITE_NFC, Menu.NONE, "NFC 쓰기");
//		menu.add(Menu.NONE, MENU_TEST, Menu.NONE, "태그 테스트");

		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
        {
            case MENU_WRITE_NFC:
            	Intent it = new Intent(NfcActivity.this, NfcWriteActivity.class);
        		AppUtil.startActivity(NfcActivity.this, it);
            	break;
            case MENU_READ_QRCODE:
            	IntentIntegrator integrator = new IntentIntegrator(NfcActivity.this);
                integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
            	break;
//            case MENU_TEST:
//            	goDetailActivity("BTA0040300");
//            	break;
        default:
            return super.onOptionsItemSelected(item);
        }

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		
		IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
	    if (result != null) {
	      String contents = result.getContents();
	      if (contents != null) {
	    	  Logg.d("result : " + contents);
	    	  goDetailActivity(contents);
	      } else {
	    	  Logg.d("result fail");
	      }
	    }
	}
	
	public void goDetailActivity(String strTag) {
		String[] tags = strTag.split("_");
		if (tags.length <= 1) {
			ToastUtil.show(NfcActivity.this, "태그한 철탑에 해당하는 스케줄 정보가 없습니다.");
		}
			
		InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(NfcActivity.this);
		ArrayList<InspectInfo> insList =  insRetDao.selectTowerInsList(tags[1]);
		if (insList.size() == 0) {
			ToastUtil.show(NfcActivity.this, "태그한 철탑에 해당하는 스케줄 정보가 없습니다.");
			return;
		}
		
		Intent it = new Intent(NfcActivity.this, InspectResultDetailActivity.class);
		it.putExtra("nfc_tag", strTag);
		it.putExtra("is_nfc", true);
		it.putExtra("inspect", insList.get(0));
		AppUtil.startActivity(NfcActivity.this, it);
		finish();
	}
}
