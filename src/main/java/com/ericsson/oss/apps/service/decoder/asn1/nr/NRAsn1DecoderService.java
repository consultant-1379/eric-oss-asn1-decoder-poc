/*******************************************************************************
 * COPYRIGHT Ericsson 2022
 *
 *
 *
 * The copyright to the computer program(s) herein is the property of
 *
 * Ericsson Inc. The programs may be used and/or copied only with written
 *
 * permission from Ericsson Inc. or in accordance with the terms and
 *
 * conditions stipulated in the agreement/contract under which the
 *
 * program(s) have been supplied.
 ******************************************************************************/
package com.ericsson.oss.apps.service.decoder.asn1.nr;

import com.ericsson.oss.apps.config.CounterName;
import com.ericsson.oss.apps.config.CustomMetrics;
import com.ericsson.oss.presentation.client.tracedecoder.gpb.decoders.GpbStreamDecoder;
import com.ericsson.oss.presentation.client.tracedecoder.gpb.exceptions.GpbDecoderException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

import static com.ericsson.oss.presentation.client.tracedecoder.gpb.decoders.asn.constants.GpbDecoderAsnConstants.ASN_MESSAGE_DECODED_KEY;

@ApplicationScoped
@Slf4j
public class NRAsn1DecoderService {

    private final List<SyncDecoderWrapper> gpbStreamDecoderList;
    private final AtomicLong roundRobin = new AtomicLong();
    private final NRAsn1DecoderFactory nrAsn1DecoderFactory;
    private final CustomMetrics customMetrics;

    @Inject
    public NRAsn1DecoderService(@ConfigProperty(name = "decoder.nr.asn1.pool.size", defaultValue = "1") int poolSize, NRAsn1DecoderFactory nrAsn1DecoderFactory, CustomMetrics customMetrics) {
        this.nrAsn1DecoderFactory = nrAsn1DecoderFactory;
        this.customMetrics = customMetrics;
        gpbStreamDecoderList = IntStream.range(0, poolSize).mapToObj(x -> new SyncDecoderWrapper())
                .toList();
    }

    /**
     * @param message the GPB message + the Kafka registry 4 magic bytes
     * @return the ASN.1 message decoded into a string
     * @throws GpbDecoderException
     * @throws IOException
     */
    public String decode(byte[] message) throws GpbDecoderException, IOException {
        customMetrics.getCounter(CounterName.NR_EVENTS_PARSED).increment();
        customMetrics.getCounter(CounterName.NR_EVENTS_BYTES_PARSED).increment(message.length);
        log.trace("Message received (base64 encoded): {}", Base64.getEncoder().encodeToString(message));
        String decodedAsn1 = gpbStreamDecoderList.get((int) (roundRobin.incrementAndGet() % gpbStreamDecoderList.size())).decode(message);
        log.trace("Decoded String: {}", decodedAsn1);
        customMetrics.getCounter(CounterName.NR_EVENTS_SUCC_PARSED).increment();
        return decodedAsn1;
    }

    private class SyncDecoderWrapper {
        private final GpbStreamDecoder gpbStreamDecoder = nrAsn1DecoderFactory.getGpbStreamDecoder();

        synchronized String decode(byte[] message) throws GpbDecoderException, IOException {
            return gpbStreamDecoder.decodeBytesFromStp(message).getOrDefault(ASN_MESSAGE_DECODED_KEY, "").toString();
        }
    }

}
