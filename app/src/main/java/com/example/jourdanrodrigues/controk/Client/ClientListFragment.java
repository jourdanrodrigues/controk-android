package com.example.jourdanrodrigues.controk.Client;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jourdanrodrigues.controk.BaseFragment;
import com.example.jourdanrodrigues.controk.MainActivity;
import com.example.jourdanrodrigues.controk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClientListFragment extends BaseFragment {
    public static final int ARG_MENU_POSITION = 0;
    private RecyclerView mClientList;
    private ClientAdapter.Listener mClientClickListener;
    private SwipeRefreshLayout mSwipeRefresh;

    public ClientListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);
        assert view != null;

        view.findViewById(R.id.fab_create_client).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).updateFragment(new ClientCreationFragment(), "ClientListFragment");
            }
        });

        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_client);
        mSwipeRefresh.setRefreshing(true);

        mClientList = (RecyclerView) view.findViewById(R.id.recycler_view_client);
        mClientClickListener = new ClientAdapter.Listener() {
            @Override
            public void onClick(Client client) {
                // Implementation for client
                Snackbar.make(view, "Clicked \"" + client.getName() + "\"", Snackbar.LENGTH_SHORT).show();
            }
        };

        setSwipeRefresh(view);
        populateClients(view);

        return view;
    }

    @Override
    public int getFragment() {
        return R.layout.fragment_client_list;
    }

    @Override
    public int getTitle() {
        return R.string.client_title;
    }

    private void setSwipeRefresh(final View view) {
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateClients(view);
            }
        });
    }

    private void populateClients(final View view) {
        final List<Client> clients = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        String url = getResources().getString(R.string.web_service_api) + "clients/";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray clientsList = new JSONArray(response);
                        for (int i = 0; i < clientsList.length(); i++) {
                            JSONObject client = (JSONObject) clientsList.get(i);
                            clients.add(new Client(
                                client.getString("name"),
                                client.getString("email"),
                                client.getString("cpf")
                            ));
                        }

                        mClientList.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mClientList.setAdapter(new ClientAdapter(clients, mClientClickListener));
                        mSwipeRefresh.setRefreshing(false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Snackbar.make(view, "Couldn't retrieve clients", Snackbar.LENGTH_LONG).show();
                }
            });
        queue.add(stringRequest);
    }

    interface VolleyCallback {
        void onSuccess(List<Client> Clients);
    }
}
