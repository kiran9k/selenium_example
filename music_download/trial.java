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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

public class trial {
	
	public static class Global {
		static ArrayList<String> prop = config_reader.get_prop();
		public static String download_location=String.valueOf(prop.get(0));//"/home/kiran/Downloads/temp";
		
		public static int start_page=Integer.parseInt(prop.get(1));//starts from 1//starts from 1
		public static int  end_page=Integer.parseInt(prop.get(2));;//starts from 1
		
		public static int start_album_id=Integer.parseInt(prop.get(3));;//starts from 1
		public static int end_album_id=Integer.parseInt(prop.get(4));;//starts from 1
		public static int kbps=Integer.parseInt(prop.get(5));// available values: 48/128/320
		
		
		
	}
	public static void song_download() throws InterruptedException
	{

		 FirefoxProfile profile = new FirefoxProfile();
		 profile.setPreference("browser.download.dir", Global.download_location);
		 profile.setPreference("browser.download.folderList", 2);
		
		 int current_page=Global.start_page;
		 String page_no="";
		 if(Global.start_page>1)
			 page_no="?l=-1&p="+String.valueOf((current_page));
		 
		profile.setPreference("browser.helperApps.neverAsk.saveToDisk","Zip archive,Application/zip"); 
		WebDriver driver = new FirefoxDriver(profile);
		driver.manage().timeouts().implicitlyWait(5
				, TimeUnit.SECONDS);
        // Go to the Google Suggest home page
		do{
			if(Global.start_page>Global.end_page)
			{
				System.out.println("invalid start page end page combination");
				break;
			}
			else if(Global.start_page==Global.end_page)
				if(Global.start_album_id>Global.end_album_id)
				{
					System.out.println("invalid start /end album ocmbination");
					break;
				}
			
			driver.get("http://h-128.filexchanger.com/Hindi_128/album.php"+page_no);
			// Enter the query string "Cheese"
			WebElement query = driver.findElement(By.tagName("ul"));
			//	System.out.println(query.getText());
			List<WebElement> q=query.findElements(By.tagName("li"));
			//System.out.println(q.toString());
			int end_count=9999;
			if(current_page==Global.end_page)
			{
				end_count=Global.end_album_id;
				if(end_count>q.size()||end_count<1)
				{
        		System.out.println("invalid end album id");
        		break;
				}
			}
			int start_count=0;
			if(current_page==Global.start_page)
			{   
				start_count=Global.start_album_id-1;
				if(Global.start_album_id-1>q.size()||Global.start_album_id<1)
				{
					System.out.println("invalid start album id");
					break;
				}
			}
			
			for(int i=start_count;i<q.size()&& i<end_count;i++)
			{
				//System.out.println("q size:"+q.size()+"edn_count"+end_count+"i"+i);
				//System.out.println(q.get(i).getText());
				String name=q.get(i).getText();
				WebElement a=q.get(i).findElement(By.tagName("a"));        	
				a.click();
				List<WebElement> a_1=driver.findElements(By.tagName("a"));				
				for (int j=0;j<a_1.size();j++)
				{					
					if(a_1.get(j).getText().matches(String.valueOf(Global.kbps)+"kbps"))
					{						
						//System.out.println("128 kbps mathced");
						a_1.get(j).click();
						List<WebElement> a_2=driver.findElements(By.tagName("a"));
						boolean available=false;
						for (int k=0;k<a_2.size();k++)
						{
							if(a_2.get(k).getText().matches("Download Now!"))
							{
								//System.out.println("128 kbpsdownload");
								a_2.get(k).click();
								//actual_count++;
								available=true;
								break;
							}
						}
						if(!available)
							System.out.println("File not avialable for : "+name.replaceAll("\n", " "));
						else
							System.out.println("Download started for "+name.replaceAll("\n"," "));
						driver.navigate().back();
						break;					
					}	
				}
				driver.navigate().back();
				//	a_1.
				query = driver.findElement(By.tagName("ul"));
				q=query.findElements(By.tagName("li"));
				
			}
			//System.out.println("1 page done");
			current_page++;
			page_no="?l=-1&p="+String.valueOf((current_page));
    	}
		while(current_page<=Global.end_page);
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
