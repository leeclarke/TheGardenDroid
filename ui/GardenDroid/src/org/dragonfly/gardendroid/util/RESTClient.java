package org.dragonfly.gardendroid.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//import sun.misc.BASE64Encoder;

/**
 * This is overkill for this app but does the basics as well as other stuff. Doesnt currently support encryption.
 */
public class RESTClient
{
    public static final String GET = "GET";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";
    public static final String POST = "POST";
    
    private RESTClient()
    {
        super();
    }
    
    private static void usage()
    {
        System.out.println("Usage:");
        System.out.println("  For get and delete:");
        System.out.println("    java -jar restclient.jar [-quiet] user pass GET/DELETE url");
        System.out.println("");
        System.out.println("  For put and post, sending the contents of a file as the body:");
        System.out.println("    java -jar restclient.jar [-quiet] user pass PUT/POST url filename");
        System.out.println("");
        System.out.println("  -quiet will cause only the response body to be printed, otherwise");
        System.out.println("  the response headers and timing information is included.");
        System.exit(1);
    }
    
    public static void main(String arguments[])
    {
        List argsList = new ArrayList(arguments.length);
        for (int i=0; i<arguments.length; i++) argsList.add(arguments[i]);
        
        Iterator args = argsList.iterator();
        
        boolean quiet = false;
        if (!args.hasNext()) usage();
        String quietFlag = (String)argsList.iterator().next();
        if ("-quiet".equalsIgnoreCase(quietFlag))
        {
            args.next();
            quiet = true;
        }
        
        if (!args.hasNext()) usage();
        String user = (String)args.next();

        if (!args.hasNext()) usage();
        String pass = (String)args.next();
        
        try
        {
            if (!args.hasNext()) usage();
            String method = (String)args.next();
            
            if (!args.hasNext()) usage();
            URL url = new URL((String)args.next());
            
            if (GET.equalsIgnoreCase(method))
            {
                if (args.hasNext()) usage();
                request(quiet, GET, url, user, pass, null);
            }
            else if (PUT.equalsIgnoreCase(method))
            {
                if (!args.hasNext()) usage();
                String file = (String)args.next();
                request(quiet, PUT, url, user, pass, new FileInputStream(new File(file)));
            }
            else if (POST.equalsIgnoreCase(method))
            {
                if (!args.hasNext()) usage();
                String file = (String)args.next();
                request(quiet, POST, url, user, pass, new FileInputStream(new File(file)));
            }
            else if (DELETE.equalsIgnoreCase(method))
            {
                if (args.hasNext()) usage();
                request(quiet, DELETE, url, user, pass, null);
            }
            else
            {
                usage();
            }
        }
        catch (Exception x)
        {
            System.err.println(x);
            System.exit(1);
        }
    }
    
    public static void request(boolean quiet, String method, URL url, String username, String password, InputStream body)
    throws IOException
    {
        // sigh.  openConnection() doesn't actually open the connection,
        // just gives you a URLConnection.  connect() will open the connection.
        if (!quiet)
        {
            System.out.println("[issuing request: " + method + " " + url + "]");
        }
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod(method);
        
//        // write auth header
//        BASE64Encoder encoder = new BASE64Encoder();
//        String encodedCredential = encoder.encode( (username + ":" + password).getBytes() );
//        connection.setRequestProperty("Authorization", "BASIC " + encodedCredential);
        
        // write body if we're doing POST or PUT
        byte buffer[] = new byte[8192];
        int read = 0;
        if (body != null)
        {
            connection.setDoOutput(true);
            
            OutputStream output = connection.getOutputStream();
            while ((read = body.read(buffer)) != -1)
            {
                output.write(buffer, 0, read);
            }
        }
        
        // do request
        long time = System.currentTimeMillis();
        connection.connect();
        
        InputStream responseBodyStream = connection.getInputStream();
        StringBuffer responseBody = new StringBuffer();
        while ((read = responseBodyStream.read(buffer)) != -1)
        {
            responseBody.append(new String(buffer, 0, read));
        }
        connection.disconnect();
        time = System.currentTimeMillis() - time;
        
        // start printing output
        if (!quiet)
            System.out.println("[read " + responseBody.length() + " chars in " + time + "ms]");
        
        // look at headers
        // the 0th header has a null key, and the value is the response line ("HTTP/1.1 200 OK" or whatever)
        if (!quiet)
        {
            String header = null;
            String headerValue = null;
            int index = 0;
            while ((headerValue = connection.getHeaderField(index)) != null)
            {
                header = connection.getHeaderFieldKey(index);
                
                if (header == null)
                    System.out.println(headerValue);
                else
                    System.out.println(header + ": " + headerValue);
                
                index++;
            }
            System.out.println("");
        }
        
        // dump body
        System.out.print(responseBody);
        System.out.flush();
    }
}