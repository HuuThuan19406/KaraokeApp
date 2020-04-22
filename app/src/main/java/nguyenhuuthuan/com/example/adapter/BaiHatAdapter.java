package nguyenhuuthuan.com.example.adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import nguyenhuuthuan.com.example.model.BaiHat;
import nguyenhuuthuan.com.example.phanmemkaraoke.MainActivity;
import nguyenhuuthuan.com.example.phanmemkaraoke.R;
import nguyenhuuthuan.com.example.phanmemkaraoke.databinding.ActivityMainBinding;
import nguyenhuuthuan.com.example.phanmemkaraoke.databinding.ItemsBinding;

public class BaiHatAdapter extends ArrayAdapter<BaiHat> {
    Activity context;
    int resource;
    private boolean willRemove=false;

    public BaiHatAdapter(@NonNull Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final View view = this.context.getLayoutInflater().inflate(this.resource, null);
        TextView lblMaBaiHai = view.findViewById(R.id.lblMaBaiHat);
        TextView lblTenBaiHat = view.findViewById(R.id.lblTenBaiHat);
        TextView lblTenCaSi = view.findViewById(R.id.lblTenCaSi);
        final ImageView imgLike = view.findViewById(R.id.imgLike);
        final ImageView imgDislike = view.findViewById(R.id.imgDislike);
        final BaiHat baiHat = getItem(position);
        lblMaBaiHai.setText(baiHat.getMa());
        lblTenBaiHat.setText(baiHat.getTen());
        lblTenCaSi.setText(baiHat.getCaSi());
        if (baiHat.getCamXuc().getDescription() == 1) {
            imgLike.setVisibility(View.INVISIBLE);
            imgDislike.setVisibility(View.VISIBLE);
        } else {
            imgLike.setVisibility(View.VISIBLE);
            imgDislike.setVisibility(View.INVISIBLE);
        }
        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (xuLyLike(baiHat, 1) > 0){
                    imgLike.setVisibility(View.INVISIBLE);
                    imgDislike.setVisibility(View.VISIBLE);
                    Toast.makeText(context,
                            "Đã thêm bài hát " + baiHat.getTen() + " vào Danh sách YÊU THÍCH thành công", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(context,
                            "Thêm vào Danh sách YÊU THÍCH thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
        imgDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (xuLyLike(baiHat, 0) > 0){
                    imgLike.setVisibility(View.VISIBLE);
                    imgDislike.setVisibility(View.INVISIBLE);
                    if(willRemove==true)
                        remove(baiHat);
                    Toast.makeText(context,
                            "Đã xóa bài hát " + baiHat.getTen() + " khỏi Danh sách YÊU THÍCH thành công", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(context,
                            "Xóa khỏi Danh sách YÊU THÍCH thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private int xuLyLike(BaiHat baiHat, int option) {
        ContentValues values = new ContentValues();
        values.put("YEUTHICH", option);
        int result = MainActivity.database.update(MainActivity.TABLE_NAME, values, "MABH=?", new String[]{baiHat.getMa()});
        if(option==1)
            baiHat.setCamXuc(BaiHat.CamXuc.like);
        return result;
    }

    public boolean isWillRemove() {
        return willRemove;
    }

    public void setWillRemove(boolean willRemove) {
        this.willRemove = willRemove;
    }
}
