package com.tesco.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponseDto {
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String gender;
    private String city;
    private String phone;
}
