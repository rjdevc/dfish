package com.rongji.dfish.ui;

/**
 * DFISH中节点的常见形态
 * @param <T>
 */
public interface JsonNode<T extends JsonNode<T>> extends JsonObject,HasId<T>,
        DataContainer<T> {
}
