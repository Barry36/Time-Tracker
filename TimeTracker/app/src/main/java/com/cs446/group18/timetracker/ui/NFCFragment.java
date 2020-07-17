package com.cs446.group18.timetracker.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cs446.group18.timetracker.R;
import com.cs446.group18.timetracker.utils.NFCUtil;

public class NFCFragment extends Fragment implements NFCUtil.CodeListener {
    private TextView textViewEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nfc, container, false);
        textViewEmpty = view.findViewById(R.id.nfcText);
        NFCUtil.setNfcListener(this);

        return view;
    }

    @Override
    public void onCodeRetrieved(String code, NFCUtil.CodeType codeType) {
        textViewEmpty.setText(code);
    }
}
