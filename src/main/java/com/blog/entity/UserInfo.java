package com.blog.entity;


//import jakarta.persistence.*;
import jakarta.persistence.*;
import lombok.*;



@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"mobileNo", "cardNo"})
})
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String mobileNo;
    private String kitNo;
    private String cardNo;
    private String password;
    private String token;




}
