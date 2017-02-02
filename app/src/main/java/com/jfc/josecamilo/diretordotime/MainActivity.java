package com.jfc.josecamilo.diretordotime;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jfc.josecamilo.diretordotime.graficos.GraficosArrecadacaoFragment;
import com.jfc.josecamilo.diretordotime.graficos.GraficosJogadoresFragment;
import com.jfc.josecamilo.diretordotime.graficos.GraficosJogosFragment;
import com.jfc.josecamilo.diretordotime.jogadores.IncJogadorFragment;
import com.jfc.josecamilo.diretordotime.jogadores.JogadoresFragment;
import com.jfc.josecamilo.diretordotime.jogos.IncJogosFragment;
import com.jfc.josecamilo.diretordotime.jogos.JogosFragment;
import com.jfc.josecamilo.diretordotime.pagamentos.PagamentosFragment;
import com.jfc.josecamilo.diretordotime.presenca.PresencaFragment;
import com.jfc.josecamilo.diretordotime.settings.SettingsFragment;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static TextView nomeTime;
    public static TextView descTime;
    public static ImageView imagemTime;
    public static LinearLayout fundoMenu;
    private static int cor_primaria;
    private static int cor_acentuada;
    public static Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    public static Context mainContext;
    public static int countTelas = 0;
    private FragmentTransaction firstFragmentTransaction;
    private static ArrayList<String> arrayToolbar = new ArrayList<String>();
    private boolean permissaoCedida = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*NOVO MODELO DE PERMISSOES > 6.0 API 23*/
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission()) {
                requestForSpecificPermission();
            }else{
                permissaoCedida = true;
            }
        }else{
            permissaoCedida = true;
        }

        super.onCreate(savedInstanceState);
        mainContext = this;
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*MENU DRAWER*/
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        /*MENU NAVIGATION*/
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*CARREGA AS PREFERENCIAS DO USUARIO*/
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String nome_time_menu = prefs.getString("nome_time_preference", "Escolha o nome do time nas configurações");
        String desc_time_menu = prefs.getString("desc_time_preference", "Escolha a descrição nas configurações");
        cor_primaria = prefs.getInt("cor_primaria_preferences", -16777216); //padrao preto
        cor_acentuada = prefs.getInt("cor_acentuada_preferences", -28672); // padrao laranja
        String fotoTime = prefs.getString("foto_time_preference","defaultString");

        View header = navigationView.getHeaderView(0);
        fundoMenu = (LinearLayout) header.findViewById(R.id.background_menu);
        nomeTime = (TextView) header.findViewById(R.id.nome_time_menu);
        descTime = (TextView) header.findViewById(R.id.desc_time_menu);
        imagemTime = (ImageView) header.findViewById(R.id.imagem_time_menu);

        nomeTime.setText(nome_time_menu);
        descTime.setText(desc_time_menu);

        setFundoMenu(cor_primaria,cor_acentuada);

        if (!fotoTime.equals("defaultString") && permissaoCedida){
            try {
                Uri imageUri = Uri.parse(fotoTime);
                setImagemTime(imageUri);
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // Inicia Home com a tela de Dashboards
        this.setToolbar("Diretor do Time");

        GraficosJogadoresFragment graficosJogadoresFragment = new GraficosJogadoresFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, graficosJogadoresFragment);
        fragmentTransaction.commit();

        /*GraficosFragment graficosFragment = new GraficosFragment();
        firstFragmentTransaction = getSupportFragmentManager().beginTransaction();
        firstFragmentTransaction.replace(R.id.fragment_container, graficosFragment);
        firstFragmentTransaction.commit();*/

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private static long back_pressed;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (MainActivity.countTelas == 0) {
                if (back_pressed + 2000 > System.currentTimeMillis()) {
                    super.onBackPressed();
                } else {
                    Toast.makeText(getBaseContext(), "Pressione novamente para sair", Toast.LENGTH_SHORT).show();
                    back_pressed = System.currentTimeMillis();
                }
            } else {
                super.onBackPressed();
            }

            if (MainActivity.countTelas > 0) {
                MainActivity.countTelas--;
            }

            setToolbar();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
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
            setToolbar("Configurações");

            SettingsFragment settingsFragment = new SettingsFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, settingsFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            MainActivity.countTelas++;

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_grafico) {

            setToolbar("Gráfico Jogadores");

            GraficosJogadoresFragment graficosJogadoresFragment = new GraficosJogadoresFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, graficosJogadoresFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            MainActivity.countTelas++;

        } else if (id == R.id.nav2_grafico) {

            setToolbar("Próximos Eventos");

            GraficosJogosFragment graficosJogosFragment = new GraficosJogosFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, graficosJogosFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            MainActivity.countTelas++;

        } else if (id == R.id.nav3_grafico) {

            setToolbar("Gráfico Arrecadações");

            GraficosArrecadacaoFragment graficosArrecadacaoFragment = new GraficosArrecadacaoFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, graficosArrecadacaoFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            MainActivity.countTelas++;

        } else if (id == R.id.nav_jogos) {

            //toolbar.setTitle("Jogos");
            setToolbar("Caléndario");

            JogosFragment jogosFragment = new JogosFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, jogosFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            MainActivity.countTelas++;

        } else if (id == R.id.nav2_jogos) {

            //toolbar.setTitle("Incluir Jogos");
            setToolbar("Incluir Jogos");

            IncJogosFragment incjogosFragment = new IncJogosFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, incjogosFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            MainActivity.countTelas++;

        } else if (id == R.id.nav3_jogos) {

            //toolbar.setTitle("Presença");
            setToolbar("Presença");

            PresencaFragment presencaFragment = new PresencaFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, presencaFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            MainActivity.countTelas++;

        } else if (id == R.id.nav_jogadores) {

            //toolbar.setTitle("Jogadores");
            setToolbar("Jogadores");

            JogadoresFragment jogadoresFragment = new JogadoresFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, jogadoresFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            MainActivity.countTelas++;

        } else if (id == R.id.nav2_jogadores) {

            //toolbar.setTitle("Incluir Jogador");
            setToolbar("Incluir Jogador");

            IncJogadorFragment incJogadorFragment = new IncJogadorFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, incJogadorFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            MainActivity.countTelas++;

        } else if (id == R.id.nav_pagamentos) {

            //toolbar.setTitle("Pagamentos");
            setToolbar("Pagamentos");

            PagamentosFragment pagamentosFragment = new PagamentosFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, pagamentosFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            MainActivity.countTelas++;

        } else if (id == R.id.nav_sobre) {
            AlertDialog alertDialog = new AlertDialog.Builder(mainContext).create();
            alertDialog.setTitle("Diretor do Time");
            alertDialog.setMessage("Dicas:\n" +
                    "-Um evento com financeiro ativado(pagamentos diversos), aparecerá na tela de Pagamentos;\n" +
                    "-Um evento com financeiro desativado(jogos ou evento de presença), aparecerá na tela de Presença;\n" +
                    "-Segure e arraste o nome do jogador para a coluna correspondente nas telas de Presença e Pagamento;\n" +
                    "-Para que um jogador ou evento não apareça mais nas listas e nos gráficos, edite o registro e oculte pela caixa de seleção Ocultar.\n\n" +
                    "Registre:\n" +
                    "-Colaboradores do time;\n" +
                    "-Eventos do time;\n" +
                    "-Confirmação de presença;\n" +
                    "-Pagamento dos eventos financeiros.\n\n" +
                    "Tenha tudo em mãos:\n" +
                    "-Calendário do time;\n" +
                    "-Gráfico de jogadores por posição;\n" +
                    "-Lista de próximos eventos;\n" +
                    "-Gráfico de arrecadação por mês;\n" +
                    "-Controle de presença;\n" +
                    "-Controle de pagamento.\n\n" +
                    "Reporte um problema: nando.f3@hotmail.com\n\n" +
                    "Desenvolvido por José Fernando Camilo");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private GoogleApiClient client;


    /*METODOS ACESSADOS POR OUTRAS TELAS*/
    public static void setToolbar(String titulo) {
        arrayToolbar.add(titulo);
        toolbar.setTitle(titulo);
    }

    public static void setToolbar() {
        if (arrayToolbar.size() > 1) {
            arrayToolbar.remove(arrayToolbar.size() - 1);
            toolbar.setTitle(arrayToolbar.get(arrayToolbar.size() - 1));
        }
    }

    public static void setNomeTime(String nome){
        nomeTime.setText(nome);
    }

    public static void setDescTime(String desc){
        descTime.setText(desc);
    }

    public static void setImagemTime(Uri imageUri){

        InputStream is = null;
        try{
            is = mainContext.getContentResolver().openInputStream(imageUri);
        }catch (FileNotFoundException e){

        }

        if (is == null){
            AlertDialog alertDialog = new AlertDialog.Builder(mainContext).create();
            alertDialog.setMessage("Não foi possível carregar esta foto. Por favor tente outra foto ou verifique possíveis caracteres especiais no nome do arquivo.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }else{
            imagemTime.setImageURI(imageUri);
        }

    }

    public static void setFundoMenu(int cor1, int cor2){
        if (cor1 != 0){
            cor_primaria = cor1;
            toolbar.setBackgroundColor(cor_primaria);
        }
        if (cor2 != 0){
            cor_acentuada = cor2;
        }

        GradientDrawable gd = new GradientDrawable( GradientDrawable.Orientation.TL_BR,
                                                    new int[] {cor_primaria,cor_acentuada});
        fundoMenu.setBackgroundDrawable(gd);
    }

    /*NOVO MODELO DE PERMISSOES > 6.0 API 23*/
    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                    permissaoCedida = true;
                } else {
                    //not granted
                    permissaoCedida = false;
                    AlertDialog alertDialog = new AlertDialog.Builder(mainContext).create();
                    alertDialog.setMessage("Não será possível carregar a foto do seu time. Por favor conceda a permissão de acesso aos seus arquivos.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
