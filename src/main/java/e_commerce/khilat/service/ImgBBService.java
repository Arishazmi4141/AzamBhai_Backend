package e_commerce.khilat.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


@Service
public class ImgBBService {
	
	private static final Logger LOGGER =
            LoggerFactory.getLogger(ImgBBService.class);

    private final String apiKey = "9952ea03416a78939e6196c54d2a21da"; // Replace with your API key

    public String uploadImage(MultipartFile file) throws Exception {

        LOGGER.debug("Uploading image to ImgBB: {}", file.getOriginalFilename());

        // Check file size (ImgBB max 32MB)
        long fileSize = file.getSize();
        LOGGER.debug("File size in bytes: {}", fileSize);
        if (fileSize > 32 * 1024 * 1024) {
            throw new RuntimeException("File size exceeds ImgBB limit of 32MB");
        }

        // 1️⃣ Compress / Resize image using Thumbnailator
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        BufferedImage resizedImage = Thumbnails.of(originalImage)
                .size(1024, 1024) // Max width/height
                .outputFormat("jpg")
                .asBufferedImage();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "jpg", baos);
        byte[] fileBytes = baos.toByteArray();
        LOGGER.debug("Compressed image bytes length: {}", fileBytes.length);

        // 2️⃣ Encode filename safely
        String fileNameEncoded = URLEncoder.encode(file.getOriginalFilename(), "UTF-8");

        // 3️⃣ Prepare multipart/form-data body
        String boundary = "Boundary-" + System.currentTimeMillis();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        String partHeader = "--" + boundary + "\r\n" +
                "Content-Disposition: form-data; name=\"image\"; filename=\"" + fileNameEncoded + "\"\r\n" +
                "Content-Type: " + file.getContentType() + "\r\n\r\n";

        outputStream.write(partHeader.getBytes());
        outputStream.write(fileBytes);
        outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());

        byte[] requestBody = outputStream.toByteArray();
        LOGGER.debug("Multipart body prepared, total length: {}", requestBody.length);

        // 4️⃣ Send POST request with retry logic
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.imgbb.com/1/upload?key=" + apiKey))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArray(requestBody))
                .build();

        int maxRetries = 3;
        int attempt = 0;

        while (attempt < maxRetries) {
            attempt++;
            try {
                LOGGER.debug("Attempt {}: Sending request to ImgBB API...", attempt);
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                LOGGER.debug("Attempt {}: Received response with status code {}", attempt, response.statusCode());
                LOGGER.debug("Attempt {}: Raw response body: {}", attempt, response.body());

                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.body());

                if (root.path("success").asBoolean(false)) {
                    String imageUrl = root.path("data").path("url").asText(null);
                    if (imageUrl == null || imageUrl.isEmpty()) {
                        LOGGER.error("Attempt {}: ImgBB URL is null! Full response: {}", attempt, response.body());
                        continue; // retry
                    }
                    LOGGER.debug("Image successfully uploaded to ImgBB: {}", imageUrl);
                    return imageUrl;
                } else {
                    LOGGER.warn("Attempt {} failed: {}", attempt, response.body());
                }

            } catch (Exception ex) {
                LOGGER.warn("Attempt {} exception: {}", attempt, ex.getMessage());
            }

            Thread.sleep(1000); // wait 1 sec before retry
        }

        throw new RuntimeException("Failed to upload image after " + maxRetries + " attempts");
    }

}
