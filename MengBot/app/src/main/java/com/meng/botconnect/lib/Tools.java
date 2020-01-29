package com.meng.botconnect.lib;

import java.io.*;
import java.nio.charset.*;
import java.security.*;
import java.text.*;
import java.util.*;

public class Tools {

	public static final String DEFAULT_ENCODING = "UTF-8";

	public static class FileTool {
		public static String readString(String fileName) {
			return readString(new File(fileName));
		}

		public static String readString(File f) {
			String s = "";
			try {      
				if (!f.exists()) {
					return "";
				}
				long filelength = f.length();
				byte[] filecontent = new byte[(int) filelength];
				FileInputStream in = new FileInputStream(f);
				in.read(filecontent);
				in.close();
				s = new String(filecontent, StandardCharsets.UTF_8);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return s;
		}
	}


	public static class StringTools {
		/**
		 * 系统换行符，根据不同的系统返回不同的换行符(字符串)
		 */
		public static final String lineSeparator = System.getProperty("line.separator");

		/**
		 * 判断是否为空
		 *
		 * @param args 字符串
		 * @return 是否为空
		 */
		public static boolean isEmpty(String... args) {
			if (args == null || args.length == 0)
				return true;
			for (String string : args)
				if (string == null || string.length() == 0)
					return true;
			return false;
		}

		/**
		 * 获取单字符的换行符 (Windows下则将\r\n 变为\n)
		 *
		 * @return 换行符
		 */
		public static char lineSeparatorSingle() {
			// 获取单字符换行符
			int len = lineSeparator.length();
			if (len == 2 || len == 0)
				return '\n';
			else
				return lineSeparator.charAt(0);
		}

		/**
		 * 去前后空格后判断是否为空
		 *
		 * @param args 字符串
		 * @return 是否为空
		 */
		public static boolean isTrimEmpty(String... args) {
			if (args == null || args.length == 0)
				return true;
			for (String string : args)
				if (string == null || string.trim().length() == 0)
					return true;
			return false;
		}

		/**
		 * 去除左边空格
		 *
		 * @param string 字符串
		 * @return 修改后的字符串
		 */
		public static String trimLeft(String string) {
			char[] val = string.toCharArray();
			int len = val.length;
			int st = 0;
			while ((st < len) && (val[st] <= ' '))
				st++;
			return st > 0 ? string.substring(st, len) : string;
		}

		/**
		 * 去除右边空格
		 *
		 * @param string 字符串
		 * @return 修改后的字符串
		 */
		public static String trimRight(String string) {
			char[] val = string.toCharArray();
			int len = val.length;
			int st = 0;
			while ((st < len) && (val[len - 1] <= ' '))
				len--;
			return len < val.length ? string.substring(st, len) : string;
		}

		/**
		 * 去除左边空格
		 *
		 * @param string 字符串
		 * @return 修改后的字符串
		 */
		public static String trimEnter(String string) {
			char[] val = string.toCharArray();
			int len = val.length;
			int st = 0;
			while ((st < len) && (val[st] < ' '))
				st++;
			while ((st < len) && (val[len - 1] < ' '))
				len--;
			return st > 0 ? string.substring(st, len) : string;
		}

		/**
		 * 去除左边空格
		 *
		 * @param string 字符串
		 * @return 修改后的字符串
		 */
		public static String trimEnterLeft(String string) {
			char[] val = string.toCharArray();
			int len = val.length;
			int st = 0;
			while ((st < len) && (val[st] < ' '))
				st++;
			return st > 0 ? string.substring(st, len) : string;
		}

		/**
		 * 去除右边空格
		 *
		 * @param string 字符串
		 * @return 修改后的字符串
		 */
		public static String trimEnterRight(String string) {
			char[] val = string.toCharArray();
			int len = val.length;
			int st = 0;
			while ((st < len) && (val[len - 1] < ' '))
				len--;
			return len < val.length ? string.substring(st, len) : string;
		}

