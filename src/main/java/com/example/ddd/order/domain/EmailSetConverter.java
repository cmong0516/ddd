package com.example.ddd.order.domain;

import com.example.ddd.order.domain.Repository.AttributeConverter;
import jakarta.validation.constraints.Email;
import java.util.Arrays;
import java.util.stream.Collectors;

public class EmailSetConverter implements AttributeConverter<EmailSet,String>{
    @Override
    public String convertToDatabaseColumn(EmailSet attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getEmails().stream()
                .map(email -> email.getAddress())
                .collect(Collectors.joining(","));
    }

    @Override
    public EmailSet convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        String[] emails = dbData.split(",");

        return Arrays.stream(eails)
                .map(value -> new Email(value))
                .collect(Collectors.toSet());

    }
}
