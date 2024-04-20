package com.auth.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
public class Customer {

    @Id
    @GeneratedValue
    private Long customerId;

    private String id;

    private String modifiedTime;

    private Boolean firstTimeLogin;

    private String gender;

    private String mobile;

    private Boolean invited;

    private String locale;

    private Boolean firstTimeSignUp;

    private Integer wrongMpinCount;

    private Integer mpinResetCount;

    private String createdBy;

    private String nameOnCard;

    private String dob;

    private Integer saltHash;

    private String name;

    private String createdTime;

    private String modifiedBy;


    private String cardHolderPaymentSideId;

    private String email;

    private String status;

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Card> cards;

}
