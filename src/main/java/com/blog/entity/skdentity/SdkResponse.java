package com.blog.entity.skdentity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
public class SdkResponse {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "mobileNo can't be Null")
    @NotEmpty(message = "mobileNo can't be Empty")
    @Size(max = 12, message = "Please Enter the 12 digits mobileNo with std code")
    @Pattern(regexp = "^91[6-9][0-9]{9}", message="Please Enter Valid MobileNo.")
    private String mobileNo;
    private String sdkToken;



}
