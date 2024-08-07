package org.study.hydro.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * The class {@link AuthenticationResponse} provides data when requested. An access token is returned.
 *
 * @author Aliaksandr Pishchala
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;

    @JsonProperty("access_token")
    private String accessToken;
}
