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

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class NRAsn1DecoderResourceTest extends NRAsn1DecoderResourceAbstractTest {

    /**
     * this test makes little sense in itself, other than checking that the required
     * files loaded by the gpb-decoder are loaded correctly from the jar.
     */
    @Test
    public void testNRAsn1Decoding() {
        given()
                .contentType(ContentType.TEXT)
                .body(BASE64_EVENT_3031)
                .when()
                .post("/decode/asn1/nr")
                .then()
                .statusCode(200)
                .body(is(DECODED_ASN1));
    }
}