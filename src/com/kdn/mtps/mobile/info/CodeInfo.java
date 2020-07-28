package com.kdn.mtps.mobile.info;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.LinkedHashMap;

import android.content.Context;

import com.kdn.mtps.mobile.constant.ConstVALUE;
import com.kdn.mtps.mobile.db.ManageCodeDao;
import com.kdn.mtps.mobile.db.bean.ManageCodeLog;
import com.kdn.mtps.mobile.util.Logg;

public class CodeInfo {
	
	static CodeInfo codeInfo;
	public static Context ctx;
	
	public  LinkedHashMap<String, String> CODE_TRACKS = new LinkedHashMap<String, String>();
	public  LinkedHashMap<String, String> CODE_WEATHER = new LinkedHashMap<String, String>();
	public  LinkedHashMap<String, String> CODE_INS_TYPE = new LinkedHashMap<String, String>();
	public  LinkedHashMap<String, String> CODE_HK_TYPE = new LinkedHashMap<String, String>();
	public  LinkedHashMap<String, String> CODE_HK_CATEGORY = new LinkedHashMap<String, String>();
	public  LinkedHashMap<String, String> CODE_EQPS = new LinkedHashMap<String, String>();
	public  LinkedHashMap<String, String> CODE_GOOD_SECD = new LinkedHashMap<String, String>();
	public  LinkedHashMap<String, String> CODE_FLIGHT_LMP = new LinkedHashMap<String, String>();
	
	public CodeInfo() {
	}
	
	public static CodeInfo getInstance(Context c) {
		ctx = c;
		if (codeInfo == null)
			codeInfo = new CodeInfo();
		return codeInfo;
	}
	
	public void setCodeData(String mType, ArrayList<ManageCodeLog> codeList) {
		
		LinkedHashMap<String, String> tempMap = new LinkedHashMap<String, String>();
		
		if (mType.equals(ConstVALUE.CODE_TYPE_WEATHER))
			tempMap = CODE_WEATHER;
		else if (mType.equals(ConstVALUE.CODE_TYPE_INS_TYPE)) {
			tempMap = CODE_INS_TYPE;
		} else if (mType.equals(ConstVALUE.CODE_TYPE_HK_CATEGORY)) {
			tempMap = CODE_HK_CATEGORY;
		} else if (mType.equals(ConstVALUE.CODE_TYPE_HK_TYPE)) {
			tempMap = CODE_HK_TYPE;
		} else if (mType.equals(ConstVALUE.CODE_TYPE_TRACKS)) {
			tempMap = CODE_TRACKS;
		} else if (mType.equals(ConstVALUE.CODE_TYPE_EQPS)) {
			tempMap = CODE_EQPS;
		} else if (mType.equals(ConstVALUE.CODE_TYPE_GOOD_SECD)) {
			tempMap = CODE_GOOD_SECD;
		} else if (mType.equals(ConstVALUE.CODE_TYPE_FLIGHT_LMP)) {
			tempMap = CODE_FLIGHT_LMP;
		}
		
		tempMap.clear();
		
		for (int i = 0; i < codeList.size(); i++) {
			ManageCodeLog log = codeList.get(i);
			tempMap.put(log.code_key, log.code_value);
			
			if (mType.equals(ConstVALUE.CODE_TYPE_INS_TYPE))
				Logg.d("eqp_nm insert : " + log.code_key + " / " + log.code_value);
		}
		
	}
	
	// 선로
	public String[] getTracksNames() {
		setData();
		
		String strArray[] = new String[CODE_TRACKS.size()];
		int index = 0;
		for (Entry<String, String> entry : CODE_TRACKS.entrySet()) {
			strArray[index++] = entry.getValue();
	    }
	    return strArray;
	}
	
	public ArrayList<String> getTracksList() {
		setData();
		
		ArrayList<String> list = new ArrayList<String>();
		
		int index = 0;
		for (Entry<String, String> entry : CODE_TRACKS.entrySet()) {
			list.add(entry.getValue());
	    }
	    return list;
	}
	
	public String getTracksCode(String value) {
		setData();
		
	    for (Entry<String, String> entry : CODE_TRACKS.entrySet()) {
	        if (value.equals(entry.getValue())) {
	            return entry.getKey();
	        }
	    }
	    return null;
	}

