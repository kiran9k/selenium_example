package music_download;

/*
 * Downloads songs from the website: 
 * "http://h-128.filexchanger.com/Hindi_128/album.php
 * 
 * uses Selenium :selenium server standalone 2.39.0
 * The program is meant to download all recent songs(count no specified by user) appearing in the first
 * page of the above stated website.
 * 
 * The download location & no of songs to be downloaded can be mentioned by user 
 * 
 * All songs are downloaded in zip format inorder to reduce size of downloads
 */

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

public class trial {
	public static class Global {
		public static String download_location="/home/kiran/Downloads/temp";
		//specifies the max count for downloading
		//if set to 0 = unlimited
		public static int download_count=20;
		
		
		
	}
	public static void song_download() throws InterruptedException
	{

		 FirefoxProfile profile = new FirefoxProfile();
		 profile.setPreference("browser.download.dir", Global.download_location);
		 profile.setPreference("browser.download.folderList", 2);
		 int actual_count=0;
		 int current_page=1;
		 String page_no="";
		profile.setPreference("browser.helperApps.neverAsk.saveToDisk","Zip archive,Application/zip"); 
		WebDriver driver = new FirefoxDriver(profile);
		driver.manage().timeouts().implicitlyWait(5
				, TimeUnit.SECONDS);
        // Go to the Google Suggest home page
		do{
        driver.get("http://h-128.filexchanger.com/Hindi_128/album.php"+page_no);
        // Enter the query string "Cheese"
        WebElement query = driver.findElement(By.tagName("ul"));
        //System.out.println(query.getText());
        List<WebElement> q=query.findElements(By.tagName("li"));
        //System.out.println(q.toString());
        for(int i=0;i<q.size()&& i<Global.download_count;i++)
        {
        	System.out.println(q.get(i).getText());
        	WebElement a=q.get(i).findElement(By.tagName("a"));        	
        	a.click();
            List<WebElement> a_1=driver.findElements(By.tagName("a"));
        	
        	for (int j=0;j<a_1.size();j++)
        	{
        		if(a_1.get(j).getText().matches("128kbps"))
        		{
        			
        			System.out.println("128 kbps mathced");
        			a_1.get(j).click();
        			List<WebElement> a_2=driver.findElements(By.tagName("a"));
                	for (int k=0;k<a_2.size();k++)
                	{
                		if(a_2.get(k).getText().matches("Download Now!"))
                		{
                			System.out.println("128 kbpsdownload");
                			//a_2.get(k).click();
                			actual_count++;
                			driver.navigate().back();
                			break;
                		}
                	}
                	driver.navigate().back();
                	//a_1.
                	query = driver.findElement(By.tagName("ul"));
                	q=query.findElements(By.tagName("li"));
                	break;
        		}
        		
        	}
        	
        	
        	}
        current_page++;
        page_no="?l=-1&p="+String.valueOf((current_page));
    	}while(actual_count<Global.download_count);
        if(still_downloading())
        	driver.quit();
        
	}
	public static boolean still_downloading()
	{
		File temp=new File(Global.download_location);
        if(temp.isDirectory())
        {
        	File[] files=temp.listFiles();
        	boolean flag=false;
        	while(!flag)
        	{
        		files=temp.listFiles();
        		boolean none_present=true;
        		for(File f:files)
        		{
        			System.out.println(f.getName());
        			//System.out.println(f.getName().endsWith(".zip"));
        			if(f.getName().endsWith(".part"))     			
        			{
        				none_present=false;
        			}
        		}
        		if(none_present==true)
        			flag=true;
        		
        	
        		}
        	}
        return true;
	}
	
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		song_download();
		
	}

}
