package com.auth.dto.sdkdto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;



@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class MobileNoDTO {


    @NotNull(message = "mobileNo can't be Null")
    @NotEmpty(message = "mobileNo can't be Empty")
    @Size(max = 12, message = "Please Enter the 12 digits mobileNo with std code")
    @Pattern(regexp = "^91[6-9][0-9]{9}", message="Please Enter Valid MobileNo.")
    private String mobileNo;

}
