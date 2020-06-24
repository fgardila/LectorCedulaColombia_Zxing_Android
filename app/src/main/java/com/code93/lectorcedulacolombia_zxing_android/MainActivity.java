package com.code93.lectorcedulacolombia_zxing_android;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * **************************************************************
 * Autor:		Fabian Guillermo Ardila Castro
 * email:		code93dev@gmail.com
 * linkdin:     https://www.linkedin.com/in/fgardila
 * ****************************************************************
 */
public class MainActivity extends AppCompatActivity {

    public static final int CUSTOMIZED_REQUEST_CODE = 0x0000ffff;

    TextView tvFirstName;
    TextView tvLastName;
    TextView tvDocumentID;
    TextView tvGender;
    TextView tvDate;
    TextView tvRH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvFirstName = findViewById(R.id.tvFirstName);
        tvLastName = findViewById(R.id.tvLastName);
        tvDocumentID = findViewById(R.id.tvDocumentID);
        tvGender = findViewById(R.id.tvGender);
        tvDate = findViewById(R.id.tvDate);
        tvRH = findViewById(R.id.tvRH);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.btnScan) {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.PDF_417);
            integrator.setPrompt("Acerca el codigo de barras de la cedula");
            integrator.setOrientationLocked(false);
            integrator.setBeepEnabled(true);
            integrator.setBarcodeImageEnabled(true);
            integrator.setTorchEnabled(true);
            integrator.initiateScan();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode != CUSTOMIZED_REQUEST_CODE && requestCode != IntentIntegrator.REQUEST_CODE) {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);

        if(result.getContents() == null) {
            Intent originalIntent = result.getOriginalIntent();
            if (originalIntent == null) {
                Log.d("LoginActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else if(originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                Log.d("MainActivity", "Cancelled scan due to missing camera permission");
                Toast.makeText(this, "Cancelled due to missing camera permission", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d("MainActivity", "Scanned: " + result.getContents());
            //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            parseDataCode(result.getContents());
        }
    }

    private void parseDataCode(String barcode) {
        InfoTarjeta infoTarjeta = null;
        if (barcode != null) {

            if (barcode.length() < 150) {
                //TODO lanzar excepcion y mensaje
            }

            infoTarjeta = new InfoTarjeta();
            String primerApellido = "", segundoApellido = "", primerNombre = "", segundoNombre = "", cedula = "", rh = "", fechaNacimiento = "", sexo = "";

            String alphaAndDigits = barcode.replaceAll("[^\\p{Alpha}\\p{Digit}\\+\\_]+", " ");
            String[] splitStr = alphaAndDigits.split("\\s+");

            if (!alphaAndDigits.contains("PubDSK")) {
                int corrimiento = 0;


                Pattern pat = Pattern.compile("[A-Z]");
                Matcher match = pat.matcher(splitStr[2 + corrimiento]);
                int lastCapitalIndex = -1;
                if (match.find()) {
                    lastCapitalIndex = match.start();
                    String TAG = "parseDataCode";
                    Log.d(TAG, "match.start: " + match.start());
                    Log.d(TAG, "match.end: " + match.end());
                    Log.d(TAG, "splitStr: " + splitStr[2 + corrimiento]);
                    Log.d(TAG, "splitStr length: " + splitStr[2 + corrimiento].length());
                    Log.d(TAG, "lastCapitalIndex: " + lastCapitalIndex);
                }
                cedula = splitStr[2 + corrimiento].substring(lastCapitalIndex - 10, lastCapitalIndex);
                primerApellido = splitStr[2 + corrimiento].substring(lastCapitalIndex);
                segundoApellido = splitStr[3 + corrimiento];
                primerNombre = splitStr[4 + corrimiento];
                /**
                 * Se verifica que contenga segundo nombre
                 */
                if (Character.isDigit(splitStr[5 + corrimiento].charAt(0))) {
                    corrimiento--;
                } else {
                    segundoNombre = splitStr[5 + corrimiento];
                }

                //sexo = splitStr[6 + corrimiento].contains("M") ? "Masculino" : "Femenino";
                sexo = splitStr[6 + corrimiento];
                rh = splitStr[6 + corrimiento].substring(splitStr[6 + corrimiento].length() - 2);
                fechaNacimiento = splitStr[6 + corrimiento].substring(2, 10);

            } else {
                int corrimiento = 0;
                Pattern pat = Pattern.compile("[A-Z]");
                if (splitStr[2 + corrimiento].length() > 7) {
                    corrimiento--;
                }


                Matcher match = pat.matcher(splitStr[3 + corrimiento]);
                int lastCapitalIndex = -1;
                if (match.find()) {
                    lastCapitalIndex = match.start();

                }

                cedula = splitStr[3 + corrimiento].substring(lastCapitalIndex - 10, lastCapitalIndex);
                primerApellido = splitStr[3 + corrimiento].substring(lastCapitalIndex);
                segundoApellido = splitStr[4 + corrimiento];
                if (splitStr[5 + corrimiento].startsWith("0")){ // UN NOMBRE UN APELLIDO
                    segundoApellido = " ";
                    primerNombre = splitStr[4 + corrimiento];
                    sexo = splitStr[5 + corrimiento].contains("M") ? "Masculino" : "Femenino";
                    rh = splitStr[5 + corrimiento].substring(splitStr[5 + corrimiento].length() - 2);
                    fechaNacimiento = splitStr[5 + corrimiento].substring(2, 10);
                } else if (splitStr[6 + corrimiento].startsWith("0")){ // DOS APELLIDOS UN NOMBRE
                    primerNombre = splitStr[5 + corrimiento];
                    segundoNombre = " ";
                    sexo = splitStr[6 + corrimiento].contains("M") ? "Masculino" : "Femenino";
                    rh = splitStr[6 + corrimiento].substring(splitStr[6 + corrimiento].length() - 2);
                    fechaNacimiento = splitStr[6 + corrimiento].substring(2, 10);
                } else { //DOS APELLIDOS DOS NOMBRES
                    primerNombre = splitStr[5 + corrimiento];
                    segundoNombre = splitStr[6 + corrimiento];
                    sexo = splitStr[7 + corrimiento].contains("M") ? "Masculino" : "Femenino";
                    rh = splitStr[7 + corrimiento].substring(splitStr[7 + corrimiento].length() - 2);
                    fechaNacimiento = splitStr[7 + corrimiento].substring(2, 10);
                }


            }
            /**
             * Se setea el objeto con los datos
             */
            String TAG = "parseDataCode";
            Log.d(TAG, "Nombre: " + primerNombre + segundoNombre) ;
            Log.d(TAG, "CEDULA: " + cedula) ;
            Log.d(TAG, "sexo: " + sexo) ;
            infoTarjeta.setPrimerNombre(primerNombre);
            infoTarjeta.setSegundoNombre(segundoNombre);
            infoTarjeta.setPrimerApellido(primerApellido);
            infoTarjeta.setSegundoApellido(segundoApellido);
            infoTarjeta.setCedula(cedula);
            infoTarjeta.setSexo(sexo);
            infoTarjeta.setFechaNacimiento(fechaNacimiento);
            infoTarjeta.setRh(rh);
            actualizarCampos(infoTarjeta);

        } else {
            Log.d("TAG", "No barcode capturado");
        }
    }

    private void actualizarCampos(InfoTarjeta infoTarjeta) {
        tvFirstName.setText(String.format("%s%s", infoTarjeta.getPrimerNombre(), infoTarjeta.getSegundoApellido()));
        tvLastName.setText(String.format("%s%s", infoTarjeta.getPrimerApellido(), infoTarjeta.getSegundoApellido()));
        tvDocumentID.setText(infoTarjeta.getCedula());
        tvGender.setText(infoTarjeta.getSexo());
        tvDate.setText(infoTarjeta.getFechaNacimiento());
        tvRH.setText(infoTarjeta.getRh());
    }
}