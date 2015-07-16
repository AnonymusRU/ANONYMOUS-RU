package com.anonymousru.thebestdevelopers.anonymousru;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.UnknownHostException;
import java.util.ArrayList;

//Se quiser que apareça o menu usar extends ActionBarActivity
public class MainActivity extends Activity {
    private ListView listViewCardapio;
    private ArrayAdapter<String> adapterCardapio;
    private TextView textViewDataCardapio;
    private Button btnCardapioMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCardapioMain = (Button)findViewById(R.id.btnCardapioMain);

        btnCardapioMain
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Thread thread = new Thread(new Thread(new Runnable(){
                            public void run() {
                                String siteUrl = "http://www.restaurantesaborfamilia.com.br/cardapio-ru-pucrs/";
                                try {
                                    Connection connection = Jsoup.connect(siteUrl);
                                    Document document = connection.get();
                                    Elements cardapio = document.select("p");
                                    Elements comidas = cardapio.select("p");
                                    ArrayList<String> lista = new ArrayList<String>();
                                    for(Element e : comidas) {
                                        try {
                                            if(e.text().toString().equalsIgnoreCase("DEVIDO A INDISPONIBILIDADE DE INSUMOS NOSSO CARDÁPIO ESTÁ SUJEITO A ALTERAÇÕES SEM AVISO PRÉVIO.")){
                                                break;
                                            }
                                            else {
                                                lista.add(e.text().toString());
                                            }
                                        }catch (Exception exception){
                                            //Não é possivel adicionar elementos no adapterCardapio aqui! Gera exception.
                                            //Não é possivel usar Toast aqui! O app para.
                                        }
                                    }
                                    Intent intent = new Intent(MainActivity.this,CardapioActivity.class);
                                    intent.putExtra("CONTEUDO",lista);
                                    intent.putExtra("CONEXAO",true);
                                    startActivityForResult(intent,0);
                                } catch (UnknownHostException exception) {
                                    Intent intent = new Intent(MainActivity.this,CardapioActivity.class);
                                    intent.putExtra("CONEXAO",false);
                                    startActivityForResult(intent,0);

                                }
                                catch (Exception exception){
                                    adapterCardapio.add("Exception no terceiro catch do método buscaDadosDoSiteDoRU - "+exception.getClass());
                                }
                            }
                        }));
                        thread.start();
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.anonymousru.thebestdevelopers.anonymousru.R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.anonymousru.thebestdevelopers.anonymousru.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }
}