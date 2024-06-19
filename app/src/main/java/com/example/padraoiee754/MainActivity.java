package com.example.padraoiee754;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public static String[] conversaoDec(float decimal) {
        //Deslocamento de bits
        // & == 0100 & 0010 == 0000
        // | == 0100 | 0010 == 0110
        // << == 0100 << 1 == 1000
        StringBuilder valorFinal = new StringBuilder();

        int sinal = 0;
        if (Float.toString(decimal).equals("-0.0")) {
            sinal = 1;
        }else{
            sinal = (decimal < 0) ? 1 : 0;
        }

        valorFinal.append(String.format("%1s", Integer.toBinaryString(sinal)).replace(' ', '0'));
        valorFinal.append(" ");

        float absNumero = Math.abs(decimal);

        int expoente = 0;

        if (absNumero == 0.0f) {
            expoente = 0;
        } else {
            while (absNumero >= 2.0) {
                absNumero /= 2.0;
                expoente += 1;
            }
            while (absNumero < 1.0 && absNumero != 0.0) {
                absNumero *= 2.0;
                expoente -= 1;
            }
        }

        int Bias = 0;
        if(absNumero != 0.0f) {
            Bias = expoente + 127;
        }else{
            expoente = -127;
        }

        String binarioBias = String.format("%8s", Integer.toBinaryString(Bias)).replace(' ', '0');
        valorFinal.append(binarioBias);
        valorFinal.append(" ");

        if (absNumero != 0.0f){
            absNumero -= 1.0f;
        }
        int mantissa = 0;

        for (int i = 0; i < 23; i++) {
            absNumero *= 2;
            if (absNumero >= 1.0) {
                mantissa += (1 << (22 - i));
                absNumero -= 1.0;
            }
        }

        valorFinal.append(String.format("%23s", Integer.toBinaryString(mantissa)).replace(' ', '0'));
        valorFinal.append(" ");

        int hexaValor = (sinal << 31) + (Bias << 23) + mantissa;

        String binarioFinal = valorFinal.toString();

        String[] valores = {Integer.toHexString(hexaValor).toUpperCase(),
        binarioFinal,
        Integer.toString(expoente),
        Integer.toString(Bias),
        binarioBias};

        return valores;
    }

    public void calcular(View v){
        EditText valor = findViewById(R.id.entradaValor);
        TextView resultadoBinario = findViewById(R.id.textoBinario),
                 resultadoHexa = findViewById(R.id.textoHexadecimal),
                 resultadoExpoente = findViewById(R.id.textoExpoente),
                 resultadoExpoenteBinario = findViewById(R.id.textoBinarioExpoente),
                 resultadoBIAS = findViewById(R.id.textoBias);

        String valorEmTexto = valor.getText().toString();

        if(valorEmTexto.equals("") || valorEmTexto.equals(" ") || valorEmTexto.equals("-") || valorEmTexto.equals(".")){
            resultadoBinario.setText("Espaço em branco!");
            resultadoExpoente.setText("");
            resultadoExpoenteBinario.setText("");
            resultadoBIAS.setText("");
            resultadoHexa.setText("");
        }else{
            float valorEmDecimal = Float.parseFloat(valorEmTexto);

            String[] resultado = conversaoDec(valorEmDecimal);

            resultadoBinario.setText("Resultado Binário: " + resultado[1]);
            resultadoExpoente.setText("Resultado Expoente: " + resultado[2]);
            resultadoExpoenteBinario.setText("Resultado Expoente/Bias (Binário): " + resultado[4]);
            resultadoBIAS.setText("Resultado BIAS: " + resultado[3]);
            resultadoHexa.setText("Resultado Hexadecimal: " + resultado[0]);
        }
    }
}