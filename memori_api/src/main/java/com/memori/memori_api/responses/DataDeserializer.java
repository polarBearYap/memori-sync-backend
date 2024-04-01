package com.memori.memori_api.responses;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataDeserializer extends JsonDeserializer<UserCardScheduleResponse.Data> {

    @Override
    public UserCardScheduleResponse.Data deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        Map<Integer, UserCardScheduleResponse.DataItem> items = mapper.readValue(p,
                new TypeReference<Map<Integer, UserCardScheduleResponse.DataItem>>() {
                });
        return new UserCardScheduleResponse.Data(items);
    }
}