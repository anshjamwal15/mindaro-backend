package com.dekhokaun.mindarobackend.utils;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


public class SmsSenderService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String API_URL = "https://we8.in/smsapi/smsapi.php";

    public int sendSmsRequest(String mobileNumber, String username, String message) throws JSONException {
        JSONObject payload = new JSONObject();
        JSONObject data1 = new JSONObject();

        data1.put("usercode", "advijr");
        data1.put("smsuser", username);
        data1.put("smskey", "Ymjsd5h7@3");
        data1.put("mobiles", mobileNumber);
        data1.put("message", message);
        data1.put("senderid", "MINDRO");
        data1.put("entityid", "1501544040000010555");
        data1.put("tempid", "1107172551760125165");
        data1.put("v1", "stage_of_sending_sms");
        data1.put("v2", "user_id_reference");
        data1.put("v3", "any_value");
        data1.put("v4", "any_value");
        data1.put("v5", "datetimesent");

        payload.put("api1", "otp");
        payload.put("data1", data1);
        payload.put("chksum1", "chksum");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(payload.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, requestEntity, String.class);

        System.out.println("Response Code: " + response.getStatusCode());
        return response.getStatusCode().value();
    }
}
