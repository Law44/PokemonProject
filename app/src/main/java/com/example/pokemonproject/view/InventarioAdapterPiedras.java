package com.example.pokemonproject.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemonproject.GlideApp;
import com.example.pokemonproject.R;
import com.example.pokemonproject.model.PiedrasEvoUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class InventarioAdapterPiedras extends RecyclerView.Adapter<InventarioAdapterPiedras.PiedraViewHolder> {

    ArrayList<PiedrasEvoUser> piedrasArrayList;
    String lastgame;
    MercadoFragment mercadoFragment;
    Map<Integer, Integer> totalPujas;
    ArrayList<Integer> pujas;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public InventarioAdapterPiedras(){


    }

    @NonNull
    @Override
    public PiedraViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PiedraViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_piedra_inventario, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PiedraViewHolder viewHolder, final int i) {
        viewHolder.setPiedra(piedrasArrayList.get(i), i);
    }

    @Override
    public int getItemCount() {
        return  (piedrasArrayList != null ? piedrasArrayList.size() : 0);
    }

    public void setPiedrasInventario(ArrayList<PiedrasEvoUser> inventarioarray) {
        piedrasArrayList = inventarioarray;
    }

    class PiedraViewHolder extends RecyclerView.ViewHolder {
        private View view;

        PiedraViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }



        public void setPiedra(final PiedrasEvoUser model, final int position) {
            TextView tvName = view.findViewById(R.id.tvPiedra);
            ImageView fondo = view.findViewById(R.id.imgFondo);
            TextView tvCantidad = view.findViewById(R.id.tvPiedraCantidad);


            if (model.getCantidad() == 1) {
                tvName.setText("Piedra " + model.getName());
            }
            else if (model.getCantidad() > 1){
                tvName.setText("Piedras " + model.getName());
            }
            tvCantidad.setText(""+model.getCantidad());


            GlideApp.with(view)
                    .load(R.drawable.stonemega)
                    .into((ImageView) view.findViewById(R.id.piedra));

            GlideApp.with(view)
                    .load(model.getSprite())
                    .circleCrop()
                    .into((ImageView)  view.findViewById(R.id.piedraSprite));


        }
    }


}