	// 지지물
	public String[] getEqpsNames() {
		setData();
		
		String strArray[] = new String[CODE_EQPS.size()];
		int index = 0;
		
		for (Entry<String, String> entry : CODE_EQPS.entrySet()) {
			strArray[index++] = entry.getValue();
			
			Logg.d("eqp_nm print : " + entry.getValue());
	    }
		
//		Iterator iter = CODE_EQPS.keySet().iterator();
//		while( iter.hasNext() ) {
//			String key = (String)iter.next();
//			strArray[index++] = CODE_EQPS.get(key);
//			Logg.d("eqp_nm print : " + key + " / " + CODE_EQPS.get(key));
//		}
		
	    return strArray;
	}
	
	public String[] getNames(String mType) {
		setData();
		
		LinkedHashMap<String, String> tempMap = new LinkedHashMap<String, String>();
		
		if (mType.equals(ConstVALUE.CODE_TYPE_WEATHER))
			tempMap = CODE_WEATHER;
		else if (mType.equals(ConstVALUE.CODE_TYPE_INS_TYPE)) {
			tempMap = CODE_INS_TYPE;
		} else if (mType.equals(ConstVALUE.CODE_TYPE_HK_CATEGORY)) {
			tempMap = CODE_HK_CATEGORY;
		} else if (mType.equals(ConstVALUE.CODE_TYPE_HK_TYPE)) {
			tempMap = CODE_HK_TYPE;
		} else if (mType.equals(ConstVALUE.CODE_TYPE_TRACKS)) {
			tempMap = CODE_TRACKS;
		} else if (mType.equals(ConstVALUE.CODE_TYPE_EQPS)) {
			tempMap = CODE_EQPS;
		} else if (mType.equals(ConstVALUE.CODE_TYPE_GOOD_SECD)) {
			tempMap = CODE_GOOD_SECD;
		} else if (mType.equals(ConstVALUE.CODE_TYPE_FLIGHT_LMP)) {
			tempMap = CODE_FLIGHT_LMP;
		}
		
		String strArray[] = new String[tempMap.size()];
		int index = 0;
		for (Entry<String, String> entry : tempMap.entrySet()) {
			strArray[index++] = entry.getValue();
	    }
	    return strArray;
	}
	
	public String getValue(String mType, String key) {
		setData();
		
		LinkedHashMap<String, String> tempMap = new LinkedHashMap<String, String>();
		
		if (mType.equals(ConstVALUE.CODE_TYPE_WEATHER))
			tempMap = CODE_WEATHER;
		else if (mType.equals(ConstVALUE.CODE_TYPE_INS_TYPE)) {
			tempMap = CODE_INS_TYPE;
		} else if (mType.equals(ConstVALUE.CODE_TYPE_HK_CATEGORY)) {
			tempMap = CODE_HK_CATEGORY;
		} else if (mType.equals(ConstVALUE.CODE_TYPE_HK_TYPE)) {
			tempMap = CODE_HK_TYPE;
		} else if (mType.equals(ConstVALUE.CODE_TYPE_TRACKS)) {
			tempMap = CODE_TRACKS;
		}  else if (mType.equals(ConstVALUE.CODE_TYPE_EQPS)) {
			tempMap = CODE_EQPS;
		} else if (mType.equals(ConstVALUE.CODE_TYPE_GOOD_SECD)) {
			tempMap = CODE_GOOD_SECD;
		} else if (mType.equals(ConstVALUE.CODE_TYPE_FLIGHT_LMP)) {
			tempMap = CODE_FLIGHT_LMP;
		}
		
		for (Entry<String, String> entry : tempMap.entrySet()) {
	        if (key.equals(entry.getKey())) {
	            return entry.getValue();
	        }
	    }
	    return "";
	}
	
