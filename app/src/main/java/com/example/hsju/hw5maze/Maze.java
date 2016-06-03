package com.example.hsju.hw5maze;

/**
 * Created by HS.Ju on 2016-06-03.
 * this class is maze class
 * 만들어진 mazeView에 maze 각각의 요소들의 좌표값에 맞추어 넣어준다.
 * author : hwanseok, Ju
 * e-mail : rokmctkd6@gmail.com
 * last_update : 2016-06-03.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

public class Maze {

    boolean flag = true;

    int startPoint = 0;
    int endPoint = 0;

    Bitmap wall;
    Bitmap path;
    Bitmap start;
    Bitmap end;
    RectF rectF;
    int mazeX, mazeY;

    public Maze(Context c, int x, int y, int wid, int hei) {
        mazeX = x;
        mazeY = y;
        wall = BitmapFactory.decodeResource(c.getResources(), R.drawable.wall);
        path = BitmapFactory.decodeResource(c.getResources(), R.drawable.path);
        start = BitmapFactory.decodeResource(c.getResources(), R.drawable.start);
        end = BitmapFactory.decodeResource(c.getResources(), R.drawable.finish);

        wall = Bitmap.createScaledBitmap(wall, wid / 8, wid / 8, true);
        path = Bitmap.createScaledBitmap(path, wid / 8, wid / 8, true);
        start = Bitmap.createScaledBitmap(start, wid / 8, wid / 8, true);
        end = Bitmap.createScaledBitmap(end, wid / 8, wid / 8, true);
        rectF = new RectF(mazeX, mazeY, mazeX + wall.getWidth(), mazeY + wall.getHeight());

    }
}

