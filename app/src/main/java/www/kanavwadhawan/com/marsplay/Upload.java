package www.kanavwadhawan.com.marsplay;

import com.google.firebase.database.Exclude;

public class Upload {

    private  String mName;
    private String mImageUri;
    private String mKey;
@Exclude
    public String getKey() {
        return mKey;
    }
@Exclude
    public void setKey(String key) {
        mKey = key;
    }

    public Upload(){


    }

    public Upload(String name,String imageUri){


        if(name.trim().equals("")){

            name="No Name";
        }

        mName=name;
        mImageUri=imageUri;
    }


    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageUri() {
        return mImageUri;
    }

    public void setImageUri(String imageUri) {
        mImageUri = imageUri;
    }
}
