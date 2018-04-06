package com.mathius.dd.unixtodate;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private AdView mAdView;
    private TextView mTvFirstState;
    private ImageView mIvChangeButtonspinner;
    private TextView mTvSecondState;
    private ImageView mIvDelete;
    private EditText mEtUpEditText;
    private ImageView mIvInsert;
    private TextView mTvDown;
    private ImageView mIvCopyAll;
    private ImageView mIvShare;
    String ml_to_km;
    Double result;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ListView mLvHistory;

    //Историю записываем
    List<Model> products = new ArrayList<>();

    MyAdapter myAdapter;

    //кнопка для созранения истории
    private ImageView mIvSave;

    //сохраняем историю в preferences
    SharedPreferences sPref;
    final String SAVED_TEXT = "saved_text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        //Проверяет имеется ли запись Истории, если имеется загружает Историю
        if (loadHistory() != null) {
            products = loadHistory();
        }

        //создаем адаптер
        myAdapter = new MyAdapter(getApplicationContext(), products);
        //передаем адаптер в listview
        mLvHistory.setAdapter(myAdapter);

        //реклама баннер
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //При вводе данных будет в ресльном времени конвертироваться
        mEtUpEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void afterTextChanged(Editable s) {

                //эффект нажатия на кнопку
                makeAnimationOnView(R.id.etResult, Techniques.FadeOut, 150, 0);
                makeAnimationOnView(R.id.etResult, Techniques.FadeIn, 350, 0);

                // конвертирует единицы
                convertText();


            }


        });


        //При нажатии одной из пунктов listview будет скопирован в вверхний edittext
        mLvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mEtUpEditText.setText(products.get(i).getTextView1().toString());
            }
        });

    }

    private void initView() {

        mAdView = findViewById(R.id.adView);
        mTvFirstState = findViewById(R.id.tvFirstState);
        mIvChangeButtonspinner = findViewById(R.id.ivChangeButtonspinner);
        mIvChangeButtonspinner.setOnClickListener(this);
        mTvSecondState = findViewById(R.id.tvSecondState);
        mIvDelete = findViewById(R.id.ivDelete);
        mIvDelete.setOnClickListener(this);
        mEtUpEditText = findViewById(R.id.etInput);
        mEtUpEditText.setOnClickListener(this);
        mIvInsert = findViewById(R.id.ivInsert);
        mIvInsert.setOnClickListener(this);
        mTvDown = findViewById(R.id.etResult);
        mTvDown.setOnClickListener(this);
        mIvCopyAll = findViewById(R.id.ivCopyAll);
        mIvCopyAll.setOnClickListener(this);
        mIvShare = findViewById(R.id.ivShare);
        mIvShare.setOnClickListener(this);
        mLvHistory = (ListView) findViewById(R.id.lvHistory);

        mIvSave = (ImageView) findViewById(R.id.ivSave);
        mIvSave.setOnClickListener(this);
    }

    //конвертируем единицу
    void convertText() {

        //считываем edittext и приобразовываем в string
        String temp = mEtUpEditText.getText().toString();
        temp = temp.replace(",", "");

        //проверяем нету ли точки или пустых значений или null или пустоты
        if (temp.equals(".") || temp.equals(" ") || temp.equals(null) || temp.equals("") || temp.isEmpty()) {

            //в случае обнаружения обрываем дальнейшее исполнение метода
            return;
        }

        //Проверяем если стоит Мили тогда конвертируем в километры и наоборот
        ml_to_km = temp;
        if (mTvFirstState.getText().equals(getString(R.string.miles))) {
            result = Double.valueOf(ml_to_km) * 1.609344;
        } else {
            result = Double.valueOf(ml_to_km) * 0.621371192237334;
        }

        //сконвертированную единицу вставляем в textview
        mTvDown.setText(result.toString());


    }

    //анимации для кнопок
    void makeAnimationOnView(int resourceId, Techniques techniques, int duration, int repeat) {
        YoYo.with(techniques)
                .duration(duration)
                .repeat(repeat)
                .playOn(findViewById(resourceId));

    }

    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        switch (v.getId()) {


            case R.id.ivDelete:// TODO 17/12/24
                //эффект нажатия на кнопку
                makeAnimationOnView(R.id.ivDelete, Techniques.FadeOut, 150, 0);
                makeAnimationOnView(R.id.ivDelete, Techniques.FadeIn, 350, 0);

                //edittext равно Пустота
                mEtUpEditText.setText("");
                mTvDown.setText("");
                break;
            case R.id.ivInsert:// TODO 17/12/24

                mEtUpEditText.setText("");

                //эффект нажатия на кнопку
                makeAnimationOnView(R.id.ivInsert, Techniques.FadeOut, 150, 0);
                makeAnimationOnView(R.id.ivInsert, Techniques.FadeIn, 350, 0);

                //Вставляем в edittext - текст с буфера обмена
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);


                //check for null
                String textFromClipboard = clipboard.getPrimaryClip().getItemAt(0).getText().toString();

                try {
                    Double.parseDouble(textFromClipboard);
                } catch (Exception e) {
                    return;
                }


                if (clipboard == null) {
                    return;
                } else {

                    mEtUpEditText.setText(textFromClipboard);
                }


                break;
            case R.id.ivCopyAll:// TODO 17/12/24
                //эффект нажатия на кнопку
                makeAnimationOnView(R.id.ivCopyAll, Techniques.FadeOut, 150, 0);
                makeAnimationOnView(R.id.ivCopyAll, Techniques.FadeIn, 350, 0);

                // Gets a handle to the clipboard service.
                ClipboardManager clipboard2 = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                // Creates a new text clip to put on the clipboard
                ClipData clip = ClipData.newPlainText("simple text", mTvDown.getText().toString());

                // Set the clipboard's primary clip.
                clipboard2.setPrimaryClip(clip);
                Toast.makeText(this, R.string.TextCopied, Toast.LENGTH_SHORT).show();
                break;
            case R.id.ivShare:// TODO 17/12/24
                //эффект нажатия на кнопку
                makeAnimationOnView(R.id.ivShare, Techniques.FadeOut, 150, 0);
                makeAnimationOnView(R.id.ivShare, Techniques.FadeIn, 350, 0);

                String shareBody = mTvDown.getText().toString();
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_using)));
                break;
            case R.id.tvFirstState:// TODO 17/12/24
                break;
            case R.id.tvSecondState:// TODO 17/12/24
                break;
            case R.id.etInput:// TODO 17/12/24
                break;
            case R.id.etResult:// TODO 17/12/24
                break;
            case R.id.ivChangeButtonspinner:// TODO 17/12/24


                //получаем текущий item в спиннере 2 (mTvSecondState)
