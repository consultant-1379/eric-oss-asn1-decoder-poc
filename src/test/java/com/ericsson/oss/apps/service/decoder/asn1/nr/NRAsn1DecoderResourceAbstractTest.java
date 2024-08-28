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

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public abstract class NRAsn1DecoderResourceAbstractTest {

    public final static String DECODED_ASN1 = getTxtFileContent("asn1/decodedAsn1Message.txt");
    public final static String BASE64_EVENT_3031 = getTxtFileContent("asn1/base64EncodedEvent.txt");

    private static String getTxtFileContent(final String resourceFile) {
        try (var inputStream = NRAsn1DecoderResourceAbstractTest.class.getClassLoader().getResourceAsStream(resourceFile)) {
            return new String(Objects.requireNonNull(inputStream).readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public abstract void testNRAsn1Decoding();

}