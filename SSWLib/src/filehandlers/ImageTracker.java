/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package filehandlers;

import Print.PrintConsts;
import java.awt.Image;
import java.util.Vector;

public class ImageTracker {
    private Media media = new Media();
    private Vector<StoredImage> imageStore = new Vector<StoredImage>();

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

        Image tempimg = media.GetImage(filename);
        if ( tempimg != null ) {
            imageStore.add(new StoredImage(filename, tempimg));
        } else {
            System.out.println("Could not load " + filename);
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
