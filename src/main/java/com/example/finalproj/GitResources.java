package com.example.finalproj;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Properties;
import okhttp3.*;
import com.fasterxml.jackson.databind.*;
import java.nio.file.*;
import java.util.ArrayList;

/**
 * This class is responsible for uploading a file to a GitHub repository
 * It reads the file content, encodes it in Base64 and sends it to the GitHub API
 *
 * @author javiergs
 * @version 1.0
 */
public class GitResources {

    private static final String OWNER = "OWNER";
    private static final String REPO = "REPO";
    private static final String BRANCH = "main";
    private static final String FILE_PATH = "FILE.csv";
    private static final String GITHUB_API_URL = "https://api.github.com/repos/";
    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static ArrayList<String> fileList = new ArrayList<String>();

    public String getLocalFileContent(String filePath) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
        if (inputStream == null) {
            System.out.println("File not found!");
            return null;
        }
        byte[] contentBytes = inputStream.readAllBytes();
        return Base64.getEncoder().encodeToString(contentBytes);
    }

    private void uploadFileToGitHub(
            String owner,
            String repo,
            String branch,
            String destinationPath, String base64Content)
            throws IOException {
        // read properties
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
        String token = properties.getProperty("GITHUB_TOKEN");
        String server = properties.getProperty("GITHUB_API_SERVER");
        // create URL
        String url = server + "/repos/" + owner + "/" + repo + "/contents/" + destinationPath;
        URL obj = new URL(url);
        // create JSON object
        JSONObject data = new JSONObject();
        data.put("message", "Upload file");
        data.put("content", base64Content);
        data.put("branch", branch);
        // create connection
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setRequestProperty("Content-Type", "application/json");
        // send data file
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(data.toString().getBytes());
        outputStream.flush();
        if (connection.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
            throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
        }
        BufferedReader br = new BufferedReader(
                new InputStreamReader((connection.getInputStream())));
        String output;
        while ((output = br.readLine()) != null) {
            System.out.println(output);
        }
        connection.disconnect();
    }

    public static void downloadRepo(String repoUrl, String saveDir) throws IOException {
        if (repoUrl.startsWith("https://github.com/")) {
            String path = repoUrl.substring("https://github.com/".length());
            System.out.println(path);
            int firstSlashIndex = path.indexOf('/');
            if (firstSlashIndex == -1) {
                throw new IllegalArgumentException("Invalid GitHub repository URL.");
            }

            String owner = path.substring(0, firstSlashIndex);
            System.out.println(owner);
            String repo = path.substring(firstSlashIndex + 1);

            String apiUrl = GITHUB_API_URL + owner + "/" + repo + "/contents";
            System.out.println(apiUrl);
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

    public static ArrayList<String> getFileList() {
        return fileList;
    }
}