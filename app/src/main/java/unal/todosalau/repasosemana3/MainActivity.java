package unal.todosalau.repasosemana3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button; //se utiliza para botones
import android.widget.EditText; //se utiliza para los textos que permiten ingresar datos
import android.widget.SeekBar; //se utiliza para barras de navegacion
import android.widget.TextView; //se utiliza para los textos
import java.time.LocalDate; //se utiliza para la fecha

public class MainActivity extends AppCompatActivity {
    private EditText latitudeEditText;
    private EditText longitudeEditText;
    private EditText areaEditText;
    private SeekBar inclinationSeekBar;
    private TextView inclinationTextView;
    private Button calculateButton;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitudeEditText = findViewById(R.id.latitud_editTextText);
        longitudeEditText = findViewById(R.id.Longitud_editTextText2);
        areaEditText = findViewById(R.id.Area_editTextText3);// ID area
        inclinationSeekBar = findViewById(R.id.inclination_seekbar);
        inclinationTextView = findViewById(R.id.inclination_textview);
        calculateButton = findViewById(R.id.button2);
        resultTextView = findViewById(R.id.Resultado_textView2);


        inclinationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progreso, boolean formulario) {
                inclinationTextView.setText("inclinacion de paneles: " + progreso + "");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int validacionResultado = validateInputFields(latitudeEditText, longitudeEditText, areaEditText);

                switch (validacionResultado) {
                    //Tipos de datos.
                    case 0:
                        double latitud = Double.parseDouble(latitudeEditText.getText().toString());
                        double longitud = Double.parseDouble(longitudeEditText.getText().toString());
                        double area = Double.parseDouble(areaEditText.getText().toString());

                        int inclinacion = inclinationSeekBar.getProgress();

                        double produccionEnergia = calcularProduccionEnergia(latitud, longitud, area, inclinacion);

                        resultTextView.setText("Producción de energía: " + produccionEnergia + " kWh");
                        break;

                    case 1:
                        resultTextView.setText("Por favor, complete todos los campos 😒😒😒");
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private int validateInputFields(EditText latitudeEditText, EditText longitudeEditText, EditText areaEditText) {
        if(!latitudeEditText.getText().toString().isEmpty()&&
                !longitudeEditText.getText().toString().isEmpty()&&
                !areaEditText.getText().toString().isEmpty()){
            return 0;
        }else{
            return 1;
        }
    }

    private double calcularProduccionEnergia(double latitud, double longitud, double area, int inclinacion) {

        //Funciones predefinidas
        double latitudRad = Math.toRadians(latitud); //
        double longitudRad = Math.toRadians(longitud); //
        double inclinacionRad = Math.toRadians(inclinacion); //

        //Calcula el dia del año actual
        int diaDelAnio = LocalDate.now().getDayOfYear();

        double anguloIncidencia = Math.acos(Math.sin(latitudRad) * Math.sin(inclinacionRad) + Math.cos(latitudRad) * Math.cos(inclinacionRad) * Math.cos(longitudRad));

        //Calcular radiacion
        double constanteSolar = 0.1367;
        double radiacion = constanteSolar * Math.cos(anguloIncidencia) * (1 + 0.033 * Math.cos(Math.toRadians(360 * diaDelAnio / 365.0)));

        //Calcular produccion energia
        double areaPanel = area / 10000.0;
        double eficienciaPanel = 0.16; // 16% de eficiencia
        double factorPerdidas = 0.9; // pérdida del 10%
        double produccionEnergia = areaPanel * radiacion * eficienciaPanel * factorPerdidas;
        return produccionEnergia;
    }
}