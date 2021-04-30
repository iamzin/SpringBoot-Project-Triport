package com.project.triport.requestDto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginReqeustDto {

    @NotNull
//    @Size(min = 5, max = 100)
    private String email;

    @NotNull
//    @Size(min = 5, max = 100)
    private String password;

}
