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

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.quarkus.test.junit.TestProfile;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.ConfigProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

@QuarkusIntegrationTest
@TestProfile(GrpcTestProfile.class)
@Slf4j
class Asn1DecoderGrpcServiceIT extends Asn1DecoderGrpcServiceAbstractTest {

    private static ManagedChannel channel;

    @BeforeAll
    static void beforeAll() {
        int port = ConfigProvider.getConfig().getValue("quarkus.http.test-port", Integer.class);
        log.info("GrpcPort {}", port);
        channel = ManagedChannelBuilder.forAddress("localhost", port)
                .usePlaintext()
                .build();
    }

    @AfterAll
    static void afterAll() {
        if (channel != null) {
            channel.shutdownNow();
            channel = null;
        }
    }

    @Override
    Channel getChannel() {
        return channel;
    }
}
