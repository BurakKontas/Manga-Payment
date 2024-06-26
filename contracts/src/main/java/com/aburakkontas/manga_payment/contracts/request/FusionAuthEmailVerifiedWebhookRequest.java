package com.aburakkontas.manga_payment.contracts.request;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class FusionAuthEmailVerifiedWebhookRequest {
    private Event event;

    @Data
    public static class Event {
        private String applicationId;
        private long createInstant;
        private String id;
        private EventInfo info;
        private Registration registration;
        private String tenantId;
        private String type;
        private User user;

        @Data
        public static class EventInfo {
            private String ipAddress;
            private String userAgent;
        }

        @Data
        public static class Registration {
            private String applicationId;
            private Map<String, Object> data;
            private String id;
            private long insertInstant; // Eksik olan alan
            private long lastLoginInstant;
            private long lastUpdateInstant; // Eksik olan alan
            private List<String> preferredLanguages;
            private List<String> roles;
            private Map<String, Object> tokens;
            private String username; // Eksik olan alan
            private String usernameStatus;
            private boolean verified;
            private long verifiedInstant; // Eksik olan alan
        }

        @Data
        public static class User {
            private boolean active;
            private String connectorId;
            private Map<String, Object> data;
            private String email;
            private String firstName;
            private String fullName; // Eksik olan alan
            private String id;
            private long insertInstant;
            private long lastLoginInstant;
            private String lastName;
            private long lastUpdateInstant;
            private List<Object> memberships;
            private String mobilePhone; // Eksik olan alan
            private boolean passwordChangeRequired;
            private long passwordLastUpdateInstant;
            private List<String> preferredLanguages;
            private List<Object> registrations;
            private String tenantId;
            private TwoFactor twoFactor;
            private String usernameStatus;
            private boolean verified;
            private long verifiedInstant;

            @Data
            public static class TwoFactor {
                private List<Object> methods;
                private List<Object> recoveryCodes;
            }
        }
    }
}
