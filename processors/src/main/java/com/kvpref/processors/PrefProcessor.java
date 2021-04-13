package com.kvpref.processors;

import com.kvpref.annotations.DefaultBoolean;
import com.kvpref.annotations.SharePref;
import com.kvpref.annotations.DefaultFloat;
import com.kvpref.annotations.DefaultInt;
import com.kvpref.annotations.DefaultLong;
import com.kvpref.annotations.DefaultString;
import com.kvpref.annotations.ObjectType;
import com.kvpref.annotations.ParcelType;
import com.kvpref.annotations.PrefKey;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({
        "com.pref.annotations.SharePref",
        "com.pref.annotations.PrefKey",
        "com.pref.annotations.DefaultInt",
        "com.pref.annotations.DefaultFloat",
        "com.pref.annotations.DefaultLong",
        "com.pref.annotations.DefaultBoolean",
        "com.pref.annotations.DefaultString",
        "com.pref.annotations.ObjectType",
        "com.pref.annotations.ParcelType"
})
public class PrefProcessor extends AbstractProcessor {

    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) throws PreferenceProcessorsException {
        ClassName superClass = null;
        if (roundEnvironment.getElementsAnnotatedWith(SharePref.class).size() > 1) {
            throw new PreferenceProcessorsException("The type of @SharePref annotation must be unique.");
        } else {
            if (roundEnvironment.getElementsAnnotatedWith(SharePref.class).iterator().hasNext()) {
                Element element = roundEnvironment.getElementsAnnotatedWith(SharePref.class).iterator().next();
                MethodBuilder.PrefClassName = element.getSimpleName() + "_";
                TypeElement typeElement = (TypeElement) element;
                String classNameString = typeElement.getQualifiedName().toString();
                superClass = ClassName.bestGuess(classNameString);
            }
        }
        TypeSpec.Builder builder = TypeSpec.classBuilder(MethodBuilder.PrefClassName);
        if (superClass != null) {
            builder.superclass(superClass.withoutAnnotations());
        }
        builder.addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addField(ClassName.get(MethodBuilder.PrefPackageName, MethodBuilder.PrefClassName), MethodBuilder.PrefInstanceName, Modifier.PRIVATE, Modifier.STATIC)
                .addField(MethodBuilder.MMKV, MethodBuilder.mmkv, Modifier.PRIVATE, Modifier.STATIC)
                .addMethod(MethodBuilder.createInitialize())
                .addMethod(MethodBuilder.createConstructor())
                .addMethod(MethodBuilder.createInstance())
                .addMethod(MethodBuilder.createImportPref());
        for (Element element : roundEnvironment.getElementsAnnotatedWith(PrefKey.class)) {
            String fieldName = element.getSimpleName().toString();
            String key = element.getAnnotation(PrefKey.class).key();
            key = key.isEmpty() ? fieldName.toLowerCase() : key;
            builder.addMethod(MethodBuilder.createRemove(fieldName, key));
            switch (element.asType().getKind()) {
                case INT:
                    int defaultInt = 0;
                    if (element.getAnnotation(DefaultInt.class) != null) {
                        defaultInt = element.getAnnotation(DefaultInt.class).value();
                    }
                    builder.addMethod(MethodBuilder.createGetInt(fieldName, key, defaultInt));
                    builder.addMethod(MethodBuilder.createPutInt(fieldName, key));
                    break;
                case FLOAT:
                    float defaultFloat = 0f;
                    if (element.getAnnotation(DefaultFloat.class) != null) {
                        defaultFloat = element.getAnnotation(DefaultFloat.class).value();
                    }
                    builder.addMethod(MethodBuilder.createGetFloat(fieldName, key, defaultFloat));
                    builder.addMethod(MethodBuilder.createPutFloat(fieldName, key));
                    break;
                case LONG:
                    long defaultLong = 0;
                    if (element.getAnnotation(DefaultLong.class) != null) {
                        defaultLong = element.getAnnotation(DefaultLong.class).value();
                    }
                    builder.addMethod(MethodBuilder.createGetLong(fieldName, key, defaultLong));
                    builder.addMethod(MethodBuilder.createPutLong(fieldName, key));
                    break;
                case BOOLEAN:
                    boolean defaultBoolean = false;
                    if (element.getAnnotation(DefaultBoolean.class) != null) {
                        defaultBoolean = element.getAnnotation(DefaultBoolean.class).value();
                    }
                    builder.addMethod(MethodBuilder.createGetBoolean(fieldName, key, defaultBoolean));
                    builder.addMethod(MethodBuilder.createPutBoolean(fieldName, key));
                    break;
                case DECLARED:
                    String defaultString = "";
                    String objectType = null;
                    String genericObjectType = null;
                    String parcelType = null;
                    String genericParcelType = null;
                    if (element.getAnnotation(DefaultString.class) != null) {
                        defaultString = element.getAnnotation(DefaultString.class).value();
                    } else if (element.getAnnotation(ObjectType.class) != null) {
                        String type = element.asType().toString();
                        if (type.contains("<") || type.contains(">")) {
                            genericObjectType = type;
                        } else {
                            objectType = type;
                        }
                    } else if (element.getAnnotation(ParcelType.class) != null) {
                        String type = element.asType().toString();
                        if (type.contains("<") || type.contains(">")) {
                            genericParcelType = type;
                        } else {
                            parcelType = type;
                        }
                    }
                    if (objectType != null) {
                        builder.addMethod(MethodBuilder.createGetObject(fieldName, key, objectType));
                        builder.addMethod(MethodBuilder.createPutObject(fieldName, key, objectType));
                    } else if (genericObjectType != null) {
                        builder.addMethod(MethodBuilder.createGetGenericObject(fieldName, key, genericObjectType));
                        builder.addMethod(MethodBuilder.createPutGenericObject(fieldName, key, genericObjectType));
                    } else if (parcelType != null) {
                        builder.addMethod(MethodBuilder.createGetParcel(fieldName, key, parcelType));
                        builder.addMethod(MethodBuilder.createPutParcel(fieldName, key, parcelType));
                    } else if (genericParcelType != null) {
                        throw new PreferenceProcessorsException("@ParcelType can not used for generic object.");
                    } else {
                        builder.addMethod(MethodBuilder.createGetString(fieldName, key, defaultString));
                        builder.addMethod(MethodBuilder.createPutString(fieldName, key));
                    }
                    break;
                default:
                    break;
            }
        }
        TypeSpec typeSpec = builder.build();
        JavaFile javaFile = JavaFile.builder(MethodBuilder.PrefPackageName, typeSpec).build();
        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
