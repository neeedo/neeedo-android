package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.favorites.Favorite;
import neeedo.imimaprx.htw.de.neeedo.events.UserFavoritesLoadedEvent;
import neeedo.imimaprx.htw.de.neeedo.fragments.adapters.ListProductsArrayAdapter;
import neeedo.imimaprx.htw.de.neeedo.models.FavoritesModel;
import neeedo.imimaprx.htw.de.neeedo.rest.favorites.GetFavoritesByIDAsyncTask;

public class ListFavoritesFragment extends ListFragment {

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (activeUser.hasActiveUser()) {
            new GetFavoritesByIDAsyncTask(activeUser.getUserId()).execute();
        }
    }

    @Subscribe
    public void fillList(UserFavoritesLoadedEvent e) {
        ArrayList<Favorite> favoriteList = FavoritesModel.getInstance().getFavorites();

        TextView tvEmpty = (TextView) view.findViewById(R.id.tvEmpty);

        if (favoriteList.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            tvEmpty.setText(getActivity().getString(R.string.favorites_view_no_favorites_found));
            tvEmpty.setVisibility(View.VISIBLE);
            return;
        } else {
            ListProductsArrayAdapter<Favorite> favoritArrayAdapter = new ListProductsArrayAdapter<>(getActivity(), R.layout.list_products_item, favoriteList);
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

            listView.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
        }
        progressBar.setVisibility(View.GONE);
    }
}
