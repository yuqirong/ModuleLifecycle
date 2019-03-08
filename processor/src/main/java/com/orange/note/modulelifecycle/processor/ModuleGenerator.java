package com.orange.note.modulelifecycle.processor;

import com.google.gson.reflect.TypeToken;
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
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

/**
 * @author maomao
 * @date 2018/12/7
 */
public class ModuleGenerator extends Generator {

    private static final String PACKAGE_NAME = "com.orange.note.modulelifecycle.template";
    private static final String MODULE_MAP_CLASS_NAME = "Module$$Lifecycle$$";
    private static final String BaseModule_CLASS_NAME = "com.orange.note.modulelifecycle.BaseModule";


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
            // 类名
            String qualifiedName = ((TypeElement) element).getQualifiedName().toString();
            // 接口
            List<? extends TypeMirror> interfaces = ((TypeElement) element).getInterfaces();

            if (interfaces == null || interfaces.isEmpty()) {
                throw new IllegalArgumentException("the @JsInterface class of " + qualifiedName + " must extends " + BaseModule_CLASS_NAME);
            }
            boolean foundBaseModuleInterface = false;
            for (TypeMirror typeMirror : interfaces) {
                if (BaseModule_CLASS_NAME.equals(typeMirror.toString())) {
                    foundBaseModuleInterface = true;
                    break;
                }
            }
            if (!foundBaseModuleInterface) {
                throw new IllegalArgumentException("the @JsInterface class of " + qualifiedName + " must extends " + BaseModule_CLASS_NAME);
            }
            mapMethod.addStatement("list.add($S)", qualifiedName);
        }

        mapMethod.addStatement("return list");

        Type type = new TypeToken<List<String>>() {
        }.getType();
        mapMethod.returns(type)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC);
        return genClass(null, mapMethod, null);
    }


}
