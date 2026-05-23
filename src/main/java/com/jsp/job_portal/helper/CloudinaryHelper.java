package com.jsp.job_portal.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CloudinaryHelper {

    @Value("${file.storage.path:uploads}")
    String storagePath;

    @Value("${file.storage.url-prefix:/files}")
    String urlPrefix;

    private void ensureDirectoryExists(String directory) throws IOException {
        Path dirPath = Paths.get(directory);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
    }

    public String saveImage(MultipartFile profilePic) {
        if (profilePic == null || profilePic.isEmpty()) {
            System.err.println("No image file provided.");
            return null;
        }

        try {
            String profileDir = Paths.get(storagePath, "profile_pictures").toString();
            ensureDirectoryExists(profileDir);

            String filename = UUID.randomUUID() + "-" + profilePic.getOriginalFilename();
            Path filepath = Paths.get(profileDir, filename);
            Files.write(filepath, profilePic.getBytes());

            return urlPrefix + "/profile_pictures/" + filename;
        } catch (Exception e) {
            System.err.println("Error saving image: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public String savePdf(MultipartFile resume) {
        if (resume == null || resume.isEmpty()) {
            System.err.println("No resume file provided.");
            return null;
        }

        try {
            String resumeDir = Paths.get(storagePath, "resumes").toString();
            ensureDirectoryExists(resumeDir);

            String filename = UUID.randomUUID() + "-" + resume.getOriginalFilename();
            Path filepath = Paths.get(resumeDir, filename);
            Files.write(filepath, resume.getBytes());

            return urlPrefix + "/resumes/" + filename;
        } catch (Exception e) {
            System.err.println("Error saving PDF: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
