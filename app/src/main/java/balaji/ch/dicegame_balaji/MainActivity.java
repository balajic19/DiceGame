package balaji.ch.dicegame_balaji;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView diceScoreTV;
    Spinner diceSpinnerS;
    Button rollButtonB;
    SwitchCompat diceSwitchSC;
    int diceSides;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        diceScoreTV = findViewById(R.id.diceScore);
        diceSpinnerS = findViewById(R.id.diceSpinner);
        diceSwitchSC = findViewById(R.id.diceSwitch);
        rollButtonB = findViewById(R.id.rollButton);

        rollButtonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResults();
            }
        });

        ArrayList<DieType> dieSides = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,dieSides);

        dieSides.add(new DieType("Dice Size (how many sides): "));
        dieSides.add(new DieType("4"));
        dieSides.add(new DieType("6"));
        dieSides.add(new DieType("8"));
        dieSides.add(new DieType("12"));
        dieSides.add(new DieType("20"));

        diceSpinnerS.setAdapter(adapter);

        diceSpinnerS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                diceSides = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    public void returnResults(){

        switch (diceSides){
            case 1:
                throwDice(4);
                break;
            case 3:
                throwDice(8);
                break;
            case 4:
                throwDice(12);
                break;
            case 5:
                throwDice(20);
                break;
            case 2:
            default:
                throwDice(6);
                break;
        }

    }


    public void throwDice(int diceSides) {
        if (!diceSwitchSC.isChecked()) {
            Dice d = new Dice(diceSides);
            d.roll();
            diceScoreTV.setText(String.valueOf(d.getSideUp()));
        } else {
            Dice d1 = new Dice(diceSides);
            d1.roll();

            Dice d2 = new Dice(diceSides);
            d2.roll();
            diceScoreTV.setText(String.valueOf(d1.getSideUp()) + "      " + String.valueOf(d2.getSideUp()));
        }
    }
}
