package gov.hygs.htgl.utils;

import java.io.File;

public class AttachmentOpt {
	public static String getAttachmentPath(){
		//String path = "/usr/local/tomcat/app/";
		//return "E:\\user\\local\\tomcat\\app\\";
		//return "/usr/local/tomcat/upload/";
		return "D:/app/tomcat/app/images/";
	}
	
	public static boolean deleteAttachmentFile(String param){
		String path = AttachmentOpt.getAttachmentPath();
		File file  = new File(path+param);
		if(file.exists()){
			if(file.isFile()){
				return file.delete();
			}
		}
		return false;
	}
}
