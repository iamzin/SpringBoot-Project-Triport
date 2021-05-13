package com.project.triport.util;

import com.project.triport.storage.StorageProperties;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import org.apache.commons.io.FileUtils;
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
    private FFmpeg ffmpeg;
    private FFprobe ffprobe;

    @Autowired
    public VideoUtil(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @PostConstruct
    public void init() {
        try {
            ffmpeg = new FFmpeg(ffmpegPath);
            ffprobe = new FFprobe(ffprobePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String storeVideo(MultipartFile file) throws IOException {
        String randomString = UUID.randomUUID().toString();
        Files.copy(file.getInputStream(), this.rootLocation.resolve(randomString+".mp4"));
        String filepath = temporaryStorage + "/" + randomString+".mp4";
        return filepath;
    }

    public VideoProbeResult probe(String filepath) throws IOException {
        FFmpegProbeResult probeResult = ffprobe.probe(filepath);

        FFmpegFormat format = probeResult.getFormat();
        double duration = format.duration;

        boolean posPlay = false;
        for (int i = 0; i < 2; i++){
            FFmpegStream stream = probeResult.getStreams().get(i);
            if(stream.codec_type == FFmpegStream.CodecType.VIDEO){
                posPlay = Boolean.parseBoolean(stream.is_avc);
            }
        }
        return new VideoProbeResult(duration, posPlay);
    }

    public void cleanStorage() throws IOException {
        File tmpStorage = new File(temporaryStorage);

        FileUtils.cleanDirectory(tmpStorage);
        System.out.println("tmp folder 비움 완료");
    }
}


