package com.memori.memori_api.responses;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.memori.memori_api.responses.UserCardScheduleResponse.Data;
import com.memori.memori_api.responses.UserCardScheduleResponse.DataItem;

public class DataSerializer extends StdSerializer<Data> {

    public DataSerializer() {
        this(null);
    }

    public DataSerializer(Class<Data> t) {
        super(t);
    }

    @Override
    public void serialize(Data value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        for (Map.Entry<Integer, DataItem> entry : value.getItems().entrySet()) {
            gen.writeObjectField(String.valueOf(entry.getKey()), entry.getValue());
        }
        gen.writeEndObject();
    }
}