package com.fjb.sunrise.dtos.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fjb.sunrise.constraints.AfterAndUntilNow;
import com.fjb.sunrise.constraints.StringMustBeDigitWithFraction;
import com.fjb.sunrise.enums.ETrans;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CreateOrUpdateTransactionRequest implements Serializable {
    private Long id;
    @NotBlank(message = "Amount must not be blank")
    @StringMustBeDigitWithFraction(fraction = 2)
    private String amount;

    private ETrans transactionType;

    private Long category;

    private static final String MY_TIME_ZONE = "Asia/Bangkok";
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",
        timezone = MY_TIME_ZONE)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @AfterAndUntilNow("2020-01-01 00:00:00")
    private LocalDateTime createdAt;


}
