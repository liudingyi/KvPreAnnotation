
## 快捷的使用SharePreferences

[![](https://jitpack.io/v/liudingyi/PrefAnnotation.svg)](https://jitpack.io/#liudingyi/PrefAnnotation)

### 1.在project中的build.gradle

    allprojects {
        repositories {
            ...
            //添加仓库路径
            maven { url "https://jitpack.io" }
        }
    }

### 2.在module中的build.gradle

    dependencies {
        ...
        implementation 'com.tencent:mmkv-static:1.2.7'
        implementation 'com.github.liudingyi.KvPreAnnotation:annotations:@version'
        annotationProcessor 'com.github.liudingyi.KvPreAnnotation:processors:@version'
    }


### 注解说明


注解名| 注解作用 | 注解属性
---|---|---
@SharePref | 标记对应的preference数据类
@PrefKey | 标记属性的key | key：属性的key
@ObjectType | 标记属性对象类型（支持泛型对象）
@ParcelType | 标记属性Parcelable对象类型（支持泛型对象）
@DefaultInt | 标记默认的Int值 | value：默认值
@DefaultFloat | 标记默认的Float值 | value：默认值
@DefaultLong | 标记默认的Long值 | value：默认值
@DefaultBoolean | 标记默认的Boolean值 | value：默认值
@DefaultString | 标记默认的String值 | value：默认值

## 简单例子

```

@SharePref
public class Pref {

    @PrefKey(key = "user_name")
    public String username;

    @PrefKey(key = "user")
    @ObjectType
    public User user;

    @PrefKey(key = "user_list")
    @ObjectType
    public List<User> userList;
}

```

## 编译生成的文件

```

public final class Pref_ extends Pref {
  private static Pref_ instance;

  private static SharedPreferences preference;

  private static Editor editor;

  private Pref_(Context context) {
    preference = context.getSharedPreferences("sample_pref",0);
    editor = preference.edit();
  }

  public static Pref_ getInstance(Context context) {
    if(instance == null) {
      instance = new Pref_(context);
    }
    return instance;
  }

  public void removeUsername() {
    synchronized(Pref_.class) {
      if(editor != null) {
        editor.remove("user_name").apply();
      }
    }
  }

  public String getUsername() {
    synchronized(Pref_.class) {
      if(preference != null) {
        return preference.getString("user_name", "");
      }
      return "";
    }
  }

  public void putUsername(String value) {
    synchronized(Pref_.class) {
      if(editor != null && value != null) {
        editor.putString("user_name", value).apply();
      }
    }
  }

  public void removeUser() {
    synchronized(Pref_.class) {
      if(editor != null) {
        editor.remove("user").apply();
      }
    }
  }

  public User getUser() {
    synchronized(Pref_.class) {
      if(preference != null) {
        String json = preference.getString("user", "");
        if(json.isEmpty()) {
          return null;
        }
        return new Gson().fromJson(json, User.class);
      }
      return null;
    }
  }

  public void putUser(User user) {
    synchronized(Pref_.class) {
      if(editor != null) {
        String json = "";
        if(user != null) {
          json = new Gson().toJson(user);
        }
        editor.putString("user", json).apply();
      }
    }
  }

  public void removeUserList() {
    synchronized(Pref_.class) {
      if(editor != null) {
        editor.remove("user_list").apply();
      }
    }
  }

  public List<User> getUserList() {
    synchronized(Pref_.class) {
      if(preference != null) {
        String json = preference.getString("user_list", "");
        if(json.isEmpty()) {
          return null;
        }
        return new Gson().fromJson(json, new TypeToken<List<User>>() {}.getType());
      }
      return null;
    }
  }

  public void putUserList(List<User> userlist) {
    synchronized(Pref_.class) {
      if(editor != null) {
        String json = "";
        if(userlist != null) {
          json = new Gson().toJson(userlist);
        }
        editor.putString("user_list", json).apply();
      }
    }
  }

```


