package com.ngo_request.ngo_request;

/**
 * Created by USER on 22-10-2015.
 */
public class ListItems {
    private String name, profilePic,status;

    public ListItems() {
    }

    public ListItems(String name, String image, String sss) {
        super();

        this.name = name;
        this.profilePic = image;
        this.status=sss;


    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String name) {
        this.status = name;
    }

}
