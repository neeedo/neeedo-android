package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.favorites.Favorite;
import neeedo.imimaprx.htw.de.neeedo.entities.favorites.Favorites;
import neeedo.imimaprx.htw.de.neeedo.events.UserFavoritesLoadedEvent;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.FavoritesModel;
import neeedo.imimaprx.htw.de.neeedo.rest.favorites.GetFavoritesByIDAsyncTask;

public class ListFavoritesFragment extends SuperFragment {

    private final ActiveUser activeUser = ActiveUser.getInstance();
    private ListView listView;
    private View view;
    private EditText text;
    private List<Favorite> favoriteList;
    private ArrayAdapter<Favorite> favoritArrayAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.list_favorites_view, container, false);

        listView = (ListView) view.findViewById(R.id.favorites_view_listView);

        text = (EditText) view.findViewById(R.id.favorites_view_edit_text);

        favoriteList = new ArrayList<>();

        favoritArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, favoriteList);
        listView.setAdapter(favoritArrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Favorite favorite = (Favorite) listView.getItemAtPosition(position);

                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = new SingleOfferFragment();

                Bundle args = new Bundle();
                args.putString("id", favorite.getId());
                fragment.setArguments(args);

                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.container, fragment)
                        .commit();
            }
        });

        if (activeUser.hasActiveUser()) {
            new GetFavoritesByIDAsyncTask(activeUser.getUserId()).execute();
        }

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Subscribe
    public void fillList(UserFavoritesLoadedEvent e) {

        favoriteList = FavoritesModel.getInstance().getFavorites();

        if (favoriteList.isEmpty()) {
            listView.setVisibility(View.INVISIBLE);
            text.setVisibility(View.VISIBLE);
            return;
        } else {
            listView.setVisibility(View.VISIBLE);
            text.setVisibility(View.INVISIBLE);
        }

        favoritArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, favoriteList);
        listView.setAdapter(favoritArrayAdapter);

    }
}
