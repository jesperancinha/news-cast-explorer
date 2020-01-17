package org.jesperancinha.twitter.filters;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import org.jesperancinha.twitter.processor.TwitterMessageProcessorImpl;

public class TwitterFilter extends Filter<ILoggingEvent> {

    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (event.getLoggerName().equals(TwitterMessageProcessorImpl.class.getCanonicalName())) {
            return FilterReply.ACCEPT;
        } else {
            return FilterReply.DENY;
        }
    }
}