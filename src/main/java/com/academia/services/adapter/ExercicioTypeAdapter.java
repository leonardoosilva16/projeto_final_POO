package com.academia.services.adapter;

import com.academia.model.treino.Exercicio;
import com.google.gson.*;
import java.lang.reflect.Type;

public class ExercicioTypeAdapter implements JsonSerializer<Exercicio>, JsonDeserializer<Exercicio> {

    private static final String CLASSNAME_PROPERTY = "CLASSE_TYPE";

    @Override
    public JsonElement serialize(Exercicio src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = context.serialize(src).getAsJsonObject();
        obj.addProperty(CLASSNAME_PROPERTY, src.getClass().getName());
        return obj;
    }

    @Override
    public Exercicio deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        JsonElement classNameElement = obj.get(CLASSNAME_PROPERTY);

        if (classNameElement == null) {
            throw new JsonParseException("Propriedade " + CLASSNAME_PROPERTY + " não encontrada no JSON de Exercicio.");
        }

        String className = classNameElement.getAsString();
        try {
            Class<?> clazz = Class.forName(className);
            return context.deserialize(json, clazz);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Classe de exercício não encontrada: " + className, e);
        }
    }
}
