package org.wiztools.cmisbulkcreate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author subwiz
 */
public class CmisProperties extends Properties {
    
    static final String default_atompub_url = "http://localhost:8080/alfresco/cmisatom";
    static final String default_user = "admin";
    static final String default_password = "admin";
    static final String default_path = "/";
    static final int default_folders_to_create = 2;
    static final int default_docs_to_create = 2;
    
    private static final String key_atompub_url = "atompub.url";
    private static final String key_user = "user";
    private static final String key_password = "password";
    private static final String key_path = "path";
    private static final String key_folders_to_create = "folders.to.create";
    private static final String key_docs_to_create = "docs.to.create";

    public CmisProperties(File file) throws IOException {
        super.load(new FileInputStream(file));
    }
    
    public boolean isValid() {
        if(getProperty(key_atompub_url) == null
                || getProperty(key_user) == null
                || getProperty(key_password) == null
                || getProperty(key_path) == null
                || getProperty(key_folders_to_create) == null
                || getProperty(key_docs_to_create) == null) {
            return false;
        }
        return true;
    }
    
    public String getAtompubUrl() {
        return getProperty(key_atompub_url);
    }
    
    public String getUser() {
        return getProperty(key_user);
    }
    
    public String getPassword() {
        return getProperty(key_password);
    }
    
    public String getPath() {
        return getProperty(key_path);
    }
    
    public int getFoldersToCreate() {
        return Integer.parseInt(getProperty(key_folders_to_create));
    }
    
    public int getDocsToCreate() {
        return Integer.parseInt(getProperty(key_docs_to_create));
    }
}
