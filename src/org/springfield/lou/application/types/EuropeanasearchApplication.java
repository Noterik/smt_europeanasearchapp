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
	
    public void onNewScreen(Screen s) {
        loadStyleSheet(s, "generic");
        loadContent(s, "titlepart");
        
        String url = s.getParameter("id");
        
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
				
				String videoPath = s.getParameter("id");
				//Build the video preview
				String videobody ="<video id=\"video1\" controls preload=\"none\" data-setup=\"{}\">";

				// if its a video we need its rawvideo node for where the file is.
				FsNode rawvideonode = Fs.getNode(videoPath+"/rawvideo/1");
				if (rawvideonode!=null) {
					String mounts[] = rawvideonode.getProperty("mount").split(",");

					// based on the type of mount (path) create the rest of the video tag.
					String mount = mounts[0];
					if (mount.indexOf("http://")==-1 && mount.indexOf("rtmp://")==-1) {
						String ap = "http://"+mount+".noterik.com/progressive/"+mount+videoPath+"/rawvideo/1/raw.mp4";
						videobody+="<source src=\""+ap+"\" type=\"video/mp4\" /></video>";
					}
					
					s.setContent("player", videobody);
				}
				System.out.println("Albright result: ");
				System.out.println(result);
				// Eto tuk result ti sadarja <fsxml>-a i prosto triabva da go parsnesh
				FSList nodes = new FSList().parseNodes(result);
	
				//i ako ima thumbnail property vav Node-a, mojesh da sglobish nqkakvo html i da go pokajesh
				// v defaultoutputa
				
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

}

