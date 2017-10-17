package com.SDT.servicedog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Settings extends AppCompatIntermediateActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private Button Pswdchange, ImageCaptureBtn, SettingsBTN, notificationBtn;
    ImageView _profileImageView, _imageView3;
    private EditText _txtFName, _txtLName, _txtContactNum, _txtEmail;
    ProgressDialog progress;
    JSONArray resultOutput;
    TextView tv_email;
    View header;
    String TaskListDescclientID;
    StringBuilder responseOutput;
    String _programmeID, responseStatus, responseMessage;
    String _loginID, clientID;
    String first_name, last_name;

    String em_tel_contact, state, city, postcode, any_disability, do_u_have_dog,
            dog_name, breed, dog_age, sex_of_dog, desexed, dog_training_history;

    private static final int RESULT_SELECT_IMAGE = 1;
    public static String _utfValue = "";
    String responseData, responseUser, reponseclientid, responsefname, responselname, responseEmail, responsephone, responseprofile_pic, encodedImageValue = null, urlencodedimagevalue = null;
    private Bitmap bitmap;
    Bitmap get_image, bitmap_default_image;
    ProgressBar _progressBarImage;

    // String encoded_data = "/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEABALDA4MChAODQ4SERATGCgaGBYWGDEjJR0oOjM9PDkzODdASFxOQERXRTc4UG1RV19iZ2hnPk1xeXBkeFxlZ2MBERISGBUYLxoaL2NCOEJjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY//AABEIADIAMgMBEQACEQEDEQH/xAGiAAABBQEBAQEBAQAAAAAAAAAAAQIDBAUGBwgJCgsQAAIBAwMCBAMFBQQEAAABfQECAwAEEQUSITFBBhNRYQcicRQygZGhCCNCscEVUtHwJDNicoIJChYXGBkaJSYnKCkqNDU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6g4SFhoeIiYqSk5SVlpeYmZqio6Slpqeoqaqys7S1tre4ubrCw8TFxsfIycrS09TV1tfY2drh4uPk5ebn6Onq8fLz9PX29/j5+gEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoLEQACAQIEBAMEBwUEBAABAncAAQIDEQQFITEGEkFRB2FxEyIygQgUQpGhscEJIzNS8BVictEKFiQ04SXxFxgZGiYnKCkqNTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqCg4SFhoeIiYqSk5SVlpeYmZqio6Slpqeoqaqys7S1tre4ubrCw8TFxsfIycrS09TV1tfY2dri4+Tl5ufo6ery8/T19vf4+fr/2gAMAwEAAhEDEQA/AJ9Z1G8i1e5SO6mRFbAVXIA4oAqDVL//AJ/J/wDvs0AI2qX/APz+z/8Afw0ARNq2of8AP7cf9/DQBE2sakOl/cf9/DQBC2s6mP8AmIXP/f00Aen27FreMkkkqCfyoA4HXjjW7v8A3/6CgBdK0q41QS/Z3jXy8Z3kjrn0B9KALsnhS/WNmMtudoJwGbP/AKDQBzrGgCFzQBA5oA9etv8Aj1h/3B/KgDz7xA2Ndu/9/wDoKANrwSS0V+BycJ/7NQBkTaNq0MLyyW7qiKWY7xwB170ATW+j219oEt5aySm6hB3xkgjjk44z06UAQXuk29joEN3cySi7n5jiBAAHXJ4z0/mKAOdc0wPYbX/j1h/3F/lSA858RtjX7z/f/oKANvwM37nUSOoCfyagDAl1vUZY2jkvJWRgQyluCD2oA2PCG+zgvdTncpaIm0j++Rzx9On40AJ43geZbXU4XMlq6BR6LnkH8f6UAca7UwPZbX/j1h/3F/lSA8z8TNjxDej/AG/6CmBBYaveaasq2k3liXAf5Qc4zjqPc0AUi9AFqXV7yTTlsGmH2VMEIFUe/JAyaAE/tu/XTTp/ng2pBGxkU989SM9aQGU70Ae12v8Ax6w/7i/yoA8w8UK//CR3uEYjf2HsKYGXtk/55v8AkaAArJ/zzb8jQAwrJ/zzf8jQBGySn/lm/wD3yaQDfJlP/LN/++TQB7Zag/ZYeP4F/lQA9o0LElFJ9xQAvlR/880/75FAB5Uf/PNP++RQAeVH/wA80/75FAB5Uf8AzzT/AL5FMA8qP/nmn/fIoAcBxSA//9k=";

    // new fields related to registration page

    Button btnSignUp;
    StringBuilder responseOutputState;
    String resultOutputMsg, resultOutputError, selectedSexOfDog, selectedValue, selectedValue_state, selectedStateKey = null;
    private EditText editTextDogName, editTextBreed, editTextDogAge, editTextDogHistory, editTextEmergencyContact, editTextCity, editTextPostCode;
    private RadioButton radioButtonYes, radioButtonNo, radioButtonDisabilitiesYes, radioButtonDisabilitiesNo;
    private CheckBox desexedCheckbox;


    private TextView spinnerStateError;
    private EditText textViewStateError;

    RelativeLayout layoutOne;
    ScrollView scrollviewOuter;

    Spinner spinnerDogSex, spinnerState;
    RadioGroup radioGroup1, radioGroup2;
    String dogSexPlaceholder = "Select Sex Of Dog", StatePlaceholder = "Select State";
    String[] sexList = {dogSexPlaceholder, "male", "female"};
    private String radio_button_value1, radio_button_value2;

    private int validationFlag = 0, submitBtnClickFlag = 0, DogSexFlag = 0;  // 1 - error and   0 - no error

    HashMap<Integer, String> hashmapStateValuesIds = new HashMap<Integer, String>();

    String image_data_post, desexedValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        // code for countdown timer for no activity logout.
