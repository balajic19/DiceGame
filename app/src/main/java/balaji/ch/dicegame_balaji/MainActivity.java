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


public class MainActivity extends AppCompatActivity {

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
    private SharedPreferences prefs;
    ArrayList<DieType> dieSides;
    ArrayAdapter adapter;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        diceScoreTV = findViewById(R.id.diceScore);
        diceSpinnerS = findViewById(R.id.diceSpinner);
        diceSwitchSC = findViewById(R.id.diceSwitch);
        rollButtonB = findViewById(R.id.rollButton);
        addDiceButton = findViewById(R.id.addButton);
        dynamicDiceET = findViewById(R.id.dynamicDice);
        historyTV = findViewById(R.id.history);
        historyData = "History: ";

        diceSides = 6;


        rollButtonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diceSpinnerS = findViewById(R.id.diceSpinner);
                String spinnerSelected = String.valueOf(diceSpinnerS.getSelectedItem());
                diceSides = (int) Double.parseDouble(spinnerSelected);
                throwDice(diceSides);
            }
        });



        dieSides = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,dieSides);

        //dieSides.add(new DieType("Dice Size (how many sides): "));
        dieSides.add(new DieType("4"));
        dieSides.add(new DieType("6"));
        dieSides.add(new DieType("8"));
        dieSides.add(new DieType("12"));
        dieSides.add(new DieType("20"));

        addDiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dynamicDiceString = dynamicDiceET.getText().toString();
                if(!dynamicDiceString.isEmpty()){
//                    if (!dieSides.contains(dynamicDiceString)){
//                        dieSides.add(new DieType(dynamicDiceString));
//                    }
                    dieSides.add(new DieType(dynamicDiceString));
                    dynamicDiceET.setText("");
                    saveData();
                    diceSpinnerS.setAdapter(adapter);
                    diceSpinnerS.setSelection(1, true);
                }
            }
        });


        diceSpinnerS.setAdapter(adapter);
        diceSpinnerS.setSelection(1, true);
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

//        String defaultSpinnerElements = "4, 6, 8, 12, 20";
//        String spinnerString = prefs.getString("spinner_string_dice_prefs", defaultSpinnerElements);
//        Log.d("spinnerString", spinnerString);
//        ArrayList<DieType> dieSides = new ArrayList<>();
//        for (String eachStringElement: spinnerString.split(",")){
//            dieSides.add(new DieType(eachStringElement.trim()));
//        }

//        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dieSides);
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

    public void throwDice(int diceSides) {
        if (!diceSwitchSC.isChecked()) {
            Dice d = new Dice(diceSides);
            d.roll();
            String dieScore = String.valueOf(d.getSideUp());
            diceScoreTV.setText(dieScore);
            historyData = historyData + dieScore + ", ";
            if (historyData.length() > 50){
                historyData = "History: ";
            }
            historyTV.setText(historyData);


        } else {
            Dice d1 = new Dice(diceSides);
            d1.roll();

            Dice d2 = new Dice(diceSides);
            d2.roll();
            String die1Score = d1.getSideUp() + "";
            String die2Score = d2.getSideUp() + "";
            String tempResult =  die1Score + "      " + die2Score;
            diceScoreTV.setText(tempResult);
            historyData = historyData + die1Score + ", " + die2Score + ", ";
            if (historyData.length() > 10){
                historyData = "History: ";
            }
            historyTV.setText(historyData);
        }
    }

    public void saveData(){
        editor = prefs.edit();
        editor.putBoolean("roll_two_dices_prefs", diceSwitchSC.isChecked());
        dynamicDiceET = findViewById(R.id.dynamicDice);
        editor.putString("dynamic_dice_prefs", String.valueOf(dynamicDiceET.getText()));



//        String tempSpinnerString = dieSides.get(0) + "";
//        Adapter adapter = diceSpinnerS.getAdapter();
//        for(int i=0;i<adapter.getCount();i++){
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
