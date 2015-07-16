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
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import java.util.Calendar;


public class CardapioActivity extends Activity {
    private ListView listViewCardapio;
    private ArrayAdapter<String> adapterCardapio;
    private TextView textViewDataCardapio;
    private boolean conexao;
    private ImageButton btnAtualizarCardapio;
    private LinearLayout layoutBtnAtualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);
        btnAtualizarCardapio = (ImageButton)findViewById(R.id.btnAtualizarCardapio);
        int layoutAdapterCardapio = android.R.layout.simple_list_item_1;
        adapterCardapio = new ArrayAdapter<String>(this, layoutAdapterCardapio);
        layoutBtnAtualizar = (LinearLayout)findViewById(R.id.layoutBtnAtualizar);

        btnAtualizarCardapio
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
                                    Intent intent = new Intent(CardapioActivity.this,CardapioActivity.class);
                                    intent.putExtra("CONTEUDO",lista);
                                    intent.putExtra("CONEXAO",true);
                                    startActivityForResult(intent,0);
                                } catch (UnknownHostException exception) {
                                    Intent intent = new Intent(CardapioActivity.this,CardapioActivity.class);
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

        listViewCardapio = (ListView)findViewById(R.id.listViewCardapio);

        textViewDataCardapio = (TextView)findViewById(R.id.textViewDataCardapio);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null && bundle.containsKey("CONEXAO")){
            conexao = (boolean)bundle.getSerializable("CONEXAO");
            if(conexao){
                layoutBtnAtualizar.setVisibility(View.GONE);
                listViewCardapio.setVisibility(View.VISIBLE);
                if(bundle!=null && bundle.containsKey("CONTEUDO")){
                    ArrayList<String> lista = (ArrayList<String>)bundle.getSerializable("CONTEUDO");
                    for (String s: lista){
                        if(lista.indexOf(s)==0){
                            textViewDataCardapio.setText(s);
                        }
                        else{
                            adapterCardapio.add(s);
                        }
                    }
                    listViewCardapio.setAdapter(adapterCardapio);
                }
            }
            else{
                layoutBtnAtualizar.setVisibility(View.VISIBLE);
                listViewCardapio.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cardapio, menu);
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
}
