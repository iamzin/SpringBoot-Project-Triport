package com.project.triport.entity;

import com.project.triport.requestDto.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String profileImgUrl;

    @Column(nullable = false)
    private String role;

    public User(UserDto userDto){
        this.email = userDto.getEmail();
        this.password = userDto.getPassword();
        this.nickname = userDto.getNickname();
        this.profileImgUrl = userDto.getProfileImgUrl();
        this.role = userDto.getRole();
    }
}
