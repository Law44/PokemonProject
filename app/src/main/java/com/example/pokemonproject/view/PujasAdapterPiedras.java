package com.example.pokemonproject.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pokemonproject.GlideApp;
import com.example.pokemonproject.R;
import com.example.pokemonproject.model.PiedrasEvoUser;
import com.example.pokemonproject.model.Username;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class PujasAdapterPiedras extends RecyclerView.Adapter<PujasAdapterPiedras.PiedraViewHolder> {

    private GameActivity context;
    ArrayList<PiedrasEvoUser> piedrasArrayList;
    String lastgame;
    String games;
    ArrayList<String> listGame;
    Username creator;
    MercadoFragment mercadoFragment;
    Map<Integer, Integer> totalPujas;
    ArrayList<Integer> pujas;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public PujasAdapterPiedras(GameActivity context, String lastgame, MercadoFragment mercadoFragment, Map<Integer, Integer> totalPujas, ArrayList<Integer> pujas){

        this.context = context;
        this.lastgame = lastgame;
        this.mercadoFragment = mercadoFragment;
        this.totalPujas = totalPujas;
        this.pujas = pujas;

    }

    @NonNull
    @Override
    public PiedraViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PiedraViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_piedra_mercado, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PiedraViewHolder viewHolder, final int i) {
        viewHolder.setPiedra(piedrasArrayList.get(i), i);
    }

    @Override
    public int getItemCount() {
        return  (piedrasArrayList != null ? piedrasArrayList.size() : 0);
    }

    public void setPiedrasPujas(ArrayList<PiedrasEvoUser> pokemonarray) {
        piedrasArrayList = pokemonarray;
    }

    class PiedraViewHolder extends RecyclerView.ViewHolder {
        private View view;

        PiedraViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }



        public void setPiedra(final PiedrasEvoUser model, final int position) {
            TextView tvName = view.findViewById(R.id.tvPiedra);
            TextView tvCostePiedra = view.findViewById(R.id.tvcostePiedra);
            ImageView fondo = view.findViewById(R.id.imgFondo);
            ImageView button = view.findViewById(R.id.imgButtonPagar);


            if (model.getCantidad() == 1) {
                tvName.setText(model.getCantidad() + " Piedra " + model.getName());
            }
            else if (model.getCantidad() > 1){
                tvName.setText(model.getCantidad() + " Piedras " + model.getName());
            }
            tvCostePiedra.setText(String.valueOf(model.getPrecio() * model.getCantidad()));


            GlideApp.with(view)
                    .load(R.drawable.pokemondollar)
                    .into((ImageView) view.findViewById(R.id.imgPokedolar));

            GlideApp.with(view)
                    .load(R.drawable.stone)
                    .into((ImageView) view.findViewById(R.id.piedra));

            GlideApp.with(view)
                    .load(model.getSprite())
                    .circleCrop()
                    .into((ImageView)  view.findViewById(R.id.piedraSprite));


            if (Integer.parseInt(String.valueOf(pujas.get(position))) > 0){
                fondo.setBackgroundColor(view.getResources().getColor(R.color.colorBuy));
            }
            else {
                fondo.setBackgroundColor(view.getResources().getColor(R.color.searchWhite));
            }

            GlideApp.with(view)
                    .load(R.drawable.icons8_pokeball_80)
                    .circleCrop()
                    .into((ImageView)view.findViewById(R.id.imgButtonPagar));

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ModalComprarPiedra(view.getContext(), model, mercadoFragment, lastgame, position, totalPujas, pujas, view);
                }
            });

        }
    }


}
