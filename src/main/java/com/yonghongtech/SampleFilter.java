package log.test;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class SampleFilter extends Filter<ILoggingEvent> {
   @Override
   public FilterReply decide(ILoggingEvent event) {
      if (event.getMessage().contains("let")) {
         return FilterReply.ACCEPT;
      } else {
         return FilterReply.DENY;
      }
   }
}