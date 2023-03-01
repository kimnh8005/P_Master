package kr.co.pulmuone.v1.promotion.linkprice.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ISO8601DateSerializer extends JsonSerializer<Date> {

    private static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";

    @Override
    public void serialize(Date date, JsonGenerator gen,
        SerializerProvider serializerProvider) throws IOException, JsonProcessingException {

        if (date == null) {
            gen.writeString("");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            gen.writeString(sdf.format(date));
        }
    }
}
