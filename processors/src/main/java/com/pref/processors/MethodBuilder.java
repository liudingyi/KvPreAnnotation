package com.pref.processors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Modifier;

public class MethodBuilder {

    public static final String PrefPackageName = "com.pref";
    public static String PrefClassName = "Pref";
    public static final String PrefInstanceName = "instance";

    private static final ClassName Context = ClassName.get("android.content", "Context");
    public static final ClassName SharedPreferences = ClassName.get("android.content", "SharedPreferences");

    public static final ClassName MMKV = ClassName.get("com.tencent.mmkv", "MMKV");
    public static final String mmkv = "mmkv";

    /**
     * @param str String
     * @return String
     */
    private static String upperCase(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    /**
     * @return MethodSpec
     */
    public static MethodSpec createInitialize() {
        return MethodSpec.methodBuilder("initialize")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(Context, "context")
                .addStatement("MMKV.initialize(context)")
                .build();

    }

    /**
     * @return MethodSpec
     */
    public static MethodSpec createConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addStatement("$N = MMKV.defaultMMKV()", mmkv)
                .build();
    }

    /**
     * @return MethodSpec
     */
    public static MethodSpec createInstance() {
        return MethodSpec.methodBuilder("getInstance")
                .addModifiers(Modifier.PUBLIC, Modifier.SYNCHRONIZED, Modifier.STATIC)
                .returns(ClassName.get(PrefPackageName, PrefClassName))
                .beginControlFlow("if($N == null)", PrefInstanceName)
                .beginControlFlow("synchronized (" + PrefClassName + ".class)")
                .addStatement("$N = new $N()", PrefInstanceName, PrefClassName)
                .endControlFlow()
                .endControlFlow()
                .addStatement("return $N", PrefInstanceName)
                .build();
    }

