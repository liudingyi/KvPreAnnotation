package com.pref.sample;

import android.app.Activity;
import android.os.Bundle;

import com.kvpref.sample.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Pref_.initialize(getApplicationContext());
//        Pref_ pref = Pref_.getInstance();
//        pref.putAge(1);
//        pref.putIsMan(true);
//        pref.putHeight(10.1f);
//        pref.putMoney(50000000000000001L);
//        pref.putUsername("Jack");
//        pref.putUser(new User("Jack"));
//
//        List<User> list = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            list.add(new User("Tome" + i));
//        }
//        pref.putUserList(list);
//
//
//        Log.i("liu", pref.getAge() + "");
//        Log.i("liu", pref.getIsMan() + "");
//        Log.i("liu", pref.getHeight() + "");
//        Log.i("liu", pref.getMoney() + "");
//        Log.i("liu", pref.getUsername() + "");
//        Log.i("liu", pref.getUser().name + "");
//        for (User user : pref.getUserList()) {
//            Log.i("liu", user.name + "");
//        }
    }
}
