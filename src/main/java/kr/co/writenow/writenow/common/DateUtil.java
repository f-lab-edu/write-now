package kr.co.writenow.writenow.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.springframework.util.ObjectUtils;

public class DateUtil {

  public static Optional<String> datetimeConvertToString(LocalDateTime dateTime) {
    if (ObjectUtils.isEmpty(dateTime)) {
      return Optional.empty();
    }
    return Optional.of(dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
  }

}
