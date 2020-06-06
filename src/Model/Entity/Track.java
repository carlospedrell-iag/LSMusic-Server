package Model.Entity;

public class Track {

    private int id;
    private String title;
    private String artist;
    private String album;
    private String genre;
    private String path;
    private int plays;
    private float rating;

    public Track(int id, String title, String artist, String album, String genre, String path, int plays, float rating) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.path = path;
        this.plays = plays;
        this.rating = rating;
    }

    public Track(String title, String artist, String album, String genre, String path) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPlays() {
        return plays;
    }

    public void setPlays(int plays) {
        this.plays = plays;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
