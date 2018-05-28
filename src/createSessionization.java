import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class createSessionization {
	
	private static final String COMMA_DELIMITER = ",";
	
	private static int inactivityTime = 0;
	public static List<User> userList;
	
	public static String currentDate;
	public static int currentTime;
	
	public static void main(String[] args) {
		//Initialize user list
		userList = new ArrayList<User>();
		
		//parameter for setting up writer
		Charset charset = Charset.forName("US-ASCII");
    	Path path = Paths.get("/home/liyuan/codeChallenge/output/sessionization.txt");
    	BufferedWriter writer = null;
    	
    	//parameter for setting up reader
    	BufferedReader csvReader = null;
    	    	
    	//Read inactive time 
		readActiveTime();
		
		try{
    		writer = new BufferedWriter(new FileWriter("/home/liyuan/codeChallenge/output/sessionization.txt"));
		    try {
		    	//read log.csv data
		        csvReader = new BufferedReader(new FileReader("/home/liyuan/codeChallenge/input/log.csv"));
		        String line=null;	 
		        
		        //skip the first line
		        csvReader.readLine();
		        
		        //Read each log
	            while((line=csvReader.readLine())!=null){
	            	
	            	//Sort out and update user information
	            	analyze(line);
	            	
	            	//Check and write users to output file whose session is over 
		            checkSS(writer);	
	            }	           	            
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    
		    //Write all users to output file who are still in the session 
		    //but log data is depleted
		    cleanList(writer);
		    
		} catch (IOException x) {
    	    System.err.format("IOException: %s%n", x);
    	}
		
		try {//close writer
	    	writer.close();
		}catch (Exception e) {
	        e.printStackTrace();
	    }  	
	    
	    try {//close reader
	    	csvReader.close();
		}catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	//Calculate and write correct user information into output file
	public static void calAndWrite(User tempUsr, BufferedWriter writer, int i) throws IOException {
		calSS(tempUsr);
		writer.write(tempUsr.toString(), 0, tempUsr.toString().length());
	    writer.newLine(); 
	}
	
	//write all the rest users log into output file
	//Because input data is depleted
	public static void cleanList(BufferedWriter writer) throws IOException {
		for (int i = 0; i < userList.size(); i++) {
	    	User tempUsr = userList.get(i);
	    	calAndWrite(tempUsr, writer, i);
	    }
	}
	
	//Analyze each log and update user information
	public static void analyze (String line) {
		String[] userDetails = line.split(COMMA_DELIMITER);
    	currentTime = string2Sec(userDetails[2]);
    	//System.out.println(currentTime);
    	currentDate = userDetails[1];
    	String curIp = userDetails[0];
    	User curUsr = new User(curIp,currentDate, userDetails[2],
    			userDetails[4],userDetails[5],userDetails[6]);
    	curUsr.ssEndDate = currentDate;
		curUsr.ssEndTime = userDetails[2];
    	if (!userList.contains(curUsr)) {
    		userList.add(curUsr);
    	}else{
    		userList.get(userList.indexOf(curUsr)).ssEndDate = currentDate;
    		userList.get(userList.indexOf(curUsr)).ssEndTime = userDetails[2];
    		userList.get(userList.indexOf(curUsr)).addAccNum();
    	}
	}
	
	//Check and write users to output file whose session is over 
	public static void checkSS (BufferedWriter writer) throws IOException {
		for (int i = 0; i < userList.size(); i++) {
    		User tempUsr = userList.get(i);
    		//if current time and last request time are in the same day
    		if(tempUsr.ssEndDate.equals(currentDate)) {
    			
    			//Check if it's still active
    			if(inactivityTime <= currentTime - string2Sec(tempUsr.ssEndTime) - 1) {
    				calAndWrite(tempUsr, writer, i);
    				userList.remove(i);
    				i --;
    			}
    		}else {//if current time and last request time are not in the same day
    			
    			//Check if it's still active
    			if(inactivityTime <= 24*3600 - string2Sec(tempUsr.ssEndTime) + currentTime - 1) {
    				calAndWrite(tempUsr, writer, i);
    				userList.remove(i);
    				i --;
    			}
    		}
    	}
	}
	
	//Read the given inactive time
	public static void readActiveTime() {
		BufferedReader timeReader = null;
		try {
	        timeReader = new BufferedReader(new FileReader("/home/liyuan/codeChallenge/input/inactivity_period.txt"));
	        inactivityTime = Integer.parseInt(timeReader.readLine());
	        //System.out.println(inactivityTime +"\n");
		} catch (Exception e) {
	        e.printStackTrace();
	    }
		
		try {
			timeReader.close();
		}catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	//calculate duration
	public static void calSS (User usr) {
		
			//Check if the session is within one day
			if (usr.ssEndDate.equals(usr.getDate())) {
				usr.duration = string2Sec(usr.ssEndTime) + 1 
						- string2Sec(usr.getTime());   
			}else {
				usr.duration = calDateDiff(usr.getDate(),usr.ssEndDate) 
						 - string2Sec(usr.ssEndTime) - string2Sec(usr.getTime());
			} 
		
	}
	
	//Transform a string time to real seconds
	public static int string2Sec (String time) {
		int hourTen = Character.getNumericValue(time.charAt(0));
		int hour = Character.getNumericValue(time.charAt(1));
		int minTen = Character.getNumericValue(time.charAt(3));
		int min = Character.getNumericValue(time.charAt(4));
		int secTen = Character.getNumericValue(time.charAt(6));
		int sec = Character.getNumericValue(time.charAt(7));
		
		return hourTen * 3600 * 10 + hour * 3600 + minTen * 60 * 10
				+min * 60 + secTen * 10 + sec;
	}
	
	//Assume the max time that user can stay in session is 
	//one month. (Even though I can modify this to calculate for 1000 years
	//difference, it's unreal for a real human user to stay in session for
	//that long time). Also assume 30 days per month.
	public static int calDateDiff (String date1, String date2) {
		int month1 = Character.getNumericValue(date1.charAt(8));
		int dayTen1 = Character.getNumericValue(date1.charAt(10));
		int day1 = Character.getNumericValue(date1.charAt(11));
		
		int month2 = Character.getNumericValue(date2.charAt(8));
		int dayTen2 = Character.getNumericValue(date2.charAt(10));
		int day2 = Character.getNumericValue(date2.charAt(11));
		
		int dayDiff = dayTen2*10+day2 - dayTen1 * 10 - day1;
		
		int result = 0;
		
		if (month1 != month2) {
			return result = 24 * 3600 * (30 + dayDiff);
		}else {
			return result = 24 * 3600 * (dayDiff);
		}
	}
}
