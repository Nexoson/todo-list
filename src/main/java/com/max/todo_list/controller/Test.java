package com.max.todo_list.controller;

import org.bytedeco.javacpp.Loader;

public class Test {
    public static void main(String[] args) {
        System.out.println("Loading OpenCV...");
        Loader.load(org.bytedeco.opencv.opencv_java.class);
        System.out.println("OpenCV loaded successfully!");
    }
}
