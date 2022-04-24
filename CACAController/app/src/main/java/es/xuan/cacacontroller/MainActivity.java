package es.xuan.cacacontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import es.xuan.cacacontroller.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private static final long CTE_VIBRATION_MS = 50;
    private Vibrator mVibr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        inicialitzar_pantalla();
    }

    private void inicialitzar_pantalla() {
        //
        mVibr = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        //
        LinearLayout m_llCuadroMando1 = (LinearLayout)findViewById(R.id.llCuadroMando1);
        m_llCuadroMando1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Utils.vibrar(mVibr, CTE_VIBRATION_MS);
                //
                Intent intent = new Intent(arg0.getContext(), ControlMandoJPActivity.class);
                startActivity(intent);
            }
        });
    }
}