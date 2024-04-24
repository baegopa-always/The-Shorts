package org.example.shortsaccount.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserRequest {
    private String username;
    private String email;
    private String password;
    private int role_id;
}