//                String resultSecondState = mTvSecondState.getText().toString();

//                if (!resultSecondState.equals(kilometres) && !resultSecondState.equals(miles)) {
//
//                    //эффект нажатия на кнопку ivChangeButton
//                    makeAnimationOnView(R.id.ivChangeButtonspinner, Techniques.RotateOut, 750, 0);
                makeAnimationOnView(R.id.ivChangeButtonspinner, Techniques.RotateIn, 700, 0);
//
//
//                    //эффект нажатия на textview mTvFirstState
//                makeAnimationOnView(R.id.tvFirstState, Techniques.FadeOut, 350, 0);
                makeAnimationOnView(R.id.tvFirstState, Techniques.FadeIn, 700, 0);
//
//
//                    //эффект нажатия на textview mTvSecondState
//                makeAnimationOnView(R.id.tvSecondState, Techniques.FadeOut, 350, 0);
                makeAnimationOnView(R.id.tvSecondState, Techniques.FadeIn, 700, 0);
//
//                } else {
                //эффект нажатия на если выбраны kilometres и miles
//                makeAnimationOnView(R.id.spinnerSecondState, Techniques.Shake, 700, 0);

//                }
                //Меняем местами название First and Second textviews
                changePlacesSpinnerCyrillicLatin();

                //проверяем пустой ли текст в mEtUpEditText
                if (mEtUpEditText == null || mEtUpEditText.equals(null) || mEtUpEditText.equals("")) {
                    break;
                }

                //преобразования текста при нажатии этой кнопки
                convertText();

                break;
            case R.id.ivSave:// TODO 18/02/20

                //эффект нажатия на кнопку
                makeAnimationOnView(R.id.ivSave, Techniques.FadeOut, 150, 0);
                makeAnimationOnView(R.id.ivSave, Techniques.FadeIn, 350, 0);

                //эффект нажатия на кнопку
                makeAnimationOnView(R.id.lvHistory, Techniques.FadeOut, 150, 0);
                makeAnimationOnView(R.id.lvHistory, Techniques.FadeIn, 350, 0);

                String temp = mEtUpEditText.getText().toString();

                if (temp.equals(".") || temp.equals(" ") || temp.equals(null) || temp.equals("") || temp.isEmpty()) {

                    //в случае обнаружения обрываем дальнейшее исполнение метода
                    return;
                }

                //получаем название kilometres и miles из strings.xml
                String kilometres = getString(R.string.kilometres);
                String miles = getString(R.string.miles);

                if (mTvFirstState.getText().equals(miles)) {
                    miles = "mi";
                    kilometres = "km";
                } else {
                    miles = "km";
                    kilometres = "mi";
                }

                //Записываем в список дял истории
                //добавляем всегда на первую позицию новый item, остальные смещаются вниз
                products.add(0, new Model(mEtUpEditText.getText().toString(), result.toString(), miles, kilometres));
                try {

                    if (products.get(30) != null) {
                        products.remove(30);
                    }
                } catch (Exception e) {
                }

                //создаем адаптер
                myAdapter = new MyAdapter(getApplicationContext(), products);
                //передаем адаптер в listview
                mLvHistory.setAdapter(myAdapter);

                break;
        }
    }

    //Меняем местами название First and Second textviews
    void changePlacesSpinnerCyrillicLatin() {

        String s1;
        s1 = mTvFirstState.getText().toString();
        mTvFirstState.setText(mTvSecondState.getText().toString());
        mTvSecondState.setText(s1);

    }


    void saveHistory() {

        // used for store arrayList in json format
        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(products);
        prefsEditor.putString(SAVED_TEXT, json);
        prefsEditor.apply();

    }

    ArrayList<Model> loadHistory() {
        Gson gson = new Gson();
        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        String json = mPrefs.getString(SAVED_TEXT, "");
        Type type = new TypeToken<List<Model>>() {
        }.getType();
        List<Model> obj = gson.fromJson(json, type);

        return (ArrayList<Model>) obj;
    }


    @Override
    protected void onPause() {
        super.onPause();

        saveHistory();
    }
}
