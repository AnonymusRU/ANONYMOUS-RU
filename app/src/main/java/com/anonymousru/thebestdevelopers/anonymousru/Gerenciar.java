package com.anonymousru.thebestdevelopers.anonymousru;

import android.app.AlertDialog;
import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Eduardo on 7/15/2015.
 */
public class Gerenciar {
    public static void salvar(Context context, Object object, String local){
        try{
            File file =context.getFileStreamPath(local);
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.close();
            fos.close();
        }catch (Exception e){
            AlertDialog.Builder dlg = new AlertDialog.Builder(context);
            dlg.setMessage("ERRO:" + e.getMessage());
            dlg.setNeutralButton("OK", null);
            dlg.show();
        }
    }

    public static Object buscar(Context context, String local){
        try{
            File file =context.getFileStreamPath(local);
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object retorno = ois.readObject();
            fis.close();
            ois.close();
            return retorno;
        }catch (Exception e){
            return null;
        }
    }
}
