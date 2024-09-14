import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Client {
    public static void main(String[] args) {
        final String sensorName = "Sensor1";

        registerSensor(sensorName);

        Random random = new Random();

        double maxTemperature = 45.0;
        for(int i = 0; i < 10; i++){
            System.out.println(i);
            sendMeasurement(random.nextDouble() * maxTemperature,
                    random.nextBoolean(), sensorName);
        }
    }

    public static void registerSensor(String sensorName) {
        final String url = "http://localhost:7070/sensors/register";

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("name", sensorName);

        makePostRequestWithJSONData(url,jsonData);
    }

    private static void sendMeasurement(double value, boolean raining, String sensorName) {
        final String url = "http://localhost:7070/measurements/add";

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("value", value);
        jsonData.put("raining", raining);

        Map<String, String> sensorData = new HashMap<>();
        sensorData.put("name", sensorName);
        jsonData.put("sensor", sensorData);

        //jsonData.put("sensor", Map.of("name", sensorName));

        makePostRequestWithJSONData(url,jsonData);
    }

    private static void makePostRequestWithJSONData(String url, Map<String, Object> jsonData) {
        final RestTemplate restTemplate = new RestTemplate();

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> request = new HttpEntity<>(jsonData, headers);

        try {
            restTemplate.postForObject(url, request, String.class);

            System.out.println("The measurement has been successfully sent to the server!");
        } catch (HttpClientErrorException e) {
            System.out.println("Error!");
            System.out.println(e.getMessage());
        }
    }
}
