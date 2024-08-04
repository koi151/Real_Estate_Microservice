package com.koi151.msproperties.utils;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import org.springframework.data.domain.Page;import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Type;
import java.util.List;

public class PageTypeAdapter implements JsonSerializer<Page<?>>, JsonDeserializer<Page<?>> {

    @Override
    public JsonElement serialize(Page<?> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject
        jsonObject = new JsonObject();
        jsonObject.add("content", context.serialize(src.getContent()));
        jsonObject.addProperty("number", src.getNumber());
        jsonObject.addProperty("size", src.getSize());
        jsonObject.addProperty("totalElements", src.getTotalElements());
        jsonObject.addProperty("totalPages", src.getTotalPages());
        jsonObject.addProperty("last", src.isLast());
        jsonObject.addProperty("first", src.isFirst());
        jsonObject.addProperty("numberOfElements", src.getNumberOfElements());
        jsonObject.addProperty("empty", src.isEmpty());
        jsonObject.add("sort", context.serialize(src.getSort()));
        return jsonObject;
    }

    @Override
    public Page<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();


        List<?> content = context.deserialize(jsonObject.get("content"), new TypeToken<List<?>>() {}.getType());

        int number = jsonObject.has("number") ? jsonObject.get("number").getAsInt() : 0;
        int size = jsonObject.has("size") ? jsonObject.get("size").getAsInt() : 10; // Default page size
        long totalElements = jsonObject.has("totalElements") ? jsonObject.get("totalElements").getAsLong() : 0L;

        // Check if "sort" exists before deserializing, use UNSORTED if missing
        Sort sort = Sort.unsorted();
        if (jsonObject.has("sort")) {
            JsonElement sortJson = jsonObject.get("sort");
            sort = context.deserialize(sortJson, Sort.class); // deserialization for Sort
        }


        PageRequest pageRequest = PageRequest.of(number, size, sort);
        return new PageImpl<>(content, pageRequest, totalElements);
    }
}

