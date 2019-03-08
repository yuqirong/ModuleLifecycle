package com.orange.note.modulelifecycle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注明是 module
 *
 * @author maomao
 * @date 2019/3/8
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface Module {
}
