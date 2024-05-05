import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;

public class Main {
    JFrame mainFrame= new JFrame();
    JScrollPane scroll=new JScrollPane();
    JPanel topPanel=new JPanel();
    JLabel countryLabel=new JLabel("Type your country code here:");
    JTextArea CountryCodeBox= new JTextArea();
    JLabel regionLabel=new JLabel("If US or Canada, add state or region code:");
    JTextArea StateCodeBox= new JTextArea();
    JButton EnterButton= new JButton();

    JTextArea resultsArea= new JTextArea();

    String key ="ee93luf4glp";
    String regionCode=new String("");
    StringBuilder toShow=new StringBuilder("");

    public static void main(String[] args) {
        Main ex=new Main();
    }

    public Main() {
        Layout();
        System.out.println(CountryCodeBox.getText());

    }

    public void Layout() {
        mainFrame.setBounds(0, 0, 1500, 600);
        mainFrame.setLayout(new BorderLayout());

        topPanel.setLayout(new GridLayout(1,5));
        topPanel.add(countryLabel);
        topPanel.add(CountryCodeBox);
        topPanel.add(regionLabel);
        topPanel.add(StateCodeBox);
        EnterButton= new JButton ("Enter");
        topPanel.add(EnterButton);
        mainFrame.add(topPanel, BorderLayout.NORTH);

        EnterButton.setActionCommand("OK");
        EnterButton.addActionListener(new ButtonClickListener());

        JPanel resultsPanel= new JPanel(new BorderLayout());
        resultsArea=new JTextArea(toShow.toString());

        JScrollPane scroll = new JScrollPane(resultsArea);
        // scroll.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        // scroll.add(results);
        scroll.setViewportView(resultsArea);
        resultsPanel.add(scroll, BorderLayout.CENTER);
        mainFrame.add(resultsPanel, BorderLayout.CENTER);

        mainFrame.setVisible(true);
    }

    public void ListBirds(){
        try {
            if(CountryCodeBox.getText()!="") {

                String ebirdApiToken = "ee93luf4glp";

                // Construct the URL
                String urlString = "https://api.ebird.org/v2/data/obs/" + regionCode + "/recent";
                URL url = new URL(urlString);

                // Create HttpURLConnection
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                // Set request headers
                conn.setRequestProperty("X-eBirdApiToken", ebirdApiToken);

                // Get the response code
                int responseCode = conn.getResponseCode();
                System.out.println("Response Code: " + responseCode);

                // Read the response
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                // Print the response
              //  System.out.println("Response: " + response.toString());

                JSONParser parser = new JSONParser();
                org.json.simple.JSONArray bigArray = (org.json.simple.JSONArray) parser.parse(response.toString());

                int n = bigArray.size();
                for (int i = 0; i < n; i++) {
                    JSONObject bird = (JSONObject) bigArray.get(i);
                    String birdName = (String) bird.get("comName");
                    System.out.println(birdName);
                    toShow.append(birdName + "\n");
                }

                // Close the connection
                conn.disconnect();
            }

        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("OK")) {

                resultsArea.setText(null);

                if(StateCodeBox.getText().isEmpty()==false) {
                    System.out.println("\n"+"\n"+"Country and State");
                    regionCode = CountryCodeBox.getText() + "-" + StateCodeBox.getText();
                }

                if(StateCodeBox.getText().isEmpty()==true){
                    System.out.println("\n"+"\n"+"No state/ region provided");
                    regionCode=CountryCodeBox.getText();
                }

                System.out.println("\n"+"The code we're using is: "+regionCode+"\n");


                if(CountryCodeBox.getText().isEmpty()==true) {
                    resultsArea.append("\n"+"NO COUNTRY CODE FOUND. PLEASE ENTER TWO LETTER COUNTRY CODE"+ "\n"+ "\n");
                }
                else{
                    resultsArea.append("\n"+"SEARCHING FOR BIRDS IN REGION: "+ regionCode+"\n"+ "\n");
                }

                ListBirds();

                resultsArea.append(toShow.toString());
                toShow.setLength(0);

            }
        }

    }



}

/* old translated Curl that I was using (BAD)
            URL url = new URL("https://api.ebird.org/v2/product/spplist/{{US-NV}}");
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");

            httpConn.setRequestProperty("X-eBirdApiToken", "{{x-ee93luf4glp}}");


            InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                    ? httpConn.getInputStream()
                    : httpConn.getErrorStream();
            Scanner s = new Scanner(responseStream).useDelimiter("\\A");
            String response = s.hasNext() ? s.next() : "";
            System.out.println(response); */
