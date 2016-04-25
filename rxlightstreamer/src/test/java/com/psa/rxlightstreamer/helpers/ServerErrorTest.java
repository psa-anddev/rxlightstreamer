package com.psa.rxlightstreamer.helpers;

import com.psa.rxlightstreamer.BaseTest;

import org.junit.Test;

import static junit.framework.Assert.fail;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * <p>Feature: As a developer, I want to have an accurate representation of the error that a
 * LightStreamer client has returned me.</p>
 * @author Pablo Sánchez Alonso
 * @version 1.0
 */
public class ServerErrorTest extends BaseTest {
    /**
     * <p>Scenario: Getting error responses from LS codes.</p>
     * <p>Given I am connected to LS
     * When I receive an error update from the LS client
     * Then I get the right reason for the error.</p>
     */
    @Test
    public void testServerErrorReturnsTheExpectedErrorReason()
    {
        try {
            ServerError serverError = ServerError.fromLSCode(-10);
            assertThat(serverError).isEqualTo(ServerError.REFUSED_BY_METADATA_ADAPTER);
            assertThat(serverError.getCode()).isEqualTo(-10);

            serverError = ServerError.fromLSCode(1);
            assertThat(serverError).isEqualTo(ServerError.USERNAME_PASSWORD_CHECK_FAILED);
            serverError = ServerError.fromLSCode(2);
            assertThat(serverError).isEqualTo(ServerError.REQUESTED_ADAPTER_SET_NOT_AVAILABLE);
            serverError = ServerError.fromLSCode(7);
            assertThat(serverError).isEqualTo(ServerError.MAXIMUM_LICENCED_SESSION_NUMBER_REACHED);
            serverError = ServerError.fromLSCode(8);
            assertThat(serverError).isEqualTo(ServerError.MAXIMUM_CONFIGURED_SESSION_NUMBER_REACHED);
            serverError = ServerError.fromLSCode(9);
            assertThat(serverError).isEqualTo(ServerError.MAXIUMUM_CONFIGURED_SERVER_LOAD_REACHED);
            serverError = ServerError.fromLSCode(10);
            assertThat(serverError).isEqualTo(ServerError.NEW_SESSIONS_TEMPORALLY_BLOCKED);
            serverError = ServerError.fromLSCode(11);
            assertThat(serverError).isEqualTo(ServerError.STREAMING_NOT_AVAILABLE);

            serverError = ServerError.fromLSCode(31);
            assertThat(serverError).isEqualTo(ServerError.CONNECTION_CLOSED_BY_DESTROY_REQUEST);
            serverError = ServerError.fromLSCode(32);
            assertThat(serverError).isEqualTo(ServerError.CONNECTION_CLOSED_BY_JMX);
            serverError = ServerError.fromLSCode(33);
            assertThat(serverError).isEqualTo(ServerError.UNEXPECTED_ERROR_WHILE_SERVER_WAS_IN_ACTIVITY);
            assertThat(serverError.getCode()).isEqualTo(33);
            serverError = ServerError.fromLSCode(34);
            assertThat(serverError).isEqualTo(ServerError.UNEXPECTED_ERROR_WHILE_SERVER_WAS_IN_ACTIVITY);
            assertThat(serverError.getCode()).isEqualTo(34);
            serverError = ServerError.fromLSCode(35);
            assertThat(serverError).isEqualTo(ServerError.CONNECTION_CLOSED_DUE_TO_USER_SESSION_LIMIT_REACHED);
            serverError = ServerError.fromLSCode(37);
            assertThat(serverError).isEqualTo(ServerError.UNKNOWN_OR_UNEXPECTED_ERROR);
            assertThat(serverError.getCode()).isEqualTo(37);

            serverError = ServerError.fromLSCode(61);
            assertThat(serverError).isEqualTo(ServerError.SERVER_RESPONSE_PARSING_ERROR);

            serverError = ServerError.fromLSCode(12);
            assertThat(serverError).isNull();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Unexpected exception thrown!");
        }
    }
}
