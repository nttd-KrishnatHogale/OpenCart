package API.utils;

import API.pojo.register;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
public class JsonFileUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void writePayloadToJson(Object payload, String filePath) {
        File file = new File(filePath);
        List<Map<String, Object>> dataList = new ArrayList<>();



        if (file.exists()) {
            try {
                // Try to read as an array first
                dataList = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
            } catch (IOException e) {
                // If array reading fails, try reading as an object
                try {
                    Map<String, Object> singleObject = objectMapper.readValue(file, new TypeReference<Map<String, Object>>() {});
                    dataList.add(singleObject);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    throw new RuntimeException("Failed to read existing JSON data from file");
                }
            }
        }

        Map<String, Object> payloadMap;
        try {
            payloadMap = objectMapper.convertValue(payload, new TypeReference<Map<String, Object>>() {});
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to convert payload to Map");
        }
        // Merge existing data with new payload
        dataList.add(payloadMap);

        // Write the updated data back to the file
        try {
            objectMapper.writeValue(file, dataList);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to write updated payload to JSON file");
        }
    }

    public static void writeDeletionRecordToJson(String email, String filePath) {
        try {
            // Create a simple map or a custom object for deletion record
            objectMapper.writeValue(new File(filePath), new DeletionRecord(email));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to write deletion record to JSON file");
        }
    }

    private static class DeletionRecord {
        private String email;

        public DeletionRecord(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}