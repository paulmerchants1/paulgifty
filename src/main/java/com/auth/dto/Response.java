package com.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response implements Serializable {
    private static final long serialVersionUID = 7104462920542626419L;
    private String status;
    private String statusCode;
    private String message;
    private transient Object data;
    private transient Object errorMessage;
    private String successRule;

    @JsonProperty("response_type")
    private String responseType;
    @JsonProperty("sdktoken")
    private String sdktoken;

    @JsonProperty("response_message")
    private String responseMessage = "TokenRequestDTO Processed Successfully";

    // Password field should not be included in JSON unless explicitly set
    @JsonIgnore
    private transient Boolean password;

    // Setter for password should be private
    public void setPassword(boolean password) {
        this.password = password;
    }

    // Getter for password should return null if password is not set
    @JsonProperty("password")
    public Boolean getPassword() {
        return password;
    }
}
