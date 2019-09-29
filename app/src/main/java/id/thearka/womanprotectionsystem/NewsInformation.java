package id.thearka.womanprotectionsystem;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import id.thearka.womanprotectionsystem.Adapters.NewsAdapter;
import id.thearka.womanprotectionsystem.Models.NewsModel;

public class NewsInformation extends Fragment {

    private ArrayList<NewsModel> listNews;
    private Context context;

    private RecyclerView rvNews;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_news_information, container, false);

        context = view.getContext();

        rvNews = view.findViewById(R.id.rvNews);
        listNews = new ArrayList<>();
        listNews.addAll(NewsData.getListData());
        newsAdapter();

        return view;
    }

    private void newsAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvNews.setLayoutManager(layoutManager);
        NewsAdapter newsAdapter = new NewsAdapter(context);
        newsAdapter.setListNews(listNews);
        rvNews.setAdapter(newsAdapter);
    }
}
