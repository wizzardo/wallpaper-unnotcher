package com.example;

import com.example.controllers.AppController;
import com.wizzardo.http.FileTreeHandler;
import com.wizzardo.http.Handler;
import com.wizzardo.http.RestHandler;
import com.wizzardo.http.framework.ControllerHandler;
import com.wizzardo.http.framework.Environment;
import com.wizzardo.http.framework.WebApplication;
import com.wizzardo.http.framework.di.DependencyFactory;
import com.wizzardo.http.framework.template.ResourceTools;
import com.wizzardo.http.framework.template.Tag;
import com.wizzardo.http.request.Header;
import com.wizzardo.http.response.Status;
import com.wizzardo.tools.io.IOTools;
import com.wizzardo.tools.misc.Unchecked;

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

        app.onSetup(it -> {
            ResourceTools resourceTools = DependencyFactory.get(ResourceTools.class);
            byte[] indexHtml = Unchecked.call(() -> IOTools.bytes(resourceTools.getResource("/public/index.html")));

            FileTreeHandler<?> fileTreeHandler = DependencyFactory.get(FileTreeHandler.class);
            Handler defaultForbiddenHandler = fileTreeHandler.forbiddenHandler();
            fileTreeHandler.forbiddenHandler((request, response) -> {
                if (request.path().length() == 0) {
                    return response.body(indexHtml)
                            .appendHeader(Header.KEY_CONTENT_TYPE, Header.VALUE_HTML_UTF8)
                            .status(Status._200);
                }
                return defaultForbiddenHandler.handle(request, response);
            });
            fileTreeHandler.notFoundHandler((request, response) -> response
                    .body(indexHtml)
                    .appendHeader(Header.KEY_CONTENT_TYPE, Header.VALUE_HTML_UTF8)
                    .status(Status._200)
            );

            it.getUrlMapping().append("/upload", new RestHandler().post(
                    new ControllerHandler<>(AppController.class, "upload")
            ));
        });

        app.start();
    }
}