//        countDownTimerObj.cancel();
//        countDownTimerObj.start();

        System.out.println("/******************************* SETTINGS CLASS STARTED ************************/");

        _txtFName = (EditText) findViewById(R.id.txtFName);
        _txtLName = (EditText) findViewById(R.id.txtLName);
        _txtContactNum = (EditText) findViewById(R.id.txtContactNum);
        _txtEmail = (EditText) findViewById(R.id.txtEmailID);
        ImageCaptureBtn = (Button) findViewById(R.id.btnImageCapture);
        Pswdchange = (Button) findViewById(R.id.changePswdBtn);
        SettingsBTN = (Button) findViewById(R.id.settingBtn);
        _profileImageView = (ImageView) findViewById(R.id.profileImageView);
        notificationBtn = (Button) findViewById(R.id.notificationBtn);

        // new fields related to registration page

        spinnerState = (Spinner) findViewById(R.id.spinnerState);
        spinnerDogSex = (Spinner) findViewById(R.id.spinnerDogSex);
        editTextDogName = (EditText) findViewById(R.id.editTextDogName);
        editTextBreed = (EditText) findViewById(R.id.editTextBreed);
        editTextDogAge = (EditText) findViewById(R.id.editTextDogAge);
        editTextDogHistory = (EditText) findViewById(R.id.editTextDogHistory);
        editTextEmergencyContact = (EditText) findViewById(R.id.editTextEmergencyContact);
        editTextCity = (EditText) findViewById(R.id.editTextCity);
        editTextPostCode = (EditText) findViewById(R.id.editTextPostCode);

        radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
        radioButtonYes = (RadioButton) findViewById(R.id.radioButtonYes);
        radioButtonNo = (RadioButton) findViewById(R.id.radioButtonNo);
        radioGroup2 = (RadioGroup) findViewById(R.id.disabilityRadioGroup);
        radioButtonDisabilitiesYes = (RadioButton) findViewById(R.id.radioButtonDisabilitiesYes);
        radioButtonDisabilitiesNo = (RadioButton) findViewById(R.id.radioButtonDisabilitiesNo);
        layoutOne = (RelativeLayout) findViewById(R.id.layoutOne);
        btnSignUp = (Button) findViewById(R.id.button1);
        // spinnerStateError = (TextView) findViewById(R.id.spinnerStateError);
        scrollviewOuter = (ScrollView) findViewById(R.id.scrollviewOuter);
        textViewStateError = (EditText) findViewById(R.id.textViewStateError);
        textViewStateError.setShowSoftInputOnFocus(false);
        desexedCheckbox = (CheckBox) findViewById(R.id.desexedCheckbox);

        // spinnerStateError.setVisibility(View.GONE);

        SharedPreferences haredpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        _txtFName.setText(haredpreferences.getString("first_name", "Null"));
        _txtLName.setText(haredpreferences.getString("last_name", "Null"));
        _txtContactNum.setText(haredpreferences.getString("phone_number", "Null"));
        _txtEmail.setText(haredpreferences.getString("email_id", "Null"));
        clientID = haredpreferences.getString("CLIENT_ID", "Null");
        _loginID = haredpreferences.getString("email_id", "Null");

        em_tel_contact = haredpreferences.getString("em_tel_contact", "Null");
        state = haredpreferences.getString("state", "Null");
        city = haredpreferences.getString("city", "Null");
        postcode = haredpreferences.getString("postcode", "Null");
        any_disability = haredpreferences.getString("any_disability", "Null");
        do_u_have_dog = haredpreferences.getString("do_u_have_dog", "Null");
        dog_name = haredpreferences.getString("dog_name", "Null");
        breed = haredpreferences.getString("breed", "Null");
        dog_age = haredpreferences.getString("dog_age", "Null");
        sex_of_dog = haredpreferences.getString("sex_of_dog", "Null");
        desexed = haredpreferences.getString("desexed", "Null");
        dog_training_history = haredpreferences.getString("dog_training_history", "Null");

        editTextEmergencyContact.setText(em_tel_contact);
        // editTextEmergencyContact.setText(state);
        editTextCity.setText(city);
        editTextPostCode.setText(postcode);
        // editTextEmergencyContact.setText(any_disability);
        // editTextEmergencyContact.setText(do_u_have_dog);
        editTextDogName.setText(dog_name);
        editTextBreed.setText(breed);
        editTextDogAge.setText(dog_age);
        // editTextEmergencyContact.setText(sex_of_dog);
        editTextDogHistory.setText(dog_training_history);

        // do_u_have_dog = "no";
        // any_disability = "no";


        editTextEmergencyContact.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {

                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    editTextEmergencyContact.moveCursorToVisibleOffset();
                    spinnerState.performClick();

                    return true; // Focus will do whatever you put in the logic.
                }
                return false;  // Focus will change according to the actionId
            }
        });

        editTextDogAge.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {

                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    editTextDogAge.moveCursorToVisibleOffset();
                    spinnerDogSex.performClick();

                    return true; // Focus will do whatever you put in the logic.
                }
                return false;  // Focus will change according to the actionId
            }
        });


        if (do_u_have_dog.equals("yes")) {

            radioButtonYes.setChecked(true);
            radioButtonNo.setChecked(false);
            layoutOne.setVisibility(View.VISIBLE);

        } else if (do_u_have_dog.equals("no")) {

            radioButtonNo.setChecked(true);
            radioButtonYes.setChecked(false);
            layoutOne.setVisibility(View.GONE);

        }

        if (any_disability.equals("yes")) {

            radioButtonDisabilitiesYes.setChecked(true);
            radioButtonDisabilitiesNo.setChecked(false);

        } else if (any_disability.equals("no")) {

            radioButtonDisabilitiesNo.setChecked(true);
            radioButtonDisabilitiesYes.setChecked(false);
        }

        if (desexed.toString().equals("yes")) {
            desexedCheckbox.setChecked(true);

            desexedValue = "yes";
        } else {
            desexedCheckbox.setChecked(false);
            desexedValue = "no";
        }


        System.out.println("image class variable value in settings page is: " + Imageclass.bitmapImageValue);
        get_image = Imageclass.bitmapImageValue;
        System.out.println("image in local variable is: " + get_image);
