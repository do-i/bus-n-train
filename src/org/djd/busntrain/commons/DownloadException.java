package org.djd.busntrain.commons;

public class DownloadException extends Exception {

  /**
   *
   */
  private static final long serialVersionUID = 848617131583392896L;

  enum ERROR_CODE {
    HTTP_PROTOCOL_ERROR, FAILED_CREATE_HTTP_CONNECTION,
    STREAM_COULD_NOT_BE_CREATED, ENTITY_IS_NOT_REPEATABLE,
    READ_FILE_ERROR, EMPTY_URL
  }

  ;

  public DownloadException(ERROR_CODE eCode) {
    super(eCode.toString());
  }


}
