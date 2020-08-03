package com.cs446.group18.timetracker.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cs446.group18.timetracker.R;
import com.cs446.group18.timetracker.adapter.TimeLineAdapter;
import com.cs446.group18.timetracker.model.TimeLineModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimelineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimelineFragment extends Fragment {

    private RecyclerView mRecycler;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TimelineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimelineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimelineFragment newInstance(String param1, String param2) {
        TimelineFragment fragment = new TimelineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);

        super.onCreate(savedInstanceState);

        mRecycler = (RecyclerView) view.findViewById(R.id.time_line_recycler);
        initRecycler();

        return view;
    }

    private void initRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        TimeLineAdapter adapter = new TimeLineAdapter(getData());

        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setAdapter(adapter);
    }

    private List<TimeLineModel> getData() {
        List<TimeLineModel> models = new ArrayList<TimeLineModel>();

        models.add(new TimeLineModel("Study for 446", "2016-08-09"));
        models.add(new TimeLineModel("Study for 452", "2016-08-09"));
        models.add(new TimeLineModel("452 Final exam", "2016-08-10"));
        models.add(new TimeLineModel("Driving test", "2016-08-10"));
        models.add(new TimeLineModel("Optometrist appointment", "2016-08-12"));
        models.add(new TimeLineModel("Study for 458", "2016-08-15"));
        models.add(new TimeLineModel("Study for 458", "2016-08-15"));
        models.add(new TimeLineModel("Study for 458", "2016-08-15"));
        models.add(new TimeLineModel("Study for 458", "2016-08-15"));
        models.add(new TimeLineModel("Study for 458", "2016-08-15"));
        models.add(new TimeLineModel("Study for 458", "2016-08-15"));
        models.add(new TimeLineModel("457A Final exam ", "2016-08-19"));
        models.add(new TimeLineModel("458 Final Exam", "2016-08-20"));
        models.add(new TimeLineModel("End of term!", "2016-08-21"));

        return models;
    }
}