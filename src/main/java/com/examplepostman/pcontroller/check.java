package com.examplepostman.pcontroller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import org.apache.http.message.BasicNameValuePair;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.examplepostman.encryptor.Encryption;
import com.examplepostman.form.FormXmlDataAsp;
import com.examplepostman.form.RequestXmlForm;
import com.examplepostman.xmlparser.AllHostNameVerifier;
import com.examplepostman.xmlparser.AspXmlGenerator;
import com.examplepostman.xmlparser.XmlSigning;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

import in.gov.uidai.auth.aua.httpclient.TrustAllManager;




@Controller
public class check {
	

	@Autowired
	private PdfEmbedder pdfEmbedder;
	
	@Autowired
	private HttpSession session;
	
	
	@RequestMapping(value = "/hello",method = RequestMethod.GET)
	@ResponseBody
	public String home()
	{
		System.out.println("hii");
		return "hey";
	}
	
	@RequestMapping(value = "/rest/signatureGenerate", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Object>signatureGenerate(
			@RequestParam(value = "pfxPassword", required = false) String pfxPassword,
			@RequestParam(value = "pfxFile", required = false) MultipartFile pfxFile){		
		
			//return new ResponseEntity<Object>( HttpStatus.OK);
			return
					  ResponseEntity.ok("working");
		}
	
	@RequestMapping(value = "/MultiFile", method = RequestMethod.POST) 
	   @ResponseBody public ResponseEntity<String> upload(@RequestParam("file")
	  MultipartFile file) {
		   // // System.out.println("hii"); 
		   return
	  ResponseEntity.ok("working"); }
	
	
	
	
	
	
	
	
	
	
	@RequestMapping(value = "/uploadMultiFile", method = RequestMethod.POST)

	@ResponseBody
	public RequestXmlForm saveEmployee(@FormParam(value = "file") MultipartFile file,
			@RequestParam(value = "authtype", required = false) String authtype, HttpServletRequest request,
			HttpSession session,Model model) {
		System.out.println("**************************************" + session.getId());
		try {
			return this.doProcess(request, file, authtype, session,model);
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private RequestXmlForm doProcess(HttpServletRequest request, MultipartFile file, String authtype,
			HttpSession session,Model model) throws KeyManagementException, NoSuchAlgorithmException {
		System.out.println("**************************************"+session.getId());
		  
		  // PdfEmbedder pdfEmbedder = new PdfEmbedder();
		   
			  // Root Directory.
		      String uploadRootPath = request.getServletContext().getRealPath("upload");
		      System.out.println("uploadRootPath=" + uploadRootPath);
		 
		      File uploadRootDir = new File(uploadRootPath);
		      // Create directory if it not exists.
		      if (!uploadRootDir.exists()) {
		         uploadRootDir.mkdirs();
		      }
		     // MultipartFile[] fileDatas = myUploadForm.getFileDatas();
		      //
		      List<File> uploadedFiles = new ArrayList<File>();
		      List<String> failedFiles = new ArrayList<String>();
		
		 	  String fileHash = "";
		      //for (MultipartFile fileData : fileDatas) {
		 
		         // Client File Name
		         String name = file.getOriginalFilename();
		         System.out.println("Client File Name = " + name);
		 
		         if (name != null && name.length() > 0) {
		            try {
		               // Create the file at server
		               File serverFile = new File(uploadRootDir.getAbsolutePath() + File.separator + name);
		 
		               BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
		               stream.write(file.getBytes());
		               stream.close();
		               //
		               uploadedFiles.add(serverFile);
		               //fileHash = calculateFileHash(uploadRootDir.getAbsolutePath() + File.separator + name);
		               fileHash = pdfEmbedder.pdfSigner(serverFile,request, session);
		               System.out.println(fileHash);
		               request.getSession().setAttribute("pdfEmbedder", pdfEmbedder);
		               System.out.println("Write file: " + serverFile);
		            } catch (Exception e) {
		               System.out.println("Error Write file: " + name);
		               failedFiles.add(name);
		            }
		         }
		      //}
			   
			  		  
			  
		 	            
		      Date now = new Date();
		      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		      dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
			   //try xml generation
			  AspXmlGenerator aspXmlGenerator = new AspXmlGenerator();
			  FormXmlDataAsp formXmalDataAsp = new FormXmlDataAsp();
			  Random randNum = new Random();
			  int randInt = randNum.nextInt();
			  formXmalDataAsp.setVer("2.1");
			  formXmalDataAsp.setSc("Y");
			  formXmalDataAsp.setTs(dateFormat.format(now));
			  //formXmalDataAsp.setTxn((myUploadForm.getAadhar() + randInt).replace("-", ""));
			  formXmalDataAsp.setTxn(("" + randInt).replace("-", ""));
			  formXmalDataAsp.setEkycId("");
			  formXmalDataAsp.setEkycIdType("A");
			  formXmalDataAsp.setAspId("DITG-900");
			  formXmalDataAsp.setAuthMode(authtype);
			  formXmalDataAsp.setResponseSigType("pkcs7");
			  formXmalDataAsp.setResponseUrl("https://172.27.32.64:8444/SpringBootESign/finalResponse");
			  //formXmalDataAsp.setResponseUrl("https://10.208.36.222:8443/SpringBootESign/finalResponse");
			  formXmalDataAsp.setId("1");
			  formXmalDataAsp.setHashAlgorithm("SHA256");
			  formXmalDataAsp.setDocInfo("My Document");
			  formXmalDataAsp.setDocHashHex(fileHash);

			  //Get encrypted string/ signed data for xml signature tag
		      String strToEncrypt = aspXmlGenerator.generateAspXml(formXmalDataAsp,request);
		      String encryptedText = "";
		      String xmlData = "";
			  try {
		      Encryption encryption = new Encryption();
			  PrivateKey rsaPrivateKey =  encryption.getPrivateKey("navneet.pem");
			  File encrFile = new File(uploadRootDir.getAbsolutePath() + File.separator + "Excrypted.xml");
			  String encryptedFile = uploadRootDir.getAbsolutePath() + File.separator + "Excrypted.xml";
			  xmlData = new XmlSigning().signXmlStringNew(uploadRootDir.getAbsolutePath() + File.separator + "Testing.xml", rsaPrivateKey);
			  System.out.println(xmlData);
			  aspXmlGenerator.writeToXmlFile(xmlData,uploadRootDir.getAbsolutePath() + File.separator + "Testing.xml");
			  
			  
		      }
		      catch(Exception e) {
		    	  System.out.println("Error in Encryption.");
		    	  e.printStackTrace();
		    	  return new RequestXmlForm();
		      }
			  
			  RequestXmlForm myRequestXmlForm = new RequestXmlForm();
				/*
				 * myRequestXmlForm.setId(""); myRequestXmlForm.setType(authtype);
				 * myRequestXmlForm.setDescription("Y");
				 */
			  myRequestXmlForm.seteSignRequest(xmlData);
			  myRequestXmlForm.setAspTxnID(("" + randInt).replaceAll("-",""));
			  myRequestXmlForm.setContentType("application/xml");

			  //myUploadForm.setXml(xmlData);
			  try {
			 
			  HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost("https://es-staging.cdac.in/esignlevel1/2.1/form/signdoc");
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
				//nameValuePairs.add(new BasicNameValuePair("action", action));
				nameValuePairs.add(new BasicNameValuePair("eSignRequest", xmlData));
				nameValuePairs.add(new BasicNameValuePair("aspTxnID", String.valueOf(randInt)));
				nameValuePairs.add(new BasicNameValuePair("Content-Type", "application/xml"));
				//nameValuePairs.add(new BasicNameValuePair("phonenumber", otpParameter.getMobileNo()));
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = client.execute(post);
				BufferedReader bf = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String line = "";
				String responseString="";
				while ((line = bf.readLine()) != null) {
					responseString = responseString + line;

				}
			  }catch (Exception e) {
//				// TODO: handle exception
				  e.printStackTrace();
			}
			  
			  
			//  redirectResponse(model,xmlData,"application/xml",myRequestXmlForm.getAspTxnID());
			  

				
				
			  
			  
			  
			  
			  
			  
		      return myRequestXmlForm;
	
		   
	}
	   
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@RequestMapping(value = "/finalResponse", method = RequestMethod.POST )
	   public String ReadEspResponse(@RequestParam("eSignResponse") String response,@RequestParam("espTxnID") String espId,RedirectAttributes rdAttr, HttpServletRequest request) throws IOException {
		  // HttpSession session = request.getSession(false);
	   //PdfEmbedder pdfEmbedder = (PdfEmbedder)request.getSession().getAttribute("pdfEmbedder");
	    System.out.println("**************************************"+session.getId());
	    System.out.println("**************************************"+response);
	   String filename = pdfEmbedder.signPdfwithDS(response, request, session);
	   System.out.println(" Response--->"+response+"ESP ID"+espId);
	   if(filename.equals("Error")) {
		   String error = response.substring(response.indexOf("errCode"),response.indexOf("resCode"));
		   ModelAndView model = new ModelAndView();
		   model.addObject("error", error);
		      System.out.println("**************************************"+session.getId());
		      return "errorFile";
	   }else {
		   return "downloadPdf";
	   }
	   }
	
	

}
