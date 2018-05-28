import java.util.ArrayList;

public class User 
{
    private String ip;
    private String date;
    private String time;
    private String cik;
    private String accession;
    private String extention;
    private int accNum;
    public String ssEndDate;
    public String ssEndTime;
    public int duration;
    
    public User(String ip, String date, 
                     String time, String cik, String accession, String extention) {
        super();
        this.ip = ip;
        this.date = date;
        this.time = time;
        this.cik = cik;
        this.accession = accession;
        this.extention = extention;
        this.accNum = 1;
        //this.ssTime = 1;
        this.ssEndDate = null;
        this.ssEndTime = null;
        this.duration = 1;
    }
    
    public int getAccNum() {
    	return accNum;
    }
    
    public void addAccNum() {
    	accNum ++;
    }
    
//    public int getSsTime() {
//    	return ssEndTime - this.time;
//    }
    
    //public void addSsTime(int Time) {
    //	ssTime = ssTime + Time;
    //}
    
    public String getIp() {
        return ip;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getCik() {
        return cik;
    }
    
    public String getAccession() {
        return accession;
    }
    
    public String getExtension() {
        return extention;
    }

    @Override
    public boolean equals(Object usr) {
    	User ptr = (User) usr;
    	return this.ip.equals(ptr.ip);
    }
    
    @Override
    public String toString() {
        return ip + "," + date + " " + time + "," + ssEndDate + " "
        		+ ssEndTime + "," + duration + "," + accNum;
    }
}