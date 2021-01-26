package com.course.capstone;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.course.capstone.adapter.BoardAdapter;
import com.course.capstone.models.Qna;
import com.course.capstone.models.QnaInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class Hot_hotFragment extends Fragment {
    ViewGroup rootView;
    private RecyclerView HotRecyclerView;
    private HotAdapter HotAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_hot_hot, container, false);
        HotRecyclerView = (RecyclerView) rootView.findViewById(R.id.h_recyclerview);
        HotRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        HotRecyclerView.setLayoutManager(layoutManager);
        HotRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
      //  refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        hotinfo();

        return rootView;
    }
    //서버 연결 후 어댑터 연결
    public void hotinfo() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ec2-13-59-15-254.us-east-2.compute.amazonaws.com:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        QnaInterface qnainterface = retrofit.create(QnaInterface.class);
        Call<List<Qna>> call = qnainterface.getAll();

        call.enqueue(new Callback<List<Qna>>() {
            @Override
            public void onResponse(Call<List<Qna>> call, Response<List<Qna>> response) {
                if (response.isSuccessful()) {
                    List<Qna> qna = response.body();

                    HotAdapter = new HotAdapter(getActivity(), qna);
                    for (int i = 0; i < qna.size(); i++) {
                        if (qna.get(i).getLikeCount() < 10) {
                            HotAdapter.removeItem(qna.get(i));

                        }
                    }
                    HotRecyclerView.setAdapter(HotAdapter);
                }
                else {
                    Log.d(TAG, "onResponse1: Something Wrong");
                }
            }

            @Override
            public void onFailure(Call<List<Qna>> call, Throwable t) {
                Toast.makeText(getContext(), "목록을 불러올 수 없습니다.", Toast.LENGTH_LONG).show();
                ;
                Log.d(TAG, "onFailure2: 게시물 목록 왜안나와");
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void onActivityResult(ActivityResultEvent activityResultEvent){
        onActivityResult(activityResultEvent.getRequestCode(), activityResultEvent.getResultCode(), activityResultEvent.getData());
    }



}





