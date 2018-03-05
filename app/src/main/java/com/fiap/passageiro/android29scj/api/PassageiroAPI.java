package com.fiap.passageiro.android29scj.api;


import com.fiap.passageiro.android29scj.model.Passageiro;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PassageiroAPI {

    @GET("/maquina")
    Call<List<Passageiro>> findAll();


}
