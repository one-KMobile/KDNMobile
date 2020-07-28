package com.kdn.mtps.mobile.aria;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;

import com.kdn.mtps.mobile.util.Logg;


/**
 * AIRA algorithm to encrypt or decrypt the data is the class that provides the ability to.
 * @author shson
 *
 */
public class Cipher {
	
	/**
	 * ARIA encryption algorithm block size
	 */
	private static final int ARIA_BLOCK_SIZE = 16;
	
	private static String masterKeyEc = "";
	
	/**
	 * ARIA algorithm to encrypt the data.
	 * @param data Target Data
	 * @param key Masterkey
	 * @param keySize Masterkey Size
	 * @param charset Data character set
	 * @return Encrypted data
	 * @throws UnsupportedEncodingException If character is not supported
	 * @throws InvalidKeyException If the Masterkey is not valid
	 */
	public static String encrypt(String data, byte[] key, int keySize, String charset)
	throws UnsupportedEncodingException, InvalidKeyException {
		
		byte[] encrypt = null;
		if( charset == null ) {
			encrypt = BlockPadding.getInstance().addPadding(data.getBytes(), ARIA_BLOCK_SIZE);
		} else {
			encrypt = BlockPadding.getInstance().addPadding(data.getBytes(charset), ARIA_BLOCK_SIZE);
		}
		
		ARIAEngine engine = new ARIAEngine(keySize);
		engine.setKey(key);
		engine.setupEncRoundKeys();
		
		int blockCount = encrypt.length / ARIA_BLOCK_SIZE;
		for( int i = 0; i < blockCount; i++ ) {
			
			byte buffer[] = new byte[ARIA_BLOCK_SIZE];
			System.arraycopy(encrypt, (i * ARIA_BLOCK_SIZE), buffer, 0, ARIA_BLOCK_SIZE);
			
			buffer = engine.encrypt(buffer, 0);
			System.arraycopy(buffer, 0, encrypt, (i * ARIA_BLOCK_SIZE), buffer.length);
		}
		
		return Base64.toString(encrypt);
	}
	
	public static String encrypt(String data, byte[] key)
			throws UnsupportedEncodingException, InvalidKeyException {
				
//		if (1==1) return data;
				String charset = null;
				int keySize = key.length*8;
						
				byte[] encrypt = null;
				if( charset == null ) {
					encrypt = BlockPadding.getInstance().addPadding(data.getBytes(), ARIA_BLOCK_SIZE);
				} else {
					encrypt = BlockPadding.getInstance().addPadding(data.getBytes(charset), ARIA_BLOCK_SIZE);
				}
				
				ARIAEngine engine = new ARIAEngine(keySize);
				engine.setKey(key);
				engine.setupEncRoundKeys();
				
				int blockCount = encrypt.length / ARIA_BLOCK_SIZE;
				for( int i = 0; i < blockCount; i++ ) {
					
					byte buffer[] = new byte[ARIA_BLOCK_SIZE];
					System.arraycopy(encrypt, (i * ARIA_BLOCK_SIZE), buffer, 0, ARIA_BLOCK_SIZE);
					
					buffer = engine.encrypt(buffer, 0);
					System.arraycopy(buffer, 0, encrypt, (i * ARIA_BLOCK_SIZE), buffer.length);
				}
				
				return Base64.toString(encrypt);
			}
	
	/**
	 * ARIA algorithm to decrypt the data.
	 * @param data Target Data
	 * @param key Masterkey
	 * @param keySize Masterkey Size
	 * @param charset Data character set
	 * @return Decrypted data
	 * @throws UnsupportedEncodingException If character is not supported
	 * @throws InvalidKeyException If the Masterkey is not valid
	 */
	public static String decrypt(String data, byte[] key)
	throws UnsupportedEncodingException, InvalidKeyException {

		String charset = null;
		int keySize = key.length*8;
		
		byte[] decrypt = Base64.toByte(data);
		
		ARIAEngine engine = new ARIAEngine(keySize);
		engine.setKey(key);
		engine.setupDecRoundKeys();
		
		int blockCount = decrypt.length / ARIA_BLOCK_SIZE;
		for( int i = 0; i < blockCount; i++ ) {
			
			byte buffer[] = new byte[ARIA_BLOCK_SIZE];
			System.arraycopy(decrypt, (i * ARIA_BLOCK_SIZE), buffer, 0, ARIA_BLOCK_SIZE);
			
			Logg.d("zz data : " + decrypt.length);
			
			buffer = engine.decrypt(buffer, 0);
			System.arraycopy(buffer, 0, decrypt, (i * ARIA_BLOCK_SIZE), buffer.length);
		}
		
		if( charset == null ) {
			return new String(BlockPadding.getInstance().removePadding(decrypt, ARIA_BLOCK_SIZE));
		} else {
			return new String(BlockPadding.getInstance().removePadding(decrypt, ARIA_BLOCK_SIZE), charset);
		}
	}
	
