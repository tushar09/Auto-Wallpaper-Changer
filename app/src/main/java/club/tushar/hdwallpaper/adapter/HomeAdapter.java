package club.tushar.hdwallpaper.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;

import java.util.List;

import club.tushar.hdwallpaper.R;
import club.tushar.hdwallpaper.activity.HomeActivity;
import club.tushar.hdwallpaper.databinding.RowHomeBinding;
import club.tushar.hdwallpaper.dto.unPlash.HomeResponseDto;

public class HomeAdapter extends BaseAdapter{

    private Context context;
    private List<HomeResponseDto.Hits> dto;
    private int count = 1;

    public HomeAdapter(Context context, List<HomeResponseDto.Hits> dto){
        this.context = context;
        this.dto = dto;
    }

    @Override
    public int getCount(){
        return dto.size();
    }

    @Override
    public Object getItem(int i){
        return dto.get(i);
    }

    @Override
    public long getItemId(int i){
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup){

        Holder holder;

        if(view == null){
            holder = new Holder(context);
            view = holder.binding.getRoot();
            view.setTag(holder);
        }else {
            holder = (Holder) view.getTag();
        }

        holder.binding.tvDownload.setText(dto.get(i).getLikes() + "");
        //Picasso.get().load(dto.get(i).getUrl()).into(holder.binding.ivPic);
        holder.binding.tvAuthor.setText("by " + dto.get(i).getUser());
        Glide.with(context).load(dto.get(i).getWebformatURL()).into(holder.binding.ivPic);
        //Log.e("url", dto.get(i).getWebformatURL());

        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //HomeActivity.ha.downloadPicture(dto.get(i).getLargeImageURL(), dto.get(i).getId() + "", dto.get(i).getLargeImageURL());
            }
        });

        return view;
    }

    private class Holder{
        RowHomeBinding binding;
        public Holder(Context context){
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_home, null, true);
        }
    }
}
