package sg.edu.rp.c346.id19047433.p10_gettingmylocationsenhanced;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class checkRecord extends AppCompatActivity {
    Button btnRefresh, btnFavorites;
    TextView tvNumberOfRecords;
    ListView lv;
    ArrayAdapter<String> recordAdapter;
    ArrayAdapter<String> favouriteAdapter;
    ArrayList<String> recordList;
    ArrayList<String> favouriteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_record);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnFavorites = findViewById(R.id.btnFavourite);
        lv = findViewById(R.id.lv);
        tvNumberOfRecords = findViewById(R.id.tvNumberOfRecords);

        recordList = new ArrayList<String>();
        favouriteList = new ArrayList<String>();

        recordAdapter = new ArrayAdapter<String>(checkRecord.this, android.R.layout.simple_list_item_1, recordList);
        lv.setAdapter(recordAdapter);

        String folderLocation_I = getFilesDir().getAbsolutePath() + "/MyFolder";
        File targetFile = new File(folderLocation_I, "data.txt");
        if (targetFile.exists() == true) {
            //recordList.clear();
            try {
                FileReader reader = new FileReader(targetFile);
                BufferedReader br = new BufferedReader(reader);
                String line = br.readLine();
                while (line != null) {
                    recordList.add(line);
                    line = br.readLine();
                    //Log.i("line", "" + recordList);
                }
                br.close();
                reader.close();
                recordAdapter.notifyDataSetChanged();
                tvNumberOfRecords.setText("Number of records: " + recordList.size());
            } catch (Exception e) {
                Toast.makeText(this, "Failed to read!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog alertDialog = new AlertDialog.Builder(checkRecord.this)
                        .setMessage("Add this location in your favorite list?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String favourite = recordList.get(position);
                                favouriteList.add(favourite);
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .show();
            }
        });

        btnFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favouriteAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, favouriteList);
                lv.setAdapter(favouriteAdapter);
            }
        });

    }
}