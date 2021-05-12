package com.project.triport.innerAPI;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class Video implements Serializable {
    private String videoUrl;
}
