package balaji.ch.dicegame_balaji;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

//    Declaring all the required variables our App

    private TextView diceScoreTV;
    private Spinner diceSpinnerS;
    private Button rollButtonB;
    private Button addDiceButton;
    private SwitchCompat diceSwitchSC;
    private int diceSides;
    private String dynamicDiceString;
    private EditText dynamicDiceET;
    private String historyData;
    private TextView historyTV;
    private String result;
    private SharedPreferences prefs;
    ArrayList<DieType> dieSides;
    ArrayAdapter adapter;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //        Initializing Every view, button, textview, editview, spinner
        diceScoreTV = findViewById(R.id.diceScore);
        diceSpinnerS = findViewById(R.id.diceSpinner);
        diceSwitchSC = findViewById(R.id.diceSwitch);
        rollButtonB = findViewById(R.id.rollButton);
        addDiceButton = findViewById(R.id.addButton);
        dynamicDiceET = findViewById(R.id.dynamicDice);
        historyTV = findViewById(R.id.history);
        historyData = "";

        diceSides = 6;


//        This snippet runs when we click on the ROLL button in the UI and it called throwDice() method
        rollButtonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                When the ROLL button is clicked, We are extracting the item that is selected on the Spinner and passing it to the throwDice() method as integer
                diceSpinnerS = findViewById(R.id.diceSpinner);
                String spinnerSelected = String.valueOf(diceSpinnerS.getSelectedItem());
                diceSides = (int) Double.parseDouble(spinnerSelected);
                throwDice(diceSides);
            }
        });



//        I am initializing an Array list of Die Type and adapter to insert the list into the spinner
        dieSides = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,dieSides);

//        I am adding the die sides in the list
        //dieSides.add(new DieType("Dice Size (how many sides): "));
        dieSides.add(new DieType("4"));
        dieSides.add(new DieType("6"));
        dieSides.add(new DieType("8"));
        dieSides.add(new DieType("10"));
        dieSides.add(new DieType("12"));
        dieSides.add(new DieType("20"));


//        The below runs when the ADD button is clicked.
        addDiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = false;
//                When the ADD button is clicked, we will extract the data from the edittext and add it to the array list which intern adds the data into the spinner
                dynamicDiceString = dynamicDiceET.getText().toString();

//                Checking if the EditText is empty or not and then adding the number which is typed in edittext will be added to the arraylist and the spinner
//                only if that number is not present
                if(!dynamicDiceString.isEmpty()){
                    for(DieType eachDie: dieSides){
                        if(String.valueOf(eachDie).equals(dynamicDiceString)){
                            flag = true;
                            break;
                        }
                    }

                    if (!flag){
                        dieSides.add(new DieType(dynamicDiceString));
                    }
                    dynamicDiceET.setText("");
//                    saveData();
//                    diceSpinnerS.setAdapter(adapter);
//                    diceSpinnerS.setSelection(1, true);
                }
            }
        });


        diceSpinnerS.setAdapter(adapter);
        diceSpinnerS.setSelection(1, true);

//        This snippet runs when we select a particular item in the spinner. When selected, we'll assign it's value to a variable roll the die when ROLL button is clicked.
        diceSpinnerS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!parent.getItemAtPosition(position).toString().isEmpty()){
                    diceSides = Integer.parseInt(parent.getItemAtPosition(position).toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

//    These are shared preferences.
//    Code saved every time in it's local storage
    @Override
    protected void onResume() {
        super.onResume();

        editor = prefs.edit();

        String dynamicDiceString = prefs.getString("dynamic_dice_prefs", "");
        ((EditText)findViewById(R.id.dynamicDice)).setText(dynamicDiceString);

        boolean rollTwoDices = prefs.getBoolean("roll_two_dices_prefs", false);
        diceSwitchSC.setChecked(rollTwoDices);

        String historyDataString = prefs.getString("history_data_prefs", historyData);
        historyTV.setText(historyDataString);

        String dieResult = prefs.getString("die_result_prefs", "result");
        diceScoreTV.setText(dieResult);

//        String defaultSpinnerElements = "4, 6, 8, 10, 12, 20";
//        String spinnerString = prefs.getString("spinner_string_dice_prefs", defaultSpinnerElements);
//        Log.d("spinnerString", spinnerString);
//        ArrayList<DieType> dieSides = new ArrayList<>();
//        for (String eachStringElement: spinnerString.split(",")){
//            dieSides.add(new DieType(eachStringElement.trim()));
//        }
//
//        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dieSides);
//        diceSpinnerS.setAdapter(adapter);
//        editor.clear();
    }

    @Override
    protected void onPause() {
        saveData();
        super.onPause();
    }

    @Override
    protected void onStop() {
        saveData();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        saveData();
        super.onDestroy();
    }

//    Our logic runs in this method.
//    We get an integer which says the number of sides a die has and then we'll call the Dice class to return a random number from 1 to the number, dicesides.
    public void throwDice(int diceSides) {
//        We are checking if one die is asked to roll or two dice.
        if (!diceSwitchSC.isChecked()) {
            Dice d = new Dice(diceSides);
            d.roll();
            result = String.valueOf(d.getSideUp());
            diceScoreTV.setText(result);
            if (historyData.isEmpty()){
                historyData = "History: " + result;
            } else if (historyData.length() > 50){
                historyData = "History: " + result;
            }else{
                historyData = historyData + "," + result;
            }
            historyTV.setText(historyData);


        } else {
            Dice d1 = new Dice(diceSides);
            d1.roll();

            Dice d2 = new Dice(diceSides);
            d2.roll();
            String die1Score = d1.getSideUp() + "";
            String die2Score = d2.getSideUp() + "";
            result =  die1Score + "      " + die2Score;
            diceScoreTV.setText(result);
            if (historyData.isEmpty()){
                historyData = "History: " + die1Score + "," + die2Score;
            } else if (historyData.length() > 50){
                historyData = "History: " + die1Score + "," + die2Score;;
            }else{
                historyData = historyData + "," + die1Score + "," + die2Score;
            }
            historyTV.setText(historyData);
        }
    }

//    This method is called in pause, stop and destroy methods which refer to the sharedPreferences.

//    We'll save the data of each view in the form of key:value pairs and return them whenever we resume or pass or stop or reopen the application.
    public void saveData(){
        editor = prefs.edit();
        editor.putBoolean("roll_two_dices_prefs", diceSwitchSC.isChecked());
        dynamicDiceET = findViewById(R.id.dynamicDice);
        editor.putString("dynamic_dice_prefs", String.valueOf(dynamicDiceET.getText()));
        editor.putString("die_result_prefs", String.valueOf(diceScoreTV.getText()));
//        String tempSpinnerString = dieSides.get(0) + "";
//
//        Adapter adapter = diceSpinnerS.getAdapter();
//        for(int i=1;i<adapter.getCount();i++){
//            tempSpinnerString = tempSpinnerString + adapter.getItem(i) + ",";
//            Log.d("SPINNER DICE", tempSpinnerString);
//        }


//        String tempSpinnerString = dieSides.get(0) + "";
//        for(int i=1;i<dieSides.size();i++){
//            tempSpinnerString = tempSpinnerString + "," + dieSides.get(i);
//            Log.d("SPINNER DICE", tempSpinnerString);
//        }
//        editor.putString("spinner_string_dice_prefs",tempSpinnerString);

        editor.putString("history_data_prefs", historyData);
        editor.apply();
    }

}
