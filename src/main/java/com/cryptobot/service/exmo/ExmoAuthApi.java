package com.cryptobot.service.exmo;

import com.cryptobot.model.Exchange;
import com.cryptobot.model.ExchangeKey;
import com.cryptobot.model.ExmoIndex;
import com.cryptobot.repository.ExmoRepository;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Component
public class ExmoAuthApi {

    private static final String HMAC_SHA_512 = "HmacSHA512";
    private static final String UTF_8 = "UTF-8";
    private long exmoIndex = 1;

    private RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private ExmoRepository exmoRepository;

    public Object request(String method, ExchangeKey exchangeKey, Class clazz) {
        return request(method, exchangeKey, clazz, new HashMap<>());
    }

    public Object request(String method, ExchangeKey exchangeKey, Class clazz, Map<String, Object> arguments) {
        ExmoIndex index = getExmoIndex();
        arguments.put("nonce", String.valueOf(index.getNone()));

        String postData = buildData(arguments);
        HttpHeaders headers = buildHeaders(exchangeKey, postData);
        HttpEntity entity = new HttpEntity(postData, headers);

        String url = exchangeService.getExchangeUrl(Exchange.EXMO) + method;
        return restTemplate.postForObject(url, entity, clazz);
    }

    private ExmoIndex getExmoIndex() {
        Optional<ExmoIndex> exmoIndex = exmoRepository.findById(this.exmoIndex);
        ExmoIndex index = exmoIndex.orElse(new ExmoIndex());
        index.setNone(index.getNone() + 1);
        exmoRepository.save(index);
        return index;
    }

    private String buildData(Map<String, Object> arguments) {
        return arguments.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));
    }

    private HttpHeaders buildHeaders(ExchangeKey exchangeKey, String postData) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Key", exchangeKey.getExchangeKey());
        headers.set("Sign", getSign(exchangeKey.getExchangeSecret(), postData));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    private SecretKeySpec getSecretKey(String key) {
        try {
            return new SecretKeySpec(key.getBytes(UTF_8), HMAC_SHA_512);
        } catch (UnsupportedEncodingException e) {
            log.error(e);
            throw new IllegalArgumentException("Error create exchangeSecret exchangeKey");
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
