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
import com.sun.star.sdbc.XResultSet;
import com.sun.star.sdbc.XRow;
import com.sun.star.ucb.Command;
import com.sun.star.ucb.OpenCommandArgument2;
import com.sun.star.ucb.OpenMode;
import com.sun.star.ucb.XCommandProcessor;
import com.sun.star.ucb.XContent;
import com.sun.star.ucb.XContentAccess;
import com.sun.star.ucb.XContentIdentifier;
import com.sun.star.ucb.XContentIdentifierFactory;
import com.sun.star.ucb.XContentProvider;
import com.sun.star.ucb.XDynamicResultSet;
import com.sun.star.uno.AnyConverter;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XInterface;
import com.sun.star.util.Date;
import java.util.logging.Logger;

/**
 
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
            XContentIdentifier id = xIDFactory.createContentIdentifier("cmis://My_Folder-0-0");
            XContent cmisContent = ucp.queryContent(id);
            XCommandProcessor xcp = (XCommandProcessor)UnoRuntime.queryInterface(XCommandProcessor.class, cmisContent);
            System.out.println(cmisContent.getContentType());
            Command cmd = new Command();
            cmd.Name = "getPropertyValues";
            cmd.Handle = 0;
            Property p = new Property();
            p.Name = "Title";
            Property p1[] = new Property[3];
            p1[0] = p;
            Property p2 = new Property();
            p2.Name = "DateCreated";
            p1[1] = p2;
            Property p3 = new Property();
            p3.Name = "Size";
            p1[2] = p3;
            cmd.Argument = p1;
            XRow xr;
            xr = (XRow) AnyConverter.toObject(XRow.class,xcp.execute(cmd,  0, null));
            System.out.println(xr.getString(1));
            Date d = xr.getDate(2);
            System.out.println(d.Day+"/"+d.Month+"/"+d.Year);
            
            OpenCommandArgument2 oc = new OpenCommandArgument2();
            oc.Mode = OpenMode.ALL;
            Property pxx[];
            pxx = new Property[4];
            
            Property pa = new Property();
            pa.Name = "Title";
            pxx[0] = pa;
            Property pb = new Property();
            pb.Name = "Size";
            pxx[1] = pb;
            Property pc = new Property();
            pc.Name = "MediaType";
            pxx[2] = pc;
            Property pd = new Property();
            pd.Name = "ContentType";
            pxx[3] = pd;
            oc.Properties = pxx;
            
            Logger.getLogger(CMISContentProviderTest.class.getName()).fine(oc.Properties[0].Name);
            Command cm = new Command();
            cm.Name = "open";
            cm.Argument = oc;
            
            XDynamicResultSet xDRS;
            
            xDRS = (XDynamicResultSet) AnyConverter.toObject(XDynamicResultSet.class,xcp.execute(cm,0,null));
            
            com.sun.star.sdbc.XResultSet xRS;
            
            xRS = (XResultSet)xDRS.getStaticResultSet();
            XContentAccess xCA = UnoRuntime.queryInterface(XContentAccess.class, xRS);
            
            xRS.next();
            
            while(!xRS.isAfterLast())
            {
                XRow xR = UnoRuntime.queryInterface(XRow.class, xRS);
                System.out.println(xR.getString(1));
                System.out.println(xR.getLong(2));
                System.out.println(xR.getString(3));
                System.out.println(xR.getString(4));
                xRS.next();
            }
            
        }
        catch (java.lang.Exception e){
            e.printStackTrace();
        }
        finally {
            System.exit( 0 );
        }
    }
    
}
