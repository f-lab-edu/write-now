package kr.co.writenow.writenow.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.util.ObjectUtils;

public class DateUtil {

  public static String datetimeConvertToString(LocalDateTime dateTime) {
    if (ObjectUtils.isEmpty(dateTime)) {
      return "";
    }
    return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }

}
