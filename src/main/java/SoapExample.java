
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SoapExample {
    public static void main(String[] args) {

        String xml = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <accesoLogin xmlns=\"http://tempuri.org/\">\n" +
                "      <strMatricula>s17120225</strMatricula>\n" +
                "      <strContrasenia>iB/5%w2Y</strContrasenia>\n" +
                "      <tipoUsuario>0</tipoUsuario>\n" +
                "    </accesoLogin>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        String responseF = callSoapService(xml);
        System.out.println(responseF);
    }

    private static String callSoapService(String soapRequest) {
        try {
            String url = "https://sicenet.itsur.edu.mx/ws/wsalumnos.asmx";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(soapRequest);
            wr.flush();
            wr.close();
            String responseStatus = con.getResponseMessage();
            System.out.println(responseStatus);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
