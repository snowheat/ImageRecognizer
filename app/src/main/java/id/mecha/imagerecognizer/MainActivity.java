package id.mecha.imagerecognizer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Downloader;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.mecha.imagerecognizer.logic.AutomaticTresholdTransformator;
import id.mecha.imagerecognizer.logic.GrayscaleImage;
import id.mecha.imagerecognizer.logic.HistogramGenerator;
import id.mecha.imagerecognizer.logic.InitialImage;
import id.mecha.imagerecognizer.logic.Matrix01FaceProportion;
import id.mecha.imagerecognizer.logic.Matrix01FaceRecognizer;
import id.mecha.imagerecognizer.logic.Matrix01ObjectsCounter;

public class MainActivity extends AppCompatActivity {

    Singleton singleton = Singleton.getInstance();
    String LOG = singleton.getLogString();
    InitialImage initialImage;
    GrayscaleImage grayscaleImage;
    HistogramGenerator histogram;
    ProgressDialog loadingDialog;
    AutomaticTresholdTransformator automaticTresholdTransformator;
    Matrix01ObjectsCounter matrix01ObjectsCounter;
    Matrix01FaceRecognizer matrix01FaceRecognizer;

    @BindView(R.id.initialImageButton) ImageButton initialImageButton;
    @BindView(R.id.blackWhiteImageView) ImageView blackWhiteImageView;
    @BindView(R.id.invertToggleButton) ToggleButton invertToggleButton;
    @BindView(R.id.changeModeSpinner) Spinner changeModeSpinner;

    @BindView(R.id.startProcessButton) Button startProcessButton;
    @BindView(R.id.histogramImageView) ImageView histogramImageView;
    @BindView(R.id.resultLinearLayout) LinearLayout resultLinearLayout;

    @BindView(R.id.guessPersonFaceButton) Button guessPersonFaceButton;
    @BindView(R.id.faceProportionTextView) TextView faceProportionTextView;
    @BindView(R.id.personNameTextView) TextView personNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(LOG,"onCreate() :");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.mode_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        changeModeSpinner.setAdapter(adapter);

        initialImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getImageIntent = new Intent(Intent.ACTION_PICK);
                getImageIntent.setType("image/*");
                startActivityForResult(getImageIntent,0);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        loadingDialog = ProgressDialog.show(MainActivity.this, ""
                ,"Proses transformasi grayscale :" +
                        "\n- Color to grayscale" +
                        "\n- Color look-up table" +
                        "\n- Image convolution" +
                        "\n- Histogram generation " +
                        "\n- Find histogram valleys", true);

        GrayscaleTransformationTask t = new GrayscaleTransformationTask(data);
        t.execute();



    }

    public void setOriginalImageButton() {
        initialImageButton.setImageBitmap(initialImage.getBitmap());
    }

    public void setBlackWhiteImageView() {
        blackWhiteImageView.setImageBitmap(grayscaleImage.getBitmap());
    }


    private void setHistogramImageView() {
        histogramImageView.setImageBitmap(histogram.getHistogramBitmap(
                histogramImageView.getWidth(),
                histogramImageView.getHeight()
        ));
    }

    private class GrayscaleTransformationTask extends AsyncTask<Void, Void, Void> {

        Intent data;

        public GrayscaleTransformationTask(Intent data) {

            this.data = data;
        }

        @Override
        protected void onPreExecute() {
            // Runs on UI thread
            Log.d(LOG, "About to start...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Runs on the background thread
            doGrayscaleTransformation(MainActivity.this,MainActivity.this.blackWhiteImageView,data);
            return null;
        }

        @Override
        protected void onPostExecute(Void res) {
            // Runs on the UI thread
            Log.d(LOG, "Big computation finished");

            loadingDialog.dismiss();

            setHistogramImageView();
            setOriginalImageButton();
            setBlackWhiteImageView();

            startProcessButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(LOG,"startProcessButton onClick()");

                    MainActivity.this.loadingDialog = ProgressDialog.show(MainActivity.this, ""
                            ,"Proses automasi treshold :" +
                                    "\n- Treshold transformation" +
                                    "\n- Treshold to matrix 0 1" +
                                    "\n- Count matrix 0 1 objects" +
                                    "\n- Chain code" +
                                    "\n- Flood fill" +
                                    "\n- Collect matrix 0 1 objects", true);

                    AutomaticTresholdTask t = new AutomaticTresholdTask(histogram,grayscaleImage);
                    t.execute();

                }
            });
        }

    }

    private void doGrayscaleTransformation(Context a, ImageView b, Intent c) {
        initialImage = new InitialImage(a,b,c);
        grayscaleImage = new GrayscaleImage(initialImage.getResizedBitmap());
        histogram = new HistogramGenerator(grayscaleImage.getBitmap());
    }

    private class AutomaticTresholdTask extends AsyncTask<Void, Void, Void> {


        public AutomaticTresholdTask(HistogramGenerator histogram, GrayscaleImage grayscaleImage) {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.d(LOG, "AutomaticTresholdTask is about to start. . .");
        }

        @Override
        protected Void doInBackground(Void... params) {
            MainActivity.this.automaticTresholdTransformator = new AutomaticTresholdTransformator(
                    MainActivity.this.histogram,
                    MainActivity.this.grayscaleImage);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(LOG, "Big computation finished");

            MainActivity.this.loadingDialog.dismiss();

            blackWhiteImageView.setImageBitmap(
                    MainActivity.this.automaticTresholdTransformator
                            .getTresholdTransformedBitmap());


            guessPersonFaceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    matrix01ObjectsCounter = MainActivity.this.automaticTresholdTransformator.getMatrix01ObjectsCounter();

                    System.out.println("total objek "+matrix01ObjectsCounter.getTotalSelectedObjects());

                    matrix01FaceRecognizer = new Matrix01FaceRecognizer(matrix01ObjectsCounter);
                    faceProportionTextView.setText(matrix01FaceRecognizer.getFaceProportionText());
                    personNameTextView.setText(matrix01FaceRecognizer.getPersonName());


                }
            });
        }
    }


}
