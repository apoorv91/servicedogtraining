package com.SDT.servicedog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by laitkor3 on 01/07/16.
 */
public class Register extends Activity {

    Button btnSignUp;
    Button dropdown_button1, dropdown_button2;
    ProgressDialog progress;
    StringBuilder responseOutput, responseOutputState;
    String resultOutput, resultOutputMsg, resultOutputError, selectedSexOfDog, selectedValue, selectedValue_state,
            selectedStateKey = null;
    private EditText txtfName, txtlName, txtcnum, txtEmail, txtPassword, txtcnfPassword, editTextDogName, editTextBreed,
            editTextDogAge, editTextDogHistory, editTextEmergencyContact, editTextCity, editTextPostCode;
    private RadioButton radioButtonYes, radioButtonNo, radioButtonDisabilitiesYes, radioButtonDisabilitiesNo;

    private TextView spinnerStateError;
    private EditText textViewStateError;
    RelativeLayout layoutOne;
    ScrollView scrollviewOuter;

    Spinner spinnerDogSex, desexed, spinnerState;
    private CheckBox desexedCheckbox;
    private String desexedValue;

    RadioGroup radioGroup1, radioGroup2;
    String dogSexPlaceholder = "Select Sex Of Dog", StatePlaceholder = "Select State";
    String[] sexList = {dogSexPlaceholder, "Male", "Female"};
    private String radio_button_value1, radio_button_value2;

    private int validationFlag = 0, submitBtnClickFlag = 0;  // 1 - error and   0 - no error

    HashMap<Integer, String> hashmapStateValuesIds = new HashMap<Integer, String>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        txtfName = (EditText) findViewById(R.id.fname);
        txtlName = (EditText) findViewById(R.id.lname);
        txtcnum = (EditText) findViewById(R.id.contactnum);
        txtEmail = (EditText) findViewById(R.id.email);
        txtPassword = (EditText) findViewById(R.id.password);
        txtcnfPassword = (EditText) findViewById(R.id.cnfpassword);

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
        //  spinnerStateError.setVisibility(View.GONE);
        //  textViewStateError.setError("Please Select State");
        textViewStateError.setShowSoftInputOnFocus(false);
        desexedCheckbox = (CheckBox) findViewById(R.id.desexedCheckbox);


        // default value of desexed checkbox is no i.e. unchecked
        desexedValue = "no";

        // code to disable spaces in first name & last name.

