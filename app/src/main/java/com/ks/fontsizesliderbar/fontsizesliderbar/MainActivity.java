
package com.ks.fontsizesliderbar.fontsizesliderbar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FontSliderBar sliderBar = (FontSliderBar) findViewById(R.id.sliderbar);
        sliderBar.setTickCount(4,new String[]{"小","中","大","特大"}).setBarLineWide(3).setTickHeight(20).setBarLineColor(
                Color.GRAY)
                .setTextColor(Color.BLACK).setTextPadding(40).setTextSize(40)
                .setThumbRadius(40).setThumbColorNormal(Color.WHITE).setThumbColorPressed(Color.BLACK)
                .setThumbColorCircle(Color.LTGRAY).setThumbIndex(1).showShadow(true)
                .withAnimation(true).apply();
    }
}
