package com.westpac.assessment.controller;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.math.BigDecimal;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "schemaVersion",
        defaultImpl = CreateAccountRequest.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateAccountRequest.class, name = "1"),
        @JsonSubTypes.Type(value = CreateAccountRequestV2.class, name = "2")
})
public interface CreateAccountVersionedRequest {
    String accountHolderName();

    BigDecimal amount();

    default String isActive() {
        return "Y";
    }
}
