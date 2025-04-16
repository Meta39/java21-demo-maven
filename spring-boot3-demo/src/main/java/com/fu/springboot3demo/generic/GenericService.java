package com.fu.springboot3demo.generic;

/**
 * 通用方法接口
 * @since 2024-07-16
 */
public interface GenericService<T> {

    /**
     * 如果实现的接口是无返回值 void，则返回 Void 对象
     */
    Object invoke(T req);
}