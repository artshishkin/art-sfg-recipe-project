package com.artarkatesoft.converters;

import lombok.SneakyThrows;
import lombok.Synchronized;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class AbstractConverter<S, T> implements Converter<S, T> {

    private final Class classOfT;

    protected AbstractConverter() {
        Type typeB = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        this.classOfT = (Class) typeB;
    }

    @SneakyThrows
    @Synchronized
    @Nullable
    @Override
    public T convert(S source) {
        if (source == null) return null;
        T target = (T) classOfT.newInstance();
        BeanUtils.copyProperties(source, target);
        return target;
    }
}
