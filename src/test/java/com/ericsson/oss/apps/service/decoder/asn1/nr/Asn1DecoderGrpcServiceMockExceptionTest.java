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

import com.ericsson.oss.apps.proto.asn1.Asn1DecoderServiceOuterClass;
import com.ericsson.oss.apps.resource.decoder.asn1.Asn1DecoderGrpcService;
import com.ericsson.oss.presentation.client.tracedecoder.gpb.exceptions.GpbDecoderException;
import com.google.protobuf.ByteString;
import io.quarkus.grpc.GrpcService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

import static com.ericsson.oss.apps.proto.asn1.Asn1DecoderServiceOuterClass.NRAsn1EventRequest;
import static com.ericsson.oss.apps.proto.asn1.Asn1DecoderServiceOuterClass.NRAsn1EventResponse;
import static com.ericsson.oss.apps.proto.asn1.Asn1DecoderServiceOuterClass.NRAsn1ParsingStatus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

/**
 * Testing exceptions (they are rather difficult to trigger from the data)
 */
@QuarkusTest
class Asn1DecoderGrpcServiceMockExceptionTest {
    @GrpcService
    Asn1DecoderGrpcService Asn1DecoderGrpcService;

    @InjectMock
    NRAsn1DecoderService nrAsn1DecoderService;

    private static Stream<Arguments> providedExceptionTestCases() {
        return Stream.of(
                Arguments.of(new GpbDecoderException(""), GPB_DECODER_EXCEPTION),
                Arguments.of(new IOException(), IO_EXCEPTION),
                Arguments.of(new RuntimeException(), INTERNAL_ERROR)
        );
    }

    @ParameterizedTest
    @MethodSource("providedExceptionTestCases")
    public void testNRAsn1DecodingException(Exception e, Asn1DecoderServiceOuterClass.NRAsn1ParsingStatus status) throws GpbDecoderException, IOException {
        when(nrAsn1DecoderService.decode(any())).thenThrow(e);
        NRAsn1EventRequest request = NRAsn1EventRequest.newBuilder()
                .addAllEncodedEvent(List.of(ByteString.copyFrom(new byte[]{1, 2, 3, 4})))
                .build();
        Uni<NRAsn1EventResponse> asn1NREventResponseUni = Asn1DecoderGrpcService.decodeNRAsn1Events(request);
        NRAsn1EventResponse response = asn1NREventResponseUni.await().atMost(Duration.ofSeconds(5));
        assertEquals("", response.getDecodedAsn1List().get(0).getDecodedEvent());
        assertEquals(status, response.getDecodedAsn1List().get(0).getParsingStatus());
    }


}