	public String getKey(String mType, String value) {
		setData();
		
		LinkedHashMap<String, String> tempMap = new LinkedHashMap<String, String>();
		
		if (mType.equals(ConstVALUE.CODE_TYPE_WEATHER))
			tempMap = CODE_WEATHER;
		else if (mType.equals(ConstVALUE.CODE_TYPE_INS_TYPE)) {
			tempMap = CODE_INS_TYPE;
		} else if (mType.equals(ConstVALUE.CODE_TYPE_HK_CATEGORY)) {
			tempMap = CODE_HK_CATEGORY;
		} else if (mType.equals(ConstVALUE.CODE_TYPE_HK_TYPE)) {
			tempMap = CODE_HK_TYPE;
		} else if (mType.equals(ConstVALUE.CODE_TYPE_TRACKS)) {
			tempMap = CODE_TRACKS;
		} else if (mType.equals(ConstVALUE.CODE_TYPE_GOOD_SECD)) {
			tempMap = CODE_GOOD_SECD;
		} else if (mType.equals(ConstVALUE.CODE_TYPE_FLIGHT_LMP)) {
			tempMap = CODE_FLIGHT_LMP;
		} else if (mType.equals(ConstVALUE.CODE_TYPE_EQPS)) {
			tempMap = CODE_EQPS;
		}
		
	    for (Entry<String, String> entry : tempMap.entrySet()) {
	        if (value.equals(entry.getValue())) {
	            return entry.getKey();
	        }
	    }
	    return "";
	}
	
	public String[] getYBNames() {
		setData();
		
		String strArray[] = new String[CODE_GOOD_SECD.size()];
		int index = 0;
		for (Entry<String, String> entry : CODE_GOOD_SECD.entrySet()) {
			if ("001".equals(entry.getKey()) || "003".equals(entry.getKey()))
				strArray[index++] = entry.getValue();
	    }
	    return strArray;
	}
	
	public String[] getArrayInputBtWeather() {
		setData();
		
		String strArray[] = new String[CODE_WEATHER.size()];
		Iterator<String> it = CODE_WEATHER.keySet().iterator();
		int idx=0;
		while (it.hasNext()) {
			String key = it.next();
			String name = CODE_WEATHER.get(key);
			strArray[idx] = name;
			idx++;
		}
		
		return strArray;
	}
	
	public String getFirstInputBtWeather() {
		setData();
		
		for (Entry<String, String> entry : CODE_WEATHER.entrySet()) {
			return entry.getValue();
	    }
		
		return null;
	}
	
	public String searchKey(LinkedHashMap<String, String> map, String value) {
		setData();
		
		for (Entry<String, String> entry : map.entrySet()) {
	        if (value.equals(entry.getValue())) {
	            return entry.getKey();
	        }
	    }
		
		return null;
	}

	public void setData() {
		ManageCodeDao manageCodeDao = ManageCodeDao.getInstance(ctx);
		
		if (CODE_TRACKS.isEmpty())
			manageCodeDao.selectCodeItems(ConstVALUE.CODE_TYPE_TRACKS);
			
		if (CODE_WEATHER.isEmpty())
			manageCodeDao.selectCodeItems(ConstVALUE.CODE_TYPE_WEATHER);
		
		if (CODE_INS_TYPE.isEmpty())
			manageCodeDao.selectCodeItems(ConstVALUE.CODE_TYPE_INS_TYPE);
		
		if (CODE_HK_TYPE.isEmpty())
			manageCodeDao.selectCodeItems(ConstVALUE.CODE_TYPE_HK_TYPE);
		
		if (CODE_HK_CATEGORY.isEmpty())
			manageCodeDao.selectCodeItems(ConstVALUE.CODE_TYPE_HK_CATEGORY);
		
		if (CODE_GOOD_SECD.isEmpty())
			manageCodeDao.selectCodeItems(ConstVALUE.CODE_TYPE_GOOD_SECD);
		
		if (CODE_FLIGHT_LMP.isEmpty())
			manageCodeDao.selectCodeItems(ConstVALUE.CODE_TYPE_FLIGHT_LMP);
		
	}
	
	public void setCodeResource() {
		ManageCodeDao manageCodeDao = ManageCodeDao.getInstance(ctx);
		
		manageCodeDao.selectCodeItems(ConstVALUE.CODE_TYPE_TRACKS);
		manageCodeDao.selectCodeItems(ConstVALUE.CODE_TYPE_WEATHER);
		manageCodeDao.selectCodeItems(ConstVALUE.CODE_TYPE_INS_TYPE);
		manageCodeDao.selectCodeItems(ConstVALUE.CODE_TYPE_HK_TYPE);
		manageCodeDao.selectCodeItems(ConstVALUE.CODE_TYPE_HK_CATEGORY);
		manageCodeDao.selectCodeItems(ConstVALUE.CODE_TYPE_GOOD_SECD);
		manageCodeDao.selectCodeItems(ConstVALUE.CODE_TYPE_FLIGHT_LMP);
		
	}
}
