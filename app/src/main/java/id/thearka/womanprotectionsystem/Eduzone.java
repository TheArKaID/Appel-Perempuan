package id.thearka.womanprotectionsystem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import id.thearka.womanprotectionsystem.Adapters.EduzoneWomanAdapter;
import id.thearka.womanprotectionsystem.Models.EduzoneModel;
import id.thearka.womanprotectionsystem.Utils.MyWebView;

public class Eduzone extends Fragment {

    private ArrayList<EduzoneModel> listWoman;
    private ArrayList<EduzoneModel> listKid;
    private ArrayList<EduzoneModel> listHuman;
    private Context context;

    private RecyclerView rvWomanEduzone;
    private RecyclerView rvKidEduzone;
    private RecyclerView rvHumanEduzone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_eduzone, container, false);
        context = v.getContext();

        rvWomanEduzone = v.findViewById(R.id.rvWomansEduzone);
        rvHumanEduzone = v.findViewById(R.id.rvHumanEduzone);
        rvKidEduzone = v.findViewById(R.id.rvKidsEduzone);

        rvWomanEduzone.setHasFixedSize(true);
        rvHumanEduzone.setHasFixedSize(true);
        rvKidEduzone.setHasFixedSize(true);

        listWoman = new ArrayList<>();
        listHuman = new ArrayList<>();
        listKid = new ArrayList<>();

        listWoman.addAll(EduzoneWomanData.getListData());
        recyclerWoman();

        listKid.addAll(EduzoneKidData.getListData());
        recyclerKid();

        listHuman.addAll(EduzoneHumanData.getListData());
        recyclerHuman();

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ItemClickSupport.addTo(rvWomanEduzone).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemCLicked(RecyclerView mRecyclerView, int adapterPosition, View v) {
                letWebView(listWoman.get(adapterPosition).getUrl());
            }
        });

        ItemClickSupport.addTo(rvKidEduzone).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemCLicked(RecyclerView mRecyclerView, int adapterPosition, View v) {
                letWebView(listKid.get(adapterPosition).getUrl());
            }
        });

        ItemClickSupport.addTo(rvHumanEduzone).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemCLicked(RecyclerView mRecyclerView, int adapterPosition, View v) {
                letWebView(listHuman.get(adapterPosition).getUrl());
            }
        });
    }

    private void letWebView(String url) {
        Intent intent = new Intent(getContext(), MyWebView.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    private void recyclerWoman() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvWomanEduzone.setLayoutManager(layoutManager);
        EduzoneWomanAdapter eduzoneAdapter = new EduzoneWomanAdapter(context);
        eduzoneAdapter.setListEduzone(listWoman);
        rvWomanEduzone.setAdapter(eduzoneAdapter);
    }

    private void recyclerKid() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvKidEduzone.setLayoutManager(layoutManager);
        EduzoneWomanAdapter eduzoneAdapter = new EduzoneWomanAdapter(context);
        eduzoneAdapter.setListEduzone(listKid);
        rvKidEduzone.setAdapter(eduzoneAdapter);
    }

    private void recyclerHuman() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvHumanEduzone.setLayoutManager(layoutManager);
        EduzoneWomanAdapter eduzoneAdapter = new EduzoneWomanAdapter(context);
        eduzoneAdapter.setListEduzone(listHuman);
        rvHumanEduzone.setAdapter(eduzoneAdapter);
    }
}
