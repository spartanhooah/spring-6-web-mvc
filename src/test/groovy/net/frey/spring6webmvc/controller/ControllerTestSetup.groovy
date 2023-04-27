package net.frey.spring6webmvc.controller

import spock.lang.Specification

import java.time.Instant

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt

class ControllerTestSetup extends Specification {
    static def CONFIGURED_JWT = jwt().jwt { jwt ->
        jwt.claims { claims ->
            claims.put("scope", "message.read")
            claims.put("scope", "message.write")
        }
            .subject("messaging-client")
            .notBefore(Instant.now().minusSeconds(5L))
    }
}