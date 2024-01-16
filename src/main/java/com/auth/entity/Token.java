package com.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Token {

    @Id
    @GeneratedValue

    private Integer id;

    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    private boolean expired;

    private boolean revoked;

    @Temporal(TemporalType.TIMESTAMP)
    private String loginTimestamp;

    @Temporal(TemporalType.TIMESTAMP)
    private String logoutTimestamp;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
