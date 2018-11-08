package www.kanavwadhawan.com.marsplay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ImagesActivity extends AppCompatActivity implements ImageAdapter.onItemClickListener{

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private DatabaseReference mDatabaseRef;
    private List<Upload> mUploads;
    private ProgressBar mProgressBar;
    private FirebaseStorage mStorage;
    private  ValueEventListener mDBListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);


        mRecyclerView=findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUploads = new ArrayList<>();
        mAdapter = new ImageAdapter(ImagesActivity.this,mUploads);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(ImagesActivity.this);
        mProgressBar=findViewById(R.id.Progress_circle);

        mStorage=FirebaseStorage.getInstance();

        mDatabaseRef= FirebaseDatabase.getInstance().getReference("uploads");
        mDBListener=mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUploads.clear();
for(DataSnapshot postSnapShot: dataSnapshot.getChildren()){
    Upload upload=postSnapShot.getValue(Upload.class);
    upload.setKey(postSnapShot.getKey());
    mUploads.add(upload);


}
mAdapter.notifyDataSetChanged();

mProgressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(ImagesActivity.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);

            }
        });
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onWhateverClick(int position) {

        //Toast.makeText(this,"View  click "+position,Toast.LENGTH_SHORT).show();

        Upload selectedItem=mUploads.get(position);
        final String selectedKey=selectedItem.getKey();

        final Bundle bundle = new Bundle();
        bundle.putStringArrayList("imageArray", buildStringArrayList(selectedItem.getImageUri().toString()));

        //Log.i("TAG",selectedItem.getImageUri()+" ");
        Intent imageDialogViewIntent = new Intent(ImagesActivity.this, ImageViewDialogActivity.class);
        imageDialogViewIntent.putExtras(bundle);
        startActivity(imageDialogViewIntent);

    }

    @Override
    public void onDeleteClick(int position) {


        Upload selectedItem=mUploads.get(position);
        final String selectedKey=selectedItem.getKey();




        StorageReference imageRef=mStorage.getReferenceFromUrl(selectedItem.getImageUri());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(ImagesActivity.this,"Item deleted",Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

    private ArrayList<String> buildStringArrayList(String imageUrl) {
        ArrayList<String> imageArrayList = new ArrayList<>();
        imageArrayList.add(imageUrl);
        return imageArrayList;
    }

}
