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

import com.ericsson.oss.presentation.client.tracedecoder.gpb.configuration.DecoderConfigurator;
import com.ericsson.oss.presentation.client.tracedecoder.gpb.constants.enums.OutputTypeImpl;
import com.ericsson.oss.presentation.client.tracedecoder.gpb.decoders.GpbStreamDecoder;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;

import static com.ericsson.oss.presentation.client.tracedecoder.gpb.constants.GpbDecoderConstants.OUTPUT_TYPE;

@ApplicationScoped
public class NRAsn1DecoderFactory {
    public GpbStreamDecoder getGpbStreamDecoder() {
        return new GpbStreamDecoder(new DecoderConfigurator(), Map.of(OUTPUT_TYPE, OutputTypeImpl.MAP.name()));
    }
}
