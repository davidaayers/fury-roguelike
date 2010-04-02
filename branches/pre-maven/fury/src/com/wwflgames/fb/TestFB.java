package com.wwflgames.fb;

import com.google.code.facebookapi.FacebookJsonRestClient;
import com.google.code.facebookapi.ProfileField;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lobobrowser.gui.*;
import org.lobobrowser.main.PlatformInit;
import org.lobobrowser.ua.NavigationEvent;
import org.lobobrowser.ua.NavigationListener;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: nikb
 * Date: Apr 1, 2010
 * Time: 10:05:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestFB {

    public static final String API_KEY = "b71ea8eaa44145bb598b051551814215";
    public static FacebookJsonRestClient restClient;
    public static String authToken;

    public static void main(String[] args) {


        JFrame jFrame = new JFrame();
        Container contentPane = jFrame.getContentPane();
        jFrame.setSize( 800, 600 );
        jFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        /*
        try {

            JEditorPane jep = new JEditorPane( "http://www.facebook.com/login.php?api_key="
                    + API_KEY + "&connect_display=popup&v=1.0&next=http://www.facebook.com/connect/login_success.html&cancel_url=http://www.facebook.com/connect/login_failure.html&fbconnect=true&return_session=true&session_key_only=true&req_perms=read_stream,publish_stream,offline_access" );

            jep.setEditable(false);
            JScrollPane jsp =
                    new JScrollPane( jep,
                            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
            contentPane.add( jsp );

        } catch (Exception e) {
            System.out.println("Opening URL" + e);
        }
        */

        try {
            PlatformInit.getInstance().initLogging(false);
            PlatformInit.getInstance().init(false, false);
        } catch (Exception e) {
            System.out.println("Init Lobo got exception: " + e);
        }
        final FramePanel panel = new FramePanel();
		//panel.setBorder(BorderFactory.createTitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Embedded browser"));
		//panel.navigate("http://lobobrowser.org/browser/home.jsp");
		//this.getContentPane().add(panel);
        try {
            panel.navigate("http://www.facebook.com/login.php?api_key="
                    + API_KEY + "&connect_display=popup&v=1.0&next=http://www.facebook.com/connect/login_success.html&cancel_url=http://www.facebook.com/connect/login_failure.html&fbconnect=true&return_session=true&session_key_only=true" );
        } catch (Exception e) {
            System.out.println("Opening URL: " + e);
        }

        panel.addNavigationListener(new LocalNavigationListener());
        panel.addResponseListener(new LocalResponseListener(panel));
        //JScrollPane jsp =
        //            new JScrollPane( panel,
        //                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        //                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
        contentPane.add( panel );

        jFrame.validate();
        jFrame.setVisible( true );
    }

    public static JSONObject parseSessionInfo(String jsonString) throws Exception {
        JSONObject jsonObject = new JSONObject(jsonString);
        return jsonObject;
    }

    public static void printFbStuff(JSONObject sessionInfo) throws Exception {
        restClient = new FacebookJsonRestClient(API_KEY, sessionInfo.getString("secret"));

        restClient.setCacheSession(sessionInfo.getString("session_key"), sessionInfo.getLong("uid"), sessionInfo.getLong("expires"));

        JSONArray jsonFriendIds = restClient.friends_get();

        System.out.println("\n\n*** Friends ***\n" + jsonFriendIds);

        ArrayList<ProfileField> fields = new ArrayList<ProfileField>();
        fields.add(ProfileField.FIRST_NAME);
        fields.add(ProfileField.LAST_NAME);

        ArrayList<Long> friendIds = new ArrayList<Long>();
        for (int i = 0; i < jsonFriendIds.length(); i++) {
            friendIds.add(jsonFriendIds.getLong(i));
        }

        JSONArray friendsInfo = restClient.users_getInfo(friendIds, fields);
        for (int i = 0; i < friendsInfo.length(); i++) {
            JSONObject friend = friendsInfo.getJSONObject(i);
            System.out.println("Friend #" + i + " " + friend.getString("first_name") + " " + friend.getString("last_name"));
        }



    }
}
class LocalResponseListener extends ResponseAdapter {

    private FramePanel panel;

    public LocalResponseListener(FramePanel panel) {
        this.panel = panel;
    }

    @Override
    public void responseProcessed(ResponseEvent event) {
        System.out.println("\n\n**********\n");
        System.out.println(event);
        String currentUrl = panel.getCurrentNavigationEntry().getUrl().toString();
        System.out.println("currentUrl=" + currentUrl);
        System.out.println("currentUrl.indexOf(\"login_success.html?session=\")=" + currentUrl.indexOf("login_success.html?session="));
        if (currentUrl.indexOf("login_success.html?session=") > -1) {
            // successful login, grab session key!
            String sessionJSONInfo = currentUrl.substring(currentUrl.indexOf("login_success.html?session=") + "login_success.html?session=".length());

            try {
                sessionJSONInfo = URLDecoder.decode(sessionJSONInfo, "UTF-8");
                System.out.println("sessionJSONInfo=" + sessionJSONInfo);
                JSONObject jsonObject = TestFB.parseSessionInfo(sessionJSONInfo);
                TestFB.printFbStuff(jsonObject);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        System.out.println("\n***********\n\n");
        //panel.setVisible(false);

    }
}

class LocalNavigationListener extends NavigationAdapter {

    @Override
    public void beforeNavigate(NavigationEvent event) {
        //System.out.println("\n***********\n\nEvent:" + event + "\n\n*********\n\n");
    }
}