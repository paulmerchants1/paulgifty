package com.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
public class Card {

    @Id
    @GeneratedValue
    private Long cardId;

    private String cardImage;

    private String van;

    private String orgName;

    private String cardMode;

    private String orgId;

    private String issuer;

    private String network;

    private Boolean primaryCard;

    private String lastFourDigits;

    private String programName;

    private String customerId;

    private String id;

    private String rootCardId;

    private String status;

    private Boolean addOnCard;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "custId")
    private Customer customer;


}
