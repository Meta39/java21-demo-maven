package com.fu.springboot3starterdemo.api;

public interface PostApi<T> {

    Object execute(T request);

}
