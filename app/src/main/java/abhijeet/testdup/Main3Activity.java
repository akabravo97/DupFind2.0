package abhijeet.testdup;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main3Activity extends AppCompatActivity {
    int iDelete;
    Button finish;
    Bitmap bMapSource;
    ImageView imageView;
    //TextView file;
    //TextView directory;
    String directoryValue;
    String fileValue, updatedDirectory, updatedFile;
    ProgressDialog mProgressDialog;
    ArrayList imageFiles = new ArrayList();
    ArrayList resultLocation = new ArrayList();
    ArrayList modDates=new ArrayList();
    TextView statusText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        Intent intent = getIntent();
        fileValue = intent.getStringExtra("file");
        updatedFile = Environment.getExternalStorageDirectory().toString() + fileValue.replaceAll("\\/document\\/primary:", "/");
        statusText=(TextView)findViewById(R.id.status);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        imageView=(ImageView)findViewById(R.id.setBackground);
        Log.d("Updated", updatedFile);
        directoryValue = intent.getStringExtra("directory");
        try {
            FileInputStream source = new FileInputStream(updatedFile);
            BufferedInputStream buf = new BufferedInputStream(source);
            byte[] bMapArray = new byte[buf.available()];
            buf.read(bMapArray);
            bMapSource = BitmapFactory.decodeByteArray(bMapArray, 0, bMapArray.length);
            Log.d("Bitmap :", String.valueOf(bMapSource));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "FileNotFoundException", Toast.LENGTH_SHORT).show();
        }
        updatedDirectory = directoryValue.replaceAll("\\/tree\\/primary:", "/");
        Log.d("Directory : ", directoryValue);
        //file=(TextView)findViewById(R.id.file);
        //directory=(TextView)findViewById(R.id.directory);
        // file.setText(fileValue);
        //directory.setText(updatedDirectory);
        String path = Environment.getExternalStorageDirectory().toString() + updatedDirectory;
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: " + files.length);
        for (int i = 0; i < files.length; i++) {
            Log.d("Files", "FileName:" + files[i].getName());
            if (files[i].getName().endsWith(".jpg") || files[i].getName().endsWith(".jpeg")) {
                Log.d("imageFilesList : ", String.valueOf(files[i].getName()));
                imageFiles.add(files[i].getName());
            }
        }
        Log.d("List :", String.valueOf(imageFiles));
        for (int i = 0; i < imageFiles.size(); i++) {
            //String destFile1=Environment.getExternalStorageDirectory().toString()+updatedDirectory+"/"+String.valueOf(imageFiles.get(i));
            //Log.d("location",destFile1);
            try {
                String destFile = Environment.getExternalStorageDirectory().toString() + updatedDirectory + "/" + String.valueOf(imageFiles.get(i));
                FileInputStream dest = new FileInputStream(destFile);
                BufferedInputStream bufDest = new BufferedInputStream(dest);
                byte[] bMapArray = new byte[bufDest.available()];
                bufDest.read(bMapArray);
                Bitmap bMapDest = BitmapFactory.decodeByteArray(bMapArray, 0, bMapArray.length);
                Boolean result = ImageComparer.compare(bMapSource, bMapDest);
                Log.d("resultFinal : ", String.valueOf(result));
                if (result) {
                    File file = new File(destFile);
                    Date lastModDate = new Date(file.lastModified());
                    String regDate=lastModDate.toString();
                    String redModDate=regDate.replaceAll("G[\\s]?.*","");
                    modDates.add(redModDate);
                    resultLocation.add(destFile);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Error Trace:", String.valueOf(e));
               // Toast.makeText(getApplicationContext(), "FileNotFoundException", Toast.LENGTH_SHORT).show();
            }
        }
        if (resultLocation.isEmpty()){
            statusText.setText("No duplicates found!!!");
            imageView.setImageResource(R.drawable.noduplicates);
        }
        else imageView.setImageResource(R.drawable.background);

        Log.d("resultLocation :", String.valueOf(resultLocation));
   //     for (int i = 0; i < resultLocation.size(); i++) {
     //       ImageView image = new ImageView(this);
       //     image.setLayoutParams(new android.view.ViewGroup.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
         //   image.setImageURI(Uri.parse(resultLocation.get(i).toString()));
           // Log.d("count :", "1");
        //}
        SwipeMenuListView swipeMenuListView=(SwipeMenuListView)findViewById(R.id.swipeLayout);
        final CustomAdapter customAdapter=new CustomAdapter();

        swipeMenuListView.setAdapter(customAdapter);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0x00, 0x66,
                        0xff)));
                // set item width
                openItem.setIcon(R.drawable.open);
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                openItem.setWidth(170);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setIcon(R.drawable.delete);
                deleteItem.setWidth(170);
                // set a icon
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        swipeMenuListView.setMenuCreator(creator);
        swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Log.d("Tag", "onMenuItemClick: clicked item " + index);
                        Uri uri=Uri.parse(resultLocation.get(position).toString());
                        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                        pdfIntent.setDataAndType(uri ,"image/*");
                        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(pdfIntent);
                        break;
                    case 1:
                        final int pos=position;
                        Log.d("Tag", "onMenuItemClick: clicked item " + index);
                        builder.setMessage("Do you want to delete this file ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        System.out.println(new File(resultLocation.get(pos).toString()).getAbsoluteFile().delete());
                                        Log.d("Delete Status : ","Deleted!!");
                                        Toast.makeText(getApplicationContext(),"Deleted!!",Toast.LENGTH_SHORT).show();
                                        Object toRemove = customAdapter.getItem(pos);
                                        resultLocation.remove(pos);
                                        customAdapter.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        finish=(Button)findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Toast.makeText(getApplicationContext(),"Good Bye!!",Toast.LENGTH_SHORT).show();
            }
        });
    }


    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return resultLocation.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            iDelete=i;
            view=getLayoutInflater().inflate(R.layout.swipe_listview,null);
            ImageView imageView=(ImageView)view.findViewById(R.id.swipeImage);
            TextView textView=(TextView)view.findViewById(R.id.swipeText);
            imageView.setImageURI(Uri.parse(resultLocation.get(i).toString()));
            textView.setText(modDates.get(i).toString());
            return view;
        }
    }
}