//        System.out.println("shared preference values are:" + haredpreferences.getString("first_name", "Null") + haredpreferences.getString("last_name", "Null") + haredpreferences.getString("phone_number", "Null") + haredpreferences.getString("email_id", "Null"));

        Intent intent = getIntent();
        _programmeID = intent.getStringExtra("PROGRAMME_ID");
        _utfValue = _programmeID;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        System.out.println("navigation opened");
        bitmap_default_image = BitmapFactory.decodeResource(this.getResources(), R.drawable.user_icon);


        _txtFName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                String str = s.toString();

                if ((str.length() > 0 && str.contains(" "))) {
                    _txtFName.setError("Space Is Not Allowed");
                    validationFlag = 1;
                } else {

                    Pattern ps = Pattern.compile("^[a-zA-Z ]+$");
                    Matcher ms = ps.matcher(_txtFName.getText().toString());
                    boolean bs = ms.matches();
                    if (bs == false) {
                        _txtFName.setError("Only Alphabets Are Allowed");
                        validationFlag = 1;
                    } else {
                        validationFlag = 0;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }
        });

        _txtLName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                String str = s.toString();

                //if (str.length() > 0 && str.contains(" ")) {
                if (str.length() == 0) {
                    _txtLName.setError("Please Enter Last Name");
                    validationFlag = 1;
                } else {

                    Pattern ps = Pattern.compile("^[a-zA-Z ]+$");
                    Matcher ms = ps.matcher(_txtLName.getText().toString());
                    boolean bs = ms.matches();
                    if (bs == false) {
                        _txtLName.setError("Only Alphabets Are Allowed");
                        validationFlag = 1;
                    } else {
                        validationFlag = 0;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }
        });

        _txtEmail.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                String str = s.toString();
                if (str.length() > 0 && str.contains(" ")) {
                    _txtEmail.setError("Space is not allowed");
                    validationFlag = 1;
                } else {
                    validationFlag = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }
        });

        ArrayAdapter<String> adapter_dogSex = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sexList);
        adapter_dogSex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDogSex.setAdapter(adapter_dogSex);

        if (submitBtnClickFlag == 0) {
            if (!sex_of_dog.toString().equals("")) {
                int ServerDogSexPosition = ((ArrayAdapter<String>) spinnerDogSex.getAdapter()).getPosition(sex_of_dog);
                spinnerDogSex.setSelection(ServerDogSexPosition);
                selectedValue = sex_of_dog;
                DogSexFlag = 1;
            } else {
                selectedValue = "null";
            }
        }


        //  if (DogSexFlag == 1) {

        spinnerDogSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long myID) {


                spinnerDogSex.setSelection(position);
                View v = spinnerDogSex.getSelectedView();
//                ((TextView) v).setTextSize(17);
//                v.setPadding(5, 0, 0, 0);
                selectedValue = (String) spinnerDogSex.getSelectedItem();

                if (selectedValue.toString().equals(dogSexPlaceholder)) {

                    selectedValue = "null";
                }
                Log.i("dog sex Spinner -> ", "onItemSelected: " + position + "/" + myID + " value " + selectedValue);
//                  Toast.makeText(getApplicationContext(), "Selected dog sex item is : " + selectedValue, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
        // }
        System.out.println("HASH MAP VALUES ARE: " + hashmapStateValuesIds);

        Log.i("VALUE OF FLAG:---", String.valueOf(submitBtnClickFlag));

        //  if (submitBtnClickFlag == 1) {
        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long myID) {

                Log.i("state Spinner -> ", "onItemSelected: " + position + "/" + myID);

                spinnerState.setSelection(position);
                View v = spinnerState.getSelectedView();
                ((TextView) v).setTextSize(17);
                v.setPadding(5, 0, 0, 0);
                selectedValue_state = spinnerState.getItemAtPosition(position).toString();
                // selectedStateKey = hashmapStateValuesIds.get(selectedValue_state);

                for (Map.Entry<Integer, String> entry : hashmapStateValuesIds.entrySet()) {

                    Log.i("State Spinner status: ", "HELLO WORLD");

                    if (submitBtnClickFlag == 1) {

                        if (entry.getValue().equals(selectedValue_state)) {
                            System.out.println("state selected key valus is : " + entry.getKey());
                            selectedStateKey = entry.getKey().toString();
                            //  spinnerStateError.setVisibility(View.GONE);
                            textViewStateError.setVisibility(View.INVISIBLE);

//                            spinnerStateError.setVisibility(View.GONE);
//                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) editTextCity.getLayoutParams();
//                            params.leftMargin = 0;
//                            params.topMargin = 20;
//                            params.rightMargin = 0;
//                            params.bottomMargin = 0;
//                            editTextCity.setLayoutParams(params);

                            validationFlag = 0;

                            break;

                        } else if (selectedValue_state.equals(StatePlaceholder)) {

                            textViewStateError.setVisibility(View.VISIBLE);
                            textViewStateError.setError("Please Select State");

                            // spinnerStateError.setVisibility(View.VISIBLE);
                            selectedStateKey = "null";

//                            spinnerStateError.setText("Please Select State");
//                            spinnerStateError.setVisibility(View.VISIBLE);
//                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) editTextCity.getLayoutParams();
//                            params.leftMargin = 0;
//                            params.topMargin = 65;
//                            params.rightMargin = 0;
//                            params.bottomMargin = 0;
//                            editTextCity.setLayoutParams(params);

                            validationFlag = 1;
                        }
                    }
                }

                //  Toast.makeText(getApplicationContext(), "Selected state item and id are : " + selectedValue_state + "id----" + selectedStateKey, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
        //}


// on clicking drop down close android keypad code here for both drop downs.

        spinnerDogSex.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return false;
            }
        });

        spinnerState.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return false;
            }
        });


        initNavigationDrawer();


        //System.out.println("Shared Preference values are: "+"first="+haredpreferences.getString("first_name", "Null")+"last="+haredpreferences.getString("last_name", "Null")+"client="+haredpreferences.getString("CLIENT_ID", "Null")+"phone="+haredpreferences.getString("phone_number", "Null")+"email="+haredpreferences.getString("email_id", "Null"));
//        System.out.println("This is settings page");

        SettingsBTN.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                submitBtnClickFlag = 1;
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

