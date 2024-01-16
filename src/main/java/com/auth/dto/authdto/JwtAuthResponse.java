package com.auth.dto.authdto;


import com.auth.dto.Response;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthResponse extends Response {
    private String token;
}