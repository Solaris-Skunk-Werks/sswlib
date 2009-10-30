/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package filehandlers;

import IO.JarResources;
import Print.PrintConsts;
import java.awt.Image;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class ImageTracker {
    private Media media = new Media();
    private Vector<StoredImage> imageStore = new Vector<StoredImage>();
    //private Vector<JarResources> resources = new Vector<JarResources>();
    private Hashtable<String, JarResources> resources = new Hashtable<String, JarResources>();

    public ImageTracker() {
        addResource( PrintConsts.PATTERNS, false);
    }

    public void addResource( JarResources resource ) {
        if ( !resources.contains(resource) ) {
            resources.put(resource.getJarFileName(), resource);
        }
    }

    public void addResource( String zipName ) {
        if ( !resources.containsKey(zipName) ) {
            resources.put(zipName, new JarResources(zipName));
        }
    }

    public void addResource( String zipName, boolean preLoad ) {
        JarResources resource = new JarResources(zipName);
        addResource(resource);
        
        if ( preLoad ) {
            Enumeration keys = resource.getContents().keys();
            while ( keys.hasMoreElements() ) {
                String curKey = (String) keys.nextElement();
                Image temp = media.LoadImage( resource.getResource(curKey) );
                imageStore.add(new StoredImage(curKey, temp));
            }
        }
    }

    public void preLoadMechImages() {
        try {
            getImage( PrintConsts.RS_TW_BP );
            getImage( PrintConsts.RS_TW_QD );
            getImage( PrintConsts.BP_ChartImage );
            getImage( PrintConsts.QD_ChartImage );
        } catch ( Exception e ) {
            System.out.println(e.getMessage());
        }
    }
    
    public void preLoadBattleForceImages() {
        getImage( PrintConsts.BF_BG );
        getImage( PrintConsts.BF_Card );
        getImage( PrintConsts.BF_Chart );
    }

    public Image getImage( String filename ) {
        for ( StoredImage PreLoadImage : imageStore ) {
            if ( PreLoadImage.filename.equals(filename) ) {
                return PreLoadImage.image;
            }
        }

        Image tempimg = null;
        for ( JarResources resource : resources.values() ) {
            byte[] image = resource.getResource(filename);
            if ( image != null ) { tempimg = media.LoadImage(resource.getResource(filename)); }
            if ( tempimg != null ) {
                //System.out.println(filename + " retrieved from zip file!");
                imageStore.add(new StoredImage(filename, tempimg));
                break;
            }
        }
        
        if ( tempimg == null ) {
            tempimg = media.GetImage(filename);
            if ( tempimg != null ) {
                //System.out.println(filename + " retrieved from file system.");
                imageStore.add(new StoredImage(filename, tempimg));
            }
        }

        return tempimg;
    }


    private class StoredImage {
        String filename = "";
        Image image;

        public StoredImage( String filename, Image image ) {
            this.filename = filename;
            this.image = image;
        }
    }
}
