package com.orange.note.jsbridge.processor;

import com.google.auto.service.AutoService;
import com.orange.note.modulelifecycle.annotation.Module;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * @author yuqirong
 */
@AutoService(Processor.class)
public class AnnotationProcessor extends AbstractProcessor {

    private static final String MODULE_NAME = "MODULE_LIFECYCLE_MODULE_NAME";
    private String moduleName;
    private Messager messager;
    private ModuleGenerator moduleGenerator;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        getOptions(env);
        messager = env.getMessager();
        moduleGenerator = new ModuleGenerator(env, moduleName);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        if (annotations != null && !annotations.isEmpty()) {
            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Module.class);
            try {
                moduleGenerator.generateTemplate(elements, moduleName);
            } catch (Exception e) {
                messager.printMessage(Diagnostic.Kind.ERROR, "exception = " + e.getMessage());
            }
            return true;
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotationSet = new HashSet<>();
        annotationSet.add(Module.class.getCanonicalName());
        return annotationSet;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedOptions() {
        Set<String> options = new HashSet<>();
        options.add(MODULE_NAME);
        return options;
    }

    private void getOptions(ProcessingEnvironment env) {
        moduleName = env.getOptions().get(MODULE_NAME);
    }


}
