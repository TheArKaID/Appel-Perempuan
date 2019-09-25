package id.thearka.womanprotectionsystem.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import id.thearka.womanprotectionsystem.Models.EduzoneModel;
import id.thearka.womanprotectionsystem.R;

public class EduzoneWomanAdapter extends RecyclerView.Adapter<EduzoneWomanAdapter.ViewHolder>{

    private Context context;
    private ArrayList<EduzoneModel> listEduzone;

    public EduzoneWomanAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_eduzone, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        EduzoneModel view = getListEduzone().get(i);
        String judul = view.getJudul();
        judul = judul.substring(0, 25);
        judul = judul+"...";
        viewHolder.tvJudul.setText(judul);
        Glide.with(context)
                .load(view.getImage())
                .apply(new RequestOptions().override(150, 150))
                .into(viewHolder.tvImage);
    }

    @Override
    public int getItemCount() {
        return listEduzone.size();
    }

    public ArrayList<EduzoneModel> getListEduzone() {
        return listEduzone;
    }

    public void setListEduzone(ArrayList<EduzoneModel> listEduzone) {
        this.listEduzone = listEduzone;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJudul;
        ImageView tvImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJudul = itemView.findViewById(R.id.tvNewsTitle);
            tvImage = itemView.findViewById(R.id.ivNewsImage);
        }
    }
}