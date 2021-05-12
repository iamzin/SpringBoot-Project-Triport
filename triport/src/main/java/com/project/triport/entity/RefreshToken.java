package com.project.triport.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    private Long Id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String value;

    public RefreshToken updateValue(String token) {
        this.value = token;
        return this;
    }
}
