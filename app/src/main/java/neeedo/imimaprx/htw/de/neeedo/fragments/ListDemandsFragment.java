package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.List;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.demand.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.demand.Demands;
import neeedo.imimaprx.htw.de.neeedo.events.GetDemandListFinishedEvent;
import neeedo.imimaprx.htw.de.neeedo.events.ServerResponseEvent;
import neeedo.imimaprx.htw.de.neeedo.fragments.adapters.ListProductsArrayAdapter;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.DemandsModel;
import neeedo.imimaprx.htw.de.neeedo.rest.demand.GetDemandsAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;

public class ListDemandsFragment extends SuperFragment {
    private final ActiveUser activeUser = ActiveUser.getInstance();
    private ListView listView;
    private View view;
    private ProgressBar progressBar;
    private DemandsModel demandsModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.list_products_view, container, false);

        listView = (ListView) view.findViewById(R.id.listview);
        progressBar = (ProgressBar) view.findViewById(R.id.list_offers_progessbar);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        demandsModel = DemandsModel.getInstance();
        BaseAsyncTask.GetEntitiesMode listMode = BaseAsyncTask.GetEntitiesMode.GET_RANDOM;
        if (activeUser.hasActiveUser()) {
            listMode = BaseAsyncTask.GetEntitiesMode.GET_BY_USER;
            if (demandsModel.isUseLocalList()) {
                demandsModel.setUseLocalList(false);
                fillList(null);
                return;
            }
        }
        new GetDemandsAsyncTask(listMode, 100, 0).execute();
    }

    @Subscribe
    public void fillList(GetDemandListFinishedEvent e) {

        List<Demand> demandList = demandsModel.getDemands();

        if (e == null) {
            demandsModel.setLastDeletedEntityId("");
        }

        TextView tvEmpty = (TextView) view.findViewById(R.id.tvEmpty);

        if (!demandList.isEmpty()) {
            ListProductsArrayAdapter<Demand> adapter = new ListProductsArrayAdapter(getActivity(),
                    R.layout.list_products_item, demandList);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);


            listView.setClickable(true);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    Demand demand = (Demand) listView.getItemAtPosition(position);

                    FragmentManager fragmentManager = getFragmentManager();
                    Fragment fragment = new SingleDemandFragmentSwiper();

                    Bundle args = new Bundle();
                    args.putString("id", demand.getId());
                    fragment.setArguments(args);

                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.container, fragment)
                            .commit();
                }
            });

            tvEmpty.setVisibility(View.GONE);
        } else {
            tvEmpty.setText(getActivity().getString(R.string.empty_demands_message));
            tvEmpty.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);
    }
}
