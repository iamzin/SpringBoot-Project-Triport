package com.project.triport.util;

import com.project.triport.storage.StorageProperties;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class VideoUtil {

    @Value("${ffmpeg.ffmpegPath}")
    private String ffmpegPath;
    @Value("${ffmpeg.ffprobePath}")
    private String ffprobePath;
    @Value("${storage.temporary}")
    private String temporaryStorage;

    private final Path rootLocation;
    private FFprobe ffprobe;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public VideoUtil(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @PostConstruct
    public void init() {
        try {
            ffprobe = new FFprobe(ffprobePath);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    public String storeVideo(MultipartFile file) throws IOException {
        String[] videoUrlStringList = file.getOriginalFilename().split("\\.");
        String videoExtension = videoUrlStringList[videoUrlStringList.length-1];
        String randomString = UUID.randomUUID().toString();
        Files.copy(file.getInputStream(), this.rootLocation.resolve(randomString+"." +videoExtension));
        String filepath = temporaryStorage + "/" + randomString+"."+videoExtension;
        return filepath;
    }

    public VideoProbeResult probe(String filepath) throws IOException {
        FFmpegProbeResult probeResult = ffprobe.probe(filepath);

        FFmpegFormat format = probeResult.getFormat();
        double duration = format.duration;

        boolean posPlay = false;
        for (FFmpegStream ffmpegStream : probeResult.getStreams()){
            if(ffmpegStream.codec_type == FFmpegStream.CodecType.VIDEO){
                posPlay = Boolean.parseBoolean(ffmpegStream.is_avc);
            }
        }
        return new VideoProbeResult(duration, posPlay);
    }

    public void deleteTmp(String filepath) {
        File tmpFile = new File(filepath);
        if(tmpFile.exists() ){
            if(tmpFile.delete()){
                logger.info("파일 삭제 성공");
            }else{
                logger.error("파일 삭제 실패");
            }
        }else{
            logger.error("파일이 존재하지 않습니다.");
        }
    }
}


