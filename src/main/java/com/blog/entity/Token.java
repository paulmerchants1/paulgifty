package com.blog.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Token {

    @GeneratedValue
    @Id
    private Integer id;

    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    private boolean expired;

    private boolean revoked;

    @Temporal(TemporalType.TIMESTAMP)
    private Date loginTimestamp;

    @Temporal(TemporalType.TIMESTAMP)
    private Date logoutTimestamp;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
