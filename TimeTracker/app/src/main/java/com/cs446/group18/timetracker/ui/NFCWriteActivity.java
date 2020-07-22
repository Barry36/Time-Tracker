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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.cs446.group18.timetracker.R;

import java.nio.charset.Charset;

public class NFCWriteActivity extends Activity {

    //NFC-related variables
    private NfcAdapter _nfcAdapter;
    private PendingIntent _nfcPendingIntent;
    private IntentFilter[] _writeTagFilters;
    private boolean _writeMode = false;

    private ImageView _imageViewImage;
    private Button _buttonWrite;
    private String Select_string;

    Spinner dropdown;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_writetag);

        _imageViewImage = (ImageView) findViewById(R.id.image);
        _buttonWrite = (Button) findViewById(R.id.buttonWriteTag);
        _buttonWrite.setOnClickListener(_tagWriter);

        _nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        dropdown = findViewById(R.id.spinner1);
        String[] items = new String[]{"Study", "Lunch", "Eat"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Select_string = "Event{eventId=1, eventName=Study, description=I want to Study}";
                        Toast.makeText(getApplicationContext(), Select_string, Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Select_string = "Event{eventId=2, eventName=Lunch, description=I want to Lunch}";
                        Toast.makeText(getApplicationContext(), Select_string, Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Select_string = "Event{eventId=3, eventName=Eat, description=I want to Eat}";
                        Toast.makeText(getApplicationContext(), Select_string, Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (_nfcAdapter == null) {
            Toast.makeText(this, "Your device does not support NFC. Cannot run this sample.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        checkNfcEnabled();

        _nfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);

        _writeTagFilters = new IntentFilter[]{tagDetected};
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkNfcEnabled();
    }

    @Override
    protected void onPause() {
        super.onPause();

        _nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (_writeMode) {
            if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
                Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                writeTag(buildNdefMessage(), detectedTag);

                _imageViewImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
                dropdown.setEnabled(true);
            }
        }
    }

    private final View.OnClickListener _tagWriter = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {

            enableTagWriteMode();

        }
    };

    private void enableTagWriteMode() {
        _writeMode = true;
        _nfcAdapter.enableForegroundDispatch(this, _nfcPendingIntent, _writeTagFilters, null);

        _imageViewImage.setImageDrawable(getResources().getDrawable(R.drawable.android_writing_logo));
        dropdown.setEnabled(false);
    }


    boolean writeTag(NdefMessage message, Tag tag) {
        int size = message.toByteArray().length;

        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();

                if (!ndef.isWritable()) {
                    Toast.makeText(this, "Cannot write to this tag. This tag is read-only.", Toast.LENGTH_LONG).show();
                    return false;
                }

                if (ndef.getMaxSize() < size) {
                    Toast.makeText(this,
                            "Cannot write to this tag. Message size (" + size + " bytes) exceeds this tag's capacity of " + ndef.getMaxSize()
                                    + " bytes.", Toast.LENGTH_LONG).show();
                    return false;
                }

                ndef.writeNdefMessage(message);
                Toast.makeText(this, "A pre-formatted tag was successfully updated.", Toast.LENGTH_LONG).show();
                return true;
            }

            Toast.makeText(this, "Cannot write to this tag. This tag does not support NDEF.", Toast.LENGTH_LONG).show();
            return false;

        } catch (Exception e) {
            Toast.makeText(this, "Cannot write to this tag due to an Exception.", Toast.LENGTH_LONG).show();
        }

        return false;
    }

    private NdefMessage buildNdefMessage() {
        if (Select_string.equals("")) {
            Toast.makeText(getApplicationContext(), "Empty Selected String", Toast.LENGTH_SHORT).show();
        } else {
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

    private void checkNfcEnabled() {
        Boolean nfcEnabled = _nfcAdapter.isEnabled();
        if (!nfcEnabled) {
            new AlertDialog.Builder(NFCWriteActivity.this).setTitle(getString(R.string.text_warning_nfc_is_off))
                    .setMessage(getString(R.string.text_turn_on_nfc)).setCancelable(false)
                    .setPositiveButton(getString(R.string.text_update_settings), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                        }
                    }).create().show();
        }
    }
}