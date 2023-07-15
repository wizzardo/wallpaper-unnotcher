package com.example;

import com.wizzardo.http.framework.Environment;
import com.wizzardo.http.framework.WebApplication;
import com.wizzardo.http.framework.template.Tag;

import java.util.Collections;
import java.util.List;

public class App extends WebApplication {

    public App(String[] args) {
        super(args);
    }


    @Override
    protected List<Class<? extends Tag>> getBasicTags() {
        return Collections.emptyList();
    }

    public static void main(String[] args) {
        App app = new App(args);
        app.environment = Environment.PRODUCTION;

        app.start();
    }
}
