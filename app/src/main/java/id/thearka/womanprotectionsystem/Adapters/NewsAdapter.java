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

import de.hdodenhof.circleimageview.CircleImageView;
import id.thearka.womanprotectionsystem.Models.NewsModel;
import id.thearka.womanprotectionsystem.R;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<NewsModel> listNews;

    public NewsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_news, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        NewsModel data = getListNews().get(i);
        viewHolder.nama.setText(data.getNama());
        viewHolder.textStatus.setText(data.getStatus());
        Glide.with(context)
                .load(data.getProfil())
                .apply(new RequestOptions().override(50, 50))
                .into(viewHolder.profil);
        if(!data.getFoto().equals("")){
            viewHolder.imageStatus.setVisibility(View.VISIBLE);
            viewHolder.imageStatus.setAdjustViewBounds(true);
            viewHolder.imageStatus.setMaxWidth(300);
            viewHolder.imageStatus.setMaxHeight(300);
            Glide.with(context)
                    .load(data.getFoto())
                    .apply(new RequestOptions().override(300, 300))
                    .into(viewHolder.imageStatus);
        } else{
            viewHolder.imageStatus.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listNews.size();
    }

    public void setListNews(ArrayList<NewsModel> listNews) {
        this.listNews = listNews;
    }
    ArrayList<NewsModel> getListNews() {
        return listNews;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profil;
        TextView nama, textStatus;
        ImageView imageStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profil = itemView.findViewById(R.id.civProfileStatus);
            nama = itemView.findViewById(R.id.tvNamaStatus);
            textStatus = itemView.findViewById(R.id.tvMessageStatus);
            imageStatus = itemView.findViewById(R.id.ivImageStatus);
        }
    }
}
