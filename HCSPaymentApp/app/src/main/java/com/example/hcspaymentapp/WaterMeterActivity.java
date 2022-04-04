package com.example.hcspaymentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class WaterMeterActivity extends AppCompatActivity implements ConnectionDatabase.Listener, ConfirmDeleteDialog.Host{
    public static final String EXTRA_ID = "EXTRA_ID";
    private long mCounterId;
    private TextView mValueTv;
    private TextView mNameTv;
    private View mTint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCounterId = getIntent().getLongExtra(EXTRA_ID, -1);
        setContentView(R.layout.activity_water_meter);

        findViewById(R.id.a_counter_back).setOnClickListener(v -> finish());
        findViewById(R.id.a_counter_del).setOnClickListener(v ->
                new ConfirmDeleteDialog().show(getSupportFragmentManager(), null));
        findViewById(R.id.a_counter_edit).setOnClickListener(v ->
                EditDialog.create(mCounterId).show(getSupportFragmentManager(), null));
        mValueTv = findViewById(R.id.textPrevious);
        mNameTv = findViewById(R.id.textCurrent);
        new FastCountButton(findViewById(R.id.plus), this::inc);
        new FastCountButton(findViewById(R.id.minus), this::dec);
        new FastCountButton(findViewById(R.id.plus2), this::inc);
        new FastCountButton(findViewById(R.id.minus2), this::dec);
        onDataChanged();
        ConnectionDatabase.getInstance(this).addListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ConnectionDatabase.getInstance(this).removeListener(this);
    }

    private void changeValue(int newValue) {
        ConnectionDatabase.getInstance(this).setValue(getCounter(), newValue);
    }

    private void inc() {
        changeValue(getCounter().result + 1);
    }

    private void dec() {
        changeValue(getCounter().result - 1);
    }

    private Counter getCounter() {
        return ConnectionDatabase.getInstance(this).getCounter(mCounterId);
    }

    @Override
    public void onDataChanged() {
        Counter counter = getCounter();
        if (counter != null) {
            mValueTv.setText(String.valueOf(counter.result));
            mTint.getBackground().setColorFilter(counter.color, PorterDuff.Mode.SRC_ATOP);
        } else {
            finish();
        }
    }

    @Override
    public void onConfirm() {
        ConnectionDatabase.getInstance(this).deleteCounter(mCounterId);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            inc();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            dec();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}