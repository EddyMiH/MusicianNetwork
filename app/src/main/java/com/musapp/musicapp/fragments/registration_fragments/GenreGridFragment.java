package com.musapp.musicapp.fragments.registration_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.GenreRecyclerViewAdapter;
import com.musapp.musicapp.fragments.registration_fragments.registration_fragment_transaction.RegistrationTransactionWrapper;
import com.musapp.musicapp.model.Genre;
import com.musapp.musicapp.preferences.AppPreferences;
import com.musapp.musicapp.utils.UIUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenreGridFragment extends Fragment {

    private GenreRecyclerViewAdapter genreRecyclerAdapter;
    private Button nextButton;
    private List<Genre> listOfGenres = new ArrayList<Genre>(
            Arrays.asList(new Genre("Rock", null)
                    ,new Genre("Blues", null)
                    ,new Genre("Classic", null)
                    ,new Genre("Classic Metal", null)
                    ,new Genre("Power Metal", null)
                    ,new Genre("Symphonic Metal", null)
                    ,new Genre("Electronic dance", null)
                    ,new Genre("Jazz", null)
                    ,new Genre("Pop", null)
                    ,new Genre("Folk", null)
                    ,new Genre("Hip Hop", null)
                    ,new Genre("Country", null)
                    ,new Genre("Rhythm & Blues", null)
                    ,new Genre("Heavy metal", null)
                    ,new Genre("Reggae", null)
                    ,new Genre("Punk Rock", null)
                    ,new Genre("Funk", null)
                    ,new Genre("Soul", null)
                    ,new Genre("Alternative Rock", null)
                    ,new Genre("Dance music", null)
                    ,new Genre("Techno", null)
                    ,new Genre("Rap", null)
                    ,new Genre("House", null)
                    ,new Genre("Singing/Vocal", null)
                    ,new Genre("Ambient", null)
                    ,new Genre("Instrumental", null)
                    ,new Genre("World music", null)
                    ,new Genre("Trance music", null)
                    ,new Genre("Progressive Rock", null)
                    ,new Genre("Latin Music", null)
                    ,new Genre("Indie Rock", null)
                    ,new Genre("Pop Rock", null)
                    ,new Genre("Hard Rock", null)
                    ,new Genre("Psychedelic music", null)
                    ,new Genre("Grunge", null)
                    ,new Genre("Electro", null)
                    ,new Genre("New Wave", null)
                    ,new Genre("Death metal", null)
                    ,new Genre("Noise", null)
                    ,new Genre("Industrial Rock", null)
                    ,new Genre("Acoustic", null)
                    ,new Genre("Jazz Fusion", null)
                    ,new Genre("African music", null))
    );

    private GenreRecyclerViewAdapter.OnItemSelectedListener mOnItemSelectedListener = new GenreRecyclerViewAdapter.OnItemSelectedListener() {
        @Override
        public void onItemSelected(Genre genre, View view) {
            //TODO Change background of textView and remember checked genre

            View temp = view.findViewById(R.id.text_genre_grid_fragment_genre_name);
            boolean flag = genre.isChecked();
            if (!flag){
                temp.setBackgroundColor(getResources().getColor(R.color.colorGenreChecked));
                Toast.makeText(getContext(),genre.getName() + " :checked " , Toast.LENGTH_SHORT).show();

            }
            else{
                Toast.makeText(getContext(),genre.getName() + " :Unchecked ", Toast.LENGTH_SHORT).show();

                temp.setBackgroundColor(getResources().getColor(R.color.colorWhiteTransparent));
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_grid_view_genre_fragment, container, false);

        RecyclerView view = rootView.findViewById(R.id.genre_recycler_view_genre_fragment);

        initRecyclerAdapter(view);
    //    nextButton = rootView.findViewById(R.id.action_fragment_grid_and_profession_next);
        //TODO very bad solution
        nextButton = UIUtils.getButtonFromView(getActivity().findViewById(R.id.layout_activity_start_content_main), R.id.action_fragment_grid_and_profession_next);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RegistrationTransactionWrapper.registerForNextFragment((int)nextButton.getTag());
            }
        });
        return rootView;
    }

    private void initRecyclerAdapter(RecyclerView view){
        genreRecyclerAdapter = new GenreRecyclerViewAdapter();
        genreRecyclerAdapter.setOnItemSelectedListener(mOnItemSelectedListener);
        genreRecyclerAdapter.setData(listOfGenres);
        view.setLayoutManager(new GridLayoutManager(getContext(), 2));
        view.setAdapter(genreRecyclerAdapter);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nextButton.setTag(R.integer.registration_fragment_grid_view_3);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String json = AppPreferences.getGenreState(getActivity().getBaseContext());
        if(json != null){
            Gson gson = new Gson();
            //String json = AppPreferences.getGenreState(getActivity().getBaseContext());
            Type type = new TypeToken<List<Genre>>() {}.getType();
            List<Genre> list = new ArrayList<>();
            list = gson.fromJson(json, type);

            listOfGenres = list;

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Gson gson = new Gson();
        String json = gson.toJson(genreRecyclerAdapter.getData());

        AppPreferences.saveGenreState(getActivity().getBaseContext(), json);
    }

    public GenreGridFragment() {
    }
    //TODO its not best solution to share one button from layout between two fragments, try something else
    public void setNextButton(Button button){
        nextButton = button;
    }


}

