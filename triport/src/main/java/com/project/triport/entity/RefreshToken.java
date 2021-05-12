package com.project.triport.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "refresh_token")
public class RefreshToken extends Timestamped {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

//    @Column(nullable = false)
    @Id
    @Column(columnDefinition = "varchar(200)")
    private String email;

//    @Column(nullable = false)
    private String value;

    public void updateValue(String token) {
        this.value = token;
    }
//
//    public RefreshToken(String email, String value) {
//        this.email = email;
//        this.value = value;
//    }
}
