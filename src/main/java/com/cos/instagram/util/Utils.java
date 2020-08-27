package com.cos.instagram.util;

import java.util.ArrayList;
import java.util.List;

public class Utils {
	
	public static List<String> tagParse(String tags){
		String temp[] = tags.split("#");
		List<String> list = new ArrayList<String>();
		
		for (int i=1; i<temp.length; i++) {
			list.add(temp[i]);
		}
		return list;
	}
}




