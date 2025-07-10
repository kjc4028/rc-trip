package com.search.srch;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "trip")
public class TripEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    private String _Id;

    private String contentId;
    private String tripNm;
    private String tripCts;
    private String mbtia;
    private String mbtib;
    private String mbtic;
    private String mbtid;
    private String regUserId;
    private Double score1;
    private Double score2;
    private Double score3;
    private Double score4;
    private Double score5;
    private Double score6;
    private Double score7;
    private Double score8;
    private List<Double> embeddingScore;

    public String get_Id() { return _Id; }
    public void set_Id(String _Id) { this._Id = _Id; }
    public String getContentId() { return contentId; }
    public void setContentId(String contentId) { this.contentId = contentId; }
    public String getTripNm() { return tripNm; }
    public void setTripNm(String tripNm) { this.tripNm = tripNm; }
    public String getTripCts() { return tripCts; }
    public void setTripCts(String tripCts) { this.tripCts = tripCts; }
    public String getMbtia() { return mbtia; }
    public void setMbtia(String mbtia) { this.mbtia = mbtia; }
    public String getMbtib() { return mbtib; }
    public void setMbtib(String mbtib) { this.mbtib = mbtib; }
    public String getMbtic() { return mbtic; }
    public void setMbtic(String mbtic) { this.mbtic = mbtic; }
    public String getMbtid() { return mbtid; }
    public void setMbtid(String mbtid) { this.mbtid = mbtid; }
    public String getRegUserId() { return regUserId; }
    public void setRegUserId(String regUserId) { this.regUserId = regUserId; }
    public Double getScore1() { return score1; }
    public void setScore1(Double score1) { this.score1 = score1; }
    public Double getScore2() { return score2; }
    public void setScore2(Double score2) { this.score2 = score2; }
    public Double getScore3() { return score3; }
    public void setScore3(Double score3) { this.score3 = score3; }
    public Double getScore4() { return score4; }
    public void setScore4(Double score4) { this.score4 = score4; }
    public Double getScore5() { return score5; }
    public void setScore5(Double score5) { this.score5 = score5; }
    public Double getScore6() { return score6; }
    public void setScore6(Double score6) { this.score6 = score6; }
    public Double getScore7() { return score7; }
    public void setScore7(Double score7) { this.score7 = score7; }
    public Double getScore8() { return score8; }
    public void setScore8(Double score8) { this.score8 = score8; }
    public List<Double> getEmbeddingScore() { return embeddingScore; }
    public void setEmbeddingScore(List<Double> embeddingScore) { this.embeddingScore = embeddingScore; }
} 