	public static String decrypt(String data, byte[] key, int keySize, String charset)
			throws UnsupportedEncodingException, InvalidKeyException {

				byte[] decrypt = Base64.toByte(data);
				
				ARIAEngine engine = new ARIAEngine(keySize);
				engine.setKey(key);
				engine.setupDecRoundKeys();
				
				int blockCount = decrypt.length / ARIA_BLOCK_SIZE;
				for( int i = 0; i < blockCount; i++ ) {
					
					byte buffer[] = new byte[ARIA_BLOCK_SIZE];
					System.arraycopy(decrypt, (i * ARIA_BLOCK_SIZE), buffer, 0, ARIA_BLOCK_SIZE);
					
					buffer = engine.decrypt(buffer, 0);
					System.arraycopy(buffer, 0, decrypt, (i * ARIA_BLOCK_SIZE), buffer.length);
				}
				
				if( charset == null ) {
					return new String(BlockPadding.getInstance().removePadding(decrypt, ARIA_BLOCK_SIZE));
				} else {
					return new String(BlockPadding.getInstance().removePadding(decrypt, ARIA_BLOCK_SIZE), charset);
				}
			}
	
	/**
	 * <설명>
	 * Aria 마스터키 생성
	 * @param [box]  
	 * @return [encryptStr]
	 * @throws [예외명] [설명] // 각 예외 당 하나씩
	 * @author [범승철]
	 * @fix(<수정자명>) [yyyy.mm.dd]: [수정 내용]
	 */
	public static byte[] createMasterKey(){
		String EncryptStr = ""; 
		byte[] key = new byte[16];  
		for (int i = 0; i < key.length; i++) {
			// 1 ~ 127까지 랜덤 숫자 구하기
			int random = (int) (Math.random() * 127) + 1;
			key[i] = (byte) random;
			if(i == (key.length - 1)){
				EncryptStr += key[i];	
			}else{
				EncryptStr += key[i] + "&";
			}
		}
		System.out.println("@@@@[EncryptStr] = " + EncryptStr);
		System.out.println();
		
		masterKeyEc = masterKeyEncrypt(EncryptStr); //Aria 마스터키를 암호화
		return key;
	}
	
	public static byte[] createNfcMasterKey(){
		String EncryptStr = ""; 
		byte[] key = new byte[16];
		int[] values = {1,2,3,4,5,6,7,8,9,10,111,112,113,114,115,116};
		for (int i = 0; i < key.length; i++) {
			key[i] = (byte) values[i];
			if(i == (key.length - 1)){
				EncryptStr += key[i];	
			}else{
				EncryptStr += key[i] + "&";
			}
		}
		System.out.println("@@@@[EncryptStr] = " + EncryptStr);
		System.out.println();
		
		return key;
	}
	
	/**
	 * <설명>
	 * Aria 마스터키를 암호화
	 * @param [box]  
	 * @return [encryptStr]
	 * @throws [예외명] [설명] // 각 예외 당 하나씩
	 * @author [범승철]
	 * @fix(<수정자명>) [yyyy.mm.dd]: [수정 내용]
	 * @param  
	 */
	public static String masterKeyEncrypt(String masterKey){ 
		byte[] key = new byte[16];  
		key[0] = 112;
		key[1] = 119;
		key[2] = 114;
		
		String data = masterKey;
		
		try {
			data = Cipher.encrypt(data, key);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("[masterKey] 데이터 = " + masterKey);
		System.out.println("[masterKey] 암호화 키 = " + data);
		
		return data;
	}
	
	public static String masterKeyDecrypt(String masterKey){ 
		byte[] key = new byte[16];  
		key[0] = 112;
		key[1] = 119;
		key[2] = 114;
		
		String data = masterKey;
		
		try {
			data = Cipher.decrypt(data, key);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("[masterKey] 데이터 = " + masterKey);
		System.out.println("[masterKey] 복호화 키 = " + data);
		
		return data;
	}
	
	/**
	 * The sample code in the Cipher class
	 * @param args none
	 */
	public static void main(String args[]) {
		
		try {
			
			/*byte[] key = new byte[16];
			for( int i = 0; i < key.length; i++ ) {
				key[i] = (byte)i;
			}*/
			
			byte[] key = createMasterKey(); //마스터 키 생성 
			
			System.out.println();
			String data = "AS12대한민국"; 
			
			data = Cipher.encrypt(data, key, key.length*8, null);
			System.out.println(data);
			
			data = Cipher.decrypt(data, key, key.length*8, null);
			System.out.println(data);   
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static String getEnMasterKey(){
		return masterKeyEc;
	}
}