		/**
		 * 对象到字符串
		 *
		 * @param obj 对象
		 * @return 字符串
		 */
		public static String objToString(Object obj) {
			return (obj == null) ? null : obj.toString();
		}

		/**
		 * 字符串转换unicode
		 *
		 * @param string 原字符串
		 * @return unicode字符串
		 */
		public static String stringToUnicode(String string) {
			StringBuilder unicode = new StringBuilder();
			for (int i = 0; i < string.length(); i++) {
				unicode.append("\\u");
				unicode.append(Integer.toHexString(string.charAt(i)));
			}
			return unicode.toString();
		}

		/**
		 * unicode 转字符串
		 *
		 * @param unicode 原Unicode字符
		 * @return 字符串
		 */
		public static String unicodeToString(String unicode) {
			StringBuilder string = new StringBuilder();
			String[] hex = unicode.split("\\\\u");
			for (int i = 1; i < hex.length; i++) {
				// 转换出每一个代码点
				int data = Integer.parseInt(hex[i], 16);
				// 追加成string
				string.append((char) data);
			}
			return string.toString();
		}

		/**
		 * 拼接字符串
		 *
		 * @param args 要拼接的对象
		 * @return 字符串
		 */
		public static String stringConcat(Object... args) {
			StringBuilder sb = new StringBuilder();
			if (args != null) {
				for (Object obj : args) {
					sb.append(obj);
				}
			}
			return sb.toString();
		}

		/**
		 * 字符串替换,替换第一个匹配的相同的字符串
		 *
		 * @param src         源字符串
		 * @param target      被替换的字符串
		 * @param replacement 替换的字符串
		 * @return 处理后的字符串
		 */
		public static String stringReplaceFirst(String src, String target, String replacement) {
			if (src == null || target == null || target.length() == 0 || replacement == null)
				return src;
			int index = src.indexOf(target);
			if (index == -1)
				return src;
			int pos = 0;
			char[] rs = new char[src.length() - target.length() + replacement.length()];
			for (int i = 0; i < src.length(); i++) {
				if (i == index) {
					for (int j = 0; j < replacement.length(); j++) {
						rs[pos] = replacement.charAt(j);
						pos++;
					}
					i += target.length() - 1;
					continue;
				}
				rs[pos] = src.charAt(i);
				pos++;
			}
			return new String(rs);
		}

		/**
		 * 字符串替换,替换所有相同的字符串
		 *
		 * @param src         源字符串
		 * @param target      被替换的字符串
		 * @param replacement 替换的字符串
		 * @return 处理后的字符串
		 */
		public static String stringReplace(String src, String target, String replacement) {
			if (src == null || target == null || target.length() == 0 || replacement == null)
				return src;
			int index = src.indexOf(target);
			if (index == -1)
				return src;
			StringBuilder sb = new StringBuilder();
			char c = target.charAt(0);
			for (int i = 0; i < src.length(); i++) {
				if (i >= index && src.charAt(i) == c) {
					int length = i + target.length(), pos = 0, end = 0;
					for (int j = i; j < length; j++) {
						if (src.charAt(j) != target.charAt(pos)) {
							end = j;
							break;
						}
						pos++;
					}
					if (end != 0) {
						for (; i < end; i++)
							sb.append(src.charAt(i));
						i = end - 1;
					} else {
						for (int j = 0; j < replacement.length(); j++)
							sb.append(replacement.charAt(j));
						i = length - 1;
					}
					continue;
				}
				sb.append(src.charAt(i));
			}
			return sb.toString();
		}
	}

	public static class Hash {
		public static String MD5(String str) {
			try {
				return MD5(str.getBytes(DEFAULT_ENCODING));
			} catch (Exception e) {
				return null;
			}
		}

		public static String MD5(byte[] bs) {
			try {
				MessageDigest mdTemp = MessageDigest.getInstance("MD5");
				mdTemp.update(bs);
				return toHexString(mdTemp.digest()).toUpperCase();
			} catch (Exception e) {
				return null;
			}
		}

