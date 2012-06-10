package org.djd.busntrain.commons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.djd.busntrain.commons.DownloadException.ERROR_CODE;

import android.util.Log;

public class Downloader {

	private static final String TAG = Downloader.class.getSimpleName();

	private Downloader() {
	}

	/**
	 * DefaultHttpClient is thread safe. It is recommended that the same
	 * instance of this class is reused for multiple request executions. When an
	 * instance of DefaultHttpClient is no longer needed and is about to go out
	 * of scope the connection manager associated with it must be shut down by
	 * calling the ClientConnectionManager#shutdown() method.
	 */
	private static final HttpClient HTTP_CLIENT = new DefaultHttpClient();

	public static String getAsString(String url) throws DownloadException{
		HttpEntity httpEntity = makeHttpRequest(url).getEntity();
		try {
			return EntityUtils.toString(httpEntity);
		} catch (IllegalStateException e) {
			throw new DownloadException(ERROR_CODE.ENTITY_IS_NOT_REPEATABLE);
		} catch (IOException e) {
			throw new DownloadException(ERROR_CODE.STREAM_COULD_NOT_BE_CREATED);
		}

		
		
	}
	public static ArrayList<String> loadTextFileToArrayList(String url)
			throws DownloadException {
		Log.i(TAG, url);
		return readTextFileToArrayList(getInputStream(url), true);

	}

	/**
	 * 
	 * @param inputStream
	 *            to Text File.
	 * @param trim
	 *            if set true blank like or a line which has only white space(s)
	 *            will be ignored.
	 * @return {@link ArrayList} contains text file contents.
	 * @throws DownloadException
	 */
	public static ArrayList<String> readTextFileToArrayList(
			InputStream inputStream, boolean trim) throws DownloadException {

		ArrayList<String> list = new ArrayList<String>();
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new InputStreamReader(inputStream));
			for (String line = null; null != (line = reader.readLine());) {
				if (trim) {
					if (line.trim().isEmpty()) {
						continue;
					}
					list.add(line);
				}
			}
		} catch (IOException e) {
			throw new DownloadException(ERROR_CODE.READ_FILE_ERROR);
		} finally {

			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					reader = null;
				}
			}
		}
		return list;
	}

	private static InputStream getInputStream(String url)
			throws DownloadException {

		HttpEntity httpEntity = makeHttpRequest(url).getEntity();
		try {
			return httpEntity.getContent();
		} catch (IllegalStateException e) {
			throw new DownloadException(ERROR_CODE.ENTITY_IS_NOT_REPEATABLE);
		} catch (IOException e) {
			throw new DownloadException(ERROR_CODE.STREAM_COULD_NOT_BE_CREATED);
		}

	}

	private static HttpResponse makeHttpRequest(String url)
			throws DownloadException {
		HttpResponse httpResponse;

		HttpGet httpGet = createHttpGet(url);

		try {
			httpResponse = HTTP_CLIENT.execute(httpGet);
		} catch (ClientProtocolException e) {
			throw new DownloadException(ERROR_CODE.HTTP_PROTOCOL_ERROR);
		} catch (IOException e) {
			Log.e(TAG, "Check android.permission.INTERNET is available.");
			throw new DownloadException(
					ERROR_CODE.FAILED_CREATE_HTTP_CONNECTION);
		}
		return httpResponse;
	}

	private static HttpGet createHttpGet(String url) throws DownloadException {
		if (null == url) {
			throw new DownloadException(ERROR_CODE.EMPTY_URL);
		}
		HttpGet request = new HttpGet(url);
		return request;
	}

	/**
	 * This method should be called open
	 */
	public static void shutdown() {
		HTTP_CLIENT.getConnectionManager().shutdown();
	}
}