    /**
     * @return MethodSpec
     */
    public static MethodSpec createImportPref() {
        return MethodSpec.methodBuilder("importPreferences")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Context, "context")
                .addParameter(String.class, "pref_name")
                .addStatement("$T preference = context.getSharedPreferences(pref_name, 0)", SharedPreferences)
                .addStatement("int count = $N.importFromSharedPreferences(preference)", mmkv)
                .beginControlFlow("if(count > 0)")
                .addStatement("preference.edit().clear().commit()")
                .endControlFlow()
                .build();
    }

    /**
     * @param fieldName String
     * @param key       String
     * @return MethodSpec
     */
    public static MethodSpec createRemove(String fieldName, String key) {
        return MethodSpec.methodBuilder("remove" + upperCase(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .beginControlFlow("synchronized($N.class)", PrefClassName)
                .beginControlFlow("if($N != null)", mmkv)
                .addStatement("$N.removeValueForKey($S)", mmkv, key)
                .endControlFlow()
                .endControlFlow()
                .build();
    }

    /**
     * @param fieldName  String
     * @param key        String
     * @param defaultInt int
     * @return MethodSpec
     */
    public static MethodSpec createGetInt(String fieldName, String key, int defaultInt) {
        return MethodSpec.methodBuilder("get" + upperCase(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .beginControlFlow("synchronized($N.class)", PrefClassName)
                .beginControlFlow("if($N != null)", mmkv)
                .addStatement("return $N.decodeInt($S, $L)", mmkv, key, defaultInt)
                .endControlFlow()
                .addStatement("return $L", defaultInt)
                .endControlFlow()
                .build();
    }

    /**
     * @param fieldName String
     * @param key       String
     * @return MethodSpec
     */
    public static MethodSpec createPutInt(String fieldName, String key) {
        return MethodSpec.methodBuilder("put" + upperCase(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(int.class, "value")
                .beginControlFlow("synchronized($N.class)", PrefClassName)
                .beginControlFlow("if($N != null)", mmkv)
                .addStatement("$N.encode($S, value)", mmkv, key)
                .endControlFlow()
                .endControlFlow()
                .build();
    }

    /**
     * @param fieldName    String
     * @param key          String
     * @param defaultFloat float
     * @return MethodSpec
     */
    public static MethodSpec createGetFloat(String fieldName, String key, float defaultFloat) {
        return MethodSpec.methodBuilder("get" + upperCase(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .returns(float.class)
                .beginControlFlow("synchronized($N.class)", PrefClassName)
                .beginControlFlow("if($N != null)", mmkv)
                .addStatement("return $N.decodeFloat($S, $L)", mmkv, key, defaultFloat + "f")
                .endControlFlow()
                .addStatement("return $L", defaultFloat + "f")
                .endControlFlow()
                .build();
    }

    /**
     * @param fieldName String
     * @param key       String
     * @return MethodSpec
     */
    public static MethodSpec createPutFloat(String fieldName, String key) {
        return MethodSpec.methodBuilder("put" + upperCase(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(float.class, "value")
                .beginControlFlow("synchronized($N.class)", PrefClassName)
                .beginControlFlow("if($N != null)", mmkv)
                .addStatement("$N.encode($S, value)", mmkv, key)
                .endControlFlow()
                .endControlFlow()
                .build();
    }


    /**
     * @param fieldName   String
     * @param key         String
     * @param defaultLong Long
     * @return MethodSpec
     */
    public static MethodSpec createGetLong(String fieldName, String key, long defaultLong) {
        return MethodSpec.methodBuilder("get" + upperCase(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .returns(long.class)
                .beginControlFlow("synchronized($N.class)", PrefClassName)
                .beginControlFlow("if($N != null)", mmkv)
                .addStatement("return $N.decodeLong($S, $L)", mmkv, key, defaultLong)
                .endControlFlow()
                .addStatement("return $L", defaultLong)
                .endControlFlow()
                .build();
    }

    /**
     * @param fieldName String
     * @param key       String
     * @return MethodSpec
     */
    public static MethodSpec createPutLong(String fieldName, String key) {
        return MethodSpec.methodBuilder("put" + upperCase(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(long.class, "value")
                .beginControlFlow("synchronized($N.class)", PrefClassName)
                .beginControlFlow("if($N != null)", mmkv)
                .addStatement("$N.encode($S, value)", mmkv, key)
                .endControlFlow()
                .endControlFlow()
                .build();
    }

    /**
     * @param fieldName      String
     * @param key            String
     * @param defaultBoolean Boolean
     * @return MethodSpec
     */
    public static MethodSpec createGetBoolean(String fieldName, String key, boolean defaultBoolean) {
        return MethodSpec.methodBuilder("get" + upperCase(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .returns(boolean.class)
                .beginControlFlow("synchronized($N.class)", PrefClassName)
                .beginControlFlow("if($N != null)", mmkv)
                .addStatement("return $N.decodeBool($S, $L)", mmkv, key, defaultBoolean)
                .endControlFlow()
                .addStatement("return $L", defaultBoolean)
                .endControlFlow()
                .build();
    }

    /**
     * @param fieldName String
     * @param key       String
     * @return MethodSpec
     */
    public static MethodSpec createPutBoolean(String fieldName, String key) {
        return MethodSpec.methodBuilder("put" + upperCase(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(boolean.class, "value")
                .beginControlFlow("synchronized($N.class)", PrefClassName)
                .beginControlFlow("if($N != null)", mmkv)
                .addStatement("$N.encode($S, value)", mmkv, key)
                .endControlFlow()
                .endControlFlow()
                .build();
    }

    /**
     * @param fieldName     String
     * @param key           String
     * @param defaultString String
     * @return MethodSpec
     */
    public static MethodSpec createGetString(String fieldName, String key, String defaultString) {
        return MethodSpec.methodBuilder("get" + upperCase(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .beginControlFlow("synchronized($N.class)", PrefClassName)
                .beginControlFlow("if($N != null)", mmkv)
                .addStatement("return $N.decodeString($S, $S)", mmkv, key, defaultString)
                .endControlFlow()
                .addStatement("return $S", defaultString)
                .endControlFlow()
                .build();
    }

    /**
     * @param fieldName String
     * @param key       String
     * @return MethodSpec
     */
    public static MethodSpec createPutString(String fieldName, String key) {
        return MethodSpec.methodBuilder("put" + upperCase(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "value")
                .beginControlFlow("synchronized($N.class)", PrefClassName)
                .beginControlFlow("if($N != null && value != null)", mmkv)
                .addStatement("$N.encode($S, value)", mmkv, key)
                .endControlFlow()
                .endControlFlow()
                .build();
    }

    /**
     * @param fieldName  String
     * @param key        String
     * @param objectType String
     * @return MethodSpec
     */
    public static MethodSpec createGetObject(String fieldName, String key, String objectType) {
        int index = objectType.lastIndexOf(".");
        String packageName = objectType.substring(0, index);
        String className = objectType.substring(index + 1);
        ClassName obj = ClassName.get(packageName, className);
        return MethodSpec.methodBuilder("get" + upperCase(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .returns(obj)
                .beginControlFlow("synchronized($N.class)", PrefClassName)
                .beginControlFlow("if($N != null)", mmkv)
                .addStatement("String json = $N.decodeString($S, $S)", mmkv, key, "")
                .beginControlFlow("if(json.isEmpty())")
                .addStatement("return null")
                .endControlFlow()
                .addStatement("return new $T().fromJson(json, $N.class)", Gson.class, className)
                .endControlFlow()
                .addStatement("return null")
                .endControlFlow()
                .build();
    }

    /**
     * @param fieldName  String
     * @param key        String
     * @param objectType String
     * @return MethodSpec
     */
    public static MethodSpec createPutObject(String fieldName, String key, String objectType) {
        int index = objectType.lastIndexOf(".");
        String packageName = objectType.substring(0, index);
        String className = objectType.substring(index + 1);
        ClassName obj = ClassName.get(packageName, className);
        return MethodSpec.methodBuilder("put" + upperCase(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(obj, fieldName)
                .beginControlFlow("synchronized($N.class)", PrefClassName)
                .beginControlFlow("if($N != null)", mmkv)
                .addStatement("String json = \"\"")
                .beginControlFlow("if($N != null)", fieldName)
                .addStatement("json = new $T().toJson($N)", Gson.class, fieldName)
                .endControlFlow()
                .addStatement("$N.encode($S, json)", mmkv, key)
                .endControlFlow()
                .endControlFlow()
                .build();
    }

    /**
     * @param fieldName  String
     * @param key        String
     * @param parcelType String
     * @return MethodSpec
     */
    public static MethodSpec createGetParcel(String fieldName, String key, String parcelType) {
        int index = parcelType.lastIndexOf(".");
        String packageName = parcelType.substring(0, index);
        String className = parcelType.substring(index + 1);
        ClassName obj = ClassName.get(packageName, className);
        return MethodSpec.methodBuilder("get" + upperCase(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .returns(obj)
                .beginControlFlow("synchronized($N.class)", PrefClassName)
                .beginControlFlow("if($N != null)", mmkv)
                .addStatement("return $N.decodeParcelable($S, $N.class)", mmkv, key, className)
                .endControlFlow()
                .addStatement("return null")
                .endControlFlow()
                .build();
    }

    /**
     * @param fieldName  String
     * @param key        String
     * @param parcelType String
     * @return MethodSpec
     */
    public static MethodSpec createPutParcel(String fieldName, String key, String parcelType) {
        int index = parcelType.lastIndexOf(".");
        String packageName = parcelType.substring(0, index);
        String className = parcelType.substring(index + 1);
        ClassName obj = ClassName.get(packageName, className);
        return MethodSpec.methodBuilder("put" + upperCase(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(obj, fieldName)
                .beginControlFlow("synchronized($N.class)", PrefClassName)
                .beginControlFlow("if($N != null)", mmkv)
                .addStatement("$N.encode($S, $N)", mmkv, key, fieldName)
                .endControlFlow()
                .endControlFlow()
                .build();
    }

    /**
     * @param fieldName         String
     * @param key               String
     * @param genericObjectType String
     * @return MethodSpec
     */
    public static MethodSpec createGetGenericObject(String fieldName, String key, String genericObjectType) {
        int start = genericObjectType.lastIndexOf("<");
        int end = genericObjectType.lastIndexOf(">");

        String mainPackagePath = genericObjectType.substring(0, start);
        int mainIndex = mainPackagePath.lastIndexOf(".");
        String mainPackageName = mainPackagePath.substring(0, mainIndex);
        String mainClassName = mainPackagePath.substring(mainIndex + 1);
        ClassName mainClass = ClassName.get(mainPackageName, mainClassName);
        //构建泛型类型
        String innerPackagePath = genericObjectType.substring(start + 1, end);
        int innerIndex = innerPackagePath.lastIndexOf(".");
        String innerPackageName = innerPackagePath.substring(0, innerIndex);
        String innerClassName = innerPackagePath.substring(innerIndex + 1);
        ClassName innerClass = ClassName.get(innerPackageName, innerClassName);
        TypeName obj = ParameterizedTypeName.get(mainClass, innerClass);
        return MethodSpec.methodBuilder("get" + upperCase(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .returns(obj)
                .beginControlFlow("synchronized($N.class)", PrefClassName)
                .beginControlFlow("if($N != null)", mmkv)
                .addStatement("String json = $N.decodeString($S, $S)", mmkv, key, "")
                .beginControlFlow("if(json.isEmpty())")
                .addStatement("return null")
                .endControlFlow()
                .addStatement("return new $T().fromJson(json, new $T<$T>() {}.getType())", Gson.class, TypeToken.class, obj)
                .endControlFlow()
                .addStatement("return null")
                .endControlFlow()
                .build();
    }

    /**
     * @param fieldName         String
     * @param key               String
     * @param genericObjectType String
     * @return MethodSpec
     */
    public static MethodSpec createPutGenericObject(String fieldName, String key, String genericObjectType) {
        int start = genericObjectType.lastIndexOf("<");
        int end = genericObjectType.lastIndexOf(">");

        String mainPackagePath = genericObjectType.substring(0, start);
        int mainIndex = mainPackagePath.lastIndexOf(".");
        String mainPackageName = mainPackagePath.substring(0, mainIndex);
        String mainClassName = mainPackagePath.substring(mainIndex + 1);
        ClassName mainClass = ClassName.get(mainPackageName, mainClassName);
        //构建泛型类型
        String innerPackagePath = genericObjectType.substring(start + 1, end);
        int innerIndex = innerPackagePath.lastIndexOf(".");
        String innerPackageName = innerPackagePath.substring(0, innerIndex);
        String innerClassName = innerPackagePath.substring(innerIndex + 1);
        ClassName innerClass = ClassName.get(innerPackageName, innerClassName);
        ParameterizedTypeName obj = ParameterizedTypeName.get(mainClass, innerClass);
        return MethodSpec.methodBuilder("put" + upperCase(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(obj, fieldName)
                .beginControlFlow("synchronized($N.class)", PrefClassName)
                .beginControlFlow("if($N != null)", mmkv)
                .addStatement("String json = \"\"")
                .beginControlFlow("if($N != null)", fieldName)
                .addStatement("json = new $T().toJson($N)", Gson.class, fieldName)
                .endControlFlow()
                .addStatement("$N.encode($S, json)", mmkv, key)
                .endControlFlow()
                .endControlFlow()
                .build();
    }

}
