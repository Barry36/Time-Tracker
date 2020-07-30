package com.cs446.group18.timetracker.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cs446.group18.timetracker.R;

public class ReadTagFragment extends Fragment implements OnNewIntentListener {

    private NfcAdapter _nfcAdapter;
    private PendingIntent _nfcPendingIntent;
    IntentFilter[]              _readTagFilters;

    private TextView _textViewData;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View readTagView = inflater.inflate(R.layout.layout_readtag, container, false);

        _textViewData = (TextView) readTagView.findViewById(R.id.textData);
        checkNfcEnabled();

        _nfcPendingIntent = PendingIntent.getActivity(getContext(), 0, new Intent(getContext(), getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try
        {
            ndefDetected.addDataType("application/com.cs446.group18.timetracker");
        }
        catch (IntentFilter.MalformedMimeTypeException e)
        {
            throw new RuntimeException("Could not add MIME type.", e);
        }

        _readTagFilters = new IntentFilter[]{ndefDetected};


        return readTagView;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        _nfcAdapter = NfcAdapter.getDefaultAdapter(activity);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (_nfcAdapter != null){
            checkNfcEnabled();

            if (getActivity().getIntent().getAction() != null)
            {
                if (getActivity().getIntent().getAction().equals(NfcAdapter.ACTION_NDEF_DISCOVERED))
                {
                    NdefMessage[] msgs = getNdefMessagesFromIntent(getActivity().getIntent());
                    NdefRecord record = msgs[0].getRecords()[0];
                    byte[] payload = record.getPayload();

                    String payloadString = new String(payload);

                    _textViewData.setText(payloadString);
                }
            }

            _nfcAdapter.enableForegroundDispatch(getActivity(), _nfcPendingIntent, _readTagFilters, null);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (_nfcAdapter != null){
            _nfcAdapter.disableForegroundDispatch(getActivity());
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (intent.getAction().equals(NfcAdapter.ACTION_NDEF_DISCOVERED))
        {
            NdefMessage[] msgs = getNdefMessagesFromIntent(intent);
            confirmDisplayedContentOverwrite(msgs[0]);

        }
        else if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED))
        {
            Toast.makeText(getContext(), "This NFC tag has no NDEF data.", Toast.LENGTH_LONG).show();
        }
    }

    NdefMessage[] getNdefMessagesFromIntent(Intent intent)
    {
        // Parse the intent
        NdefMessage[] msgs = null;
        String action = intent.getAction();
        if (action.equals(NfcAdapter.ACTION_TAG_DISCOVERED) || action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED))
        {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null)
            {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++)
                {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }

            }
            else
            {
                // Unknown tag type
                byte[] empty = new byte[]{};
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                NdefMessage msg = new NdefMessage(new NdefRecord[]{record});
                msgs = new NdefMessage[]{msg};
            }

        }
        else
        {
            Log.e("Read Tag", "Unknown intent.");
        }
        return msgs;
    }
    private void confirmDisplayedContentOverwrite(final NdefMessage msg)
    {
        final String data = _textViewData.getText().toString().trim();

        new AlertDialog.Builder(getContext()).setTitle("New tag found!").setMessage("Do you wanna show the content of this tag?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        String payload = new String(msg.getRecords()[0].getPayload());

                        _textViewData.setText(new String(payload));
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                _textViewData.setText(data);
                dialog.cancel();
            }
        }).show();
    }
    private void checkNfcEnabled()
    {
        if (_nfcAdapter != null){
            Boolean nfcEnabled = _nfcAdapter.isEnabled();
            if (!nfcEnabled)
            {
                new AlertDialog.Builder(getContext()).setTitle(getString(R.string.text_warning_nfc_is_off))
                        .setMessage(getString(R.string.text_turn_on_nfc)).setCancelable(false)
                        .setPositiveButton(getString(R.string.text_update_settings), new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                            }
                        }).create().show();
            }
        }
    }
}
