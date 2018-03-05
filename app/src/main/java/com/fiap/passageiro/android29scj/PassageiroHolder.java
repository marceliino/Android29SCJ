package com.fiap.passageiro.android29scj;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by marcelo on 18/02/2018.
 */

public class PassageiroHolder extends RecyclerView.ViewHolder {

    public TextView nomePassageiro;
    public ImageButton btnEditar;
    public ImageButton btnExcluir;

    public PassageiroHolder(View itemView) {
        super(itemView);
        nomePassageiro = (TextView) itemView.findViewById(R.id.nomePassageiro);
        btnEditar = (ImageButton) itemView.findViewById(R.id.btnEdit);
        btnExcluir = (ImageButton) itemView.findViewById(R.id.btnDelete);
    }
}