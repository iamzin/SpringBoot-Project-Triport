package com.project.triport.util;

import com.project.triport.entity.Post;
import com.project.triport.requestDto.VideoUrlDto;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@NoArgsConstructor
public class APIUtil {

    @Value("${server.ipAddress}")
    private String encodedIpAddress;

    public VideoUrlDto encodingFile(Post post){
        VideoUrlDto videoUrlDto = new VideoUrlDto(post);

        RestTemplate restTemplate = new RestTemplate();
        String videoResourceUrl = encodedIpAddress + "/videos/encoding";

        HttpEntity<VideoUrlDto> request = new HttpEntity<>(videoUrlDto);

        return restTemplate.postForObject(videoResourceUrl, request, VideoUrlDto.class);
    }
}
