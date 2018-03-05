package com.fiap.passageiro.android29scj;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fiap.passageiro.android29scj.model.Passageiro;

import java.util.List;


public class PassageiroAdapter extends RecyclerView.Adapter<PassageiroHolder> {

    private final List<Passageiro> passageiros;

    public PassageiroAdapter(List<Passageiro> passageiros) {
        this.passageiros = passageiros;
    }

    public void atualizarPassageiro(Passageiro passageiro){
        passageiros.set(passageiros.indexOf(passageiro), passageiro);
        notifyItemChanged(passageiros.indexOf(passageiro));
    }

    public void adicionarPassageiro(Passageiro passageiro){
        passageiros.add(passageiro);
        notifyItemInserted(getItemCount());
    }

    public void removerPassageiro(Passageiro passageiro){
        int position = passageiros.indexOf(passageiro);
        passageiros.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public PassageiroHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PassageiroHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista, parent, false));
    }

    @Override
    public void onBindViewHolder(PassageiroHolder holder, int position) {
        holder.nomePassageiro.setText(passageiros.get(position).getNome());
        final Passageiro passageiro = passageiros.get(position);
        holder.btnExcluir.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = v;
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Confirmação")
                        .setMessage("Tem certeza que deseja excluir este passageiro?")
                        .setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PassageiroDAO dao = new PassageiroDAO(view.getContext());
                                boolean sucesso = dao.excluir(passageiro.getId());
                                if(sucesso) {
                                    removerPassageiro(passageiro);
                                    Snackbar.make(view, "Excluiu!", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }else{
                                    Snackbar.make(view, "Erro ao excluir o passageiro!", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .create()
                        .show();
            }
        });

        holder.btnEditar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity(v);
                Intent intent = activity.getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("passageiro", passageiro);
                activity.finish();
                activity.startActivity(intent);
            }
        });
    }

    private Activity getActivity(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return passageiros != null ? passageiros.size() : 0;
    }
}
