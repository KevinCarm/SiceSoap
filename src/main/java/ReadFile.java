import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class ReadFile {
    private static final  String filePath = "C:\\Users\\kevin\\Documents\\cert.cer";
    static public void main(String[] args) {
        callWebService();
    }

    static private void callWebService() {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = new BufferedInputStream(fis);
            Certificate ca;
            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());

            //Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null,null);
            keyStore.setCertificateEntry("ca",ca);

            //Create a TrustManager that trust the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            //Create an SSLContext that uses out TrustManager
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null,tmf.getTrustManagers(),null);

            //Tell the URLConnection to use a SocketFactory from our SSLContext
            URL url = new URL("https://sicenet.itsur.edu.mx/ws/wsalumnos.asmx");
            URLConnection connection = url.openConnection();
            HttpsURLConnection httpConn = (HttpsURLConnection) connection;
            httpConn.setSSLSocketFactory(context.getSocketFactory());

            //Set http params



             String b = "<?xml version=1.0 encoding=utf-8?>\n"+
            "<soap:Envelope xmlns:xsi=http://www.w3.org/2001/XMLSchema-instance/ xmlns:xsd=http://www.w3.org/2001/XMLSchema/xmlns:soap=http://schemas.xmlsoap.org/soap/envelope/ "+ "\n" +
            "  <soap:Body>\n"+
            "    <accesoLogin xmlns=http://tempuri.org/"+ "\n" +
            "      <strMatricula>string</strMatricula>\n"+
            "      <strContrasenia>string</strContrasenia>\n"+
            "      <tipoUsuario>ALUMNO</tipoUsuario>\n"+
            "    </accesoLogin>\n"+
            "  </soap:Body>\n"+
            "</soap:Envelope>";

             String c = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                     "<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                     "    <Body>\n" +
                     "        <accesoLogin xmlns=\"http://tempuri.org/\"\n" +
                     "            <strMatricula>s17120225</strMatricula>\n" +
                     "            <strContrasenia>iB/5%w2Y</strContrasenia>\n" +
                     "            <tipoUsuario>ALUMNO</tipoUsuario>\n" +
                     "        </accesoLogin>\n" +
                     "    </Body>\n" +
                     "</Envelope> \n";

            //String request = "strMatricula=s17120225&strContrasenia=iB/5%w2Y&tipoUsuario=0";
            //httpConn.setRequestProperty("Content-Length", String.valueOf(request.length()));
            String headers = "POST\n" +
                    "Host: sicenet.itsur.edu.mx\n" +
                    "Content-Type: text/xml; charset=utf-8\n" +
                    "Content-Length: length\n" +
                    "SOAPAction: \"http://tempuri.org/accesoLogin\"\n";
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);

            //Send the post body to the http connection
            OutputStreamWriter out = new OutputStreamWriter(httpConn.getOutputStream());
            out.write(headers + c);
            out.close();

            InputStream item = httpConn.getInputStream();
            String temp;
            StringBuilder tempResponse = new StringBuilder();
            String responseXML;
            //Read the response
            InputStreamReader isr = new InputStreamReader(item);
            BufferedReader br = new BufferedReader(isr);
            System.out.println("Request status: " + httpConn.getResponseMessage());



            //Creating a string using response from web service
            while((temp = br.readLine()) != null) {
                tempResponse.append(temp);
            }
            responseXML = tempResponse.toString();
            System.out.println(responseXML);
            br.close();
            isr.close();
        } catch (IOException | CertificateException | KeyStoreException | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }
}
