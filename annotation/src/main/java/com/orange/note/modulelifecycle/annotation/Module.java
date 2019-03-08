package com.orange.note.modulelifecycle.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注明是module接口
 * @author maomao
 * @date 2018/12/7
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface Module {
}
