package balaji.ch.dicegame_balaji;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView diceScoreTV;
    Spinner diceSpinnerS;
    Button rollButtonB;
    SwitchCompat diceSwitchSC;

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
                Dice d6 = new Dice();
                d6.roll();
                diceScoreTV.setText(String.valueOf(d6.getSideUp()));
            }
        });
    }
}