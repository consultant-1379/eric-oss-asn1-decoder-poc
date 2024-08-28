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

import com.google.protobuf.ByteString;
import io.grpc.Channel;
import io.quarkus.grpc.GrpcClient;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static com.ericsson.oss.apps.proto.asn1.Asn1DecoderServiceOuterClass.NRAsn1EventRequest;
import static com.ericsson.oss.apps.proto.asn1.Asn1DecoderServiceOuterClass.NRAsn1EventResponse;
import static com.ericsson.oss.apps.proto.asn1.Asn1DecoderServiceOuterClass.NRAsn1ParsingStatus.OK;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class Asn1DecoderGrpcServiceTest extends Asn1DecoderGrpcServiceAbstractTest {

    @GrpcClient
    Channel channel;
    Channel getChannel() {
        return channel;
    }

    @Test
    @Override
    public void testNRAsn1Decoding() {
        NRAsn1EventRequest request = NRAsn1EventRequest.newBuilder()
                .addAllEncodedEvent(List.of(ByteString.copyFrom(Base64.getDecoder().decode(BASE64_EVENT_3031))))
                .build();
        var client = com.ericsson.oss.apps.proto.asn1.Asn1DecoderServiceGrpc.newBlockingStub(getChannel());

        NRAsn1EventResponse response = client.decodeNRAsn1Events(request);
        assertEquals(DECODED_ASN1, response.getDecodedAsn1List().get(0).getDecodedEvent());
        assertEquals(OK, response.getDecodedAsn1List().get(0).getParsingStatus());
    }


}
