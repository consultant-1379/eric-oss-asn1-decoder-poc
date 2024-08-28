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
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.io.IOException;
import java.util.Base64;

/**
 * This class is used as entry point for decoding until GRPC is available.
 * Needed to test that libraries are loaded correctly at runtime.
 */
@Path("/decode/asn1/nr")
public class Asn1DecoderResource {

    @Inject
    NRAsn1DecoderService nrAsn1DecoderService;

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String decode(String event) throws GpbDecoderException, IOException {
        return nrAsn1DecoderService.decode(Base64.getDecoder().decode(event));
    }
}