		public static String md5(File file) {
			InputStream inputStream = null;
			try {
				inputStream = new FileInputStream(file);
				return md5(inputStream);
			} catch (Exception e) {
				return null;
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		public static String md5(InputStream inputStream) {
			try {
				MessageDigest mdTemp = MessageDigest.getInstance("MD5");
				byte[] buffer = new byte[1024];
				int numRead = 0;
				while ((numRead = inputStream.read(buffer)) > 0) {
					mdTemp.update(buffer, 0, numRead);
				}
				return toHexString(mdTemp.digest());
			} catch (Exception e) {
				return null;
			}
		}
		private static String toHexString(byte[] md) {
			char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
			int j = md.length;
			char str[] = new char[j * 2];
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[2 * i] = hexDigits[byte0 >>> 4 & 0xf];
				str[i * 2 + 1] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		}
	}

	public static class Network {
		public static Map<String, String> cookieToMap(String value) {
			Map<String, String> map = new HashMap<>();
			String[] values = value.split("; ");
			for (String val : values) {
				String[] vals = val.split("=");
				if (vals.length == 2) {
					map.put(vals[0], vals[1]);
				} else if (vals.length == 1) {
					map.put(vals[0], "");
				}
			}
			return map;
		}
	}

	public static class CQ {
		public static int getG_tk(String skey) {
			int hash = 5381;
			int flag = skey.length();
			for (int i = 0; i < flag; i++) {
				hash = hash + hash * 32 + skey.charAt(i);
			}
			return hash & 0x7fffffff;
		}

		public static String getTime() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		}

		public static String getTime(long timeStamp) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timeStamp));
		}

		public static String getDate() {
			return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		}

