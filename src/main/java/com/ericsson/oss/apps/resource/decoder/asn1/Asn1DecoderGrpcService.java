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
package com.ericsson.oss.apps.resource.decoder.asn1;

import com.ericsson.oss.apps.service.decoder.asn1.nr.NRAsn1DecoderService;
import com.ericsson.oss.presentation.client.tracedecoder.gpb.exceptions.GpbDecoderException;
import com.google.protobuf.ByteString;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Base64;

import static com.ericsson.oss.apps.proto.asn1.Asn1DecoderServiceOuterClass.*;
import static com.ericsson.oss.apps.proto.asn1.Asn1DecoderServiceOuterClass.NRAsn1ParsingStatus.*;


@GrpcService
@Singleton
@Slf4j
public class Asn1DecoderGrpcService implements com.ericsson.oss.apps.proto.asn1.Asn1DecoderService {

    private final NRAsn1DecoderService nrAsn1DecoderService;

    @Inject
    public Asn1DecoderGrpcService(NRAsn1DecoderService nrAsn1DecoderService) {
        this.nrAsn1DecoderService = nrAsn1DecoderService;
    }

    @Override
    public Uni<NRAsn1EventResponse> decodeNRAsn1Events(NRAsn1EventRequest request) {
        // Decode Event in the list
        var eventList = request.getEncodedEventList().stream()
                .map(ByteString::toByteArray)
                .map(this::getNrAsn1Decoded).toList();

        // Create response
        NRAsn1EventResponse response = NRAsn1EventResponse
                .newBuilder()
                .addAllDecodedAsn1(eventList)
                .build();

        return Uni.createFrom().item(response);
    }

    private NRAsn1Decoded getNrAsn1Decoded(byte[] message) {
        try {
            String decoded = nrAsn1DecoderService.decode(message);
            return NRAsn1Decoded.newBuilder().setDecodedEvent(decoded).setParsingStatus(OK).build();
        } catch (GpbDecoderException e) {
            return getNrAsn1DecodingError(message, e, GPB_DECODER_EXCEPTION);
        } catch (IOException e) {
            return getNrAsn1DecodingError(message, e, IO_EXCEPTION);
        } catch (RuntimeException e) {
            return getNrAsn1DecodingError(message, e, INTERNAL_ERROR);
        }
    }

    private static NRAsn1Decoded getNrAsn1DecodingError(byte[] message, Exception e, NRAsn1ParsingStatus status) {
        log.error("Failed to parse message {} ", Base64.getEncoder().encodeToString(message), e);
        return NRAsn1Decoded.newBuilder().setDecodedEvent("").setParsingStatus(status).build();
    }

}