        txtfName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                String str = s.toString();
                if (str.length() > 0 && str.contains(" ")) {
                    txtfName.setError("Space Is Not Allowed");
                    validationFlag = 1;
                } else {
                    validationFlag = 0;

                    Pattern ps = Pattern.compile("^[a-zA-Z ]+$");
                    Matcher ms = ps.matcher(txtfName.getText().toString());
                    boolean bs = ms.matches();
                    if (bs == false) {
                        txtfName.setError("Only Alphabets Are Allowed");
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


        txtlName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                String str = s.toString();
                //  if (str.length() > 0 && str.contains(" ")) {
                if (str.length() == 0) {
                    txtlName.setError("Please Enter Last Name");
                    validationFlag = 1;
                } else {

                    Pattern ps = Pattern.compile("^[a-zA-Z ]+$");
                    Matcher ms = ps.matcher(txtlName.getText().toString());
                    boolean bs = ms.matches();
                    if (bs == false) {
                        txtlName.setError("Only Alphabets Are Allowed");
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

        txtEmail.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                String str = s.toString();
                if (str.length() > 0 && str.contains(" ")) {
                    txtEmail.setError("Space is not allowed");
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


        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sexList);
        adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDogSex.setAdapter(adapter_state);


        // on clicking spinner get selected value code here for both spinners

        spinnerDogSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long myID) {
                Log.i("dog sex Spinner -> ", "onItemSelected: " + position + "/" + myID);

                spinnerDogSex.setSelection(position);
                View v1 = spinnerDogSex.getSelectedView();
                ((TextView) v1).setTextSize(17);
                v1.setPadding(5, 0, 0, 0);
                selectedValue = (String) spinnerDogSex.getSelectedItem();

                if (selectedValue.toString().equals(dogSexPlaceholder)) {
                    selectedValue = "null";
                }

//                  Toast.makeText(getApplicationContext(), "Selected dog sex item is : " + selectedValue, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
        System.out.println("HASH MAP VALUES ARE: " + hashmapStateValuesIds);

        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long myID) {


                Log.i("state Spinner -> ", "onItemSelected: " + position + "/" + myID);

                spinnerState.setSelection(position);
                View v = spinnerState.getSelectedView();
                ((TextView) v).setTextSize(17);
                v.setPadding(5, 0, 0, 0);
                selectedValue_state = spinnerState.getItemAtPosition(position).toString();
                selectedStateKey = hashmapStateValuesIds.get(selectedValue_state);

                for (Map.Entry<Integer, String> entry : hashmapStateValuesIds.entrySet()) {

                    if (entry.getValue().equals(selectedValue_state)) {
                        System.out.println(entry.getKey());
                        selectedStateKey = entry.getKey().toString();

                        // spinnerStateError.setVisibility(View.GONE);
                        textViewStateError.setVisibility(View.INVISIBLE);

//                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) editTextCity.getLayoutParams();
//                        params.leftMargin = 0;
//                        params.topMargin = 20;
//                        params.rightMargin = 0;
//                        params.bottomMargin = 0;
//                        editTextCity.setLayoutParams(params);

                        validationFlag = 0;

                        break;

                    } else if (selectedValue_state.equals(StatePlaceholder)) {

                        if (submitBtnClickFlag == 1) {


                            textViewStateError.setVisibility(View.VISIBLE);
                            textViewStateError.setError("Please Select State");

                            // spinnerStateError.setVisibility(View.VISIBLE);
                            selectedStateKey = null;
                            // spinnerStateError.setText("Please Select State");

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

                submitBtnClickFlag = 1;
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return false;

            }
        });


        // sign up button click operation here.

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);


                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                android.net.NetworkInfo wifi = cm
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                android.net.NetworkInfo datac = cm
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if ((wifi != null & datac != null) && (wifi.isConnected() | datac.isConnected())) {

                    if (!txtfName.getText().toString().equals("") && !txtlName.getText().toString().equals("")
                            && !txtcnum.getText().toString().equals("") && !txtEmail.getText().toString().equals("")
                            && !txtPassword.getText().toString().equals("") && !txtcnfPassword.getText().toString().equals("") && txtPassword.getText().toString().equals(txtcnfPassword.getText().toString())
                            && !editTextEmergencyContact.getText().toString().equals("") && !editTextCity.getText().toString().equals("")
                            && !editTextPostCode.getText().toString().equals("") && (editTextPostCode.getText().toString().length() == 4) && (txtcnum.getText().toString().length() == 10)
                            && (editTextEmergencyContact.getText().toString().length() == 10) && (!selectedValue_state.toString().equals("") && !selectedValue_state.toString().equals(StatePlaceholder))
                            && (selectedStateKey != null) && validationFlag == 0) {

                        sendPostRequest(v);

                    } else {

                        // Toast.makeText(getApplicationContext(), "Please Enter All Mandatory Fields", Toast.LENGTH_LONG).show();

                        scrollviewOuter.scrollTo(10, 10);
                        scrollviewOuter.fullScroll(View.FOCUS_UP);

                        if (txtfName.getText().toString().equals("")) {
                            txtfName.setError("Please enter first name");
                            validationFlag = 1;
                        } else {
                            validationFlag = 0;
                        }
                        if (txtlName.getText().toString().equals("")) {
                            txtlName.setError("Please enter Last name");
                            validationFlag = 1;
                        } else {
                            validationFlag = 0;
                        }
                        if (txtPassword.getText().toString().equals("")) {
                            txtPassword.setError("Please enter Last name");
                            validationFlag = 1;
                        } else {
                            validationFlag = 0;
                        }
                        if (txtPassword.getText().toString().length() < 6) {
                            txtPassword.setError("Password Should Be Minimum 6 Characters");
                            validationFlag = 1;
                        } else {
                            validationFlag = 0;
                        }
                        if (!txtPassword.getText().toString().equals(txtcnfPassword.getText().toString())) {
                            txtcnfPassword.setError("Password & Confirm Password Must Be Same.");
                            validationFlag = 1;
                        } else {
                            validationFlag = 0;
                        }

                        if (txtEmail.getText().toString().equals("")) {
                            txtEmail.setError("Please enter Email Id");
                            validationFlag = 1;
                        } else {

                            if (!isValidEmail(txtEmail.getText().toString())) {
                                txtEmail.setError("Invalid Email");
                                validationFlag = 1;
                            } else {
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
                        if (txtcnum.getText().toString().equals("")) {
                            txtcnum.setError("Please Enter Contact Number");
                            validationFlag = 1;

                        } else {

                            if (!isValidMobile(txtcnum.getText().toString())) {
                                txtcnum.setError("Invalid Contact Number");
                                validationFlag = 1;
                            } else if (txtcnum.getText().toString().length() != 10) {
                                txtcnum.setError("Contact Number Should Be 10 Digits");
                                validationFlag = 1;
                            } else if (txtcnum.getText().toString().length() == 10) {
                                validationFlag = 0;
                            }
                            // validationFlag = 0;
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

                            //  spinnerStateError.setText("Please Select State");
                            //  spinnerStateError.setVisibility(View.VISIBLE);

//                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) editTextCity.getLayoutParams();
//                            params.leftMargin = 0;
//                            params.topMargin = 65;
//                            params.rightMargin = 0;
//                            params.bottomMargin = 0;
//                            editTextCity.setLayoutParams(params);

                            validationFlag = 1;

                        } else {
                            // spinnerStateError.setVisibility(View.GONE);
                            textViewStateError.setVisibility(View.INVISIBLE);

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
                    //no connection
                    Toast toast = Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG);
                    toast.show();
                    //startActivity(new Intent(this, MainActivity.class));
                }
            }

            private boolean isValidEmail(String email) {

                String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

                Pattern pattern = Pattern.compile(EMAIL_PATTERN);
                Matcher matcher = pattern.matcher(email);
                return matcher.matches();

            }

        });

        Log.i("Page Name : ", "************************** Register page *********************************");

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo datac = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null & datac != null) && (wifi.isConnected() | datac.isConnected())) {

            // Server Request URL
            String serverURL = ActivityIntermediateClass.baseApiUrl+"/users/getStates/?api_key="+ActivityIntermediateClass.apiKeyValue;
            // Create Object and call AsyncTask execute Method
            new GetStateData(this, serverURL).execute();

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

                if (radio_button_value1.equals("No")) {
                    layoutOne.setVisibility(View.GONE);

                    editTextDogName.setText("");
                    editTextBreed.setText("");
                    editTextDogAge.setText("");
                    editTextDogHistory.setText("");

                } else if (radio_button_value1.equals("Yes")) {
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

//    public void setError(View v, CharSequence s) {
//        TextView name = (TextView) v.findViewById(R.id.textViewStateError);
//        name.setError(s);
//    }


    private boolean isValidMobile(String mobile) {
        if (!TextUtils.isEmpty(mobile)) {
            return Patterns.PHONE.matcher(mobile).matches();
        }
        return false;
    }


    public void sendPostRequest(View View) {
        new PostClass(this).execute();
    }

    private class PostClass extends AsyncTask<String, Void, Void> {

        private final Context context;
        StringBuilder error_message = new StringBuilder(100);

        public PostClass(Context c) {
            this.context = c;
        }

        protected void onPreExecute() {
            progress = new ProgressDialog(this.context, R.style.dialog);
            progress.setMessage("Loading");
            progress.show();
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            AlertDialog.Builder goLogin = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
            System.out.println("result output" + resultOutput);

            // if (resultOutput.contains("User already registered.")) {

            if (resultOutput.equals("0")) {

                goLogin.setMessage(error_message);
                //  goLogin.setMessage(resultOutputError);
                goLogin.setCancelable(false);
                goLogin.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertLogin = goLogin.create();
                alertLogin.show();

                Button bq = alertLogin.getButton(DialogInterface.BUTTON_POSITIVE);
                bq.setTextColor(Color.BLACK);
            }
            //else if (resultOutput.contains("Registration Successful")) {

            if (resultOutput.equals("1")) {

                goLogin.setMessage(resultOutputMsg);
                goLogin.setCancelable(false);
                goLogin.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent activityChangeIntent = new Intent(Register.this, MainActivity.class);
                        activityChangeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Register.this.startActivity(activityChangeIntent);
                        dialog.cancel();
                    }
                });
                AlertDialog alertLogin = goLogin.create();
                alertLogin.show();

                Button bq = alertLogin.getButton(DialogInterface.BUTTON_POSITIVE);
                bq.setTextColor(Color.BLACK);
            }
        }

        @Override
        protected Void doInBackground(String... params) {

            try {

                final EditText txtfName = (EditText) findViewById(R.id.fname);
                final EditText txtlName = (EditText) findViewById(R.id.lname);
                final EditText txtcnum = (EditText) findViewById(R.id.contactnum);
                final EditText txtEmail = (EditText) findViewById(R.id.email);
                final EditText txtPassword = (EditText) findViewById(R.id.password);
                final EditText txtcnfPassword = (EditText) findViewById(R.id.cnfpassword);

                URL url = null;
                try {
                    url = new URL(ActivityIntermediateClass.baseApiUrl+"/users/register/?api_key="+ActivityIntermediateClass.apiKeyValue);

                    Log.i("register test: ", "url is " + url);


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

                String post_values = "&firstname=" + txtfName.getText().toString() + "&lastname=" +
                        txtlName.getText().toString() + "&email=" + txtEmail.getText().toString() +
                        "&password=" + txtPassword.getText().toString() + "&confirm_password=" +
                        txtcnfPassword.getText().toString() + "&phone=" + txtcnum.getText().toString() +
                        "&do_u_have_dog=" + radio_button_value1 + "&dog_name=" + editTextDogName.getText().toString() + "&breed=" + editTextBreed.getText().toString() +
                        "&dog_age=" + editTextDogAge.getText().toString() + "&sex_of_dog=" + selectedValue.toString() + "&dog_training_history=" + editTextDogHistory.getText().toString() +
                        "&em_tel_contact=" + editTextEmergencyContact.getText().toString() + "&city=" + editTextCity.getText().toString() + "&postcode=" + editTextPostCode.getText().toString() +
                        "&any_disability=" + radio_button_value2 + "&state=" + selectedStateKey + "&desexed=" + desexedValue.toString() + "&device_type=android";

                Log.i("POST VALUES DATA : ", post_values);

                OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
                wr.write(post_values);
                wr.flush();
                // Log.i("output stream writer: ", wr.toString() + "\n URL is: " + url);

                int responseCode = connection.getResponseCode();

                final StringBuilder output = new StringBuilder("Request URL " + url);
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();

                Register.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        Log.i("check json response", "response is :" + responseOutput);

                        output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());


                        JSONObject json = null;
                        JSONObject json_error = null;

                        try {
                            json = new JSONObject(responseOutput.toString());
                            resultOutput = json.getString("status");
                            resultOutputMsg = json.getString("message");

                            System.out.println("the output" + resultOutput);

                            if (resultOutput.equals("0")) {

                                if (json.has("error")) {

                                    resultOutputError = json.getString("error");
                                    json_error = new JSONObject(resultOutputError.toString());

                                    // String firstname_error = json_error.getString("firstname");
                                    try {
                                        if (json_error.getJSONArray("firstname") != null) {

                                            JSONArray firstname_array = json_error.getJSONArray("firstname");
                                            String firstname_msg = firstname_array.get(0).toString();

                                            error_message.append("* " + firstname_msg.toString() + "\n");

                                            txtfName.setError("Please enter first name");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        if (json_error.getJSONArray("phone") != null) {

                                            JSONArray phone_array = json_error.getJSONArray("phone");
                                            String phone_msg = phone_array.get(0).toString();

                                            error_message.append("* " + phone_msg.toString() + "\n");

                                            txtcnum.setError(phone_msg.toString());
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        if (json_error.getJSONArray("email") != null) {

                                            JSONArray email_array = json_error.getJSONArray("email");
                                            String email_msg = email_array.get(0).toString();

                                            error_message.append("* " + email_msg.toString() + "\n");

                                            txtEmail.setError(email_msg.toString());
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

                                            editTextEmergencyContact.setError(em_tel_contact_msg.toString());
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

                                            editTextPostCode.setError(postcode_msg.toString());
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

                                } else {
                                    error_message.append(resultOutputMsg.toString());
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progress.dismiss();
                    }
                });

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
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
        private ProgressDialog progress_state;

        HttpURLConnection connection;

        public GetStateData(Context c, String url) {
            this.context = c;
            this.url_value = url;
        }

        protected void onPreExecute() {

            progress_state = new ProgressDialog(this.context, R.style.dialog) {
                @Override
                public void onBackPressed() {
                    progress_state.dismiss();

                    Intent intentgoBack = new Intent(Register.this, MainActivity.class);
                    intentgoBack.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentgoBack);
                }
            };
            progress_state.setCancelable(false);
            progress_state.setMessage("Loading Data...");
            progress_state.show();
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

                Register.this.runOnUiThread(new Runnable() {
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

            try {

                System.out.println("Radio button 1 value is : " + responseOutputState.toString());

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Register.this, android.R.layout.simple_spinner_item, state_names);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerState.setAdapter(dataAdapter);
                progress_state.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState == null) {
            finish();
        }
    }
}

