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
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cs446.group18.timetracker.R;

import java.nio.charset.Charset;
import java.util.Objects;

public class WriteTagFragment extends Fragment implements OnNewIntentListener {

    private NfcAdapter _nfcAdapter;
    private PendingIntent _nfcPendingIntent;
    private IntentFilter[] _writeTagFilters;
    private boolean        _writeMode = false;

    private ImageView _imageViewImage;
    private Button _buttonWrite;
    private String         Select_string;

    Spinner dropdown;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View writeTagFragment = inflater.inflate(R.layout.layout_writetag, container, false);



        _imageViewImage = (ImageView) writeTagFragment.findViewById(R.id.image);
        _buttonWrite = (Button) writeTagFragment.findViewById(R.id.buttonWriteTag);
        _buttonWrite.setOnClickListener(_tagWriter);



        dropdown = writeTagFragment.findViewById(R.id.spinner1);
        String[] items = new String[]{"Study", "Lunch", "Eat"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Select_string = "Event{eventId=1, eventName=Study, description=I want to Study}";
                        //Toast.makeText(getContext(),Select_string,Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        //Select_string = "Event{eventId=2, eventName=Lunch, description=I want to Lunch}";
                        Toast.makeText(getContext(),Select_string,Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        //Select_string = "Event{eventId=3, eventName=Eat, description=I want to Eat}";
                        Toast.makeText(getContext(),Select_string,Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (_nfcAdapter == null)
        {
            Toast.makeText(getContext(), "Your device does not support NFC. Cannot run this sample.", Toast.LENGTH_LONG).show();

        }

        checkNfcEnabled();

        _nfcPendingIntent = PendingIntent.getActivity(getContext(), 0, new Intent(getContext(), getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);

        _writeTagFilters = new IntentFilter[]{tagDetected};


        return writeTagFragment;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        _nfcAdapter = NfcAdapter.getDefaultAdapter(activity);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (_nfcAdapter != null)checkNfcEnabled();
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
        if (_writeMode)
        {
            if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED))
            {
                Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                writeTag(buildNdefMessage(), detectedTag);

                _imageViewImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
                dropdown.setEnabled(true);
            }
        }
    }

    private final View.OnClickListener _tagWriter = new View.OnClickListener()
    {
        @Override
        public void onClick(View arg0)
        {

            enableTagWriteMode();

        }
    };

    private void enableTagWriteMode()
    {
        _writeMode = true;
        _nfcAdapter.enableForegroundDispatch(getActivity(), _nfcPendingIntent, _writeTagFilters, null);

        _imageViewImage.setImageDrawable(getResources().getDrawable(R.drawable.android_writing_logo));
        dropdown.setEnabled(false);
    }
    boolean writeTag(NdefMessage message, Tag tag)
    {
        int size = message.toByteArray().length;

        try
        {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null)
            {
                ndef.connect();

                if (!ndef.isWritable())
                {
                    Toast.makeText(getContext(), "Cannot write to this tag. This tag is read-only.", Toast.LENGTH_LONG).show();
                    return false;
                }

                if (ndef.getMaxSize() < size)
                {
                    Toast.makeText(getContext(),
                            "Cannot write to this tag. Message size (" + size + " bytes) exceeds this tag's capacity of " + ndef.getMaxSize()
                                    + " bytes.", Toast.LENGTH_LONG).show();
                    return false;
                }

                ndef.writeNdefMessage(message);
                Toast.makeText(getContext(), "A pre-formatted tag was successfully updated.", Toast.LENGTH_LONG).show();
                return true;
            }

            Toast.makeText(getContext(), "Cannot write to this tag. This tag does not support NDEF.", Toast.LENGTH_LONG).show();
            return false;

        }
        catch (Exception e)
        {
            Toast.makeText(getContext(), "Cannot write to this tag due to an Exception.", Toast.LENGTH_LONG).show();
        }

        return false;
    }
    private NdefMessage buildNdefMessage()
    {
        if (Select_string.equals("")){
            Toast.makeText(getContext(),"Empty Selected String",Toast.LENGTH_SHORT).show();
        }else{
            String data = Select_string;

            String mimeType = "application/com.cs446.group18.timetracker";

            byte[] mimeBytes = mimeType.getBytes(Charset.forName("UTF-8"));
            byte[] dataBytes = data.getBytes(Charset.forName("UTF-8"));
            byte[] id = new byte[0];

            NdefRecord record = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytes, id, dataBytes);
            NdefMessage message = new NdefMessage(new NdefRecord[]{record});

            return message;
        }
        return null;
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
