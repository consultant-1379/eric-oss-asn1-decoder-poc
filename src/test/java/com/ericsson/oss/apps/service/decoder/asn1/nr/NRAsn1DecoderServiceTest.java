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

import com.ericsson.oss.presentation.client.tracedecoder.gpb.decoders.GpbStreamDecoder;
import com.ericsson.oss.presentation.client.tracedecoder.gpb.exceptions.GpbDecoderException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Map;

import static com.ericsson.oss.presentation.client.tracedecoder.gpb.decoders.asn.constants.GpbDecoderAsnConstants.ASN_MESSAGE_DECODED_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@QuarkusTest
@TestProfile(DecoderTestProfile.class)
class NRAsn1DecoderServiceTest {
    private static final String DECODED = "decoded";
    @Inject
    NRAsn1DecoderService nrAsn1DecoderService;

    @InjectMock
    NRAsn1DecoderFactory nrAsn1DecoderFactory;

    GpbStreamDecoder gpbStreamDecoder;

    @BeforeEach
    void setUp() {
        this.gpbStreamDecoder = Mockito.mock(GpbStreamDecoder.class);
        when(nrAsn1DecoderFactory.getGpbStreamDecoder()).thenReturn(gpbStreamDecoder);
    }

    @Test
    void decode() throws GpbDecoderException, IOException {
        byte[] message1 = new byte[]{1, 2, 3, 4, 5};
        byte[] message2 = new byte[]{2, 3, 4, 5, 6};
        checkDecode(message1, ASN_MESSAGE_DECODED_KEY, DECODED, DECODED);
        checkDecode(message2, "anotherKey", "someValue", "");
        verify(nrAsn1DecoderFactory, times(3)).getGpbStreamDecoder();

    }

    private void checkDecode(byte[] message, String asnMessageDecodedKey, String decoded, String expected) throws IOException, GpbDecoderException {
        when(gpbStreamDecoder.decodeBytesFromStp(message)).thenReturn(Map.of(asnMessageDecodedKey, decoded));
        assertEquals(expected, nrAsn1DecoderService.decode(message));
        verify(gpbStreamDecoder, times(1)).decodeBytesFromStp(message);
    }

}