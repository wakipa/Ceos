package sicae.oise.ceos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class Formulaire extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner_input_commune, spinner_input_nom_poste, spinner_input_ref_wapiti, spinner_type_inter, spinner_type_antenne, spinner_emplacement_antenne, spinner_type_raccord, spinner_emplacement_concentrateur;
    Button btnValider;
    EditText input_date, input_nom_agent, input_num_serie, input_ip_sim, input_signal_in, input_signal_ext;
    String dateTime, FormulaireTypeAtnFin, FormulaireRaccordFin, FormulaireEmpAtnFin, FormulaireEmpConcentFin, formulaireDate, formulaireNomAgent, formulaireCommune, formulaireSignExt, formulaireNomPoste, formulaireSignIn, formulaireRef, formulaireEmpAtn, formulaireTypeAtn, formulaireInter, formulaireSerie, formulaireRaccord, formulaireIpSim, formulaireEmpConcent;
    private static final int PERMISSION_REQUEST_CODE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire);

        spinner_input_commune = findViewById(R.id.input_commune);
        spinner_input_nom_poste = findViewById(R.id.input_nom_poste);
        spinner_input_ref_wapiti = findViewById(R.id.input_ref_wapiti);
        spinner_type_inter = findViewById(R.id.spinner_type_inter);
        spinner_type_antenne = findViewById(R.id.spinner_type_antenne);
        spinner_emplacement_antenne = findViewById(R.id.spinner_emplacement_antenne);
        spinner_type_raccord = findViewById(R.id.spinner_type_raccord);
        spinner_input_commune.setOnItemSelectedListener(this);
        spinner_input_nom_poste.setOnItemSelectedListener(this);
        spinner_input_ref_wapiti.setOnItemSelectedListener(this);
        spinner_emplacement_concentrateur = findViewById(R.id.spinner_emplacement_concentrateur);

        btnValider = findViewById(R.id.btnValider);
        input_date = findViewById(R.id.input_date);
        input_nom_agent = findViewById(R.id.input_nom_agent);
        input_num_serie = findViewById(R.id.input_num_serie);
        input_ip_sim = findViewById(R.id.input_ip_sim);
        input_signal_in = findViewById(R.id.input_signal_in);
        input_signal_ext = findViewById(R.id.input_signal_ext);

        /**
         * Datetime automatique du jour sur le champ date.
         */
        Calendar calendrier = Calendar.getInstance();
        SimpleDateFormat dateDuJour = new SimpleDateFormat("yyyy-MM-dd");
        dateTime = dateDuJour.format(calendrier.getTime());
        input_date.setText(dateTime);


        /**
         * Initialisation des spinner parents synchro enfants.
         */
        spinner_input_commune.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                BddCeos db = new BddCeos(getApplicationContext());

                List<String> labelsPoste = db.getFindPosteByCommune(spinner_input_commune.getSelectedItem().toString());
                ArrayAdapter<String> dataAdapterPoste = new ArrayAdapter<String>(Formulaire.this, android.R.layout.simple_spinner_item, labelsPoste);
                spinner_input_nom_poste.setAdapter(dataAdapterPoste);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        spinner_input_nom_poste.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                BddCeos db = new BddCeos(getApplicationContext());

                List<String> labelsRef = db.getFindRefWapitiByPoste(spinner_input_nom_poste.getSelectedItem().toString());
                ArrayAdapter<String> dataAdapterRef = new ArrayAdapter<String>(Formulaire.this, android.R.layout.simple_spinner_item, labelsRef);
                spinner_input_ref_wapiti.setAdapter(dataAdapterRef);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });


        btnValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formulaireDate = input_date.getText().toString().trim();
                formulaireNomAgent = input_nom_agent.getText().toString().trim();
                formulaireCommune = spinner_input_commune.getSelectedItem().toString().trim();
                formulaireNomPoste = spinner_input_nom_poste.getSelectedItem().toString().trim();
                formulaireRef = spinner_input_ref_wapiti.getSelectedItem().toString().trim();
                formulaireInter = spinner_type_inter.getSelectedItem().toString().trim();
                formulaireSerie = input_num_serie.getText().toString().trim();
                formulaireIpSim = input_ip_sim.getText().toString().trim();
                formulaireRaccord = String.valueOf(spinner_type_raccord.getSelectedItemId());
                formulaireTypeAtn = String.valueOf(spinner_type_antenne.getSelectedItemId());
                formulaireEmpAtn = String.valueOf(spinner_emplacement_antenne.getSelectedItemId());
                formulaireSignIn = input_signal_in.getText().toString().trim();
                formulaireSignExt = input_signal_ext.getText().toString().trim();
                formulaireEmpConcent = String.valueOf(spinner_emplacement_concentrateur.getSelectedItemId());


                /**
                 * Retraduction du résultat en sortie de chaque spinner pour conversion format BDD "Wapiti" sous forme d'ID
                 */
                if (formulaireRaccord.equals("0")){
                    FormulaireRaccordFin = "541";
                }
                if (formulaireRaccord.equals("1")){
                    FormulaireRaccordFin = "542";
                }
                if (formulaireRaccord.equals("2")){
                    FormulaireRaccordFin = "543";
                }
                if (formulaireRaccord.equals("3")){
                    FormulaireRaccordFin = "544";
                }
                if (formulaireRaccord.equals("4")){
                    FormulaireRaccordFin = "545";
                }
                if (formulaireRaccord.equals("5")){
                    FormulaireRaccordFin = "546";
                }


                if (formulaireTypeAtn.equals("0")){
                    FormulaireTypeAtnFin = "518";
                }
                if (formulaireTypeAtn.equals("1")){
                    FormulaireTypeAtnFin = "519";
                }
                if (formulaireTypeAtn.equals("2")){
                    FormulaireTypeAtnFin = "520";
                }


                if (formulaireEmpAtn.equals("0")){
                    FormulaireEmpAtnFin = "521";
                }
                if (formulaireEmpAtn.equals("1")){
                    FormulaireEmpAtnFin = "522";
                }
                if (formulaireEmpAtn.equals("2")){
                    FormulaireEmpAtnFin = "523";
                }
                if (formulaireEmpAtn.equals("3")){
                    FormulaireEmpAtnFin = "524";
                }


                if (formulaireEmpConcent.equals("0")){
                    FormulaireEmpConcentFin = "515";
                }
                if (formulaireEmpConcent.equals("1")){
                    FormulaireEmpConcentFin = "516";
                }
                if (formulaireEmpConcent.equals("2")){
                    FormulaireEmpConcentFin = "517";
                }
                if (formulaireEmpConcent.equals("3")){
                    FormulaireEmpConcentFin = "547";
                }
                if (formulaireEmpConcent.equals("4")){
                    FormulaireEmpConcentFin = "548";
                }
                if (formulaireEmpConcent.equals("5")){
                    FormulaireEmpConcentFin = "549";
                }


                /**
                 * Vérification du remplissage du formulaire
                 */
                if (formulaireDate.isEmpty()){
                    Toast.makeText(Formulaire.this, "Veuillez renseigner le champ 'date' ", Toast.LENGTH_SHORT) .show();
                }
                if (formulaireNomAgent.isEmpty()){
                    Toast.makeText(Formulaire.this, "Veuillez renseigner le champ 'Nom agent' ", Toast.LENGTH_SHORT) .show();
                }
                if (formulaireCommune.isEmpty()){
                    Toast.makeText(Formulaire.this, "Veuillez renseigner le champ 'Commune' ", Toast.LENGTH_SHORT) .show();
                }
                if (formulaireNomPoste.isEmpty()){
                    Toast.makeText(Formulaire.this, "Veuillez renseigner le champ 'Nom poste' ", Toast.LENGTH_SHORT) .show();
                }
                if (formulaireRef.isEmpty()){
                    Toast.makeText(Formulaire.this, "Veuillez renseigner le champ 'Référence wapiti' ", Toast.LENGTH_SHORT) .show();
                }
                if (formulaireInter.isEmpty()){
                    Toast.makeText(Formulaire.this, "Veuillez renseigner le champ 'Type intervention' ", Toast.LENGTH_SHORT) .show();
                }
                if (formulaireSerie.equals(14)){
                    Toast.makeText(Formulaire.this, "Veuillez renseigner le champ 'Numéro de série' ", Toast.LENGTH_SHORT) .show();
                }
                if (formulaireIpSim.isEmpty()){
                    Toast.makeText(Formulaire.this, "Veuillez renseigner le champ 'Ip carte SIM' ", Toast.LENGTH_SHORT) .show();
                }
                if (formulaireRaccord.isEmpty()){
                    Toast.makeText(Formulaire.this, "Veuillez renseigner le champ 'Type de raccordement' ", Toast.LENGTH_SHORT) .show();
                }
                if (formulaireTypeAtn.isEmpty()){
                    Toast.makeText(Formulaire.this, "Veuillez renseigner le champ 'Type antenne' ", Toast.LENGTH_SHORT) .show();
                }
                if (formulaireEmpAtn.isEmpty()){
                    Toast.makeText(Formulaire.this, "Veuillez renseigner le champ 'Emplacement antenne' ", Toast.LENGTH_SHORT) .show();
                }
                if (formulaireEmpConcent.isEmpty()){
                    Toast.makeText(Formulaire.this, "Veuillez renseigner le champ 'Emplacement concentrateur' ", Toast.LENGTH_SHORT) .show();
                }
                else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_DENIED) {
                            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
                        }
                        else {
                            saveToTxtFile(formulaireDate + "," +
                                    formulaireNomAgent + "," +
                                    formulaireCommune + "," +
                                    formulaireNomPoste + "," +
                                    formulaireRef + "," +
                                    formulaireInter + "," +
                                    formulaireSerie + "," +
                                    formulaireIpSim + "," +
                                    FormulaireRaccordFin + "," +
                                    FormulaireTypeAtnFin + "," +
                                    FormulaireEmpAtnFin + "," +
                                    formulaireSignIn + "," +
                                    formulaireSignExt+ "," +
                                    FormulaireEmpConcentFin);
                        }
                    }
                    else {
                        saveToTxtFile(formulaireDate + "," +
                                formulaireNomAgent + "," +
                                formulaireCommune + "," +
                                formulaireNomPoste + "," +
                                formulaireRef + "," +
                                formulaireInter + "," +
                                formulaireSerie + "," +
                                formulaireIpSim + "," +
                                FormulaireRaccordFin + "," +
                                FormulaireTypeAtnFin + "," +
                                FormulaireEmpAtnFin + "," +
                                formulaireSignIn + "," +
                                formulaireSignExt + "," +
                                FormulaireEmpConcentFin);
                    }
                }
                    Intent intent = new Intent(Formulaire.this, MainActivity.class);
                    startActivity(intent);
            }
        });

        // Loading spinner data from database
        loadSpinnerData();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveToTxtFile(formulaireDate + "," +
                            formulaireNomAgent + "," +
                            formulaireCommune + "," +
                            formulaireNomPoste + "," +
                            formulaireRef + "," +
                            formulaireInter + "," +
                            formulaireSerie + "," +
                            formulaireIpSim + "," +
                            FormulaireRaccordFin + "," +
                            FormulaireTypeAtnFin + "," +
                            FormulaireEmpAtnFin + "," +
                            formulaireSignIn + "," +
                            formulaireSignExt + "," +
                            FormulaireEmpConcentFin);
                }  else {
                    Toast.makeText(this, "Droit de sauvegarde requis", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void saveToTxtFile(String formulaire) {
        try {
            String Date = new SimpleDateFormat("dd-MM-yyyy_HH-mm", Locale.getDefault()).format(new Date()) + ".csv";
            String FileName = "Ceos_" + Date;
            File CreateFile = new File(this.getExternalFilesDir(null), FileName);
            if (!CreateFile.exists())
                CreateFile.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(CreateFile, true /*append*/));
            writer.write(formulaire);
            writer.close();
            MediaScannerConnection.scanFile(this,
                    new String[]{CreateFile.toString()},
                    null,
                    null);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
                Toast.makeText(this, "Fichier sauvegardé", Toast.LENGTH_SHORT).show();
    }


    /**
     * Function to load the Commune spinner data from SQLite database
     */
    private void loadSpinnerData() {
        BddCeos db = new BddCeos(getApplicationContext());
        List<String> labelsCommune = db.getAllCommune();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterCommune = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labelsCommune);

        // attaching data adapter to spinner
        spinner_input_commune.setAdapter(dataAdapterCommune);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}