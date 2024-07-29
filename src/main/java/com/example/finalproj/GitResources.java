package com.example.finalproj;

import okhttp3.*;
import com.fasterxml.jackson.databind.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.*;

import static com.example.finalproj.MainApplication.getFolderPath;

public class GitResources {
    private static final String GITHUB_API_URL = "https://api.github.com/repos/";
    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static ArrayList<String> fileList = new ArrayList<>();

    public static void downloadRepo(String repoUrl, String saveDir) throws IOException {
        if (repoUrl.startsWith("https://github.com/")) {
            String path = repoUrl.substring("https://github.com/".length());
            int firstSlashIndex = path.indexOf('/');
            if (firstSlashIndex == -1) {
                throw new IllegalArgumentException("Invalid GitHub repository URL.");
            }

            String owner = path.substring(0, firstSlashIndex);
            String repo = path.substring(firstSlashIndex + 1);

            String apiUrl = GITHUB_API_URL + owner + "/" + repo + "/contents";
            downloadFiles(apiUrl, saveDir);
        } else {
            throw new IllegalArgumentException("Invalid GitHub repository URL.");
        }
    }

    private static void downloadFiles(String apiUrl, String saveDir) throws IOException {
        Request request = new Request.Builder().url(apiUrl).build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            JsonNode jsonNode = objectMapper.readTree(response.body().string());
            for (JsonNode node : jsonNode) {
                MainApplication.setDownloading(true);

                String type = node.get("type").asText();
                String path = node.get("path").asText();
                String downloadUrl = node.get("download_url").asText();

                if ("file".equals(type)) {
                    saveFile(downloadUrl, saveDir + File.separator + path);
                } else if ("dir".equals(type)) {
                    downloadFiles(node.get("url").asText(), saveDir);
                }
            }
            MainApplication.setDownloading(false);
        }
    }

    private static void saveFile(String fileUrl, String filePath) throws IOException {
        Request request = new Request.Builder().url(fileUrl).build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            Files.createDirectories(Paths.get(filePath).getParent());
            try (InputStream inputStream = response.body().byteStream()) {
                Files.copy(inputStream, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    public static void addFilesToList(String filePath) {
        File rootDir = new File(filePath);
        if (rootDir.exists() && rootDir.isDirectory()) {
            traverseDirectory(rootDir, "");
        } else {
            System.out.println("Invalid directory path: " + filePath);
        }
    }

    private static void traverseDirectory(File directory, String parentPath) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    fileList.add(parentPath + file.getName());
                } else if (file.isDirectory()) {
                    traverseDirectory(file, parentPath + file.getName() + "/");
                }
            }
        }
    }

    public static ArrayList<String> getFileList() {
        return fileList;
    }

    public static List<String> getCommitHistory() throws IOException {
        Path gitDir = Paths.get(getFolderPath(), ".git");
        if (Files.exists(gitDir)) {
            // Use a more detailed git log format
            ProcessBuilder pb = new ProcessBuilder("git", "log", "--pretty=format:%h %an %ad %s", "--date=short");
            pb.directory(gitDir.toFile());
            Process process = pb.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                return reader.lines().map(line -> {
                    String[] parts = line.split(" ", 4);
                    return String.format("Commit: %s, Author: %s, Date: %s, Message: %s",
                            parts[0], parts[1], parts[2], parts[3]);
                }).toList();
            }
        }
        throw new IOException("Git repository not found.");
    }
}
