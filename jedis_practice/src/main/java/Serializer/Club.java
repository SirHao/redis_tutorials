package Serializer;

public class Club {
    private Integer id;
    private String clubName;
    private String clubData;
    private Integer Rank;

    public Club(Integer id, String clubName, String clubData, Integer rank) {
        this.id = id;
        this.clubName = clubName;
        this.clubData = clubData;
        Rank = rank;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public void setClubData(String clubData) {
        this.clubData = clubData;
    }

    public void setRank(Integer rank) {
        Rank = rank;
    }

    public Integer getId() {
        return id;
    }

    public String getClubName() {
        return clubName;
    }

    public String getClubData() {
        return clubData;
    }

    public Integer getRank() {
        return Rank;
    }
}
