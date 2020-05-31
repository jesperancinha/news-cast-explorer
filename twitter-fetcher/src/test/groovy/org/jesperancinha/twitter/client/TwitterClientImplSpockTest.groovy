package org.jesperancinha.twitter.client

import com.twitter.hbc.httpclient.auth.Authentication
import org.apache.http.impl.client.AbstractHttpClient
import org.jesperancinha.twitter.data.PageDto
import org.jesperancinha.twitter.processor.TwitterMessageProcessor
import spock.lang.Specification

import java.util.concurrent.BlockingQueue

class TwitterClientImplSpockTest extends Specification {

    private TwitterMessageProcessor twitterMessageProcessor = Mock()

    private Authentication authentication = Mock()

    private BlockingQueue<String> blockingQueue = Mock()

    private List<String> searchTerms = Mock()

    def setup() {
    }

    def cleanup() {
    }

    def "Should end gracefully when timeout is 0"() {
        Set<String> messages = null
        Long startTimestamp = null
        Long endTimeStamp = null
        given:
        def twitterClient = TwitterClientImpl
                .builder()
                .authentication(authentication)
                .twitterMessageProcessor(twitterMessageProcessor)
                .stringLinkedBlockingQueue(blockingQueue)
                .searchTerms(searchTerms)
                .timeToWaitSeconds(0)
                .build()
        def iterator = List.of("mockString").iterator()

        when:
        twitterClient.startFetchProcess()

        then:
        1 * searchTerms.iterator() >> { args -> iterator }
        1 * twitterMessageProcessor.processAllMessages(_ as Set<String>, _ as Long, _ as Long) >> {
            args ->
                messages = args[0] as Set<String>
                startTimestamp = args[1] as Long
                endTimeStamp = args[2] as Long
                return PageDto.builder().build()
        }
        messages.isEmpty()
        final long timeStampDiff = endTimeStamp - startTimestamp
        timeStampDiff >= 0
        timeStampDiff <= 1
        1 * blockingQueue.remainingCapacity()
        (0.._) * authentication.setupConnection(_ as AbstractHttpClient)
    }
}
