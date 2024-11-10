package com.tesco.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String gender;
    private String city;
}
