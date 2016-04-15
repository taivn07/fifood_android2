package Object;

/**
 * Created by USER on 13/4/2016.
 */
public class Food {

    private String name;
    private String address;
    private double lat;
    private double longth;
    private double distance;

    private String shop_id;
    private int totalComment;
    private int rating;
    private String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Food() {
    }

    public Food(String name, String address, long lat, long longth, String shop_id) {
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.longth = longth;
        this.shop_id = shop_id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongth() {
        return longth;
    }

    public void setLongth(double longth) {
        this.longth = longth;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getTotalComment() {
        return totalComment;
    }

    public void setTotalComment(int totalComment) {
        this.totalComment = totalComment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
