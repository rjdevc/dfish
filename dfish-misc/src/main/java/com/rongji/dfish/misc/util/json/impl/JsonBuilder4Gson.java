package com.rongji.dfish.misc.util.json.impl;

import com.google.gson.*;
import com.rongji.dfish.base.util.json.impl.AbstractJsonBuilder;
import com.rongji.dfish.base.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Json对象构建器采用gson实现
 * @author lamontYu
 * @date 2019-09-29
 */
public class JsonBuilder4Gson extends AbstractJsonBuilder {

    private Map<String, Gson> gsonMap = new HashMap<>();

    private Gson getGson() {
        Gson gson = gsonMap.get(dateFormat);
        if (gson == null) {
            gson = new GsonBuilder().setDateFormat(dateFormat).create();
            gsonMap.put(dateFormat, gson);
        }
        return gson;
    }

    @Override
    public String toJson(Object obj) {
        return getGson().toJson(obj);
    }

    @Override
    public <T> T parseObject(String json, Class<T> objClass) {
        if (Utils.isEmpty(json)) {
            return null;
        }
        T obj = getGson().fromJson(json, objClass);
        return obj;
    }

    @Override
    public <T> List<T> parseArray(String json, Class<T> objClass) {
        if (Utils.isEmpty(json)) {
            return new ArrayList<>(0);
        }
        JsonArray jsonArray = new JsonParser().parse(json).getAsJsonArray();
        List<T> objList = new ArrayList<>(jsonArray.size());
        Gson gson = getGson();
        for (JsonElement jsonElement : jsonArray) {
            objList.add(gson.fromJson(jsonElement, objClass));
        }
        return objList;
    }
}
