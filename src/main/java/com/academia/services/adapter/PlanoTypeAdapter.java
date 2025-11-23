package com.academia.services.adapter;

import com.academia.model.plano.Plano;
import com.google.gson.*;
import java.lang.reflect.Type;

public class PlanoTypeAdapter implements JsonSerializer<Plano>, JsonDeserializer<Plano> {

    private static final String CLASSNAME_PROPERTY = "CLASSE_TYPE";

    @Override
    public JsonElement serialize(Plano src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = context.serialize(src).getAsJsonObject();
        obj.addProperty(CLASSNAME_PROPERTY, src.getClass().getName());
        return obj;
    }

    @Override
    public Plano deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        JsonElement classNameElement = obj.get(CLASSNAME_PROPERTY);

        if (classNameElement == null) {
            throw new JsonParseException("Propriedade " + CLASSNAME_PROPERTY + " não encontrada no JSON.");
        }

        String className = classNameElement.getAsString();
        try {
            Class<?> clazz = Class.forName(className);
            return context.deserialize(json, clazz);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Classe não encontrada: " + className, e);
        }
    }
}
