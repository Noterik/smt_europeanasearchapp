/* 
* EuropeanasearchApplication.java
* 
* Copyright (c) 2012 Noterik B.V.
* 
* This file is part of Lou, related to the Noterik Springfield project.
* It was created as a example of how to use the multiscreen toolkit
*
* Europeanasearch app is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* Europeanasearch app is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with Europeanasearch app.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.springfield.lou.application.types;
import java.util.Iterator;

import org.springfield.fs.FSList;
import org.springfield.fs.Fs;
import org.springfield.fs.FsNode;
import org.springfield.lou.application.*;
import org.springfield.lou.screen.*;
import org.springfield.mojo.interfaces.ServiceInterface;
import org.springfield.mojo.interfaces.ServiceManager;

public class EuropeanasearchApplication extends Html5Application{
	
 	public EuropeanasearchApplication(String id) {
		super(id); 
	}
 	
 	public void decadefilter(Screen s,String content) {
 		System.out.println("decadeFilter:" +content);
 		String filter = (String)s.getProperty("filter");
 		if(filter==null) {
 			filter = "ep_images";
 		}
 		String url = (String)s.getProperty("videoid");
 		url = url + "/"+filter+"/";
 		
 		String decade = content;
 		
 		ServiceInterface albright = ServiceManager.getService("albright");
		if (albright==null) {
			String result = "Service Albright not found";
			s.setContent("defaultoutput", result);
		} else {
			String videoPath = (String)s.getProperty("videoid");
			FsNode videonode = Fs.getNode(videoPath);
			if(videonode!=null) {
				String genreProprty = videonode.getProperty("genre");
				String terms = videonode.getProperty("ThesaurusTerm");
				
				
				terms = terms.replaceAll(","," "); //Terms can be multiple separated by comma, so replace comma with space
				String all = genreProprty + " " + terms + " " + decade;

				String body = "<input type=\"text\" value=\""+all+"\" />";
				body += "<div>decade Filter: " + decade + "</div>";
				s.setContent("searchkeys", body);
			}
			
			String fsxml = "<fsxml><properties><keywords>"+decade+"</keywords></properties></fsxml>";
			System.out.println("Albright search: " + url);
			System.out.println(fsxml);
			String result = albright.get(url,fsxml,"text/xml");
			if(result==null) { //No video
				s.setContent("defaultoutput", "<h2>No such video</h2>");
			} else { // Build up related result

				System.out.println("Albright result: ");
				System.out.println(result);
				//  result contains the <fsxml> it must be parsed
				FSList nodes = new FSList().parseNodes(result);
	
				//if there's a thumbnail property in the node you can make html and show it in defaultoutput
				
				System.out.println("NDOE SIZE DIVCODE="+nodes.size());
				// Loop through all the nodes
				// Available properties are :
				/*
				 * title
				 * url
				 * creator
				 * thumbnail
				 * provider
				 */
				String body = "<div>";
				for(Iterator<FsNode> iter = nodes.getNodes().iterator() ; iter.hasNext(); ) {
					FsNode n = (FsNode)iter.next(); 
					body += "<div class='item'>";
					String thumbnail = n.getProperty("thumbnail");
					String objurl = n.getProperty("url");
					
					if(thumbnail!=null) { // Check if there is a thumbnail
						body += "<a href='" + objurl + "' target=\"_blank\">";
						body += "<img src='" + thumbnail + "' />";
						body += "</a>";
					}
				   body += "</div>";
				}
				body += "</div>";
				
				s.setContent("defaultoutput", body);
			}
		}
 	}
 	
 	public void filetypefilter(Screen s,String content) {
 		System.out.println("filetypefilter:" +content);
 		String filter = content;
 		s.setProperty("filter", filter);
 		String url = (String)s.getProperty("videoid");
 		url = url + "/"+filter+"/";
 		
 		ServiceInterface albright = ServiceManager.getService("albright");
		if (albright==null) {
			String result = "Service Albright not found";
			s.setContent("defaultoutput", result);
		} else {
			String result = albright.get(url,null,null);
			if(result==null) { //No video
				s.setContent("defaultoutput", "<h2>No such video</h2>");
			} else { // Build up related result

				System.out.println("Albright result: ");
				System.out.println(result);
				//  result contains the <fsxml> it must be parsed
				FSList nodes = new FSList().parseNodes(result);
	
				//if there's a thumbnail property in the node you can make html and show it in defaultoutput
				
				System.out.println("NDOE SIZE DIVCODE="+nodes.size());
				// Loop through all the nodes
				// Available properties are :
				/*
				 * title
				 * url
				 * creator
				 * thumbnail
				 * provider
				 */
				String body = "<div>";
				for(Iterator<FsNode> iter = nodes.getNodes().iterator() ; iter.hasNext(); ) {
					FsNode n = (FsNode)iter.next(); 
					body += "<div class='item'>";
					String thumbnail = n.getProperty("thumbnail");
					String objurl = n.getProperty("url");
					
					if(thumbnail!=null) { // Check if there is a thumbnail
						body += "<a href='" + objurl + "' target=\"_blank\">";
						body += "<img src='" + thumbnail + "' />";
						body += "</a>";
					}
				   body += "</div>";
				}
				body += "</div>";
				
				s.setContent("defaultoutput", body);
			}
		}
 	}
    
    public void searchvideo(Screen s,String content) {
    	System.out.println("searchVideo:" +content);
		contentToProperties(s,content);
		String url = (String)s.getProperty("videouri.value");
		System.out.println("VIDEO ID: " + url);
		s.setProperty("videoid", url);
		url = url + "/ep_images/";
        
        ServiceInterface albright = ServiceManager.getService("albright");
		if (albright==null) {
			String result = "Service Albright not found";
			s.setContent("defaultoutput", result);
		} else {
			String result = albright.get(url,null,null);
			if(result==null) { //No video
				s.setContent("defaultoutput", "<h2>No such video</h2>");
			} else { // Build up related result
				
				String videoPath = (String)s.getProperty("videouri.value");
				FsNode videonode = Fs.getNode(videoPath);
				String videobuild = "";
				loadContent(s, "filetypefilter");
				loadContent(s, "decadefilter");
				
				if(videonode!=null) {
					String title = videonode.getProperty("TitleSet_TitleSetInEnglish_title");
					String orgtitle = videonode.getProperty("TitleSet_TitleSetInOriginalLanguage_title");
					String year = videonode.getProperty("SpatioTemporalInformation_TemporalInformation_productionYear");
					String language = videonode.getProperty("originallanguage");
					String decades = videonode.getProperty("decade");
					videobuild += "<div class=\"descriptionleft\">";
					videobuild += "<p class=\"t2\">"+title+"</p>";
					videobuild += "<p id=\"t1\">ORIGINAL TITLE</p>";
					videobuild += "<p>"+orgtitle+"</p>";
					videobuild += "<p id=\"t1\">PRODUCTION YEAR</p>";
					videobuild += "<p>"+year+"</p>";
					videobuild += "<p id=\"t1\">LANGUAGE</p>";
					videobuild += "<p>"+language+"</p>";
					videobuild += "</div>";	
				}
				
				 
				
				
				
				
				// if it's a video we need it's rawvideo node for where the file is.
				FsNode rawvideonode = Fs.getNode(videoPath+"/rawvideo/1");
				if (rawvideonode!=null) {
					//Build the video preview
					videobuild += "<video id=\"video1\" controls preload=\"none\" data-setup=\"{}\">";
					String mounts[] = rawvideonode.getProperty("mount").split(",");
					
					// based on the type of mount (path) create the rest of the video tag.
					String mount = mounts[0];
					//Fixed the videoPath so that it get the original video from EuscreenXL domain
					videoPath = videoPath.replace("espace", "euscreenxl").replace("luce", "eu_luce");
					if (mount.indexOf("http://")==-1 && mount.indexOf("rtmp://")==-1) {
						String ap = "http://"+mount+".noterik.com/progressive/"+mount+videoPath+"/rawvideo/1/raw.mp4";
						videobuild+="<source src=\""+ap+"\" type=\"video/mp4\" /></video>";
					}

				}
				
				if(videonode!=null) {
					
					String sum = videonode.getProperty("summary");
					String series = videonode.getProperty("TitleSet_TitleSetInEnglish_seriesOrCollectionTitle");
					
					videobuild += "<div class=\"descriptionright\">";
					videobuild += "<p id=\"t1\">SUMMARY</p>";
					videobuild += "<p>"+sum+"</p>";
					videobuild += "<p id=\"t1\">THIS ITEM IS PART OF THE SERIES/COLLECTION</p>";
					videobuild += "<p>"+series+"</p>";
					videobuild += "</div>";	
				}
				
				s.setContent("player", videobuild);
				
				
				if(videonode!=null) { //Build the search terms html
					String genreProprty = videonode.getProperty("genre");
					String terms = videonode.getProperty("ThesaurusTerm");
					
					
					
					String all = genreProprty + " " + terms;
					
					String body = "<div class=\"rul1\">Genre: " + genreProprty + "</div>";
					body += "<div class=\"rul1\">Thesaurus Terms: " + terms + "</div>";
					s.setContent("searchkeys", body);
				}

				
				
				
				System.out.println("Albright result: ");
				System.out.println(result);
				//  result contains the <fsxml> it must be parsed
				FSList nodes = new FSList().parseNodes(result);
	
				//if there's a thumbnail property in the node you can make html and show it in defaultoutput
				
				System.out.println("NDOE SIZE DIVCODE="+nodes.size());
				// Loop through all the nodes
				// Available properties are :
				/*
				 * title
				 * url
				 * creator
				 * thumbnail
				 * provider
				 */
				String body = "<div id=\"slider1\">"
						+ "<a class=\"buttons prev\" href=\"#\">&#60;</a>"
						+ "<div class=\"viewport\">"
						+ "<ul class=\"overview\">";

				for(Iterator<FsNode> iter = nodes.getNodes().iterator() ; iter.hasNext(); ) {
					FsNode n = (FsNode)iter.next(); 
					//body += "<div class='item'>";
					String thumbnail = n.getProperty("thumbnail");
					String objurl = n.getProperty("url");
					
					if(thumbnail!=null) { // Check if there is a thumbnail
						//body += "<a href='" + objurl + "' target=\"_blank\">";
						body += "<li><img src=\""+thumbnail+"\"></li>";

						//body += "<img src='" + thumbnail + "' />";
						//body += "</a>";
					}
				   //body += "</div>";
				}
				body += "</ul>" 
						+"</div>"
						+ "<a class=\"buttons next\" href=\"#\">&#62;</a>" 
						+ "</div>";
				s.setContent("defaultoutput", body);
			}
		}
	}
    
    public void contentToProperties(Screen s,String content) {
		String[] cmd=content.split(",");
		for (int i=0;i<cmd.length;i++) {
			String[] param = cmd[i].split("=");
			s.setProperty(param[0],param[1]);
		}
		
	}

}

