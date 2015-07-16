package com.anonymousru.thebestdevelopers.anonymousru;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by Eduardo on 7/15/2015.
 */
public class BuscaDadosDoSiteDoRU extends Activity implements Runnable {
    private Context context;
    BuscaDadosDoSiteDoRU(Context context){
        this.context = context;
    }
    @Override
    public void run() {
        String siteUrl = "http://www.restaurantesaborfamilia.com.br/cardapio-ru-pucrs/";
        try {
            int adapterLayout = android.R.layout.simple_list_item_1;
            ArrayList<String> adapter = new ArrayList<String>();
            Connection connection = Jsoup.connect(siteUrl);
            Document document = connection.get();
            Elements cardapio = document.select("p");
            Elements comidas = cardapio.select("p");
            for(Element e : comidas) {
                try {
                    if(e.text().toString().equalsIgnoreCase("DEVIDO A INDISPONIBILIDADE DE INSUMOS NOSSO CARDÁPIO ESTÁ SUJEITO A ALTERAÇÕES SEM AVISO PRÉVIO.")){
                        break;
                    }
                    else {
                        adapter.add(e.text().toString());
                    }
                }catch (Exception exception){

                }
            }
            Gerenciar.salvar(context,adapter,"CONTEUDO-RU");
            Intent intent = new Intent(context,CardapioActivity.class);
            startActivityForResult(intent, 0);
        } catch (Exception exception){
            //adapterCardapio.add("Exception no terceiro catch do método buscaDadosDoSiteDoRU - "+exception.getClass());
        }
    }
}
