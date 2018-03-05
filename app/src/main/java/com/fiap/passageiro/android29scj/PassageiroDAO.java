package com.fiap.passageiro.android29scj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.fiap.passageiro.android29scj.model.Passageiro;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcelo on 18/02/2018.
 */

public class PassageiroDAO {

    private final String TABLE_PASSAGEIRO = "passageiro";
    private DbGateway gw;

    public PassageiroDAO(Context ctx){
        gw = DbGateway.getInstance(ctx);
    }

    public List<Passageiro> getPassageiros(){
        List<Passageiro> passageiros = new ArrayList<>();
        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM passageiro", null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("ID"));
            String nome = cursor.getString(cursor.getColumnIndex("Nome"));
            String sexo = cursor.getString(cursor.getColumnIndex("Sexo"));
            String uf = cursor.getString(cursor.getColumnIndex("UF"));
            boolean vip = cursor.getInt(cursor.getColumnIndex("primeiraclasse")) > 0;
            passageiros.add(new Passageiro(id, nome, sexo, uf, vip));
        }
        cursor.close();
        return passageiros;
    }

    public Passageiro getPassageiro(){
        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM passageiro ORDER BY ID DESC", null);
        if(cursor.moveToFirst()){
            int id = cursor.getInt(cursor.getColumnIndex("ID"));
            String nome = cursor.getString(cursor.getColumnIndex("Nome"));
            String sexo = cursor.getString(cursor.getColumnIndex("Sexo"));
            String uf = cursor.getString(cursor.getColumnIndex("UF"));
            boolean vip = cursor.getInt(cursor.getColumnIndex("primeiraclasse")) > 0;
            cursor.close();
            return new Passageiro(id, nome, sexo, uf, vip);
        }

        return null;
    }

    public boolean salvar(String nome, String sexo, String uf, boolean primeiraClasse){
        return salvar(0, nome, sexo, uf, primeiraClasse);
    }

    public boolean salvar(int id, String nome, String sexo, String uf, boolean primeiraClasse){
        ContentValues cv = new ContentValues();
        cv.put("Nome", nome);
        cv.put("Sexo", sexo);
        cv.put("UF", uf);
        cv.put("primeiraClasse", primeiraClasse ? 1 : 0);
        if(id > 0)
            return gw.getDatabase().update(TABLE_PASSAGEIRO, cv, "ID=?", new String[]{ id + "" }) > 0;
        else
            return gw.getDatabase().insert(TABLE_PASSAGEIRO, null, cv) > 0;
    }

    public boolean excluir(int id){
        return gw.getDatabase().delete(TABLE_PASSAGEIRO, "ID=?", new String[]{ id + "" }) > 0;
    }
}