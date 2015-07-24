package com.example.bharath.ultimatetic_tac_toe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity{
    DrawTable table;
    static int[][] field = new int[9][9];
    static int[][] overall = new int[3][3];
    float touch_x,touch_y;

    int flag = 1;
    float density;
    int text_flag=1;
    int check_flag =0;
    int previous_box_X;
    int previous_box_Y;
    int first_flag=0;
    Paint TextPaint;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //hide title bar

        getWindow().setFlags(0xFFFFFFFF, WindowManager.LayoutParams.FLAG_FULLSCREEN| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        table = new DrawTable(this);
        setContentView(table);
        density = getResources().getDisplayMetrics().density;
        TextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        TextPaint.setColor(Color.BLUE);
        TextPaint.setTextSize(40);



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void callOverall(){
        for(int[] A:overall){
            for(int c : A){
                System.out.print(" " + c + " ");
            }
            System.out.println("\n");
        }
    }
    public boolean check_box(int x,int y,int[][] field) {

        x = x-(x%3);
        y=  y-(y%3);
        return (check_Rows(x, y, field) || check_Columns(x, y, field) || check_Diagonal(x, y, field));

    }
    private boolean check_Rows(int x,int y,int[][] field) {

        int c = x+3;

        for (int i = x; i < c; i++) {

            if (check_Each(field[i][y], field[i][y + 1], field[i][y + 2]) == true) {

                return true;

            }

        }

        return false;

    }
    private boolean check_Columns(int x,int y,int[][] field) {

        int c = y+3;
        for (int i = y; i < c; i++) {

            if (check_Each(field[x][i], field[x + 1][i], field[x + 2][i]) == true) {

                return true;

            }

        }

        return false;

    }
    private boolean check_Diagonal(int x,int y,int[][] field) {

        return ((check_Each(field[x][y], field[x + 1][y + 1], field[x + 2][y + 2]) == true) || (check_Each(field[x][y + 2], field[x + 1][y + 1], field[x + 2][y]) == true));

    }
    private boolean check_Each(int c1, int c2, int c3) {

        return ((c1 != 0) && (c1 == c2) && (c2 == c3));

    }









    public void checkTouch(){

        System.out.println("\n**** ***** Check touch called  ****\n");
        if(((touch_x)>=60)&&(touch_x<=600)&&(touch_y>=360)&&(touch_y<=900)) {

            //    System.out.println("\n**** x = " + touch_x + " y = " + touch_y + " ****\n");
            float x = touch_x - (touch_x % 60);
            float y = touch_y - (touch_y % 60);
            //    System.out.println("\n**** x = " + x + " y = " + y + " ****\n");
            int temp_x = ((int) y - 360) / 60;
            int temp_y = ((int) x - 60) / 60;
            if (first_flag == 0) {



                if ((overall[temp_x / 3][temp_y / 3] == 0)) {
                    if (field[temp_x][temp_y] == 0) {
                        previous_box_X = temp_x % 3;
                        previous_box_Y = temp_y % 3;
                        first_flag = 1;
                        if (flag % 2 == 1) field[temp_x][temp_y] = 1;
                        else field[temp_x][temp_y] = 2;

                        flag = flag + 1;
                        text_flag = flag % 2;
                        table.invalidate();
                      
                    }

                }

            } else {
                if ((previous_box_X == temp_x / 3) && (previous_box_Y == temp_y / 3)) {
                    previous_box_X = temp_x % 3;
                    previous_box_Y = temp_y % 3;
                    if (overall[temp_x / 3][temp_y / 3] == 0) {
                        if (field[temp_x][temp_y] == 0) {
                            System.out.println("\n**** x = " + temp_x + " y = " + temp_y + " ****\n");
                            if (flag % 2 == 1) field[temp_x][temp_y] = 1;
                            else field[temp_x][temp_y] = 2;
                            flag = flag + 1;
                            text_flag = flag % 2;

                            System.out.println("\n**** ***** calling check_box  ****\n");
                            if (check_box(temp_x, temp_y, field)) {
                                int c = 0;
                                if ((flag - 1) % 2 == 1) c = 1;
                                else c = 2;
                                overall[temp_x / 3][temp_y / 3] = c;
                                first_flag = 0;
                                table.invalidate();

                                if (check_box(0, 0, overall)) {

                                    new AlertDialog.Builder(MainActivity.this)
                                            .setTitle(" Player " + c + " has won !!!")
                                            .setMessage("Do you want to play again ? ")
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // continue with delete
                                                    for (int i = 0; i < 9; i++) {
                                                        for (int j = 0; j < 9; j++)
                                                            field[i][j] = 0;
                                                    }
                                                    for (int i = 0; i < 3; i++)
                                                        for (int j = 0; j < 3; j++)
                                                            overall[i][j] = 0;
                                                    first_flag = 0;
                                                    text_flag = 1;
                                                    table.invalidate();

                                                }
                                            })
                                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // do nothing
                                                    finish();
                                                    System.exit(0);
                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();

                                }


                            }

                        }
                    } else {
                        first_flag = 0;
                        text_flag = 2;
                        table.invalidate();
                    }
                } else {
                    text_flag = 3;
                    table.invalidate();
                }
            }
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            touch_x = event.getX();
            touch_y =  event.getY()-140;

        }
        //  System.out.println("\n**** x = " + touch_x + " y = " + touch_y + " ****\n");
        checkTouch();
        return super.onTouchEvent(event);

    }

    public class DrawTable extends View {
        private Paint myPaint;
        private Paint myPaint2;
        private Paint linePaint;
        private Paint ballPaint;
        private Paint rectPaint;
        int x = 60;
        int y = 360;
        public DrawTable(Context context) {
            super(context);
            myPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            myPaint.setColor(Color.BLACK);
            myPaint.setStrokeWidth(5);
            myPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
            myPaint2.setColor(Color.GRAY);
            myPaint2.setStrokeWidth(2);
            linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            linePaint.setColor(Color.RED);
            linePaint.setStrokeWidth(2);
            ballPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            ballPaint.setColor(Color.GREEN);
            ballPaint.setStrokeWidth(2);
            rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            rectPaint.setColor(Color.YELLOW);
            rectPaint.setStrokeWidth(2);

        }
        public int giveX(){
            x+=60;
            if(x>=600){
                x=120;
            }
            return x;
        }
        public int giveY(){
            y+=60;
            if(y>=900){
                y = 420;
            }
            return y;
        }


        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            switch (text_flag){
                case 1: canvas.drawText("Player 1's Turn ",20,30,TextPaint);
                    break;
                case 0: canvas.drawText("Player 2's Turn ",20,30,TextPaint);
                    break;
                case 2: canvas.drawText(" Box filled choose another box ",20,30,TextPaint);
                    break;
                case 3: canvas.drawText("You can't play here ",20,30,TextPaint);
                    break;
            }
            if(first_flag!=0){
                canvas.drawRect(previous_box_Y*180+60,previous_box_X*180+360,previous_box_Y*180+240,previous_box_X*180+540,rectPaint);


            }
            for(int i =0; i<3;i++){
                for(int j=0;j<3;j++){
                    if(overall[i][j] == 1){

                        int x1 = (j*180)+60;
                        int y1 = (i*180) + 360;
                        //          System.out.println("\n**** overall of (  "+ i+" , "+ j+ " = " + overall[i][j] +" ** ****\n");
                        canvas.drawLine(x1,y1,x1+180,y1+180,linePaint);
                        canvas.drawLine(x1+180,y1,x1,y1+180,linePaint);
                    }
                    else if(overall[i][j]==2){
                        int x1 = (j*180)+60+90;
                        int y1 = (i*180) + 360+90;
                        canvas.drawCircle(x1,y1,90,ballPaint);
                    }
                }
            }
            //  System.out.println("\n**** overall of (  "+ 0+" , "+ 0+ " = " + overall[0][0] +" ** ****\n");
            for(int i =0; i<9;i++){
                for(int j=0;j<9;j++){
                    if(field[i][j] == 1){

                        int x1 = (j*60)+60;
                        int y1 = (i*60) + 360;
                        //   System.out.println("\n**** x = " + x1 + " y = " + y1 + " ****\n");

                        canvas.drawLine(x1,y1,x1+60,y1+60,linePaint);
                        canvas.drawLine(x1+60,y1,x1,y1+60,linePaint);
                    }
                    else if(field[i][j]==2){
                        int x1 = (j*60)+60+30;
                        int y1 = (i*60) + 360+30;
                        canvas.drawCircle(x1,y1,30,ballPaint);
                    }
                }
            }

            x = giveX();
            canvas.drawLine(60,360,60,900,myPaint);
            canvas.drawLine(600,360,600,900,myPaint);
            canvas.drawLine(240,360,240,900,myPaint);
            canvas.drawLine(420,360,420,900,myPaint);
            canvas.drawLine(60,540,600,540,myPaint);
            canvas.drawLine(60,720,600,720,myPaint);
            canvas.drawLine(x,360,x,900,myPaint2);
            x = giveX();
            canvas.drawLine(x,360,x,900,myPaint2);
            x = giveX();
            canvas.drawLine(x,360,x,900,myPaint2);
            x = giveX();
            canvas.drawLine(x,360,x,900,myPaint2);
            x = giveX();
            canvas.drawLine(x,360,x,900,myPaint2);
            x = giveX();
            canvas.drawLine(x,360,x,900,myPaint2);
            x = giveX();
            canvas.drawLine(x,360,x,900,myPaint2);
            x = giveX();
            canvas.drawLine(x,360,x,900,myPaint2);
            x = giveX();
            canvas.drawLine(x,360,x,900,myPaint2);
            y = giveY();
            canvas.drawLine(60,y,600,y,myPaint2);
            y = giveY();
            canvas.drawLine(60,y,600,y,myPaint2);
            y = giveY();
            canvas.drawLine(60,y,600,y,myPaint2);
            y = giveY();
            canvas.drawLine(60,y,600,y,myPaint2);
            y = giveY();
            canvas.drawLine(60,y,600,y,myPaint2);
            y = giveY();
            canvas.drawLine(60,y,600,y,myPaint2);
            y = giveY();
            canvas.drawLine(60,y,600,y,myPaint2);
            y = giveY();
            canvas.drawLine(60,y,600,y,myPaint2);





        }
    }

}
