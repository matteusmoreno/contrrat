package br.com.matteusmoreno.contrrat.utils;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/signature")
public class SignatureController {

    @Autowired
    private Cloudinary cloudinary;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getSignature() {
        long timestamp = System.currentTimeMillis() / 1000L;
        Map<String, Object> paramsToSign = new HashMap<>();
        paramsToSign.put("timestamp", timestamp);
        // Você pode adicionar outras transformações ou pastas aqui se quiser
        // paramsToSign.put("folder", "profiles");

        String signature = cloudinary.apiSignRequest(paramsToSign, cloudinary.config.apiSecret);

        Map<String, Object> response = new HashMap<>();
        response.put("signature", signature);
        response.put("timestamp", timestamp);
        response.put("api_key", cloudinary.config.apiKey);
        // Adicione o cloud_name para o frontend saber para onde enviar
        response.put("cloud_name", cloudinary.config.cloudName);

        return ResponseEntity.ok(response);
    }
}