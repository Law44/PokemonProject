package com.example.pokemonproject.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pokemonproject.GlideApp;
import com.example.pokemonproject.R;
import com.example.pokemonproject.model.Combate;
import com.example.pokemonproject.model.Equipo;

import java.util.ArrayList;
import java.util.List;

public class CombatesAdapter extends RecyclerView.Adapter<CombatesAdapter.CombateViewHolder> {

    List<Combate> combateList;
    int counterWinRight = 0;
    int counterWinLeft = 0;
    int nullleft = 0;
    int nullRight = 0;

    public CombatesAdapter(List<Combate> combateList) {
        this.combateList = combateList;
    }

    @NonNull
    @Override
    public CombateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CombatesAdapter.CombateViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_combate, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CombateViewHolder holder, int i) {
        vaciarDatos(holder);
        Equipo equipoIzquierda = combateList.get(i).getEquipo1();
        Equipo equipoDerecha = combateList.get(i).getEquipo2();

        holder.tvNameTeamIzq.setText(equipoIzquierda.getUsername());
        holder.tvNameTeamDerecha.setText(equipoDerecha.getUsername());
        GlideApp.with(holder.itemView.getContext()).load(equipoIzquierda.getImguser()).circleCrop().into(holder.imgEquipoIzq);
        GlideApp.with(holder.itemView.getContext()).load(equipoDerecha.getImguser()).circleCrop().into(holder.imgEquipoDer);

        loadEquipoIzquierda(holder,equipoIzquierda,combateList.get(i).getFinalizado());
        loadEquipoDerecha(holder,equipoDerecha,combateList.get(i).getFinalizado());


        if (!combateList.get(i).getFinalizado().equals("no")) {

            if (counterWinLeft > counterWinRight) {
                holder.resultadoLeft.setText("GANADOR");
                holder.resultadoLeft.setTextColor(holder.itemView.getResources().getColor(R.color.colorWin));
                holder.resultadoRight.setText("PERDEDOR");
                holder.resultadoRight.setTextColor(holder.itemView.getResources().getColor(R.color.colorLose));
            } else if (counterWinRight == counterWinLeft) {
                holder.resultadoLeft.setText("EMPATE");
                holder.resultadoLeft.setTextColor(holder.itemView.getResources().getColor(R.color.colorBuy));
                holder.resultadoRight.setText("EMPATE");
                holder.resultadoRight.setTextColor(holder.itemView.getResources().getColor(R.color.colorBuy));
            } else {
                holder.resultadoLeft.setText("PERDEDOR");
                holder.resultadoLeft.setTextColor(holder.itemView.getResources().getColor(R.color.colorLose));
                holder.resultadoRight.setText("GANADOR");
                holder.resultadoRight.setTextColor(holder.itemView.getResources().getColor(R.color.colorWin));
            }


            if (nullleft == 6 && nullRight == 6) {
                holder.resultadoLeft.setText("EMPATE");
                holder.resultadoLeft.setTextColor(holder.itemView.getResources().getColor(R.color.colorBuy));
                holder.resultadoRight.setText("EMPATE");
                holder.resultadoRight.setTextColor(holder.itemView.getResources().getColor(R.color.colorBuy));
            }

            counterWinLeft = 0;
            counterWinRight = 0;
            nullRight = 0;
            nullleft = 0;
        }
        else {
            holder.resultadoLeft.setText("ESPERANDO");
            holder.resultadoRight.setText("ESPERANDO");
        }


    }

    private void vaciarDatos(CombateViewHolder holder) {
        for (int i = 0; i < holder.winnerRight.size(); i++) {
            GlideApp.with(holder.itemView.getContext()).load(0).into(holder.pokesImgIzq.get(i));
            GlideApp.with(holder.itemView.getContext()).load(0).into(holder.pokesImgDer.get(i));

            GlideApp.with(holder.itemView.getContext()).load(0).into(holder.winnerLeft.get(i));
            GlideApp.with(holder.itemView.getContext()).load(0).into(holder.winnerRight.get(i));

            holder.pokesTvIzq.get(i).setText("");
            holder.pokesImgIzq.get(i).setBackgroundColor(0);

            holder.pokesTvDer.get(i).setText("");
            holder.pokesImgDer.get(i).setBackgroundColor(0);
        }
    }

    private void loadEquipoDerecha(CombateViewHolder holder, Equipo equipoDerecha, String finalizado) {

        for (int j = 0; j<equipoDerecha.getAlineacion().getLista().size();j++){
            if (equipoDerecha.getAlineacion().getLista().get(j)!=null) {
                GlideApp.with(holder.itemView.getContext()).load(equipoDerecha.getAlineacion().getLista().get(j).getSprites().front_default).into(holder.pokesImgDer.get(j));
                holder.pokesTvDer.get(j).setText(equipoDerecha.getAlineacion().getLista().get(j).getName());
                GlideApp.with(holder.itemView.getContext()).load(R.drawable.pokeball_combat_bien).into(holder.winnerRight.get(j) );
                if (!finalizado.equals("no")) {
                    if (equipoDerecha.getAlineacion().getLista().get(j).getLife() > 0) {
//                        holder.pokesTvDer.get(j).setBackgroundColor(holder.itemView.getResources().getColor(R.color.colorWin));
                        holder.pokesImgDer.get(j).setBackgroundColor(holder.itemView.getResources().getColor(R.color.colorWin));
                        counterWinRight++;
                    } else {
//                        holder.pokesTvDer.get(j).setBackgroundColor(holder.itemView.getResources().getColor(R.color.colorLose));
                        holder.pokesImgDer.get(j).setBackgroundColor(holder.itemView.getResources().getColor(R.color.colorLose));
                        GlideApp.with(holder.itemView.getContext()).load(R.drawable.pokeball_combat_derrota).into(holder.winnerRight.get(j) );
                    }
                }
            }
            else {
                nullRight++;
            }
        }
    }

    private void loadEquipoIzquierda(CombateViewHolder holder, Equipo equipoIzquierda, String finalizado) {

        for (int j = 0; j<equipoIzquierda.getAlineacion().getLista().size();j++){
            if (equipoIzquierda.getAlineacion().getLista().get(j)!=null) {
                GlideApp.with(holder.itemView.getContext()).load(equipoIzquierda.getAlineacion().getLista().get(j).getSprites().front_default).into(holder.pokesImgIzq.get(j));
                holder.pokesTvIzq.get(j).setText(equipoIzquierda.getAlineacion().getLista().get(j).getName());
                GlideApp.with(holder.itemView.getContext()).load(R.drawable.pokeball_combat_bien).into(holder.winnerLeft.get(j) );
                if (!finalizado.equals("no")) {
                    if (equipoIzquierda.getAlineacion().getLista().get(j).getLife() > 0) {
//                        holder.pokesTvIzq.get(j).setBackgroundColor(holder.itemView.getResources().getColor(R.color.colorWin));
                        holder.pokesImgIzq.get(j).setBackgroundColor(holder.itemView.getResources().getColor(R.color.colorWin));
                        counterWinLeft++;
                    } else {
//                        holder.pokesTvIzq.get(j).setBackgroundColor(holder.itemView.getResources().getColor(R.color.colorLose));
                        holder.pokesImgIzq.get(j).setBackgroundColor(holder.itemView.getResources().getColor(R.color.colorLose));
                        GlideApp.with(holder.itemView.getContext()).load(R.drawable.pokeball_combat_derrota).into(holder.winnerLeft.get(j) );
                    }
                }
            }
            else{
                nullleft++;
            }
        }
    }

    @Override
    public int getItemCount() {
        return (combateList != null ? combateList.size() : 0);
    }

    class CombateViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameTeamIzq;
        TextView tvNameTeamDerecha;
        ImageView imgEquipoIzq;
        ImageView imgEquipoDer;

        TextView tvPokeName1Izquierda;
        TextView tvPokeName2Izquierda;
        TextView tvPokeName3Izquierda;
        TextView tvPokeName4Izquierda;
        TextView tvPokeName5Izquierda;
        TextView tvPokeName6Izquierda;

        TextView tvPokeName1Derecha;
        TextView tvPokeName2Derecha;
        TextView tvPokeName3Derecha;
        TextView tvPokeName4Derecha;
        TextView tvPokeName5Derecha;
        TextView tvPokeName6Derecha;

        ImageView imgPokeImg1Izq;
        ImageView imgPokeImg2Izq;
        ImageView imgPokeImg3Izq;
        ImageView imgPokeImg4Izq;
        ImageView imgPokeImg5Izq;
        ImageView imgPokeImg6Izq;

        ImageView imgPokeImg1Der;
        ImageView imgPokeImg2Der;
        ImageView imgPokeImg3Der;
        ImageView imgPokeImg4Der;
        ImageView imgPokeImg5Der;
        ImageView imgPokeImg6Der;

        ImageView imgCombWin1Der;
        ImageView imgCombWin2Der;
        ImageView imgCombWin3Der;
        ImageView imgCombWin4Der;
        ImageView imgCombWin5Der;
        ImageView imgCombWin6Der;

        ImageView imgCombWin1Izq;
        ImageView imgCombWin2Izq;
        ImageView imgCombWin3Izq;
        ImageView imgCombWin4Izq;
        ImageView imgCombWin5Izq;
        ImageView imgCombWin6Izq;



        List<ImageView> pokesImgDer = new ArrayList<>();
        List<ImageView> pokesImgIzq = new ArrayList<>();

        List<TextView> pokesTvIzq = new ArrayList<>();
        List<TextView> pokesTvDer = new ArrayList<>();

        List<ImageView> winnerLeft = new ArrayList<>();
        List<ImageView> winnerRight = new ArrayList<>();

        TextView resultadoRight;
        TextView resultadoLeft;



        public CombateViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNameTeamIzq = itemView.findViewById(R.id.tvCombateNameEquipo);
            tvNameTeamDerecha = itemView.findViewById(R.id.tvCombateNameEquipoDerecha);
            imgEquipoIzq = itemView.findViewById(R.id.imgCombateIzq);
            imgEquipoDer = itemView.findViewById(R.id.imgCombateDer);

            tvPokeName1Izquierda = itemView.findViewById(R.id.tvPokeName1Left);
            tvPokeName2Izquierda = itemView.findViewById(R.id.tvPokeName2Left);
            tvPokeName3Izquierda = itemView.findViewById(R.id.tvPokeName3Left);
            tvPokeName4Izquierda = itemView.findViewById(R.id.tvPokeName4Left);
            tvPokeName5Izquierda = itemView.findViewById(R.id.tvPokeName5Left);
            tvPokeName6Izquierda = itemView.findViewById(R.id.tvPokeName6Left);

            tvPokeName1Derecha = itemView.findViewById(R.id.tvPokeName1Right);
            tvPokeName2Derecha = itemView.findViewById(R.id.tvPokeName2Right);
            tvPokeName3Derecha = itemView.findViewById(R.id.tvPokeName3Right);
            tvPokeName4Derecha = itemView.findViewById(R.id.tvPokeName4Right);
            tvPokeName5Derecha = itemView.findViewById(R.id.tvPokeName5Right);
            tvPokeName6Derecha = itemView.findViewById(R.id.tvPokeName6Right);

            imgPokeImg1Izq = itemView.findViewById(R.id.imgPoke1TeamLeft);
            imgPokeImg2Izq = itemView.findViewById(R.id.imgPoke2TeamLeft);
            imgPokeImg3Izq = itemView.findViewById(R.id.imgPoke3TeamLeft);
            imgPokeImg4Izq = itemView.findViewById(R.id.imgPoke4TeamLeft);
            imgPokeImg5Izq = itemView.findViewById(R.id.imgPoke5TeamLeft);
            imgPokeImg6Izq = itemView.findViewById(R.id.imgPoke6TeamLeft);

            imgPokeImg1Der = itemView.findViewById(R.id.imgPoke1TeamRight);
            imgPokeImg2Der = itemView.findViewById(R.id.imgPoke2TeamRight);
            imgPokeImg3Der = itemView.findViewById(R.id.imgPoke3TeamRight);
            imgPokeImg4Der = itemView.findViewById(R.id.imgPoke4TeamRight);
            imgPokeImg5Der = itemView.findViewById(R.id.imgPoke5TeamRight);
            imgPokeImg6Der = itemView.findViewById(R.id.imgPoke6TeamRight);

            resultadoLeft = itemView.findViewById(R.id.ResultadoLeft);
            resultadoRight = itemView.findViewById(R.id.ResultadoRight);

            imgCombWin1Der = itemView.findViewById(R.id.imgCombVicDer1);
            imgCombWin2Der = itemView.findViewById(R.id.imgCombVicDer2);
            imgCombWin3Der = itemView.findViewById(R.id.imgCombVicDer3);
            imgCombWin4Der = itemView.findViewById(R.id.imgCombVicDer4);
            imgCombWin5Der = itemView.findViewById(R.id.imgCombVicDer5);
            imgCombWin6Der = itemView.findViewById(R.id.imgCombVicDer6);

            imgCombWin1Izq = itemView.findViewById(R.id.imgCombVicIzq1);
            imgCombWin2Izq = itemView.findViewById(R.id.imgCombVicIzq2);
            imgCombWin3Izq = itemView.findViewById(R.id.imgCombVicIzq3);
            imgCombWin4Izq = itemView.findViewById(R.id.imgCombVicIzq4);
            imgCombWin5Izq = itemView.findViewById(R.id.imgCombVicIzq5);
            imgCombWin6Izq = itemView.findViewById(R.id.imgCombVicIzq6);

            winnerLeft.add(imgCombWin1Izq);
            winnerLeft.add(imgCombWin2Izq);
            winnerLeft.add(imgCombWin3Izq);
            winnerLeft.add(imgCombWin4Izq);
            winnerLeft.add(imgCombWin5Izq);
            winnerLeft.add(imgCombWin6Izq);

            winnerRight.add(imgCombWin1Der);
            winnerRight.add(imgCombWin2Der);
            winnerRight.add(imgCombWin3Der);
            winnerRight.add(imgCombWin4Der);
            winnerRight.add(imgCombWin5Der);
            winnerRight.add(imgCombWin6Der);

            pokesImgDer.add(imgPokeImg1Der);
            pokesImgDer.add(imgPokeImg2Der);
            pokesImgDer.add(imgPokeImg3Der);
            pokesImgDer.add(imgPokeImg4Der);
            pokesImgDer.add(imgPokeImg5Der);
            pokesImgDer.add(imgPokeImg6Der);

            pokesImgIzq.add(imgPokeImg1Izq);
            pokesImgIzq.add(imgPokeImg2Izq);
            pokesImgIzq.add(imgPokeImg3Izq);
            pokesImgIzq.add(imgPokeImg4Izq);
            pokesImgIzq.add(imgPokeImg5Izq);
            pokesImgIzq.add(imgPokeImg6Izq);

            pokesTvIzq.add(tvPokeName1Izquierda);
            pokesTvIzq.add(tvPokeName2Izquierda);
            pokesTvIzq.add(tvPokeName3Izquierda);
            pokesTvIzq.add(tvPokeName4Izquierda);
            pokesTvIzq.add(tvPokeName5Izquierda);
            pokesTvIzq.add(tvPokeName6Izquierda);

            pokesTvDer.add(tvPokeName1Derecha);
            pokesTvDer.add(tvPokeName2Derecha);
            pokesTvDer.add(tvPokeName3Derecha);
            pokesTvDer.add(tvPokeName4Derecha);
            pokesTvDer.add(tvPokeName5Derecha);
            pokesTvDer.add(tvPokeName6Derecha);




        }
    }
}
