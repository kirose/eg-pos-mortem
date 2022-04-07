package com.fimet;

import java.io.ByteArrayInputStream;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.utils.DateUtils;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public final class Version implements Serializable {
	private static Logger logger = LoggerFactory.getLogger(Version.class);
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 173839934905L;
	private static final String VERSION_OBJECT = "\u0072\u004f\u0030\u0041\u0042\u0058\u004e\u0079\u0041\u0042\u0046\u006a\u0062\u0032\u0030\u0075\u005a\u006d\u006c\u0074\u005a\u0058\u0051\u0075\u0056\u006d\u0056\u0079\u0063\u0032\u006c\u0076\u0062\u0067\u0041\u0041\u0041\u0043\u0068\u0035\u0071\u0075\u0057\u0035\u0041\u0067\u0041\u0043\u0054\u0041\u0041\u0044\u0061\u0032\u0056\u0035\u0064\u0041\u0041\u0053\u0054\u0047\u0070\u0068\u0064\u006d\u0045\u0076\u0062\u0047\u0046\u0075\u005a\u0079\u0039\u0054\u0064\u0048\u004a\u0070\u0062\u006d\u0063\u0037\u0054\u0041\u0041\u0048\u0064\u006d\u0056\u0079\u0063\u0032\u006c\u0076\u0062\u006e\u0045\u0041\u0066\u0067\u0041\u0042\u0065\u0048\u0042\u0030\u0041\u0042\u0035\u0068\u004d\u0055\u004e\u0039\u0055\u0069\u0030\u0067\u004f\u0044\u0031\u0055\u0055\u0069\u0035\u0048\u004c\u006b\u0078\u0044\u0055\u0079\u0078\u006e\u004b\u0046\u006c\u006d\u005a\u0053\u004e\u0065\u004e\u0069\u0042\u0031\u004f\u0044\u0052\u0030\u0041\u0041\u0074\u0047\u0061\u0057\u0031\u006c\u0064\u0043\u0041\u0079\u004c\u006a\u0045\u0075\u004e\u0041\u003d\u003d";
	private static Version INSTANCE = fromString(VERSION_OBJECT);
	private String version;
	private String key;
	public Version(String version, String key){
		this.version = version;
		this.key = key;
	}
	public static final String getVersion() {
		return INSTANCE.version;
	}
	public static final boolean isValidProductKey(String productKey) {
		if (productKey != null && productKey.length() > 0) {
			String pk = decrypt(productKey);
			if (pk.startsWith("FIMET") && pk.length() == 13) {
				try {
					Date date = DateUtils.yyyyMMdd_FMT.parse(pk.substring(5));
					return date.after(new Date());
				} catch (ParseException e) {}
			}
		}
		return false;
	}
	public static final String decrypt(String encrypted) {
		return INSTANCE._decrypt(encrypted);
	}
	public final String _decrypt(String encrypted) {
		try {
			byte[] decrypted = decrypt_v4(encrypted.getBytes(), key.getBytes());
			return new String(decrypted);
		} catch (Exception e) {
			logger.warn("Version.decrypt: "+e.getMessage(), e);
			return null;
		}
	}
	private static Version fromString(String s) {
		byte [] data = Base64.getDecoder().decode(s);
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new ByteArrayInputStream(data));
			Object o  = ois.readObject();
			ois.close();
			return (Version)o;
		} catch (Exception e) {
			logger.error("Invalid Version "+s,e);
			throw new FimetException("Invalid version");
		}
	}
	private static byte[] des_cbc_decrypt(byte[] encrypted_password, byte[] decryption_key, byte[] iv) throws GeneralSecurityException {
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryption_key, "DES"), new IvParameterSpec(iv));
		return cipher.doFinal(encrypted_password);
	}
	private static byte[] doKey(byte[] db_system_id) throws NoSuchAlgorithmException {
		byte[] salt = DatatypeConverter.parseHexBinary("051399429372e8ad");
		// key = db_system_id + salt
		byte[] key = new byte[db_system_id.length + salt.length];
		System.arraycopy(db_system_id, 0, key, 0, db_system_id.length);
		System.arraycopy(salt, 0, key, db_system_id.length, salt.length);
		java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
		for (int i=0; i<42; i++) {
			key = md.digest(key);
		}
		return key;
	}
	private static byte[] decrypt_v4(byte[] encrypted, byte[] db_system_id) throws GeneralSecurityException {

		byte[] encrypted_password = Base64.getDecoder().decode(encrypted);

		// key = db_system_id + salt
		byte[] key = doKey(db_system_id);

		// secret_key = key [0..7]
		byte[] secret_key = new byte[8];
		System.arraycopy(key, 0, secret_key, 0, 8);

		// iv = key [8..]
		byte[] iv = new byte[key.length - 8];
		System.arraycopy(key, 8, iv, 0, key.length - 8);

		return des_cbc_decrypt(encrypted_password, secret_key, iv);

	}
}