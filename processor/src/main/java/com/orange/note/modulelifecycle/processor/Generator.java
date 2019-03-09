package com.orange.note.modulelifecycle.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * @author yuqirong
 */
public abstract class Generator {

    protected final Messager messager;
    protected final Filer filer;
    private final String packageName;
    private final String className;
    protected final Elements elementUtils;

    public Generator(ProcessingEnvironment processingEnv, String packageName, String className) {
        this.messager = processingEnv.getMessager();
        this.filer = processingEnv.getFiler();
        this.packageName = packageName;
        this.className = className;
        elementUtils = processingEnv.getElementUtils();
    }

    /**
     * 生成class
     *
     * @param mapMethod
     * @return
     */
    protected final boolean genClass(FieldSpec.Builder fieldSpec, MethodSpec.Builder mapMethod, CodeBlock.Builder codeBlock) {
        TypeSpec.Builder builder = TypeSpec.classBuilder(className)
                .addJavadoc("this is auto generated code by module-lifecycle, don't edit it !!!")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        // implement com.orange.note.modulelifecycle.template.Module$$Lifecycle
        builder.addSuperinterface(ClassName.get(elementUtils.getTypeElement("com.orange.note.modulelifecycle.template.Module$$Lifecycle")));
        if (codeBlock != null) {
            builder = builder.addStaticBlock(codeBlock.build());
        }
        if (mapMethod != null) {
            builder = builder.addMethod(mapMethod.build());
        }
        if (fieldSpec != null) {
            builder = builder.addField(fieldSpec.build());
        }
        TypeSpec routerMapping = builder.build();
        try {
            JavaFile.builder(packageName, routerMapping).build().writeTo(filer);
        } catch (Throwable e) {
            messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
            return false;
        }
        return true;
    }


}
