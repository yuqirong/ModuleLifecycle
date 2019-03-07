package com.orange.note.lifecycle.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注此注解的类会回调 onAppCreate onAppStart onAppStop
 * @author maomao
 * @date 2019/3/7
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface Module {
}
