/**************************************************************
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 *************************************************************/
/*
 * CMISContentProviderTest.java
 *
 * Created on 2013.06.04 - 01:56:06
 *
 */

package apache.ooffice.gsoc.cmisucp.test;

import com.sun.star.beans.Property;
import com.sun.star.uno.XComponentContext;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.sdbc.XRow;
import com.sun.star.ucb.Command;
import com.sun.star.ucb.XCommandProcessor;
import com.sun.star.ucb.XContent;
import com.sun.star.ucb.XContentIdentifier;
import com.sun.star.ucb.XContentIdentifierFactory;
import com.sun.star.ucb.XContentProvider;
import com.sun.star.uno.AnyConverter;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XInterface;

/**
 *
 * @author rajath
 */
public class CMISContentProviderTest {
    
    /** Creates a new instance of CMISContentProviderTest */
    public CMISContentProviderTest() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // get the remote office component context
            XComponentContext xContext = Bootstrap.bootstrap();
            if (xContext == null) {
                System.err.println("ERROR: Could not bootstrap default Office.");
            }
            XMultiComponentFactory xmcf = xContext.getServiceManager();
            //XMultiServiceFactory xServiceFactory = (XMultiServiceFactory) xContext.getServiceManager();
            XInterface xUCB;
            
            String keys[] = new String[2];
            keys[0] = "Local";
            keys[1] = "Office";
            
            xUCB = (XInterface) xmcf.createInstanceWithArgumentsAndContext("com.sun.star.ucb.UniversalContentBroker", keys,xContext);
            XContentIdentifierFactory xIDFactory = (XContentIdentifierFactory)UnoRuntime.queryInterface(XContentIdentifierFactory.class, xUCB);
            XContentProvider ucp = (XContentProvider)UnoRuntime.queryInterface(XContentProvider.class, xUCB);
            XContentIdentifier id = xIDFactory.createContentIdentifier("cmis:///CMISUpload.odt");
            XContent cmisContent = ucp.queryContent(id);
            XCommandProcessor xcp = (XCommandProcessor)UnoRuntime.queryInterface(XCommandProcessor.class, cmisContent);
            System.out.println(cmisContent.getContentType());
            Command cmd = new Command();
            cmd.Name = "getPropertyValues";
            cmd.Handle = 0;
            Property p = new Property();
            p.Name = "Title";
            Property p1[] = new Property[1];
            p1[0] = p;
            cmd.Argument = p1;
            XRow xr;
            xr = (XRow) AnyConverter.toObject(XRow.class,xcp.execute(cmd,  0, null));
            System.out.println(xr.getString(1));
            
        }
        catch (java.lang.Exception e){
            e.printStackTrace();
        }
        finally {
            System.exit( 0 );
        }
    }
    
}
