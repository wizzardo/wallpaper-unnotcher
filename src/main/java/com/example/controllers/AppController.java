package com.example.controllers;

import com.example.services.UnnotcherService;
import com.wizzardo.http.framework.Controller;
import com.wizzardo.http.framework.template.Renderer;
import com.wizzardo.http.request.Header;
import com.wizzardo.http.response.Status;
import com.wizzardo.tools.io.FileTools;
import com.wizzardo.tools.misc.Stopwatch;
import com.wizzardo.tools.misc.Unchecked;

import java.io.File;

public class AppController extends Controller {
    UnnotcherService unnotcherService;

    public Renderer upload(File file, String name, int width, int height) {
        System.out.println("file uploaded: " + file.length() + " " + name);

        Stopwatch stopwatch = new Stopwatch("unnotching");
        byte[] bytes;
        try {
            bytes = unnotcherService.process(file, name, width, height);
        } catch (Exception e) {
            e.printStackTrace();
            response.status(Status._500)
                    .appendHeader(Header.KEY_CONTENT_LENGTH, String.valueOf(e.getMessage().length()));
            return renderString(e.getMessage());
        }
        System.out.println(stopwatch);

        response.appendHeader(Header.KEY_CONTENT_TYPE, "image/jpeg")
                .appendHeader(Header.KEY_CONTENT_LENGTH, String.valueOf(bytes.length));
        return renderData(bytes);
    }
}
