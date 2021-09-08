import javax.xml.soap.*;
import java.util.ArrayList;

public class SoapCall {
    public static void main(String[] args) {
        String soapEndPoint = "https://sicenet.itsur.edu.mx/ws/wsalumnos.asmx";
        String soapAction = "http://tempuri.org/accesoLogin";
        callWebService(soapEndPoint,soapAction);
    }
    private static void callWebService(String soapEndPoint,String soapAction) {
        try {
            //Create SOAP connection
            SOAPConnectionFactory soapConnectionFactory =
                    SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            //Send SOAP Message to SOAP service
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(soapAction),soapEndPoint);
            //Print the SOAP Response
            System.out.println("Response SOAP Message");
            soapResponse.writeTo(System.out);
            System.out.println();
            soapConnection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private static SOAPMessage createSOAPRequest(String soapAction) {
        try {
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage soapMessage = messageFactory.createMessage();

            createSoapEnvelope(soapMessage);

            MimeHeaders headers = soapMessage.getMimeHeaders();
            headers.addHeader("SOAPAction", soapAction);
            headers.addHeader("Content-Type","text/xml; charset=utf-8");
            soapMessage.saveChanges();

            /* Print the request message, just for debugging purposes */
            System.out.println("Request SOAP Message:");
            soapMessage.writeTo(System.out);
            System.out.println("\n");

            return soapMessage;
        } catch (Exception e) {
            return null;
        }
    }
    private static void createSoapEnvelope(SOAPMessage soapMessage) {
        try {
            SOAPPart soapPart = soapMessage.getSOAPPart();
            String myNamespace = "accesoLogin";
            String myNamespaceUri = "http://tempuri.org/";

            //SOAP Envelope
            SOAPEnvelope envelope = soapPart.getEnvelope();
            envelope.addNamespaceDeclaration(myNamespace,myNamespaceUri);

            SOAPBody soapBody = envelope.getBody();
            SOAPElement soapBodyElem = soapBody.addChildElement("accesoLogin");
            SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("strMatricula","accesoLogin");
            SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("strContrasenia","accesoLogin");
            SOAPElement soapBodyElem3 = soapBodyElem.addChildElement("tipoUsuario","accesoLogin");
            soapBodyElem1.addTextNode("s17120225");
            soapBodyElem2.addTextNode("iB/5%w2Y");
            soapBodyElem3.addTextNode("0");

        } catch (SOAPException e) {
            System.out.println(e.getMessage());
        }
    }
}
