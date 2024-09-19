package API.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonFileUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static void writePayloadToJson(Object payload, String filePath) {
        File file = new File(filePath);
        List<Map<String, Object>> dataList = new ArrayList<>();

        if (file.exists()) {
            try {
                // read as an array
                dataList = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
            } catch (IOException e) {
                // array reading fails, try reading as an object
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

public static List<Map<String, Object>> removeRecordFromJson(String email, String filePath) {
    File file = new File(filePath);
    if (!file.exists() || !file.isFile()) {
        throw new IllegalArgumentException("Invalid file path provided: " + filePath);
    }

    List<Map<String, Object>> dataList;
    List<Map<String, Object>> removedRecords = new ArrayList<>();

    try {
        dataList = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
    } catch (IOException e) {
        throw new RuntimeException("Failed to read existing JSON data from file", e);
    }

    // Identify records with the matching email and add them to removedRecords
    removedRecords = dataList.stream()
            .filter(map -> email.equals(map.get("email")))
            .collect(Collectors.toList());

    // Remove records with the matching email from the original list
    dataList.removeIf(map -> email.equals(map.get("email")));

    try {
        // Write the updated list back to the file
        objectMapper.writeValue(file, dataList);
    } catch (IOException e) {
        throw new RuntimeException("Failed to write updated data to JSON file", e);
    }

    // Return the list of removed records
    return removedRecords;
}

    public static void appendRecordsToJson(List<Map<String, Object>> records, String filePath) {
        File file = new File(filePath);
        List<Map<String, Object>> existingRecords = new ArrayList<>();

        if (file.exists()) {
            try {
                existingRecords = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
            } catch (IOException e) {
                if (file.length() == 0) {
                    // If the file is empty, initialize an empty list
                    existingRecords = new ArrayList<>();
                } else {
                    e.printStackTrace();
                    throw new RuntimeException("Failed to read existing data from file, the file might be corrupted");
                }
            }
        }

        // Add new records to the list
        existingRecords.addAll(records);

        try {
            // Write the updated list back to the file
            objectMapper.writeValue(file, existingRecords);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to write updated records to JSON file");
        }
    }
}