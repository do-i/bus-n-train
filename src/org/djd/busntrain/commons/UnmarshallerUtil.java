package org.djd.busntrain.commons;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.djd.busntrain.train.StationModel;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static org.djd.busntrain.commons.ApplicationCommons.URL_STATIONS_TXT;

/**
 * Created with IntelliJ IDEA.
 * User: acorn
 * Date: 11/3/12
 * Time: 11:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class UnmarshallerUtil<T> {
  private final Type TYPE;

  public UnmarshallerUtil() {
    TYPE = new TypeToken<T>() {
    }.getType();
  }

  public T fromUrl(String url) throws UnmarshallException {
    Reader reader = null;
    try {
      reader = new InputStreamReader(new URL(url).openStream());
      return new Gson().fromJson(reader, TYPE);
    } catch (MalformedURLException e) {
      throw new UnmarshallException(e);
    } catch (IOException e) {
      throw new UnmarshallException(e);
    } finally {
      if (reader != null) {
        try {

          reader.close();
        } catch (IOException e) {
        }
      }
    }
  }
}
