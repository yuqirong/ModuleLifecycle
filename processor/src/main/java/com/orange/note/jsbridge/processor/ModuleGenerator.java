package com.orange.note.jsbridge.processor;

import com.google.common.reflect.TypeToken;
import com.orange.note.modulelifecycle.annotation.Module;
import com.squareup.javapoet.MethodSpec;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * @author maomao
 * @date 2018/12/7
 */
public class ModuleGenerator extends Generator {

    private static final String PACKAGE_NAME = "com.orange.note.modulelifecycle.template";
    private static final String MODULE_MAP_CLASS_NAME = "Module$$";
    private static final String BaseModule_CLASS_NAME = "com.orange.note.lifecycle.BaseModule";


    public ModuleGenerator(ProcessingEnvironment processingEnv, String moduleName) {
        super(processingEnv, PACKAGE_NAME, MODULE_MAP_CLASS_NAME + moduleName);
    }

    public final boolean generateTemplate(Set<? extends Element> elements, String moduleName) {
        MethodSpec.Builder mapMethod = MethodSpec.methodBuilder("getModuleList")
                .addStatement("$T<$T> list = new $T<>()", List.class, String.class, ArrayList.class);

        for (Element element : elements) {
            Module module = element.getAnnotation(Module.class);
            if (module == null) {
                messager.printMessage(Diagnostic.Kind.ERROR, "@Module lost:\n" + element);
                continue;
            }
            //类名
            String qualifiedName = ((TypeElement) element).getQualifiedName().toString();
            String superClassName = ((TypeElement) element).getSuperclass().toString();
            if (!BaseModule_CLASS_NAME.equals(superClassName)) {
                throw new IllegalArgumentException("the @JsInterface class of " + qualifiedName + " must extends " + BaseModule_CLASS_NAME);
            }
            mapMethod.addStatement("list.add($S)", qualifiedName);
        }

        Type type = new TypeToken<List<String>>() {
        }.getType();
        mapMethod.returns(type)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC);
        return genClass(null, mapMethod, null);
    }


}
