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

public class Eduzone extends Fragment {

    private ArrayList<EduzoneModel> list;
    private Context context;

    private RecyclerView rvWomanEduzone;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_eduzone, container, false);
        context = v.getContext();

        rvWomanEduzone = v.findViewById(R.id.rvWomansEduzone);
        rvWomanEduzone.setHasFixedSize(true);
        list = new ArrayList<>();

//        if(savedInstanceState == null){
            list.addAll(EduzoneData.getListData());
            showRecyclerList();
//            mode = R.id.action_list;
//        } else{
//            String setTitle = savedInstanceState.getString(STATE_TITLE);
//            ArrayList<President> stateList = savedInstanceState.getParcelableArrayList(STATE_LIST);
//            int stateMode = savedInstanceState.getInt(STATE_MODE);
//            setActionBarTitle(setTitle);
//            list.addAll(stateList);
//            setMode(stateMode);
//        }

        return v;
    }

    private void showRecyclerList() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.HORIZONTAL);
        rvWomanEduzone.setLayoutManager(layoutManager);
        EduzoneAdapter eduzoneAdapter = new EduzoneAdapter(context);
        eduzoneAdapter.setListEduzone(list);
        rvWomanEduzone.setAdapter(eduzoneAdapter);

//        ItemClickSupport.addTo(rvCategory).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
//            @Override
//            public void onItemCLicked(RecyclerView mRecyclerView, int adapterPosition, View v) {
//                showSelectedPresident(list.get(adapterPosition));
//            }
//        });
    }
}
