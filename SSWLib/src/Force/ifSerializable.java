/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Force;

import java.io.BufferedWriter;
import java.io.IOException;

public interface ifSerializable {
    public void SerializeXML(BufferedWriter file) throws IOException;
    public String SerializeClipboard();
    public String SerializeData();
}
