package de.timpietrusky.leetedit;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * LeetEdit is an HTML editor for Eclipse RCP based on TinyMCE. 
 * 
 * @author Tim Pietrusky - www.tim-pietrusky.de
 * 
 * <p>
 * Example:
 * </p>
 * <pre>
 *   LeetEdit leetEdit = new LeetEdit(parent, SWT.NONE);
 *   
 *   // Set text
 *   leetEdit.setText("LeetEdit is kind of leet.");
 *   
 *   // Get text
 *   String text = leetEdit.getText();
 * </pre>
 * 
 */
public class LeetEdit extends Composite {
    
    protected Browser browser;
    protected String editor_content;

    public LeetEdit(Composite parent, int style) {
        super(parent, style);
        
        setLayout(new GridLayout(1, true));
        
        browser = new Browser(this, SWT.NONE);
        browser.setJavascriptEnabled(true);
        browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        // Set url pointed to editor
        try {
            URL url_bundle = FileLocator.find(
                    Platform.getBundle("de.timpietrusky.leetedit"),
                        new Path("www/tinymce/index.html"), null); 
            URL url_file = FileLocator.toFileURL(url_bundle);
            
            browser.setUrl(url_file.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        // Set content of editor
        browser.addProgressListener(new ProgressListener() {
            public void changed(ProgressEvent event) {
            }

            public void completed(ProgressEvent event) {
                /**
                 * @TODO [TimPietrusky] - 20120415: Escape some characters before set innerHTML (is this necessary?)
                 */
                browser.execute("document.getElementById('content').innerHTML = '" + editor_content + "';");
            }
        });
        
        // Listen to status change event
        browser.addStatusTextListener(new StatusTextListener() {
            public void changed(StatusTextEvent event) {
                browser.setData("leet-content", event.text);
            }
        });
    }
    
    /**
     * Set the content of the HTML editor.
     * 
     * @param String text
     */
    public void setText(String text) {
        editor_content = text;
    }
    
    /**
     * Returns the content of the HTML editor. 
     * 
     * @return String
     */
    public String getText() {
        String content = "";
        
        boolean executed = browser.execute("window.status=getContent();");
        
        if (executed) {
            content = (String) browser.getData("leet-content");
        }
        
        return content;
    }
}