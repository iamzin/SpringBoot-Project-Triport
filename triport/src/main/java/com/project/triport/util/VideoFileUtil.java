package com.project.triport.util;

import com.project.triport.storage.StorageProperties;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class VideoFileUtil {

    private final Path rootLocation;

    private String ffmpegPath = "/usr/local/Cellar/ffmpeg/4.4/bin/ffmpeg";
    private String ffprobePath = "/usr/local/Cellar/ffmpeg/4.4/bin/ffprobe";

    private FFmpeg ffmpeg;
    private FFprobe ffprobe;

    @Autowired
    public VideoFileUtil(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @PostConstruct
    public void init(){
        try{
            ffmpeg = new FFmpeg(ffmpegPath);
            ffprobe = new FFprobe(ffprobePath);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void storeVideo(MultipartFile file) throws IOException {
        Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
    }

    public String encodingVideo(MultipartFile file){
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput("/Users/son-younhwan/Desktop/영상test/test2/"+file.getOriginalFilename())   // Filename, or a FFmpegProbeResult
                .overrideOutputFiles(true) // Override the output if it exists

                .addOutput("/Users/son-younhwan/Desktop/영상test/"+file.getName()+"/"+file.getName()+".m3u8")   // Filename for the destination
//                .setFormat("mp4")        // Format is inferred from filename, or can be set
//                .setTargetSize(250_000)  // Aim for a 250KB file

                .disableSubtitle()       // No subtiles

                .setAudioChannels(1)         // Mono audio
                .setAudioCodec("aac")        // using the aac codec
                .setAudioSampleRate(48_000)  // at 48KHz
                .setAudioBitRate(32768)      // at 32 kbit/s

                .setVideoCodec("copy")     // Video using x264
                .setVideoFrameRate(24, 1)     // at 24 frames per second
                .setVideoResolution(640, 480) // at 640x480 resolution

                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to use experimental specs
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        // Run a one-pass encode
        executor.createJob(builder).run();

        return "/Users/son-younhwan/Desktop/영상test/"+file.getName();
    }
}
