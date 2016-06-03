package com.example.hsju.hw5maze;


/**
 * this class is mazeView class.
 * 여기에선 2차원어레이를 사용하여 화면에 맞게 mae들을 만들어주고
 * 처음에는 flag값을 모두 false로 만들어서 길이 없는 maze를 만든다.
 * 또한 시작점을 랜덤으로 선택하여 그위치부터 주어진 계산식대로 maze를 그린다.
 * maze가 그려지는 도중 좌표값에 벗어나는 maze요소는  조건문으로 처리한다.
 * 시작점과 끝점으로 이루어진 maze를 터치이벤트로 길을 그리고
 * 시작점부터 마지막점까지 도착했을경우엔 다이얼로그창으로 선택할수 있게 한다.
 *  author : hwanseok, Ju
 *  e-mail : rokmctkd6@gmail.com
 *  last_update : 2016. 06.03
 */


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MazeView extends View {
    boolean boxFlag[][] =
            {{false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false}};
    Maze mazeBox[][] = new Maze[8][8];
    int wid, hei;
    int spx, spy, epx, epy; // for starting point & ending point x, y
    boolean pathCheck, finshCheck;
    Context c;
    Path mPath;
    Point mPoint;
    Paint mPaint;
    String msg;
    AlertDialog dialBox = createDialogBox();

    public MazeView(Context c) {
        super(c);
    }

    public MazeView(Context c, AttributeSet a) {
        super(c);
        this.c = c;
        init();
    }

    public void init() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boxFlag[i][j] = false;
            }
        }


        int mWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        int mHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        wid = mWidth;
        hei = mHeight - 300;
        int row = 0; // for select the random path.
        int cal; // for select the random path.

        finshCheck = true;
        mPath = new Path();
        mPaint = new Paint();

        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(5);
        mPoint = new Point();

        while (true) {

            cal = (int) (Math.random() * 7); // ramdomly choose the starting point.
            spx = cal; // for starting point.
            spy = row; // for starting point.
            boxFlag[cal][row++] = true; //0,4  2 1 0
            boxFlag[cal++][row] = true; //1,4  2 1 0
            if (cal < 8)
                boxFlag[cal][row++] = true; //1,5  3 2 1
            else
                cal--;

            boxFlag[cal][row++] = true; //2,5  3 2 1
            boxFlag[cal--][row] = true; //3,5  3 2 1
            boxFlag[cal--][row] = true; //3,4  2 1 0
            if (cal >= 0)
                boxFlag[cal--][row] = true; //3,3  1 0
            else
                cal++;
            if (cal < 0)
                cal++;
            boxFlag[cal][row++] = true; //3,2  0 0
            boxFlag[cal][row++] = true; //4,2  0 0
            boxFlag[cal][row++] = true; //5,2  0 0
            boxFlag[cal++][row] = true; //6,2  0 0
            boxFlag[cal][row++] = true; //6,3  1 1
            boxFlag[cal][row] = true; //7,3    1 1
            epx = cal;
            epy = row;
            break;
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (boxFlag[i][j] == true) {
                    mazeBox[i][j] = new Maze(c, wid * j / 8, hei * i / 8, wid, hei);
                    mazeBox[i][j].flag = true;

                } else {
                    mazeBox[i][j] = new Maze(c, wid * j / 8, hei * i / 8, wid, hei);
                    mazeBox[i][j].flag = false;
                }
            }
        }
        mazeBox[spx][spy].startPoint = 1;
        mazeBox[epx][epy].endPoint = 1;


    }

    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (mazeBox[i][j].flag == true) {
                    canvas.drawBitmap(mazeBox[i][j].path, mazeBox[i][j].mazeX, mazeBox[i][j].mazeY, null);
                } else {
                    canvas.drawBitmap(mazeBox[i][j].wall, mazeBox[i][j].mazeX, mazeBox[i][j].mazeY, null);
                }

            }
        }
        canvas.drawBitmap(mazeBox[spx][spy].start, mazeBox[spx][spy].mazeX, mazeBox[spx][spy].mazeY, null);
        canvas.drawBitmap(mazeBox[epx][epy].end, mazeBox[epx][epy].mazeX, mazeBox[epx][epy].mazeY, null);
        canvas.drawPath(mPath, mPaint);
    }

    // if you don't select the starting point, toast the message
    // and if you draw the incorrect or correct line, toast the message
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // mPath.reset();
                //   mPath.moveTo(eventX, eventY);
                if (mazeBox[spx][spy].rectF.contains((int) eventX, (int) eventY)) {

                    pathCheck = true;
                    mPath.moveTo(eventX, eventY);
                } else {
                    pathCheck = false;
                    Toast.makeText(c, "This is not start point", Toast.LENGTH_SHORT).show();
                }

            case MotionEvent.ACTION_MOVE:
                if (pathCheck) {
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            if (mazeBox[i][j].rectF.contains(eventX, eventY)) {
                                if (mazeBox[i][j].flag == false) {
                                    Toast.makeText(c, "You Lose", Toast.LENGTH_SHORT).show();
                                    dialBox.show();
                                    finshCheck = false;
                                    break;
                                } else
                                    mPath.lineTo(eventX, eventY);
                            }
                        }
                    }
                } else {
                    mPoint.x = spx;
                    mPoint.y = spy;
                    mPath.reset();
                }

                break;

            case MotionEvent.ACTION_UP:
                if (mazeBox[epx][epy].rectF.contains((int) eventX, (int) eventY)) {
                    Toast.makeText(c, "You Win", Toast.LENGTH_SHORT).show();
                    finshCheck = false;
                    dialBox.show();
                } else {
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            if (mazeBox[i][j].rectF.contains(eventX, eventY)) {
                                // Toast.makeText(c, "  " + eventX + " point " + eventY + " " + i + "f " + j, Toast.LENGTH_SHORT).show();
                                spx = i;
                                spy = j;
                            }
                        }
                    }
                    finshCheck = true;
                }
                break;
        }
        mPoint.x = (int) eventX;
        mPoint.y = (int) eventY;
        invalidate();
        return true;
    }

    private AlertDialog createDialogBox() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(getContext())
                .setTitle("Finsh")
                .setMessage("Are you sure that you want to quit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        System.exit(0);
                    }
                })
                .setNegativeButton("Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        init();
                        invalidate();
                    }
                }).create();
        return myQuittingDialogBox;
    }

}

