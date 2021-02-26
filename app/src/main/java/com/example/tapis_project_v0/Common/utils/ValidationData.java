package com.example.tapis_project_v0.Common.utils;

import android.text.TextUtils;

public class ValidationData {


    public static boolean fieldValidation(String field) {
        return !TextUtils.isEmpty(field) && field.length() >= 6;
    }

}
