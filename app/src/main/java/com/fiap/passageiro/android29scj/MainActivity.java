package com.fiap.passageiro.android29scj;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.fiap.passageiro.android29scj.api.PassageiroAPI;
import com.fiap.passageiro.android29scj.model.Passageiro;
import com.fiap.passageiro.android29scj.model.Passageiros;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    Passageiro passageiroEditado = null;

    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if(intent.hasExtra("passageiro")){
            findViewById(R.id.includemain).setVisibility(View.INVISIBLE);
            findViewById(R.id.includecadastro).setVisibility(View.VISIBLE);
            findViewById(R.id.fab).setVisibility(View.INVISIBLE);
            passageiroEditado = (Passageiro) intent.getSerializableExtra("passageiro");
            EditText txtNome = (EditText)findViewById(R.id.txtNome);
            Spinner spnEstado = (Spinner)findViewById(R.id.spnEstado);
            CheckBox chkPrimeiraClasse = (CheckBox)findViewById(R.id.chkPrimeiraClasse);

            txtNome.setText(passageiroEditado.getNome());
            chkPrimeiraClasse.setChecked(passageiroEditado.getPrimeiraClasse());
            spnEstado.setSelection(getIndex(spnEstado, passageiroEditado.getUf()));
            if(passageiroEditado.getSexo() != null){
                RadioButton rb;
                if(passageiroEditado.getSexo().equals("M"))
                    rb = (RadioButton)findViewById(R.id.rbMasculino);
                else
                    rb = (RadioButton)findViewById(R.id.rbFeminino);
                rb.setChecked(true);
            }
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.includemain).setVisibility(View.INVISIBLE);
                findViewById(R.id.includecadastro).setVisibility(View.VISIBLE);
                findViewById(R.id.fab).setVisibility(View.INVISIBLE);
            }
        });

        Button btnCancelar = (Button)findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.includemain).setVisibility(View.VISIBLE);
                findViewById(R.id.includecadastro).setVisibility(View.INVISIBLE);
                findViewById(R.id.fab).setVisibility(View.VISIBLE);
            }
        });

        Button btnSalvar = (Button)findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText txtNome = (EditText)findViewById(R.id.txtNome);
                Spinner spnEstado = (Spinner)findViewById(R.id.spnEstado);
                RadioGroup rgSexo = (RadioGroup)findViewById(R.id.rgSexo);
                CheckBox chkPrimeiraClasse = (CheckBox)findViewById(R.id.chkPrimeiraClasse);

                String nome = txtNome.getText().toString();
                String uf = spnEstado.getSelectedItem().toString();
                boolean primeiraClasse = chkPrimeiraClasse.isChecked();
                String sexo = rgSexo.getCheckedRadioButtonId() == R.id.rbMasculino ? "M" : "F";

                PassageiroDAO dao = new PassageiroDAO(getBaseContext());
                boolean sucesso;
                if(passageiroEditado != null)
                    sucesso = dao.salvar(passageiroEditado.getId(), nome, sexo, uf, primeiraClasse);
                else
                    sucesso = dao.salvar(nome, sexo, uf, primeiraClasse);

                if(sucesso) {
                    Passageiro passageiro = dao.getPassageiro();
                    if(passageiroEditado != null)
                        adapter.atualizarPassageiro(passageiro);
                    else
                        adapter.adicionarPassageiro(passageiro);

                    passageiroEditado = null;
                    txtNome.setText("");
                    rgSexo.setSelected(false);
                    spnEstado.setSelection(0);
                    chkPrimeiraClasse.setChecked(false);

                    Snackbar.make(view, "Passageiro salvo com sucesso!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    findViewById(R.id.includemain).setVisibility(View.VISIBLE);
                    findViewById(R.id.includecadastro).setVisibility(View.INVISIBLE);
                    findViewById(R.id.fab).setVisibility(View.VISIBLE);
                }else{
                    Snackbar.make(view, "Erro ao salvar, consulte os logs!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        configurarRecycler();

    }

    RecyclerView recyclerView;
    private PassageiroAdapter adapter;

    private void configurarRecycler() {

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        PassageiroDAO dao = new PassageiroDAO(this);
        adapter = new PassageiroAdapter(dao.getPassageiros());
        //adapter = new PassageiroAdapter(this.getPassageiros2());
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    public List<Passageiro> getPassageiros2() {

        PassageiroAPI api = getRetrofit().create(PassageiroAPI.class);

        List<Passageiro> listPax = (List<Passageiro>) api.findAll();

        return listPax;
    }

    /*
    private void carregarDados() {

        PassageiroAPI api = getRetrofit().create(PassageiroAPI.class);


        api.findAll()
                .enqueue(new Callback<Passageiros>() {
                    @Override
                    public void onResponse(Call<Passageiros> call,
                                           Response<Passageiros> response) {

                        if(response.body() != null) {
                            if(response.body().getItems().size() > 0) {
                                recyclerView.setAdapter(new PassageiroAdapter(response.body().getItems()));
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Deu ruim", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<Passageiros> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),
                                "Error", Toast.LENGTH_SHORT).show();

                    }
                });
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://marceliino.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}