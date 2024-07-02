# MailAttachmentDownloader
***Spring Boot project to download the attachments from a specific  e-mailId and save it in a mentioned location***

#application.properties
Add all the properties such as email,password(application specific),protocol type(imap is preferred),etc.

#pom.xml
Add all the required dependencies for javaMailApi(jakarta mail dependency is preferred)
Note:If we use javax.mail dependency there may be some collision bettween the buit-in jars

#EmailController.java
We are creating a simple rest controller in the controller class to trigger the email reading process and to download the attachments
Note:@RestController annotation tells a controller that the object returned is automatically serialized into JSON and passed back into the HttpResponse object

#MailService.java
This class contains all the logic to read and to download the attachements 

  <>Properties class in MailService is used to load the configurations from the application.properties file
  
  <>For each user account, the server has a store which is the storage of user’s messages. The store is divided into folders, and the “inbox” folder is the primarily folder which contains e-mail messages
  
  <>A default session is created by the Session class by injecting the properties in it
  
  <>Store object is obtained by invoking the getStore(protocol) of session class and connecting to the Store by calling its method connect(String user, String pass), disconnecting by calling its close() method.

  <> A Folder object can be obtained from the store by invoking the getFolder(String folderName) method. For a regular mail box, the folder name must be “inbox” (case-insensitive).The most important methods of the Folder class are:
  
           ->open(int mode): Opens the folder either in READ_ONLY mode or READ_WRITE mode.
           ->getMessages(): Retrieves an array of Message objects .
           ->close(boolean expunge): Closes the folder.
           
  <>We will be reading and dowloading the attachements from a specific email where the emails are "unread" which can be done by "Flags.Flag" class in the "javax.mail package"
  
  <>If the email is from that specified email id which is unread then we will check if the mail contains a multipart file that is an attachhment
  
  <>Here,"Multipart" refers to the contents that may be a text or a file or an HTML content,so in order to specify that we use "BodyPart"

  <>Then we use "MimeBodyPart" which is a sub class of "BodyPart" with some added functionalities is used to download and save the attachment in a specified location




