package com.example.mymaze;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static org.opencv.imgproc.Imgproc.cvtColor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.os.EnvironmentCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.icu.util.UniversalTimeScale;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button[][] buttons = new Button[10][6];
    private Button bt1;
    private Button bt2;
    private Button bt3;
    private Button bt4;

    private Button bt5;
    private Button bt6;
    private Button bt7;
    private ImageView iv1, iv2;
    private Mat mat1, mat2;
    private Bitmap bitmap;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PHOTO_PICKER_SINGLE_SELECT = 2;

    private EditText et1;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniLoadOpenCV();

        int flag[][] = new int[10][6];
        // bt[1][1] - bt[6][6]
        for (int i = 1; i <= 9; i++) {
            int jCount;
            if(i % 2 == 1) jCount = 4;
            else jCount = 5;
            for(int j = 1; j <= jCount; j++) {
                String buttonID = "button" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = ((Button) findViewById(resID));
                flag[i][j] = 0;
            }
        }

        // buttons[1][1] - button[9][5] click
        for (int i = 1; i <= 9; i++) {
            int jCount;
            if(i % 2 == 1) jCount = 4;
            else jCount = 5;
            for(int j = 1; j <= jCount; j++) {
                final int finalI = i;
                final int finalJ = j;
                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(flag[finalI][finalJ] == 0) {
                            buttons[finalI][finalJ].setBackgroundColor(Color.parseColor("#9900FF"));
                            flag[finalI][finalJ] = 1;
                        } else {
                            buttons[finalI][finalJ].setBackgroundColor(Color.parseColor("#D3D3D3"));
                            flag[finalI][finalJ] = 0;
                        }
                    }
                });
            }
        }

        bt1 = (Button) findViewById(R.id.button1);
        bt2 = (Button) findViewById(R.id.button2);
        bt3 = (Button) findViewById(R.id.button3);
        bt4 = (Button) findViewById(R.id.button4);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int conditionNow[][] = {
                        {0,0,0,0,0,0},
                        {0,1,1,1,1,0},
                        {0,1,0,0,0,1},
                        {0,0,0,0,0,0},
                        {0,1,0,0,0,1},
                        {0,0,0,0,0,0},
                        {0,1,0,0,0,1},
                        {0,0,0,0,0,0},
                        {0,1,0,0,0,1},
                        {0,1,1,1,1,0}};
                for(int i = 1; i <= 9; i++) {
                    int jCount;
                    if(i % 2 == 1) jCount = 4;
                    else jCount = 5;
                    for(int j = 1; j <= jCount; j++) {
                        if(conditionNow[i][j] == 1) {
                            buttons[i][j].setBackgroundColor(Color.parseColor("#9900FF"));
                            flag[i][j] = 1;
                        } else {
                            buttons[i][j].setBackgroundColor(Color.parseColor("#D3D3D3"));
                            flag[i][j] = 0;
                        }
                    }
                }
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 1; i <= 9; i++) {
                    int jCount;
                    if(i % 2 == 1) jCount = 4;
                    else jCount = 5;
                    for(int j = 1; j <= jCount; j++) {
                        buttons[i][j].setBackgroundColor(Color.parseColor("#9900FF"));
                        flag[i][j] = 1;
                    }
                }
            }
        });

        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 1; i <= 9; i++) {
                    int jCount;
                    if(i % 2 == 1) jCount = 4;
                    else jCount = 5;
                    for(int j = 1; j <= jCount; j++) {
                        buttons[i][j].setBackgroundColor(Color.parseColor("#D3D3D3"));
                        flag[i][j] = 0;
                    }
                }
            }
        });

        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // print as 1D array
                int nowIndex = 0;
                int[] ans = new int[40];
                for(int i = 1; i <= 9; i++) {
                    int jCount;
                    if(i % 2 == 1) jCount = 4;
                    else jCount = 5;
                    for(int j = 1; j <= jCount; j++) {
                        ans[nowIndex++] = flag[i][j];
                    }
                }
