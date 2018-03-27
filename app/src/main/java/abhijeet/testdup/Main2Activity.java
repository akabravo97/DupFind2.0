package abhijeet.testdup;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {
     //TextView imagePath;
     //TextView directoryPath;
    Button finishButton;
     Button chooseDirectory,Scan;
     String directory;
    String imageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        final Intent intent=getIntent();
        imageUrl=intent.getStringExtra("imageUrl");
        //imagePath=(TextView)findViewById(R.id.imageUrl1);
        chooseDirectory=(Button)findViewById(R.id.chooseDirectory);
       // directoryPath=(TextView)findViewById(R.id.directoryPath);
        //imagePath.setText(imageUrl);
        chooseDirectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             showDirectorySelector();
            }
        });
        Scan=(Button)findViewById(R.id.scan);
        Scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(Main2Activity.this,Main3Activity.class);
                intent1.putExtra("file",imageUrl);
                intent1.putExtra("directory",directory);
                startActivity(intent1);
            }
        });
        finishButton=(Button)findViewById(R.id.finish);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showDirectorySelector(){
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(Intent.createChooser(i, "Choose directory"), 9999);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 9999:
                Log.i("Test", "Result URI " + data.getData());
                Uri uri = data.getData();
                directory=String.valueOf(uri.getPath());
                //directoryPath.setText(directory);
                Toast.makeText(getApplicationContext(),"Directory : "+directory+" selected",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
