package org.wiztools.cmisbulkcreate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.wiztools.commons.Charsets;

/**
 *
 * @author subwiz
 */
public class CmisBulkCreateMain {
    
    private static final String atompub_url = "http://localhost:8080/alfresco/cmisatom";
    private static final String user = "admin";
    private static final String password = "admin";
    private static final String path = "/MyFolder";
    
    public static void main(String[] arg) throws IOException {
        Map<String, String> parameter = new HashMap<String, String>();

        // Set the user credentials
        parameter.put(SessionParameter.USER, user);
        parameter.put(SessionParameter.PASSWORD, password);

        // Specify the connection settings
        parameter.put(SessionParameter.ATOMPUB_URL, atompub_url);
        parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());

        // Create a session
        SessionFactory factory = SessionFactoryImpl.newInstance();
        List<Repository> repositories = factory.getRepositories(parameter);
        
        final Repository r = repositories.get(0);
        System.out.println("Repository Id: " + r.getId());
        System.out.println("Repository Name: " + r.getName());
        System.out.println("Repository Description: " + r.getDescription());
        Session session = r.createSession();
        System.out.println("[Opened session to repository]");
        
        Folder rootFolder = (Folder) session.getObjectByPath(path);
        for(int i=0; i<2; i++) {
            // Create folder:
            final String folderName = "Folder-" + i;
            Map<String, String> folderProps = new HashMap<String, String>();
            folderProps.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
            folderProps.put(PropertyIds.NAME, folderName);
            final Folder newFolder = rootFolder.createFolder(folderProps);
            
            System.out.println();
            System.out.println("Created folder: " + folderName);
            
            // Create documents:
            for(int j=0; j<2; j++) {
                final String fileName = "doc-" + i + "." + j + ".txt";
                Map<String, Object> docProps = new HashMap<String, Object>();
                docProps.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
                docProps.put(PropertyIds.NAME, fileName);
                byte[] buf = ("document-" + i + "." + j).getBytes(Charsets.UTF_8);
                ByteArrayInputStream bais = new ByteArrayInputStream(buf);
                ContentStream cs = session.getObjectFactory().createContentStream(
                        fileName, buf.length, "text/plain; charset=UTF-8", bais);
                newFolder.createDocument(docProps, cs, VersioningState.MAJOR);
                
                System.out.println("\tDocument created: " + fileName);
            }
        }
    }
}