//                if (!_txtFName.getText().toString().equals("") && !_txtLName.getText().toString().equals("")
//                        && !_txtContactNum.getText().toString().equals("") && !_txtEmail.getText().toString().equals("")) {
//                    if (!isValidMobile(_txtContactNum.getText().toString())) {
//                        _txtContactNum.setError("Invalid Phone Number");
//                    }
//                    if (!isValidEmail(_txtEmail.getText().toString())) {
//                        _txtEmail.setError("Invalid Email");
//                    }
//                    if (isValidMobile(_txtContactNum.getText().toString()) && isValidEmail(_txtEmail.getText().toString())) {

                ConnectivityManager cm_object = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                android.net.NetworkInfo wifi_connect = cm_object.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                android.net.NetworkInfo data_connect = cm_object.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                if ((wifi_connect != null & data_connect != null) && (wifi_connect.isConnected() | data_connect.isConnected())) {

                    if (!_txtFName.getText().toString().equals("") && !_txtLName.getText().toString().equals("")
                            && !_txtContactNum.getText().toString().equals("") && !_txtEmail.getText().toString().equals("")
                            && !editTextEmergencyContact.getText().toString().equals("") && !editTextCity.getText().toString().equals("")
                            && !editTextPostCode.getText().toString().equals("") && (editTextPostCode.getText().toString().length() == 4) && (_txtContactNum.getText().toString().length() == 10)
                            && (editTextEmergencyContact.getText().toString().length() == 10) && (!selectedValue_state.toString().equals("") || !selectedValue_state.toString().equals(StatePlaceholder))
                            && !selectedStateKey.toString().equals("null") && validationFlag == 0) {

//                        String post_values = "&firstname=" + _txtFName.getText().toString() + "&lastname=" +
//                                _txtLName.getText().toString() + "&email=" + _txtEmail.getText().toString() + "&phone=" + _txtContactNum.getText().toString() +
//                                "&do_u_have_dog=" + radio_button_value1 + "&dog_name=" + editTextDogName.getText().toString() + "&breed=" + editTextBreed.getText().toString() +
//                                "&dog_age=" + editTextDogAge.getText().toString() + "&sex_of_dog=" + selectedValue.toString() + "&dog_training_history=" + editTextDogHistory.getText().toString() +
//                                "&em_tel_contact=" + editTextEmergencyContact.getText().toString() + "&city=" + editTextCity.getText().toString() + "&postcode=" + editTextPostCode.getText().toString() +
//                                "&any_disability=" + radio_button_value2 + "&state=" + selectedStateKey + "&device_type=android" + image_data_post;
//
//                        Log.i("POST VALUES DATA : ", post_values);

                        receiveGetRequest(v);

                    } else {

                        scrollviewOuter.scrollTo(10, 10);
                        scrollviewOuter.fullScroll(View.FOCUS_UP);

                        if (_txtFName.getText().toString().equals("")) {
                            _txtFName.setError("Please enter first name");
                            validationFlag = 1;
                        } else {
                            validationFlag = 0;
                        }
                        if (_txtLName.getText().toString().equals("")) {
                            _txtLName.setError("Please enter Last name");
                            validationFlag = 1;
                        } else {
                            validationFlag = 0;
                        }

                        if (_txtEmail.getText().toString().equals("")) {
                            _txtEmail.setError("Please enter Email Id");
                            validationFlag = 1;
                        } else {

                            if (!isValidEmail(_txtEmail.getText().toString())) {
                                _txtEmail.setError("Invalid Email");
                                validationFlag = 1;
                            } else {
                                validationFlag = 0;
                            }
                            // validationFlag = 0;
                        }

                        if (_txtContactNum.getText().toString().equals("")) {
                            _txtContactNum.setError("Please Enter Contact Number");
                            validationFlag = 1;

                        } else {

                            if (!isValidMobile(_txtContactNum.getText().toString())) {
                                _txtContactNum.setError("Invalid Contact Number");
                                validationFlag = 1;
                            } else if (_txtContactNum.getText().toString().length() != 10) {
                                _txtContactNum.setError("Contact Number Should Be 10 Digits");
                                validationFlag = 1;
                            } else if (_txtContactNum.getText().toString().length() == 10) {
                                validationFlag = 0;
                            }
                            // validationFlag = 0;
                        }


                        if (editTextEmergencyContact.getText().toString().equals("")) {
                            editTextEmergencyContact.setError("Please Enter Contact Number");
                            validationFlag = 1;
                        } else {

                            if (editTextEmergencyContact.getText().toString().length() != 10) {

                                editTextEmergencyContact.setError("Contact Number Should Be 10 Digits");
                                validationFlag = 1;
                            } else if (editTextEmergencyContact.getText().toString().length() == 10) {
                                validationFlag = 0;
                            }
                        }


                        if (editTextCity.getText().toString().equals("")) {
                            editTextCity.setError("Please enter City");
                            validationFlag = 1;
                        } else {
                            validationFlag = 0;
                        }
                        if (editTextPostCode.getText().toString().equals("")) {
                            editTextPostCode.setError("Please enter Postcode");
                            validationFlag = 1;
                        } else {

                            if (editTextPostCode.getText().toString().length() != 4) {
                                editTextPostCode.setError("Postcode Should Be 4 Digits");
                                validationFlag = 1;
                            } else {
                                validationFlag = 0;
                            }
                            // validationFlag = 0;
                        }

                        if (selectedValue_state.equals(StatePlaceholder)) {

                            textViewStateError.setVisibility(View.VISIBLE);
                            textViewStateError.setError("Please Select State");

//                            spinnerStateError.setText("Please Select Sate");
//                            spinnerStateError.setVisibility(View.VISIBLE);
//                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) editTextCity.getLayoutParams();
//                            params.leftMargin = 0;
//                            params.topMargin = 65;
//                            params.rightMargin = 0;
//                            params.bottomMargin = 0;
//                            editTextCity.setLayoutParams(params);

                            validationFlag = 1;

                        } else {

                            textViewStateError.setVisibility(View.INVISIBLE);

//                            spinnerStateError.setVisibility(View.GONE);
//                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) editTextCity.getLayoutParams();
//                            params.leftMargin = 0;
//                            params.topMargin = 15;
//                            params.rightMargin = 0;
//                            params.bottomMargin = 0;
//                            editTextCity.setLayoutParams(params);

                            validationFlag = 0;
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
//                    }
//                } else {
//                    Toast.makeText(getApplicationContext(), "Please Enter All Fields", Toast.LENGTH_LONG).show();
//                }
            }

            private boolean isValidEmail(String email) {

                String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

                Pattern pattern = Pattern.compile(EMAIL_PATTERN);
                Matcher matcher = pattern.matcher(email);
                return matcher.matches();

            }

            private boolean isValidMobile(String phone) {
                return android.util.Patterns.PHONE.matcher(phone).matches();
            }
        });

        Pswdchange.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent newIntent = new Intent(Settings.this, ChangePassword.class);
                startActivity(newIntent);

                // Toast.makeText(getApplicationContext(), "CapturedImage", Toast.LENGTH_LONG).show();
            }
        });

        _txtEmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Email Id Cannot Be Updated", Toast.LENGTH_LONG).show();
            }
        });

        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_notification = new Intent(Settings.this, DonotDisturbActivity.class);
                startActivity(intent_notification);
            }
        });

        ImageCaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//call the function to select image from album
                loadImagefromGallery();
            }
        });


        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo datac = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null & datac != null) && (wifi.isConnected() | datac.isConnected())) {

            // Server Request URL
            String serverURL = ActivityIntermediateClass.baseApiUrl+"/users/getStates/?api_key="+ActivityIntermediateClass.apiKeyValue;
            // Create Object and call AsyncTask execute Method
            new Settings.GetStateData(this, serverURL).execute();

        } else {
            //no connection
            Toast toast = Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG);
            toast.show();
            //startActivity(new Intent(this, MainActivity.class));
        }

        radio_button_value1 = ((RadioButton) findViewById(radioGroup1.getCheckedRadioButtonId())).getText().toString();
        radio_button_value2 = ((RadioButton) findViewById(radioGroup2.getCheckedRadioButtonId())).getText().toString();

        System.out.println("Radio button 1 and 2  values on load are : " + radio_button_value1 + "------" + radio_button_value2);


        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                radio_button_value1 = ((RadioButton) findViewById(radioGroup1.getCheckedRadioButtonId())).getText().toString();
                System.out.println("Radio button 1 value is : " + radio_button_value1);

                // Toast.makeText(getApplicationContext(), radio_button_value1, Toast.LENGTH_LONG).show();

                if (radio_button_value1.equals("no")) {
                    layoutOne.setVisibility(View.GONE);

                    editTextDogName.setText("");
                    editTextBreed.setText("");
                    editTextDogAge.setText("");
                    editTextDogHistory.setText("");

                    // int ServerDogSexPosition = ((ArrayAdapter<String>) spinnerDogSex.getAdapter()).getPosition(sex_of_dog);
                    spinnerDogSex.setSelection(0);
                    selectedValue = "";

                } else if (radio_button_value1.equals("yes")) {
                    layoutOne.setVisibility(View.VISIBLE);
                }
            }
        });

        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                radio_button_value2 = ((RadioButton) findViewById(radioGroup2.getCheckedRadioButtonId())).getText().toString();
                System.out.println("Radio button 2 value is : " + radio_button_value2);

                // Toast.makeText(getApplicationContext(), radio_button_value2, Toast.LENGTH_LONG).show();
            }
        });

        desexedCheckbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (desexedCheckbox.isChecked()) {

                    desexedValue = "yes";
                    // Toast.makeText(Settings.this, "checkbox checked", Toast.LENGTH_SHORT).show();

                } else {

                    desexedValue = "no";
                    // Toast.makeText(Settings.this, "checkbox Unchecked", Toast.LENGTH_SHORT).show();

                }
                Log.i("Desexed Check Status: ", desexedValue.toString());
            }
        });

    }

    public String getStringImage(Bitmap bmp) throws MalformedURLException, URISyntaxException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        try {
            // System.out.println("64 encoded data: " + encodedImage);
            urlencodedimagevalue = URLEncoder.encode(encodedImage, "utf-8");
            System.out.println("image encoded value: " + urlencodedimagevalue);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return urlencodedimagevalue;
        // return "hello";
    }

    // image browse and upload code starts here

    public void loadImagefromGallery() {


        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_SELECT_IMAGE);

