package org.trdc.announcement.utils;

import java.util.Map;
/**
 * Author Ibrahim Jamali
 */
public interface Client {
	String getCookieFolderName();

	/**
	 * 
	 * @param url
	 * @param referer
	 * @param data
	 * @param headers
	 * @param ipAddress
	 * @return
	 * @throws Exception
	 */
	Response doPost(String url, String referer, Object data, Map<String, String> headers, String ipAddress)
			throws Exception;

	/**
	 * 
	 * @param url
	 * @param referer
	 * @param data
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	Response doPost(String url, String referer, Object data, Map<String, String> headers) throws Exception;

	/**
	 * 
	 * @param url
	 * @param referer
	 * @param headers
	 * @param ipAddress
	 * @return
	 * @throws Exception
	 */
	Response doGet(String url, String referer, Map<String, String> headers, String ipAddress) throws Exception;

	/**
	 * 
	 * @param url
	 * @param referer
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	Response doGet(String url, String referer, Map<String, String> headers) throws Exception;

}
