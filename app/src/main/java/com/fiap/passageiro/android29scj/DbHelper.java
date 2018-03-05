package com.fiap.passageiro.android29scj;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by marcelo on 18/02/2018.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "29SCJ.db";
    private static final int DATABASE_VERSION = 1;
    private final String CREATE_TABLE = "CREATE TABLE passageiro (ID INTEGER PRIMARY KEY AUTOINCREMENT, Nome TEXT NOT NULL, Sexo TEXT, UF TEXT NOT NULL, primeiraclasse INTEGER NOT NULL);";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}