//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_SELECT_IMAGE);

        System.out.println("inside load gallery method");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_SELECT_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            System.out.println("data is : " + filePath);

            try {
                String filename = GetFileName(filePath);
                String filenameArray[] = filename.split("\\.");
                String extension = filenameArray[filenameArray.length - 1];

                if (extension.equals("mp4") || extension.equals("3gp") || extension.equals("avi") || extension.equals("mpg") || extension.equals("flv") || extension.equals("mkv")) {

                    Toast.makeText(this, "Invalid File Type Selected,please try again.", Toast.LENGTH_LONG).show();

                } else {

                    //Getting the Bitmap from Gallery.compressing the image and then setting as profile image.

                    String filePathNew = getPath(getApplicationContext(), filePath);

//                    // code starts to check profile image size in KB
//
//                    File file = new File(filePathNew);
//                     long length = file.length() / 1024; // Size in KB
//                    System.out.println("Profile image actual size in KB : " + length);
//
//                    // code ends to check profile image size in KB

                    byte[] bitmapdata = ImageUtils.compressImage(filePathNew.toString());

                    bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                    // bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                    //Setting the Bitmap to ImageView
                    _profileImageView.setImageBitmap(bitmap);
                    try {
                        encodedImageValue = getStringImage(bitmap);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    System.out.println("bitmap value SETTINGS is: " + bitmap);

                    _profileImageView = (ImageView) findViewById(R.id.profileImageView);

                    if (bitmap != null) {

                        int targetWidth = 200;
                        int targetHeight = 200;
                        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
                        BitmapShader shader;

                        System.out.println("target bitmap value SETTINGS is: " + targetBitmap);
                        shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

                        Paint paint = new Paint();
                        paint.setAntiAlias(true);
                        paint.setShader(shader);
                        Canvas canvas = new Canvas(targetBitmap);
                        Path path = new Path();
                        path.addCircle(((float) targetWidth) / 2,
                                ((float) targetHeight) / 2,
                                (Math.min(((float) targetWidth), ((float) targetHeight)) / 2), Path.Direction.CCW);
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                        paint.setStyle(Paint.Style.STROKE);
                        canvas.clipPath(path);
                        Bitmap sourceBitmap = bitmap;
                        canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight()),
                                new Rect(0, 0, targetWidth, targetHeight), null);
                        _profileImageView.setImageBitmap(targetBitmap);   //set the circular image to your imageview
                        //  _imageView3.setImageBitmap(targetBitmap);

                        System.out.println("gallery image value is: " + bitmap);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // navigation drawer on clicks code here

    public void initNavigationDrawer() {
        //  new GetData(this).execute();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id) {
                    case R.id._aboutus:

                        Intent aboutusintent = new Intent(Settings.this, AboutUS.class);
                        aboutusintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(aboutusintent);
                        //Toast.makeText(getApplicationContext(), "About us", Toast.LENGTH_SHORT).show();
                        //drawerLayout.closeDrawers();
                        break;
                    case R.id._trainingprog:
                        //Toast.makeText(getApplicationContext(), "Training Programmes", Toast.LENGTH_SHORT).show();
                        Intent trainingintent = new Intent(Settings.this, TrainingProgrammes.class);
                        trainingintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(trainingintent);
                        break;
                    case R.id._taskhis:
                        //Toast.makeText(getApplicationContext(), "Task History", Toast.LENGTH_SHORT).show();
                        //drawerLayout.closeDrawers();
                        Intent historyintent = new Intent(Settings.this, TaskHistory.class);
                        historyintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(historyintent);
                        break;
                    case R.id.taskrem:
                        //Toast.makeText(getApplicationContext(), "Task Reminders", Toast.LENGTH_SHORT).show();
                        Intent newintent = new Intent(Settings.this, TaskReminders.class);
                        newintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(newintent);

                        break;
                    case R.id.settingschange:
                        Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.help_btn:
                        // Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        Intent helpIintent = new Intent(Settings.this, Help.class);
                        helpIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(helpIintent);

                        break;

                    case R.id.menu_btn:
                        // Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        Intent menuIintent = new Intent(Settings.this, UserMenuActivity.class);
                        menuIintent.putExtra("EMAIL_ID", _loginID);
                        menuIintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(menuIintent);
                        break;

                    case R.id.logout:

                        Intent intent = new Intent(Settings.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish(); // Call once you redirect to another activity
                }
                return true;
            }
        });


        SharedPreferences haredpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        header = navigationView.getHeaderView(0);
        tv_email = (TextView) header.findViewById(R.id.tv_email);
        _imageView3 = (ImageView) header.findViewById(R.id.imageView3);
        _progressBarImage = (ProgressBar) header.findViewById(R.id.progressBarImage);

        if (get_image == null) {

            _profileImageView.setImageBitmap(bitmap_default_image);
            _progressBarImage.setVisibility(View.VISIBLE);
            _imageView3.setImageBitmap(bitmap_default_image);
            _progressBarImage.setVisibility(View.INVISIBLE);

        } else {

            _profileImageView.setImageBitmap(Imageclass.ResizeBitmapImage());
            _progressBarImage.setVisibility(View.VISIBLE);
            _imageView3.setImageBitmap(Imageclass.ResizeBitmapImage());
            _progressBarImage.setVisibility(View.INVISIBLE);

            System.out.println("Client id is =" + clientID);
        }

        tv_email.setText(haredpreferences.getString("first_name", "Null") + " " + haredpreferences.getString("last_name", "Null"));
        TaskListDescclientID = haredpreferences.getString("CLIENT_ID", "Null");

