package vn.com.misa.sticker.child_fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;

import vn.com.misa.sticker.R;

public class LoveFragment extends Fragment {
    private RecyclerView rvList;
    private ImageAdapter imageAdapter;
    private ArrayList<String> list;

    public static LoveFragment newInstance() {
        Bundle args = new Bundle();
        LoveFragment fragment = new LoveFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.child_fragment_sticker, container, false);
        rvList = view.findViewById(R.id.rvList);
        new GetImages().execute();
        imageAdapter = new ImageAdapter(getParentFragment(), list);
        rvList.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4,
                GridLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(gridLayoutManager);
        rvList.addItemDecoration(new EqualSpacingItemDecoration(16));
        rvList.setAdapter(imageAdapter);
        return view;
    }



    private class GetImages extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            if (list==null){
                list=new ArrayList<>();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            list.clear();
            String s;
            String[] sticker;
            try {
                s = "file:///android_asset/sticker/love/";
                sticker = getActivity().getAssets().list("sticker/love");
                assert sticker != null;
                for (String str : sticker) {
                    list.add(s + str);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            imageAdapter.notifyDataSetChanged();
        }
    }
}
