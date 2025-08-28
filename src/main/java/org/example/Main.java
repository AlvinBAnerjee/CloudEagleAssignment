package org.example;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        String CLIENT_ID = "m96dzvffpbvm5rv";
        String CLIENT_SECRET = "ut22gwgcov9bb92";
        String REDIRECT_URI = "http://localhost/redirectUrlPlaceHolder";

        // Step 1: Direct user to authorization URL
        String authURL = "https://www.dropbox.com/oauth2/authorize?client_id=m96dzvffpbvm5rv&redirect_uri=http://localhost/redirectUrlPlaceHolder&response_type=code&token_access_type=offline&scope=team_info.read+members.read+events.read";
        System.out.println("Go to the following URL and authorize the app:");
        System.out.println(authURL);

        // Step 2: Read the authorization code from user input
        System.out.print("Enter the authorization code here: ");
        Scanner scanner = new Scanner(System.in);
        String code = scanner.nextLine().trim();
        scanner.close();
        System.out.println("The Auth code is " + code);

        // Step 3: Exchange authorization code for access token
        URL tokenUrl = new URL("https://api.dropboxapi.com/oauth2/token");
        HttpURLConnection tokenConn = (HttpURLConnection) tokenUrl.openConnection();
        tokenConn.setRequestMethod("POST");
        tokenConn.setDoOutput(true);

        String params = "code=" + URLEncoder.encode(code, "UTF-8")
                + "&grant_type=authorization_code"
                + "&client_id=" + URLEncoder.encode(CLIENT_ID, "UTF-8")
                + "&client_secret=" + URLEncoder.encode(CLIENT_SECRET, "UTF-8")
                + "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, "UTF-8");
        tokenConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        OutputStream tokenOs = tokenConn.getOutputStream();
        tokenOs.write(params.getBytes("UTF-8"));
        tokenOs.close();

        InputStream stream;
        if (tokenConn.getResponseCode() >= 400) {
            stream = tokenConn.getErrorStream(); // read error response
        } else {
            stream = tokenConn.getInputStream();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        System.out.println("Response: " + response.toString());
         //Extract the access_token (simple parsing)
        String responseString = response.toString();
        String accessToken = responseString.split("\"access_token\":")[1].split("\"")[1];
        System.out.println("Access Token: " + accessToken);
    }
}
