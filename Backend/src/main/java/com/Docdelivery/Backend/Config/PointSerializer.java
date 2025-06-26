package com.Docdelivery.Backend.Config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.geolatte.geom.Point;
import org.geolatte.geom.Position;

import java.io.IOException;

public class PointSerializer extends JsonSerializer<Point> {
    @Override
    public void serialize(Point point, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (point == null) {
            gen.writeNull();
        } else {
            gen.writeStartObject();
            Position position = point.getPosition();
            gen.writeNumberField("longitude", position.getCoordinate(0));
            gen.writeNumberField("latitude", position.getCoordinate(1));
            gen.writeEndObject();
        }
    }
}