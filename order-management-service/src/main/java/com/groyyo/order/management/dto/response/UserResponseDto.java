package com.groyyo.order.management.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.groyyo.core.dto.userservice.BaseResponseDto;
import com.groyyo.core.user.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto extends BaseResponseDto {
    private String id;
    private String lastName;
    private String emailId;
    private String phone;
    private String factoryId;
    private String addressId;
    private UserType userType;
}
