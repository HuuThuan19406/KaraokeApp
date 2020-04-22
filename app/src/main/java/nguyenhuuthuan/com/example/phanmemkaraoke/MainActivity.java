package nguyenhuuthuan.com.example.phanmemkaraoke;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import nguyenhuuthuan.com.example.adapter.BaiHatAdapter;
import nguyenhuuthuan.com.example.model.BaiHat;
import nguyenhuuthuan.com.example.phanmemkaraoke.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    String DATABASE_NAME = "Arirang.sqlite";
    public static String TABLE_NAME="ArirangSongList";
    String DB_PATH_SUFFIX = "/databases/";
    String query="";
    public static SQLiteDatabase database;
    BaiHatAdapter adapterAll, adapterLove;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        try{
        processCopy();
        setupTabHost();
        addControls();
        addEvents();
        showAllSongs();}
        catch (Exception e){
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    private void addEvents() {
        adapterLove.setWillRemove(true);
        binding.tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(tabId=="tap1")
                    showAllSongs();
                if(tabId=="tap2")
                    showLoveSongs();
            }
        });
    }

    private void showLoveSongs() {
        try {
            adapterLove.clear();
            for (int i = 0; i < adapterAll.getCount(); i++) {
                if (adapterAll.getItem(i).getCamXuc().getDescription() == 1)
                    adapterLove.add(adapterAll.getItem(i));
            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void setupTabHost() {
        binding.tabHost.setup();
        TabHost.TabSpec tap1=binding.tabHost.newTabSpec("tap1");
        tap1.setContent(R.id.ALL);
        tap1.setIndicator("Tổng hợp");
        binding.tabHost.addTab(tap1);
        TabHost.TabSpec tap2=binding.tabHost.newTabSpec("tap2");
        tap2.setContent(R.id.LOVE);
        tap2.setIndicator("Yêu thích");
        binding.tabHost.addTab(tap2);
    }

    private void showAllSongs() {
        database=openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        Cursor cursor=database.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        adapterAll.clear();
        while(cursor.moveToNext()){
            String MaBaiHat=cursor.getString(0);
            String TenBaiHat=cursor.getString(1);
            String TenCaSi=cursor.getString(3);
            int CamXuc=cursor.getInt(5);
            if(CamXuc==0)
                adapterAll.add(new BaiHat(MaBaiHat,TenBaiHat,TenCaSi,BaiHat.CamXuc.dislike));
            else
                adapterAll.add(new BaiHat(MaBaiHat,TenBaiHat,TenCaSi,BaiHat.CamXuc.like));
        }
        cursor.close();
    }

    private void addControls() {
        adapterAll=new BaiHatAdapter(MainActivity.this,R.layout.items);
        adapterLove=new BaiHatAdapter(MainActivity.this,R.layout.items);
        binding.lvAll.setAdapter(adapterAll);
        binding.lvLove.setAdapter(adapterLove);
    }

    private void processCopy() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset();
                Toast.makeText(this,
                        "Copying sucess from Assets folder",
                        Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getDatabasePath() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX+ DATABASE_NAME;
    }

    private void CopyDataBaseFromAsset() {
        try {
            InputStream myInput;
            myInput = getAssets().open(DATABASE_NAME);
            String outFileName = getDatabasePath();
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem mnuSearch=menu.findItem(R.id.mnuSearch);
        SearchView searchView= (SearchView) mnuSearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                xyLyTimKiem(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void xyLyTimKiem(String s) {
        Cursor cursor=database.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE MABH like ? OR TENBH like ? OR TACGIA like ?",
                                        new String[] {"%"+s+"%","%"+s.toUpperCase()+"%","%"+s+"%"});
        adapterAll.clear();
        while(cursor.moveToNext()){
            String MaBaiHat=cursor.getString(0);
            String TenBaiHat=cursor.getString(1);
            String TenCaSi=cursor.getString(3);
            int CamXuc=cursor.getInt(5);
            if(CamXuc==0)
                adapterAll.add(new BaiHat(MaBaiHat,TenBaiHat,TenCaSi,BaiHat.CamXuc.dislike));
            else
                adapterAll.add(new BaiHat(MaBaiHat,TenBaiHat,TenCaSi,BaiHat.CamXuc.like));
        }
        cursor.close();
    }
}
