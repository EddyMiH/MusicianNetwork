package com.musapp.musicapp.fragments.registration_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.musapp.musicapp.R;
import com.musapp.musicapp.adapters.GridViewAdapter;
import com.musapp.musicapp.model.Genre;
import com.musapp.musicapp.preferences.AppPreferences;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenreGridFragment extends Fragment {

    private GridView genreGridView;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_grid_view_genre_fragment, container, false);
        genreGridView = rootView.findViewById(R.id.genre_grid_view);
        nextButton =rootView.findViewById(R.id.btn_next);

        initGridView();
        genreGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO Change background of textView and remember checked genre
                View temp = view.findViewById(R.id.genre_label_text_view);
                boolean flag = gridAdapter.getItem(position).isChecked();
                gridAdapter.setItemCheck(position,!flag);
                if (!flag){
                    temp.setBackgroundColor(getResources().getColor(R.color.colorGenreChecked));
                }
                else{
                    temp.setBackgroundColor(getResources().getColor(R.color.colorWhiteTransparent));
                }

                Toast.makeText(getContext(), listOfGenres.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO save checked genres and change fragment
                //--ProfessionAndBioFragment fragment = new ProfessionAndBioFragment();
//                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right
//                        , R.anim.enter_from_right, R.anim.exit_to_right).replace(R.id.container,fragment).addToBackStack(null).commit();
                //--getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left
                       //-- , R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.container,fragment).addToBackStack(null).commit();
            }
        });
        return rootView;
    }
    private GridViewAdapter gridAdapter;

    private void initGridView(){
        gridAdapter = new GridViewAdapter(getContext(), listOfGenres);
        genreGridView.setAdapter(gridAdapter);
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
        String json = gson.toJson(gridAdapter.getData());

        AppPreferences.saveGenreState(getActivity().getBaseContext(), json);
    }

    public GenreGridFragment() {
    }
}

