package com.example.vacancyparser.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@AllArgsConstructor
public class TechstarsNextDataExtractor {

    private static final Pattern NEXT_DATA_PATTERN = Pattern.compile(
            "<script id=\"__NEXT_DATA__\" type=\"application/json\">(.*?)</script>",
            Pattern.DOTALL
    );

    private final ObjectMapper objectMapper;

    public JsonNode extract(String html) throws IOException {
        Matcher matcher = NEXT_DATA_PATTERN.matcher(html);
        if (!matcher.find()) {
            throw new IllegalStateException("Could not locate __NEXT_DATA__ script in HTML response");
        }
        return objectMapper.readTree(matcher.group(1));
    }
}
