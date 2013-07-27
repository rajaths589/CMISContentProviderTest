/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package apache.ooffice.gsoc.cmisucp.test;

import com.sun.star.io.XInputStream;
import com.sun.star.lib.uno.helper.WeakBase;
import com.sun.star.uno.XComponentContext;

/**
 *
 * @author rajath
 */


import com.sun.star.io.XInputStream;
import com.sun.star.uno.XComponentContext;
import com.sun.star.lib.uno.helper.WeakBase;


public final class CMISActiveDataSink extends WeakBase
   implements com.sun.star.io.XActiveDataSink
{
    private final XComponentContext m_xContext;
    private static final String m_implementationName = CMISActiveDataSink.class.getName();
    
    private XInputStream xInputStream;

    public CMISActiveDataSink( XComponentContext context)
    {
        m_xContext = context;                
        //xInputStream = new CMISInputStream(context, doc);
    };

    // com.sun.star.io.XActiveDataSink:
    public void setInputStream(com.sun.star.io.XInputStream aStream)
    {
        xInputStream = aStream;
    }

    public com.sun.star.io.XInputStream getInputStream()
    {
        
        return xInputStream;
    }

}


