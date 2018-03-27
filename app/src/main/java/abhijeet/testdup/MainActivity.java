package abhijeet.testdup;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
   // TextView imageUri;
   // TextView imageUrl;
    Uri globalUri;
    String Uri;
    String Url;
    Button nextActivity;
    private static final int FILE_SELECT_CODE = 0;
    Button button;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=(Button)findViewById(R.id.chooseImage);
       // imageUri=(TextView)findViewById(R.id.imageUri);
       // imageUrl=(TextView)findViewById(R.id.imageUrl);
        nextActivity=(Button)findViewById(R.id.nextActivity2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
        nextActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                intent.putExtra("imageUrl",Url);
                startActivity(intent);
            }
        });
    }
    private void showFileChooser() {
        String[] mimeTypes = {"image/jpeg", "image/jpg"};
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //Toast.makeText(getApplicationContext(),"Okay",Toast.LENGTH_SHORT).show();
        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageView=(ImageView)findViewById(R.id.showImage);
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {

                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    globalUri=uri;
                    Uri=String.valueOf(uri);
                    Log.d("File Uri: ",uri.toString());
                    // Get the path
                    try {
                      String path = uri.getPath();
                        Url=path;
                    }catch (Exception e){
                       Log.d("Error",String.valueOf(e));
                    }
                    //Log.d("File Path: ",path);
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                }
               // imageUrl.setText(Url);
               // imageUri.setText(Uri);
                try {
                    imageView.setImageDrawable(Drawable.createFromStream(
                            getContentResolver().openInputStream(globalUri),
                            null));
                }catch (Exception e){
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(),"File : "+Url.replaceAll("/.+/","") + " Selected",Toast.LENGTH_SHORT).show();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