		public static String getDate(long timeStamp) {
			return new SimpleDateFormat("yyyy-MM-dd").format(new Date(timeStamp));
		}
	}

	public static class ArrayTool {

		public static byte[] mergeArray(byte[]... arrays) {
			int allLen=0;
			for (byte[] bs:arrays) {
				allLen += bs.length;
			}
			byte[] finalArray=new byte[allLen];
			int flag=0;
			for (byte[] byteArray:arrays) {
				for (int i=0;i < byteArray.length;++flag,++i) {
					finalArray[flag] = byteArray[i];
				}
			}
			return finalArray;
		}

		public static String[] mergeArray(String[]... arrays) {
			int allLen=0;
			for (String[] bs:arrays) {
				allLen += bs.length;
			}
			String[] finalArray=new String[allLen];
			int flag=0;
			for (String[] byteArray:arrays) {
				for (int i=0;i < byteArray.length;++flag,++i) {
					finalArray[flag] = byteArray[i];
				}
			}
			return finalArray;
		}
	}

	public static class BitConverter {
		public static byte[] getBytes(short s) {
			byte[] bs=new byte[2];
			bs[0] = (byte) ((s >> 0) & 0xff);
			bs[1] = (byte) ((s >> 8) & 0xff) ;
			return bs;	
		}

		public static byte[] getBytes(int i) {
			byte[] bs=new byte[4];
			bs[0] = (byte) ((i >> 0) & 0xff);
			bs[1] = (byte) ((i >> 8) & 0xff);
			bs[2] = (byte) ((i >> 16) & 0xff);
			bs[3] = (byte) ((i >> 24) & 0xff);
			return bs;	
		}

		public static byte[] getBytes(long l) {
			byte[] bs=new byte[8];
			bs[0] = (byte) ((l >> 0) & 0xff);
			bs[1] = (byte) ((l >> 8) & 0xff);
			bs[2] = (byte) ((l >> 16) & 0xff);
			bs[3] = (byte) ((l >> 24) & 0xff);
			bs[4] = (byte) ((l >> 32) & 0xff);
			bs[5] = (byte) ((l >> 40) & 0xff);
			bs[6] = (byte) ((l >> 48) & 0xff);
			bs[7] = (byte) ((l >> 56) & 0xff);
			return bs;
		}

		public static byte[] getBytes(float f) {
			return getBytes(Float.floatToIntBits(f));
		}

		public static byte[] getBytes(Double d) {
			return getBytes(Double.doubleToLongBits(d));
		}

		public static byte[] getBytes(String s) {
			try {
				return s.getBytes(DEFAULT_ENCODING);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		}

		public static short toShort(byte[] data, int pos) {
			return (short) ((data[pos] & 0xff) << 0 | (data[pos + 1] & 0xff) << 8);
		}

		public static short toShort(byte[] data) {
			return toShort(data , 0);
		}

		public static int toInt(byte[] data, int pos) {
			return (data[pos] & 0xff) << 0 | (data[pos + 1] & 0xff) << 8 | (data[pos + 2] & 0xff) << 16 | (data[pos + 3] & 0xff) << 24;
		}

		public static int toInt(byte[] data) {
			return toInt(data, 0);
		}

		public static long toLong(byte[] data, int pos) {
			return ((data[pos] & 0xffL) << 0) | (data[pos + 1] & 0xffL) << 8 | (data[pos + 2] & 0xffL) << 16 | (data[pos + 3] & 0xffL) << 24 | (data[pos + 4] & 0xffL) << 32 | (data[pos + 5] & 0xffL) << 40 | (data[pos + 6] & 0xffL) << 48 | (data[pos + 7] & 0xffL) << 56;
		}

		public static long toLong(byte[] data) {
			return toLong(data , 0);
		}

		public static float toFloat(byte[] data, int pos) {
			return Float.intBitsToFloat(toInt(data, pos));
		}

		public static float toFloat(byte[] data) {
			return toFloat(data , 0);
		}

		public static double toDouble(byte[] data, int pos) {
			return Double.longBitsToDouble(toLong(data, pos));
		}

		public static double toDouble(byte[] data) {
			return toDouble(data , 0);
		}

		public static String toString(byte[] data, int pos, int byteCount) {
			try {
				return new String(data, pos, byteCount, DEFAULT_ENCODING);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		}

		public static String toString(byte[] data) {
			return toString(data, 0, data.length);
		}
	}

	public static class Base64 {
		public static final byte[] encode(String str) {
			try {
				return encode(str.getBytes(DEFAULT_ENCODING));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		}
		public static final byte[] encode(byte[] byteData) {
			if (byteData == null) { 
				throw new IllegalArgumentException("byteData cannot be null");
			}
			int iSrcIdx; 
			int iDestIdx; 
			byte[] byteDest = new byte[((byteData.length + 2) / 3) * 4];
			for (iSrcIdx = 0, iDestIdx = 0; iSrcIdx < byteData.length - 2; iSrcIdx += 3) {
				byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx] >>> 2) & 077);
				byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx + 1] >>> 4) & 017 | (byteData[iSrcIdx] << 4) & 077);
				byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx + 2] >>> 6) & 003 | (byteData[iSrcIdx + 1] << 2) & 077);
				byteDest[iDestIdx++] = (byte) (byteData[iSrcIdx + 2] & 077);
			}
			if (iSrcIdx < byteData.length) {
				byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx] >>> 2) & 077);
				if (iSrcIdx < byteData.length - 1) {
					byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx + 1] >>> 4) & 017 | (byteData[iSrcIdx] << 4) & 077);
					byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx + 1] << 2) & 077);
				} else {
					byteDest[iDestIdx++] = (byte) ((byteData[iSrcIdx] << 4) & 077);
				}
			}
			for (iSrcIdx = 0; iSrcIdx < iDestIdx; iSrcIdx++) {
				if (byteDest[iSrcIdx] < 26) {
					byteDest[iSrcIdx] = (byte) (byteDest[iSrcIdx] + 'A');
				} else if (byteDest[iSrcIdx] < 52) {
					byteDest[iSrcIdx] = (byte) (byteDest[iSrcIdx] + 'a' - 26);
				} else if (byteDest[iSrcIdx] < 62) {
					byteDest[iSrcIdx] = (byte) (byteDest[iSrcIdx] + '0' - 52);
				} else if (byteDest[iSrcIdx] < 63) {
					byteDest[iSrcIdx] = '+';
				} else {
					byteDest[iSrcIdx] = '/';
				}
			}
			for (; iSrcIdx < byteDest.length; iSrcIdx++) {
				byteDest[iSrcIdx] = '=';
			}
			return byteDest;
		}

		public final static byte[] decode(String str) throws IllegalArgumentException {
			byte[] byteData = null;
			try {
				byteData = str.getBytes(DEFAULT_ENCODING);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if (byteData == null) { 
				throw new IllegalArgumentException("byteData cannot be null");
			}
			int iSrcIdx; 
			int reviSrcIdx; 
			int iDestIdx; 
			byte[] byteTemp = new byte[byteData.length];
			for (reviSrcIdx = byteData.length; reviSrcIdx - 1 > 0 && byteData[reviSrcIdx - 1] == '='; reviSrcIdx--) {
				; // do nothing. I'm just interested in value of reviSrcIdx
			}
			if (reviSrcIdx - 1 == 0)	{ 
				return null; 
			}
			byte byteDest[] = new byte[((reviSrcIdx * 3) / 4)];
			for (iSrcIdx = 0; iSrcIdx < reviSrcIdx; iSrcIdx++) {
				if (byteData[iSrcIdx] == '+') {
					byteTemp[iSrcIdx] = 62;
				} else if (byteData[iSrcIdx] == '/') {
					byteTemp[iSrcIdx] = 63;
				} else if (byteData[iSrcIdx] < '0' + 10) {
					byteTemp[iSrcIdx] = (byte) (byteData[iSrcIdx] + 52 - '0');
				} else if (byteData[iSrcIdx] < ('A' + 26)) {
					byteTemp[iSrcIdx] = (byte) (byteData[iSrcIdx] - 'A');
				}  else if (byteData[iSrcIdx] < 'a' + 26) {
					byteTemp[iSrcIdx] = (byte) (byteData[iSrcIdx] + 26 - 'a');
				}
			}
			for (iSrcIdx = 0, iDestIdx = 0; iSrcIdx < reviSrcIdx && iDestIdx < ((byteDest.length / 3) * 3); iSrcIdx += 4) {
				byteDest[iDestIdx++] = (byte) ((byteTemp[iSrcIdx] << 2) & 0xFC | (byteTemp[iSrcIdx + 1] >>> 4) & 0x03);
				byteDest[iDestIdx++] = (byte) ((byteTemp[iSrcIdx + 1] << 4) & 0xF0 | (byteTemp[iSrcIdx + 2] >>> 2) & 0x0F);
				byteDest[iDestIdx++] = (byte) ((byteTemp[iSrcIdx + 2] << 6) & 0xC0 | byteTemp[iSrcIdx + 3] & 0x3F);
			}
			if (iSrcIdx < reviSrcIdx) {
				if (iSrcIdx < reviSrcIdx - 2) {
					byteDest[iDestIdx++] = (byte) ((byteTemp[iSrcIdx] << 2) & 0xFC | (byteTemp[iSrcIdx + 1] >>> 4) & 0x03);
					byteDest[iDestIdx++] = (byte) ((byteTemp[iSrcIdx + 1] << 4) & 0xF0 | (byteTemp[iSrcIdx + 2] >>> 2) & 0x0F);
				} else if (iSrcIdx < reviSrcIdx - 1) {
					byteDest[iDestIdx++] = (byte) ((byteTemp[iSrcIdx] << 2) & 0xFC | (byteTemp[iSrcIdx + 1] >>> 4) & 0x03);
				}  else {
					throw new IllegalArgumentException("Warning: 1 input bytes left to process. This was not Base64 input");
				}
			}
			return byteDest;
		}
	}
}

