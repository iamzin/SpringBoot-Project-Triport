package com.project.triport.util;

import com.project.triport.storage.StorageProperties;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
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
public class VideoFileUtil {

    @Value("${ffmpeg.ffmpegPath}")
    private String ffmpegPath;
    @Value("${ffmpeg.ffprobePath}")
    private String ffprobePath;
    @Value("${storage.origin}")
    private String originStorage;
    @Value("${storage.encoded}")
    private String encodedStorage;

    private final Path rootLocation;
    private FFmpeg ffmpeg;
    private FFprobe ffprobe;

    @Autowired
    public VideoFileUtil(StorageProperties properties) {
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

    public void storeVideo(MultipartFile file) throws IOException {
        Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
    }

    public String encodingVideo(String originalFilename) {
        String randomString = UUID.randomUUID().toString();
        String encodedDirectory = encodedStorage + randomString;
        File dir = new File(encodedDirectory);
        if (!dir.exists()) {
            dir.mkdir();
        }

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(originStorage + originalFilename)   // Filename, or a FFmpegProbeResult
                .overrideOutputFiles(true) // Override the output if it exists

                .addOutput(encodedDirectory + "/" + randomString + ".m3u8")   // Filename for the destination
                .disableSubtitle()       // No subtiles

                .setAudioChannels(1)         // Mono audio
                .setAudioCodec("aac")        // using the aac codec
                .setAudioSampleRate(48_000)  // at 48KHz
                .setAudioBitRate(32768)      // at 32 kbit/s

                .setVideoCodec("copy")     // Video using x264
                .setVideoFrameRate(24, 1)     // at 24 frames per second

                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to use experimental specs
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        // Run a one-pass encode
        executor.createJob(builder).run();

        return encodedDirectory;
    }

    public void cleanStorage() throws IOException {
        File originDir = new File(originStorage);
        File encodedDir = new File(encodedStorage);

        FileUtils.cleanDirectory(originDir);
        FileUtils.cleanDirectory(encodedDir);
    }
}
