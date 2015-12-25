/**
 * 
 */
package com.momega.spacesimulator.simulation;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.service.utils.TimeUtils;

/**
 * @author martin
 *
 */
public class TimestampTypeAdapter implements JsonSerializer<Timestamp>, JsonDeserializer<Timestamp> {

	@Override
	public JsonElement serialize(Timestamp src, Type typeOfSrc,	JsonSerializationContext context) {
		String s = TimeUtils.timeAsString(src);
		return new JsonPrimitive(s);
	}

	@Override
	public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		String s = json.getAsJsonPrimitive().getAsString();
		return TimeUtils.parseTimestamp(s);
	}

	

}
