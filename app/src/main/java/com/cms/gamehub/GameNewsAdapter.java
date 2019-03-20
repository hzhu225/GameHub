package com.cms.gamehub;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GameNewsAdapter extends ArrayAdapter<GameNews>
{
    public GameNewsAdapter(Context context, List<GameNews> gameNewsList)
    {
        super(context, 0, gameNewsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        GameNews currentGN = getItem(position);

        TextView titleTV = convertView.findViewById(R.id.title_tv);
        titleTV.setText(currentGN.getTitle());

        TextView sectionTV = convertView.findViewById(R.id.section_tv);
        sectionTV.setText(currentGN.getSectionName());

        TextView timeTV = convertView .findViewById(R.id.time_tv);
        Date date = new Date(currentGN.getPublicationTime());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        timeTV.setText(sdf.format(date));


        TextView authorTV = convertView.findViewById(R.id.author_tv);
        if(currentGN.getAuthor() == null)
        {
            authorTV.setVisibility(View.GONE);
        }
        else
        {
            authorTV.setVisibility(View.VISIBLE);
            authorTV.setText(currentGN.getAuthor());
        }
        return convertView;
    }
}
