package com.cryptobot.service.exmo;

import com.cryptobot.model.Exchange;
import com.cryptobot.model.ExchangeKey;
import com.cryptobot.service.ExchangeService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Component
public class ExmoAuthApi {

    private static final String HMAC_SHA_512 = "HmacSHA512";
    private static final String UTF_8 = "UTF-8";
    private long nonce = 1;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ExchangeService exchangeService;

    public String request(String method, ExchangeKey exchangeKey) {
        return request(method, exchangeKey, new HashMap<>());
    }

    public String request(String method, ExchangeKey exchangeKey, Map<String, String> arguments) {
        arguments.put("nonce", String.valueOf(nonce++));

        String postData = buildData(arguments);
        HttpHeaders headers = buildHeaders(exchangeKey, postData);
        HttpEntity entity = new HttpEntity(postData, headers);

        String url = exchangeService.getExchangeUrl(Exchange.EXMO) + method;
        return restTemplate.postForObject(url, entity, String.class);
    }

    private String buildData(Map<String, String> arguments) {
        return arguments.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));
    }

    private HttpHeaders buildHeaders(ExchangeKey exchangeKey, String postData) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Key", exchangeKey.getKey());
        headers.set("Sign", getSign(exchangeKey.getSecret(), postData));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    private SecretKeySpec getSecretKey(String key) {
        try {
            return new SecretKeySpec(key.getBytes(UTF_8), HMAC_SHA_512);
        } catch (UnsupportedEncodingException e) {
            log.error(e);
            throw new IllegalArgumentException("Error create secret key");
        }
    }

    private String getSign(String key, String postData) {
        try {
            Mac mac = getMac(getSecretKey(key));
            return Hex.encodeHexString(mac.doFinal(postData.getBytes(UTF_8)));
        } catch (UnsupportedEncodingException e) {
            log.error(e);
            throw new IllegalArgumentException("Error create sign");
        }
    }

    private Mac getMac(SecretKeySpec secretKeySpec) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA_512);
            mac.init(secretKeySpec);
            return mac;
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            log.error(e);
            throw new IllegalArgumentException("Error init Mac");
        }


    }
}