//                Log.i(Arrays.toString(ans), "gag");
                System.out.println(Arrays.toString(ans));
                System.out.println(ans.length);

                // ??????post??????
                String mazeData = "";
                // ?????????????????????????????????mazedata
                for (int an : ans) {
                    mazeData += an;
                }

                et1 = (EditText) findViewById(R.id.editText1);
                // ???editText???????????????
                String myUrl = et1.getText().toString();

                String finalMazeData = mazeData;
                new Thread(new Runnable() {
                    public void run() {
                        // ???????????????????????????
                        try {
                            // ?????? URL
                            URL url = new URL("http://" + myUrl + ":8443/api/maze_init?msg=" + finalMazeData);
//                            System.out.println(url);
                            // ????????????
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            // ????????????????????? POST
//                            connection.setRequestMethod("POST");
                            connection.setRequestMethod("GET");
                            // ??????????????????????????? application/json
                            connection.setRequestProperty("Content-Type", "application/json");
                            // ????????????????????????
                            connection.setDoOutput(true);
                            // ??????????????????
//                            String requestBody = "{\"mazeData\":\"" + finalMazeData + "\"}";
//                            System.out.println(requestBody);
                            OutputStream outputStream = connection.getOutputStream();
//                            outputStream.write(requestBody.getBytes());
                            outputStream.flush();
                            outputStream.close();
                            // ??????????????????
                            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            String line;
                            StringBuilder response = new StringBuilder();
                            while ((line = reader.readLine()) != null) {
                                response.append(line);
                            }
                            reader.close();
                            // ??????????????????
                            System.out.println(response.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        bt5 = (Button) findViewById(R.id.button5);
//        iv1 = (ImageView) findViewById(R.id.imageView1);
        iv2 = (ImageView) findViewById(R.id.imageView2);
        mat1 = new Mat();
        mat2 = new Mat();
        // ?????? mat2 ???CvType ??? scalar
//        try {
//            mat1 = Utils.loadResource(this, R.drawable.test5);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ??? R.id.imageView2 ?????? mat1
                bitmap = ((BitmapDrawable) iv2.getDrawable()).getBitmap();
                Utils.bitmapToMat(bitmap, mat1);
                // ???canny?????????????????????mat2
                Imgproc.Canny(mat1, mat2, 50, 150);
                // ????????????
                Imgproc.GaussianBlur(mat2, mat2, new Size(5, 5), 0);


                // ??? mat2 ??????CvType ??? scalar
                mat2.convertTo(mat2, CvType.CV_8UC1);
                mat2.setTo(new Scalar(255, 255, 255), mat2);


//                bitmap = Bitmap.createBitmap(mat2.cols(), mat2.rows(), Bitmap.Config.ARGB_8888);
//                Utils.matToBitmap(mat2, bitmap);
//                iv2.setImageBitmap(bitmap);


                // ?????????????????? mat3 ???????????????????????????mat2?????????
//                Mat mat3 = new Mat(mat2.size(), CvType.CV_8UC3, new Scalar(255, 255, 255));
//                List<MatOfPoint> contours = new ArrayList<>();
//                Mat hierarchy = new Mat();
//                Imgproc.findContours(mat2, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
//                Imgproc.drawContours(mat3, contours, -1, new Scalar(0, 0, 0), 10);
//                bitmap = Bitmap.createBitmap(mat3.cols(), mat3.rows(), Bitmap.Config.ARGB_8888);
//                Utils.matToBitmap(mat3, bitmap);
//                iv2.setImageBitmap(bitmap);


                // ?????? mat2 ????????????????????????
                List<MatOfPoint> contours = new ArrayList<>();
                Mat hierarchy = new Mat();
                Imgproc.findContours(mat2, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
                // ????????????????????????
                double maxArea = 0;
                int maxAreaIndex = 0;
                for(int i = 0; i < contours.size(); i++) {
                    double area = Imgproc.contourArea(contours.get(i));
                    if(area > maxArea) {
                        maxArea = area;
                        maxAreaIndex = i;
                    }
                }

                // ????????????mat3 ???????????????????????????????????????mat2?????????
//                Mat mat3 = new Mat(mat2.size(), CvType.CV_8UC3, new Scalar(255, 255, 255));
//                Imgproc.drawContours(mat3, contours, maxAreaIndex, new Scalar(0, 0, 0), 10);
//                bitmap = Bitmap.createBitmap(mat3.cols(), mat3.rows(), Bitmap.Config.ARGB_8888);
//                Utils.matToBitmap(mat3, bitmap);
//                iv2.setImageBitmap(bitmap);


//                // ????????? canny ?????????????????? mat2
//                Imgproc.Canny(mat1, mat2, 200.0, 300.0, 3, false);
//                // ?????????????????????
//                Imgproc.GaussianBlur(mat2, mat2, new Size(5, 5), 0);
                // ?????? mat2 ???????????????????????????????????????
                MatOfPoint2f approxCurve = new MatOfPoint2f();
                MatOfPoint2f contour2f = new MatOfPoint2f(contours.get(maxAreaIndex).toArray());
                double approxDistance = Imgproc.arcLength(contour2f, true) * 0.02;
                Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);
                MatOfPoint points = new MatOfPoint(approxCurve.toArray());

                // ???????????????????????? mat2 ??????????????????????????????????????????????????????
                MatOfPoint2f src = new MatOfPoint2f(points.toArray());
                MatOfPoint2f dst = new MatOfPoint2f(
                        new Point(0, 0),
                        new Point(0, 400),
                        new Point(400, 400),
                        new Point(400, 0)
                );
                Mat perspectiveTransform = Imgproc.getPerspectiveTransform(src, dst);
                Imgproc.warpPerspective(mat2, mat2, perspectiveTransform, new Size(400, 400));
                // ???mat2?????????bitmap?????????bitmap
                bitmap = Bitmap.createBitmap(mat2.cols(), mat2.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(mat2, bitmap);
                iv2.setImageBitmap(bitmap);

                // ????????? HoughLinesP ??????????????????
                Mat lines = new Mat();
                Imgproc.HoughLinesP(mat2, lines, 1, Math.PI / 180, 80, 40, 10);
                // ????????????????????????
                Log.d("HoughLinesP", "lines: " + lines.rows());
//                for(int i = 0; i < lines.rows(); i++) {
//                    double[] vec = lines.get(i, 0);
//                    double x1 = vec[0];
//                    double y1 = vec[1];
//                    double x2 = vec[2];
//                    double y2 = vec[3];
//                    double length = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
//                    Log.d("HoughLinesP", "line: " + i + " (" + x1 + ", " + y1 + ") (" + x2 + ", " + y2 + ") " + length);
//                }


                // ?????????????????????????????????????????????(0, 20)????????????????????? 0
                // ?????? (80, 120)?????????????????????100???(180, 220)?????????????????? 200???(280, 320)?????????????????? 300
                // (380, 400)?????????????????? 400
                for(int i = 0; i < lines.rows(); i++) {
                    double[] vec = lines.get(i, 0);
                    double x1 = vec[0];
                    double y1 = vec[1];
                    double x2 = vec[2];
                    double y2 = vec[3];
                    if(y1 >= 0 && y1 <= 20) {
                        y1 = 0;
                    } else if(y1 >= 80 && y1 <= 120) {
                        y1 = 99;
                    } else if(y1 >= 180 && y1 <= 220) {
                        y1 = 199;
                    } else if(y1 >= 280 && y1 <= 320) {
                        y1 = 299;
                    } else if(y1 >= 380 && y1 <= 400) {
                        y1 = 399;
                    }
                    if(y2 >= 0 && y2 <= 20) {
                        y2 = 0;
                    } else if(y2 >= 80 && y2 <= 120) {
                        y2 = 99;
                    } else if(y2 >= 180 && y2 <= 220) {
                        y2 = 199;
                    } else if(y2 >= 280 && y2 <= 320) {
                        y2 = 299;
                    } else if(y2 >= 380 && y2 <= 400) {
                        y2 = 399;
                    }
                    // x????????????
                    if(x1 >= 0 && x1 <= 20) {
                        x1 = 0;
                    } else if(x1 >= 80 && x1 <= 120) {
                        x1 = 99;
                    } else if(x1 >= 180 && x1 <= 220) {
                        x1 = 199;
                    } else if(x1 >= 280 && x1 <= 320) {
                        x1 = 299;
                    } else if(x1 >= 380 && x1 <= 400) {
                        x1 = 399;
                    }
                    if(x2 >= 0 && x2 <= 20) {
                        x2 = 0;
                    } else if(x2 >= 80 && x2 <= 120) {
                        x2 = 99;
                    } else if(x2 >= 180 && x2 <= 220) {
                        x2 = 199;
                    } else if(x2 >= 280 && x2 <= 320) {
                        x2 = 299;
                    } else if(x2 >= 380 && x2 <= 400) {
                        x2 = 399;
                    }
                    vec[0] = x1;
                    vec[1] = y1;
                    vec[2] = x2;
                    vec[3] = y2;
                    lines.put(i, 0, vec);
                }

                // ????????????????????????
//                Log.d("HoughLinesP", "lines: " + lines.rows());
//                for(int i = 0; i < lines.rows(); i++) {
//                    double[] vec = lines.get(i, 0);
//                    double x1 = vec[0];
//                    double y1 = vec[1];
//                    double x2 = vec[2];
//                    double y2 = vec[3];
//                    double length = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
//                    Log.d("HoughLinesP", "line: " + i + " (" + x1 + ", " + y1 + ") (" + x2 + ", " + y2 + ") " + length);
//                }

                // ???????????????????????????????????? mat3 ?????????????????????????????? mat2 ???????????????
                Mat mat3 = new Mat(mat2.rows(), mat2.cols(), CvType.CV_8UC3, new Scalar(255, 255, 255));
                for(int i = 0; i < lines.rows(); i++) {
                    double[] vec = lines.get(i, 0);
                    double x1 = vec[0];
                    double y1 = vec[1];
                    double x2 = vec[2];
                    double y2 = vec[3];
                    Point start = new Point(x1, y1);
                    Point end = new Point(x2, y2);
                    Imgproc.line(mat3, start, end, new Scalar(0, 0, 0), 3);
                }
                // ???mat3?????????bitmap?????????bitmap
                bitmap = Bitmap.createBitmap(mat3.cols(), mat3.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(mat3, bitmap);
                iv2.setImageBitmap(bitmap);

                // ????????????400*400?????????????????????????????????
                int[][] array = new int[400][400];
                for(int i = 0; i < lines.rows(); i++) {
                    double[] vec = lines.get(i, 0);
                    double x1 = vec[0];
                    double y1 = vec[1];
                    double x2 = vec[2];
                    double y2 = vec[3];
                    // ???????????????????????????????????????
                    if(x1 == x2) {
                        if(y1 > y2) {
                            double temp = y1;
                            y1 = y2;
                            y2 = temp;
                        }
                        for(int j = (int)y1; j <= y2; j++) {
                            array[j][(int)x1] = 1;
                        }
                    } else if(y1 == y2) {
                        if(x1 > x2) {
                            double temp = x1;
                            x1 = x2;
                            x2 = temp;
                        }
                        // ???????????????????????????????????????
                        for(int j = (int)x1; j <= x2; j++) {
                            array[(int)y1][j] = 1;
                        }
                    }
                }

//                 ????????????????????????
//                for(int i = 0; i < 400; i++) {
//                    String str = "";
//                    for(int j = 0; j < 400; j++) {
//                        str += array[i][j];
//                    }
//                    Log.d("array", str);
//                }

//                int x1 = 0, x2 = 99 , y1 = 0, y2 = 0;
//                // ?????????????????????
//                System.out.println("???????????????" + myFind(x1,x2,y1,y2,array));
//
//                x1 = 0;x2 = 99;y1 = 199;y2 = 199;
//                System.out.println("???????????????" + myFind(x1,x2,y1,y2,array));

                int[][] conditionNow = new int[10][10];
                int x1, x2, y1 = 0, y2 = 0;
                for(int i = 1; i <= 9; i++) {
                    int jCount;
                    if(i % 2 == 1) {
                        jCount = 4;
                        if(i == 1) y1 = y2 = 0;
                        else if(i == 3) y1 = y2 = 99;
                        else if(i == 5) y1 = y2 = 199;
                        else if(i == 7) y1 = y2 = 299;
                        else y1 = y2 = 399;
                        for(int j = 1; j <= jCount; j++) {
                            if(j == 1) {x1 = 0; x2 = 99;}
                            else if(j == 2) {x1 = 100; x2 = 199;}
                            else if(j == 3) {x1 = 200; x2 = 299;}
                            else {x1 = 300; x2 = 399;}
                            conditionNow[i][j] = myFind(x1, x2, y1, y2, array);
                        }
                    }
                    else {
                        jCount = 5;
                        if(i == 2) {y1 = 0; y2 = 99;}
                        else if(i == 4) {y1 = 100; y2 = 199;}
                        else if(i == 6) {y1 = 200; y2 = 299;}
                        else {y1 = 300; y2 = 399;}
                        for(int j = 1; j <= jCount; j++) {
                            if(j == 1) x1 = x2 = 0;
                            else if(j == 2) x1 = x2 = 99;
                            else if(j == 3) x1 = x2 = 199;
                            else if(j == 4) x1 = x2 = 299;
                            else x1 = x2 = 399;
                            conditionNow[i][j] = myFind(x1, x2, y1, y2, array);
                        }
                    }
                }

                // ??????conditionNow
//                for(int i = 1; i <= 9; i++) {
//                    String str = "";
//                    for(int j = 1; j <= 9; j++) {
//                        str += conditionNow[i][j];
//                    }
//                    Log.d("conditionNow", str);
//                }

                for(int i = 1; i <= 9; i++) {
                    int jCount;
                    if(i % 2 == 1) jCount = 4;
                    else jCount = 5;
                    for(int j = 1; j <= jCount; j++) {
                        if(conditionNow[i][j] == 1) {
                            buttons[i][j].setBackgroundColor(Color.parseColor("#9900FF"));
                            flag[i][j] = 1;
                        } else {
                            buttons[i][j].setBackgroundColor(Color.parseColor("#D3D3D3"));
                            flag[i][j] = 0;
                        }
                    }
                }



            }

        });

        bt6 = (Button) findViewById(R.id.button6);

        bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                // ????????????????????????????????? Intent
//                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                    // ???????????????????????????????????????
//                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//                }
                try {
                    createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dispatchTakePictureIntent();
                galleryAddPic();

            }
        });


        bt7 = (Button) findViewById(R.id.button7);
        bt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhotoFromGallery();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // ??????????????????????????????????????????????????????????????????????????????
            Uri currentUri = Uri.parse(currentPhotoPath);
            ImageView imageView = findViewById(R.id.imageView2);
            imageView.setImageURI(currentUri);
            mat1 = Imgcodecs.imread(currentUri.getPath());
        }
        else if(requestCode == REQUEST_PHOTO_PICKER_SINGLE_SELECT && resultCode == RESULT_OK) {
            Uri currentUri = data.getData();
            ImageView imageView = findViewById(R.id.imageView2);
            imageView.setImageURI(currentUri);
            mat1 = Imgcodecs.imread(currentUri.getPath());
        }
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void getPhotoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_PHOTO_PICKER_SINGLE_SELECT);
    }


    private int myFind(int x1, int x2, int y1, int y2, int[][] array) {
        int count = 0;
        if(x1 == x2) {
            for(int i = y1; i <= y2; i++) {
                if(array[i][x1] == 1) {
                    count++;
                }
            }
        } else if(y1 == y2) {
            for(int i = x1; i <= x2; i++) {
                if(array[y1][i] == 1) {
                    count++;
                }
            }
        }
        if(count >= 70) {
            return 1;
        } else {
            return 0;
        }
    }

    private void iniLoadOpenCV() {
        boolean success = OpenCVLoader.initDebug();
        if(success) {
            Log.d(TAG, "OpenCV loaded successfully");
        } else {
            Log.d(TAG, "OpenCV not loaded");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mat1.release();
        mat2.release();
    }

}