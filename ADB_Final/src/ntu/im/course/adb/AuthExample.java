package ntu.im.course.adb;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.auth.Auth;
import com.aetrion.flickr.auth.AuthInterface;
import com.aetrion.flickr.auth.Permission;
import com.aetrion.flickr.groups.members.Member;
import com.aetrion.flickr.groups.members.MembersInterface;
import com.aetrion.flickr.groups.members.MembersList;
import com.aetrion.flickr.util.IOUtilities;

public class AuthExample {
    Flickr f;
    RequestContext requestContext;
    String frob = "";
    String token = "";
    Properties properties = null;
    AuthInterface authInterface = null;
    MembersInterface member_interface =null;
    
    public AuthExample() throws ParserConfigurationException, IOException, SAXException {
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream("/setup.properties");
            properties = new Properties();
            properties.load(in);
        } finally {
            IOUtilities.close(in);
        }
        f = new Flickr(
            properties.getProperty("apiKey"),
            properties.getProperty("secret"),
            new REST()
        );
        Flickr.debugStream = false;
        requestContext = RequestContext.getRequestContext();
        authInterface = f.getAuthInterface();
		
        try {
            frob = authInterface.getFrob();
        } catch (FlickrException e) {
            e.printStackTrace();
        }
        System.out.println("frob: " + frob);
        URL url = authInterface.buildAuthenticationUrl(Permission.READ, frob);
        System.out.println("Press return after you granted access at this URL:");
        System.out.println(url.toExternalForm());
        BufferedReader infile =
          new BufferedReader ( new InputStreamReader (System.in) );
        String line = infile.readLine();
        try {
            Auth auth = authInterface.getToken(frob);
            System.out.println("Authentication success");
            // This token can be used until the user revokes it.
            System.out.println("Token: " + auth.getToken());
            System.out.println("nsid: " + auth.getUser().getId());
            System.out.println("Realname: " + auth.getUser().getRealName());
            System.out.println("Username: " + auth.getUser().getUsername());
            System.out.println("Permission: " + auth.getPermission().getType());
        } catch (FlickrException e) {
            System.out.println("Authentication failed");
            e.printStackTrace();
        }
        member_interface = f.getMembersInterface();
        searchByGroup();
        
    }
    
    public void searchByGroup(){
		
		String group_id = "74744754@N00";
		List<String> members = new ArrayList<String>();  
		
		//62
		try {
			for(int i=1;i<=1;i++){
				MembersList members_list = member_interface.getList(group_id, null , 100,i);
				for(int j=0; j<members_list.getTotal();j++){
					Member m = (Member) members_list.get(j);
					members.add(m.getId());
				}
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Iterator<String> iterator = members.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}
		
		
		
	}
    
    public static void main(String[] args) {
        try {
            AuthExample t = new AuthExample();
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}