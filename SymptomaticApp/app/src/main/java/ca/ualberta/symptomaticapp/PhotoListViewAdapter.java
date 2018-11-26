package ca.ualberta.symptomaticapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
// Custom list view adapter with image view reference:
//AZZOLA, FRANCESCO. “Android ListView Tutorial: Custom Adapter, ImageView Rows, Checkbox.” SwA, 19 Nov. 2018,
// www.survivingwithandroid.com/2012/10/android-listview-custom-adapter-and.html#android_listview_with_custom_layout_using_image_imageview.
// Accessed: 24th November, 2018

public class PhotoListViewAdapter extends ArrayAdapter<Photo> {
    private ArrayList<Photo> photoList;
    private Context context;

    public PhotoListViewAdapter(ArrayList<Photo> photoList, Context context) {
        super(context, R.layout.photos_listview, photoList);
        this.photoList = photoList;
        this.context = context;
    }


    public View getView(final int position, View convertView, ViewGroup parent){
        View view = convertView;

        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.photos_listview, null);

        }


        // Now we can fill the layout with the right values
        TextView date = (TextView) view.findViewById(R.id.photoDate);
        ImageView img = (ImageView) view.findViewById(R.id.photoImage);
//        System.out.println("Position ["+position+"]");
        Photo p = photoList.get(position);
        date.setText("" + p.getTimestamp());
        img.setImageBitmap(p.getPhotoBitmap());


        Button deletePhoto = view.findViewById(R.id.photoDelete);

        deletePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Photo p = photoList.get(position);
                photoList.remove(p);
                notifyDataSetChanged();

            }
        });


        return view;
    }



}