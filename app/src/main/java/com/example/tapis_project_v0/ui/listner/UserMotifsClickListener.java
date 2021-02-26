package com.example.tapis_project_v0.ui.listner;

import android.widget.ImageView;

import com.example.tapis_project_v0.model.UserMotif;

public interface UserMotifsClickListener {

    void onDashboardCourseClick(UserMotif userMotif, ImageView imageView); // Shoud use imageview to make the shared animation between the two activity

}