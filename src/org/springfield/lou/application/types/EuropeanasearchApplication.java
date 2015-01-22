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
        s.setContent("defaultoutput", url);
        
        url = url + "/ep_images/";
        
        ServiceInterface albright = ServiceManager.getService("albright");
		if (albright==null) {
			String result = "Service Albright not found";
			s.setContent("defaultoutput", result);
			;
		} else {
			String result = albright.get(url,null,null);
			s.setContent("defaultoutput", result);
		}
		
		String viduri = "/domain/msp/user/luce/video/7";
		FsNode video = Fs.getNode(viduri);
		String src = null;
		
    }

}