//        System.out.println("training prog client id shared prefernce" + TaskListDescclientID);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);

                InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(Settings.this.getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //TODO your background code
                        CheckProfileImageFlagMethod(header);
                    }
                });
            }
        });
    }

    private void CheckProfileImageFlagMethod(View view) {

        if (Imageclass.image_getdata_flag == 1) {

            ImageView profileImageView = (ImageView) view.findViewById(R.id.imageView3);
            profileImageView.setImageBitmap(Imageclass.ResizeBitmapImage());

        }
//        else {
//            CheckProfileImageFlagMethod(view);
//        }
    }


    private void receiveGetRequest(View v) {
        new GetData(this).execute();
    }

    private class GetData extends AsyncTask<String, Void, Void> {

        private final Context context;
        StringBuilder responseOutput = null;
        StringBuilder error_message = new StringBuilder(100);

        public GetData(Context c) {
            this.context = c;
        }

        protected void onPreExecute() {
            progress = new ProgressDialog(this.context, R.style.dialog);
            progress.setMessage("Loading");
            progress.show();
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            AlertDialog.Builder gosettings = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);

//            System.out.println("json output is = " + responsefname.toString() + responselname.toString() + responseEmail.toString() + responsephone.toString() + responseprofile_pic);

            NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
            View header = navigationView.getHeaderView(0);
            TextView tv_email = (TextView) header.findViewById(R.id.tv_email);
//            tv_email.setText(responsefname.toString() + " " + responselname.toString());
            // responseStatus = "1";

            if (responseStatus.equals("1")) {
                gosettings.setMessage(responseMessage);
                gosettings.setCancelable(false);
                gosettings.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertLogin = gosettings.create();
                alertLogin.show();
                tv_email.setText(_txtFName.getText().toString() + " " + _txtLName.getText().toString());
                Imageclass.bitmapImageValue = bitmap;

                //   image_data_post;

                SharedPreferences sharedpreference = getSharedPreferences("MyPREFERENCES", context.MODE_PRIVATE);
                SharedPreferences.Editor sharedEditor = sharedpreference.edit();
                sharedEditor.putString("first_name", _txtFName.getText().toString());
                sharedEditor.putString("last_name", _txtLName.getText().toString());
                sharedEditor.putString("phone_number", _txtContactNum.getText().toString());
                sharedEditor.putString("email_id", _txtEmail.getText().toString());
                // sharedEditor.putString("profile_pic", Imageclass.bitmapImageValue);

                // new registration fields
                sharedEditor.putString("em_tel_contact", editTextEmergencyContact.getText().toString());
                sharedEditor.putString("state", selectedStateKey.toString());
                sharedEditor.putString("city", editTextCity.getText().toString());
                sharedEditor.putString("postcode", editTextPostCode.getText().toString());
                sharedEditor.putString("any_disability", radio_button_value2.toString());
                sharedEditor.putString("do_u_have_dog", radio_button_value1.toString());
                sharedEditor.putString("dog_name", editTextDogName.getText().toString());
                sharedEditor.putString("breed", editTextBreed.getText().toString());
                sharedEditor.putString("dog_age", editTextDogAge.getText().toString());
                sharedEditor.putString("sex_of_dog", selectedValue.toString());
                sharedEditor.putString("desexed", desexedValue.toString());
                sharedEditor.putString("dog_training_history", editTextDogHistory.getText().toString());

                sharedEditor.commit();

            } else if (responseStatus.equals("0")) {
                gosettings.setMessage(error_message);
                gosettings.setCancelable(false);
                gosettings.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertLogin = gosettings.create();
                alertLogin.show();

                if (get_image == null) {

                    _profileImageView.setImageBitmap(bitmap_default_image);
                    _progressBarImage.setVisibility(View.VISIBLE);
                    _imageView3.setImageBitmap(bitmap_default_image);
                    _progressBarImage.setVisibility(View.INVISIBLE);


                } else {

                    _profileImageView.setImageBitmap(Imageclass.ResizeBitmapImage());
                    _progressBarImage.setVisibility(View.VISIBLE);
                    _imageView3.setImageBitmap(Imageclass.ResizeBitmapImage());
                    _progressBarImage.setVisibility(View.INVISIBLE);


                    System.out.println("Client id is =" + clientID);
                }
            }
            _progressBarImage.setVisibility(View.VISIBLE);
            receiveGetImage();
        }

        @Override
        protected Void doInBackground(String... params) {

            _txtFName = (EditText) findViewById(R.id.txtFName);
            _txtLName = (EditText) findViewById(R.id.txtLName);
            _txtContactNum = (EditText) findViewById(R.id.txtContactNum);
            _txtEmail = (EditText) findViewById(R.id.txtEmailID);
            _profileImageView = (ImageView) findViewById(R.id.profileImageView);

            System.out.println("values are:" + _txtFName + _txtLName + _txtContactNum + _txtEmail + "profile image is=");

            try {
                URL url = null;

//                System.out.println("ENCODED IMAGE VALUE IS: "+encodedImageValue+"~~~~~");
//                System.out.println("IMAGE VARIABLE VALUE IS: "+Imageclass.bitmapImageValue+"~~~~~");
                if (encodedImageValue == null) {
                    try {
                        if (Imageclass.bitmapImageValue == null) {

                            _progressBarImage.setVisibility(View.VISIBLE);
                            Imageclass.bitmapImageValue = bitmap_default_image;
                            _progressBarImage.setVisibility(View.INVISIBLE);

                        }

                        encodedImageValue = getStringImage(Imageclass.bitmapImageValue);
                        System.out.println("ENCODED IMAGE VALUE IS UPDATED: " + encodedImageValue + "~~~~~");
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }


                image_data_post = URLEncoder.encode("image", "UTF-8") + "=" + encodedImageValue;
                try {

                    url = new URL(ActivityIntermediateClass.baseApiUrl+"/users/updateprofile/?api_key="+ActivityIntermediateClass.apiKeyValue+"&client_id=" + TaskListDescclientID);
                    // System.out.println("Url value is =" + url);
//                    System.out.println("encoded data is =" +"\n"+encodedImageValue);

                    System.out.println("post data is: " + image_data_post);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                HttpURLConnection connection = null;

                try {
                    connection = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    connection.setRequestMethod("POST");
                } catch (ProtocolException e) {
                    e.printStackTrace();
                }
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setDoOutput(true);

                String post_values = "&firstname=" + _txtFName.getText().toString() + "&lastname=" +
                        _txtLName.getText().toString() + "&email=" + _txtEmail.getText().toString() + "&phone=" + _txtContactNum.getText().toString() +
                        "&do_u_have_dog=" + radio_button_value1 + "&dog_name=" + editTextDogName.getText().toString() + "&breed=" + editTextBreed.getText().toString() +
                        "&dog_age=" + editTextDogAge.getText().toString() + "&sex_of_dog=" + selectedValue.toString() + "&dog_training_history=" + editTextDogHistory.getText().toString() +
                        "&em_tel_contact=" + editTextEmergencyContact.getText().toString() + "&city=" + editTextCity.getText().toString() + "&postcode=" + editTextPostCode.getText().toString() +
                        "&any_disability=" + radio_button_value2 + "&state=" + selectedStateKey + "&device_type=android" + "&desexed=" + desexedValue.toString() + "&" + image_data_post;

                Log.i("POST VALUES DATA : ", post_values);

                OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
                wr.write(post_values);
                wr.flush();


                final int responseCode = connection.getResponseCode();

                System.out.println("RESPONSE CODE is: " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "POST");
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line = "";
                    responseOutput = new StringBuilder();
                    System.out.println("output===============" + br);
                    while ((line = br.readLine()) != null) {
                        responseOutput.append(line);
                    }
                    br.close();
                }

                Log.i("JSON STGS output is :", responseOutput.toString());

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());

                Settings.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        JSONObject json_new, json_1, json_2;
                        JSONObject json = null;
                        JSONObject json_error = null;

                        try {
                            json_new = new JSONObject(responseOutput.toString());
                            responseStatus = json_new.getString("status");
                            responseMessage = json_new.getString("message");
                            responseData = json_new.getString("data");

                            System.out.println("The reponse status: " + responseStatus);

                            if (responseStatus.equals("0")) {

                                resultOutputError = json_new.getString("error");
                                json_error = new JSONObject(resultOutputError.toString());

                                // String firstname_error = json_error.getString("firstname");
                                try {
                                    if (json_error.getJSONArray("firstname") != null) {

                                        JSONArray firstname_array = json_error.getJSONArray("firstname");
                                        String firstname_msg = firstname_array.get(0).toString();

                                        error_message.append("* " + firstname_msg.toString() + "\n");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    if (json_error.getJSONArray("phone") != null) {

                                        JSONArray phone_array = json_error.getJSONArray("phone");
                                        String phone_msg = phone_array.get(0).toString();

                                        error_message.append("* " + phone_msg.toString() + "\n");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    if (json_error.getJSONArray("email") != null) {

                                        JSONArray email_array = json_error.getJSONArray("email");
                                        String email_msg = email_array.get(0).toString();

                                        error_message.append("* " + email_msg.toString() + "\n");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    if (json_error.getJSONArray("password") != null) {

                                        JSONArray password_array = json_error.getJSONArray("password");
                                        String password_msg = password_array.get(0).toString();

                                        error_message.append("* " + password_msg.toString() + "\n");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    if (json_error.getJSONArray("password_confirm") != null) {

                                        JSONArray confrm_password_array = json_error.getJSONArray("password_confirm");
                                        String confrm_password_msg = confrm_password_array.get(0).toString();

                                        error_message.append("* " + confrm_password_msg.toString() + "\n");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    if (json_error.getJSONArray("do_u_have_dog") != null) {

                                        JSONArray do_u_have_dog_array = json_error.getJSONArray("do_u_have_dog");
                                        String do_u_have_dog_msg = do_u_have_dog_array.get(0).toString();

                                        error_message.append("* " + do_u_have_dog_msg.toString() + "\n");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    if (json_error.getJSONArray("em_tel_contact") != null) {

                                        JSONArray em_tel_contact_array = json_error.getJSONArray("em_tel_contact");
                                        String em_tel_contact_msg = em_tel_contact_array.get(0).toString();

                                        error_message.append("* " + em_tel_contact_msg.toString() + "\n");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    if (json_error.getJSONArray("city") != null) {

                                        JSONArray city_array = json_error.getJSONArray("city");
                                        String city_msg = city_array.get(0).toString();

                                        error_message.append("* " + city_msg.toString() + "\n");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    if (json_error.getJSONArray("postcode") != null) {

                                        JSONArray postcode_array = json_error.getJSONArray("postcode");
                                        String postcode_msg = postcode_array.get(0).toString();

                                        error_message.append("* " + postcode_msg.toString() + "\n");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    if (json_error.getJSONArray("state") != null) {

                                        JSONArray state_array = json_error.getJSONArray("state");
                                        String state_msg = state_array.get(0).toString();

                                        error_message.append("* " + state_msg.toString() + "\n");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    if (json_error.getJSONArray("any_disability") != null) {

                                        JSONArray any_disability_array = json_error.getJSONArray("any_disability");
                                        String any_disability_msg = any_disability_array.get(0).toString();

                                        error_message.append("* " + any_disability_msg.toString() + "\n");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            json_1 = new JSONObject(responseData.toString());
                            responseUser = json_1.getString("User");
                            json_2 = new JSONObject(responseUser.toString());
                            reponseclientid = json_2.getString("id");
                            responsefname = json_2.getString("firstname");
                            responselname = json_2.getString("lastname");
                            responseEmail = json_2.getString("email");
                            responsephone = json_2.getString("phone");
                            responseprofile_pic = json_2.getString("profile_pic");

                            System.out.println("JSON latest output is = " + responsefname + responselname + responseEmail + responsephone + responseprofile_pic);

                            SharedPreferences haredpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
                            SharedPreferences.Editor sharedEditor = haredpreferences.edit();
                            sharedEditor.putString("first_name", responsefname);
                            sharedEditor.putString("last_name", responselname);
                            sharedEditor.putString("CLIENT_ID", reponseclientid);
                            sharedEditor.putString("phone_number", responsephone);
                            sharedEditor.putString("email_id", responseEmail);
                            sharedEditor.putString("profile_pic", responseprofile_pic);

                            sharedEditor.commit();
//                            System.out.println("testing try block = "+sharedEditor.commit());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progress.dismiss();
                    }
                });

//            } catch (MalformedURLException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
    } // end of async task method


    // This method is used to get image url from server after login using async task class in background.

    private void receiveGetImage() {

        new SetImageData(this).execute();
    }

    private class SetImageData extends AsyncTask<String, Void, Void> {

        private final Context context;

        public SetImageData(Context c) {
            this.context = c;
        }

        protected void onPreExecute() {
        }

        protected void onPostExecute(Void result) {

            // System.out.println("UPDATED bitmap value is: " + Imageclass.ResizeBitmapImage());

            _profileImageView.setImageBitmap(Imageclass.ResizeBitmapImage());
            _progressBarImage.setVisibility(View.VISIBLE);
            _imageView3.setImageBitmap(Imageclass.ResizeBitmapImage());
            _progressBarImage.setVisibility(View.INVISIBLE);


//            SharedPreferences haredpreference = getSharedPreferences("MyPREFERENCES",Context.MODE_PRIVATE);
//            SharedPreferences.Editor sharedEditor = haredpreference.edit();
//            sharedEditor.putString("profile_pic_bitmap_value",Imageclass.ResizeBitmapImage().toString());

        }

        @Override
        protected Void doInBackground(String... params) {

            try {

                SharedPreferences haredpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
                String profile_image_url = haredpreferences.getString("profile_pic", "null");

                System.out.println("image URL is " + profile_image_url);

                java.net.URL url_image = new java.net.URL(profile_image_url.toString());
                HttpURLConnection connection_image = (HttpURLConnection) url_image.openConnection();
                connection_image.setDoInput(true);
                connection_image.connect();
                InputStream input = connection_image.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                // return myBitmap;
                // Bitmap bitmapImageValue = myBitmap;
                // System.out.println("bitmap IMAGE is---- " + bitmapImageValue);
                Imageclass.bitmapImageValue = myBitmap;

                System.out.println("bitmap value in class variable is " + Imageclass.bitmapImageValue);
            } catch (IOException e) {
                e.printStackTrace();
                //return null;
            }
            return null;
        }
    }


    // Async task method that gets state data from state API
    private class GetStateData extends AsyncTask<String, Void, Void> {

        private final Context context;
        private final String url_value;
        List<String> state_ids = new ArrayList<String>();
        List<String> state_names = new ArrayList<String>();

        HttpURLConnection connection;

        public GetStateData(Context c, String url) {
            this.context = c;
            this.url_value = url;
        }

        protected void onPreExecute() {
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {

            state_names.add(0, StatePlaceholder);
            state_ids.add(0, "null");


            URL url = null;
            try {
                url = new URL(url_value);
            } catch (MalformedURLException mue) {
                mue.printStackTrace();
            }

            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setDoInput(true);

                final int responseCode = connection.getResponseCode();
                final StringBuilder output = new StringBuilder("Request URL" + url);

                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator") + "Type " + "GET");

                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";

                responseOutputState = new StringBuilder();

                while ((line = bufferedreader.readLine()) != null) {
                    responseOutputState.append(line);
                }
                bufferedreader.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutputState.toString());

                Settings.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        JSONObject json = null;
                        Gson gson = new Gson();

                        Gson gson_state = new Gson();
                        JSONObject jsonobject1 = null, jsonobject2 = null;
                        JSONArray jsonArray1 = null;

                        Log.i("State API response: ", responseOutputState.toString());

                        try {

                            RootObjectRegisterState rootobjectstate = gson_state.fromJson(responseOutputState.toString(), RootObjectRegisterState.class);
                            jsonobject1 = new JSONObject(responseOutputState.toString());
                            jsonArray1 = jsonobject1.getJSONArray("data");

                            for (int i = 0; i < jsonArray1.length(); i++) {
                                state_ids.add(rootobjectstate.data.get(i).State.id);
                                state_names.add(rootobjectstate.data.get(i).State.state);
                                hashmapStateValuesIds.put(Integer.valueOf(rootobjectstate.data.get(i).State.id), rootobjectstate.data.get(i).State.state);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("STATE ARRAY IDS IS: ", state_ids.toString());
                        Log.i("STATE ARRAY NAMES IS: ", state_names.toString());
                        Log.i("STATE HASHMAP IS: ", hashmapStateValuesIds.toString());
                    }
                });

            } catch (ProtocolException pe) {
                pe.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void unused) {

            System.out.println("Radio button 1 value is : " + responseOutputState.toString());

            ArrayAdapter<String> stateDataAdapter = new ArrayAdapter<String>(Settings.this, android.R.layout.simple_spinner_item, state_names);
            stateDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerState.setAdapter(stateDataAdapter);

            if (submitBtnClickFlag == 0) {
                if (!state.toString().equals("")) {
                    // String ServerStatePosition = ((ArrayAdapter<String>) spinnerState.getAdapter()).getItem(Integer.parseInt(state));
                    spinnerState.setSelection(Integer.parseInt(state));
                    spinnerState.setSelection(state_names.indexOf(state_names.get(state_ids.indexOf(state))));
                    selectedStateKey = state;
                    submitBtnClickFlag = 1;
                    // Log.i("State value from Server", ServerStatePosition);
                }
            }
        }

    }

    public String GetFileName(Uri uri) {
//
        String fileName;
        String path = "";
        String data_path = "";
        // String[] projection = {MediaStore.MediaColumns.DATA};
        String[] projection = {MediaStore.MediaColumns.DISPLAY_NAME};
        ContentResolver cr = getApplicationContext().getContentResolver();
        try (Cursor metaCursor = cr.query(uri, projection, null, null, null)) {
            if (metaCursor != null) {
                try {
                    if (metaCursor.moveToFirst()) {
                        path = metaCursor.getString(0);
                    }
                } finally {
                    metaCursor.close();
                }
            }
        }
        fileName = path;
        System.out.println("Hello: " + uri);
        System.out.println("Hello: " + path);

        String[] projection_data = {MediaStore.MediaColumns.DATA};

        try (Cursor metaCursor = cr.query(uri, projection_data, null, null, null)) {
            if (metaCursor != null) {
                try {
                    if (metaCursor.moveToFirst()) {
                        data_path = metaCursor.getString(0);
                    }
                } finally {
                    metaCursor.close();
                }
            }
        }
        System.out.println("Hello: " + data_path);
        return fileName;

    }

    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKatOrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKatOrAbove && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    public String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

} // end of class
