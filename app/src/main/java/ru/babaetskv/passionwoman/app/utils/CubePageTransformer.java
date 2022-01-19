package ru.babaetskv.passionwoman.app.utils;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class CubePageTransformer implements ViewPager2.PageTransformer {
    private float rotationDegrees = 45f;

    public CubePageTransformer() {
    }

    public CubePageTransformer(float rotationDegrees) {
        this.rotationDegrees = rotationDegrees;
    }

    @Override
    public void transformPage(@NonNull View page, float position) {
        float deltaY = .5f;
        float pivotX = position < 0f ? page.getWidth() : 0f;
        float pivotY = page.getHeight() * deltaY;
        float rotationY = rotationDegrees * position;
        page.setPivotX(pivotX);
        page.setPivotY(pivotY);
        page.setRotationY(rotationY);
    }
}
