package com.example.seydazimovnurbol.pencilofpaint;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.util.ArrayList;

import petrov.kristiyan.colorpicker.ColorPicker;

public class MainActivity extends AppCompatActivity {
    private CanvasView canvasView;
    ArrayList <String> colors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        canvasView = (CanvasView) findViewById(R.id.canvas);

        init();


        // Color Picker
        Button fab = (Button) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ColorPicker colorPicker = new ColorPicker(MainActivity.this);
                colorPicker.setColors(colors)
                        .setDefaultColorButton(Color.BLACK)
                        .setColumns(5)
                        .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                            @Override
                            public void onChooseColor(int position, int color) {
                                Log.d("position",""+position);// will be fired only when OK button was tapped
                                canvasView.setCurrentColor(color);
                            }

                            @Override
                            public void onCancel() {}
                        })
                        .addListenerButton("", new ColorPicker.OnButtonListener() {
                            @Override
                            public void onClick(View v, int position, int color) {
                                Log.d("position",""+position);
                            }
                        })
                        .setRoundColorButton(true).show();
            }
        });

        	    SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        	    seekBar.setProgress(5);
        	    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                	    @Override
            		    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                				canvasView.setWidth(i);
                		    }

                        @Override
            		    public void onStartTrackingTouch(SeekBar seekBar) {

                        		    }

                    		    @Override
            		    public void onStopTrackingTouch(SeekBar seekBar) {

                        		    }
               });
    }


    private void init() {
        colors.add("#82B926");
        colors.add("#a276eb");
        colors.add("#6a3ab2");
        colors.add("#666666");
        colors.add("#FFFF00");
        colors.add("#3C8D2F");
        colors.add("#FA9F00");
        colors.add("#FF0000");
    }

    public void clearCanvas(View v) {
        canvasView.clearCanvas();
    }

    public void fillShape(View v) {
        canvasView.fillShape();
    }
    public void pickEraser(View v) { canvasView.setCurrentColor(-1); }


}
