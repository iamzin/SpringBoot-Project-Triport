package com.project.triport.requestDto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthLoginReqeustDto {

    @NotNull
//    @Size(min = 5, max = 100)
    private String email;

    @NotNull
//    @Size(min = 5, max = 100)
    private String password;

}
