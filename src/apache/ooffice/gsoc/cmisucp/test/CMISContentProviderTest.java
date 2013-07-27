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
import com.sun.star.beans.XPropertySetInfo;
import com.sun.star.uno.XComponentContext;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.comp.helper.BootstrapException;
import com.sun.star.io.XInputStream;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.sdbc.XResultSet;
import com.sun.star.sdbc.XRow;
import com.sun.star.ucb.Command;
import com.sun.star.ucb.ContentInfo;
import com.sun.star.ucb.OpenCommandArgument2;
import com.sun.star.ucb.OpenMode;
import com.sun.star.ucb.XCommandInfo;
import com.sun.star.ucb.XCommandProcessor;
import com.sun.star.ucb.XContent;
import com.sun.star.ucb.XContentIdentifier;
import com.sun.star.ucb.XContentIdentifierFactory;
import com.sun.star.ucb.XContentProvider;
import com.sun.star.ucb.XDynamicResultSet;
import com.sun.star.uno.AnyConverter;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XInterface;
import com.sun.star.util.Date;

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
    public static void main(String[] args) throws Exception, BootstrapException {
        try {
            // get the remote office component context
            XComponentContext xContext = Bootstrap.bootstrap();
            if (xContext == null) {
                System.out.println("ERROR: Could not bootstrap default Office.");                
            }
            XMultiComponentFactory xmcf = xContext.getServiceManager();            
            
            XInterface xUCB;
            
            String keys[] = new String[2];
            keys[0] = "Local";
            keys[1] = "Office";
            
            xUCB = (XInterface) xmcf.createInstanceWithArgumentsAndContext("com.sun.star.ucb.UniversalContentBroker", keys,xContext);
            
            XContentIdentifierFactory xIDFactory = (XContentIdentifierFactory)UnoRuntime.queryInterface(XContentIdentifierFactory.class, xUCB);
            XContentProvider ucp = (XContentProvider)UnoRuntime.queryInterface(XContentProvider.class, xUCB);
            
            XContentIdentifier id1 = xIDFactory.createContentIdentifier("cmis://My_Folder-0-0");
            XContentIdentifier id2 = xIDFactory.createContentIdentifier("cmis://My_Document-0-0");
            
            
            XContent cmisFolder = ucp.queryContent(id1);
            XContent cmisDocument = ucp.queryContent(id2);
            
            XCommandProcessor xcp1 = (XCommandProcessor)UnoRuntime.queryInterface(XCommandProcessor.class, cmisFolder);
            XCommandProcessor xcp2 = (XCommandProcessor)UnoRuntime.queryInterface(XCommandProcessor.class, cmisDocument);
            
            System.out.println(cmisFolder.getContentType());
            System.out.println(cmisDocument.getContentType());
            
            //getCommandInfo
            Command gCInfo = new Command();
            gCInfo.Name = "getCommandInfo";
            gCInfo.Handle = -1;
            XCommandInfo xCommandInfo1 = (XCommandInfo) AnyConverter.toObject(XCommandInfo.class,xcp1.execute(gCInfo,0,null));
            XCommandInfo xCommandInfo2 = (XCommandInfo) AnyConverter.toObject(XCommandInfo.class,xcp2.execute(gCInfo,0,null));
            
            //getPropertySetInfo
            Command gPSInfo = new Command();
            gPSInfo.Name = "getPropertySetInfo";
            gPSInfo.Handle = -1;
            XPropertySetInfo xPropertySetInfo1 = (XPropertySetInfo) AnyConverter.toObject(XPropertySetInfo.class,xcp1.execute(gPSInfo,0,null));
            XPropertySetInfo xPropertySetInfo2 = (XPropertySetInfo) AnyConverter.toObject(XPropertySetInfo.class,xcp2.execute(gPSInfo,0,null));
                        
            // getPropertyValues
            Command gPV1 = new Command();
            gPV1.Name = "getPropertyValues";
            gPV1.Handle = -1;
            
            Command gPV2 = new Command();
            gPV2.Name = "getPropertyValues";
            gPV2.Handle = -1;
            
            Property query1[] = xPropertySetInfo1.getProperties();
            Property query2[] = xPropertySetInfo2.getProperties();
            
            gPV1.Argument = query1;
            gPV2.Argument = query2;
                        
            XRow xRow1 = (XRow) AnyConverter.toObject(XRow.class, xcp1.execute(gPV1, -1, null));
            XRow xRow2 = (XRow) AnyConverter.toObject(XRow.class, xcp2.execute(gPV2, -1, null));            
            
            System.out.println("Properties of 1 and 2:");
            
            String title1 = xRow1.getString(1);
            String title2 = xRow2.getString(1);                     
            if((!xRow1.wasNull()) && (!xRow2.wasNull()))
            {
                System.out.println("\t -1 "+title1);
                System.out.println("\t -2 "+title2);
            }
            System.out.println();

            boolean isFolder1 = xRow1.getBoolean(2);
            boolean isFolder2 = xRow2.getBoolean(2);
            
            if((!xRow1.wasNull()) && (!xRow2.wasNull()))
            {
                System.out.println("\t -1"+isFolder1);
                System.out.println("\t -2"+isFolder2);
            }
            System.out.println();
            
            boolean isDocument1 = xRow1.getBoolean(3);
            boolean isDocument2 = xRow2.getBoolean(3);
            
            if((!xRow1.wasNull()) && (!xRow2.wasNull()))
            {
                System.out.println("\t -1"+isDocument1);
                System.out.println("\t -2"+isDocument2);
            }
            System.out.println();
            
            Date date1 = xRow1.getDate(4);
            Date date2 = xRow2.getDate(4);
            if((!xRow1.wasNull()) && (!xRow2.wasNull()))
            {
                System.out.println("\t -1"+date1.toString());
                System.out.println("\t -2"+date2.toString());
            }
            System.out.println();
            
            Date date11 = xRow1.getDate(5);
            Date date22 = xRow2.getDate(5);
            if((!xRow1.wasNull()) && (!xRow2.wasNull()))
            {
                System.out.println("\t -1"+date11.toString());
                System.out.println("\t -2"+date22.toString());
            }
            System.out.println();
                                    
            long size1 = xRow1.getLong(6);
            long size2 = xRow2.getLong(6);
            if((!xRow1.wasNull()) && (!xRow2.wasNull()))
            {
                System.out.println("\t -1"+size1);
                System.out.println("\t -2"+size2);
            }
            System.out.println();
            
            String mime1 = xRow1.getString(7);
            String mime2 = xRow2.getString(7);                     
            if((!xRow1.wasNull()) && (!xRow2.wasNull()))
            {
                System.out.println("\t -1 "+mime1);
                System.out.println("\t -2 "+mime2);
            }            
            System.out.println("\t -2 "+mime2);
            System.out.println();          
            String content1 = xRow1.getString(8);
            String content2 = xRow2.getString(8);                     
            if((!xRow1.wasNull()) && (!xRow2.wasNull()))
            {
                System.out.println("\t -1 "+content1);
                System.out.println("\t -2 "+content2);
            }
            System.out.println();
            
            OpenCommandArgument2 open1Arg = new OpenCommandArgument2();
            OpenCommandArgument2 open2Arg = new OpenCommandArgument2();
            open1Arg.Mode = OpenMode.ALL;
            open1Arg.Properties = query1;
            Command open1 = new Command();
            open1.Name = "open";
            open1.Argument = open1Arg;
            open1.Handle = -1;
            com.sun.star.ucb.XDynamicResultSet xDynamicResultSet1 = (com.sun.star.ucb.XDynamicResultSet)AnyConverter.toObject(XDynamicResultSet.class, xcp1.execute(open1, -1, null)) ;
            XResultSet xResultSet1 = xDynamicResultSet1.getStaticResultSet();
            while(!xResultSet1.isAfterLast())
            {
                XRow current = UnoRuntime.queryInterface(XRow.class, xResultSet1);
                System.out.println();
                System.out.println(current.getString(1));
                System.out.println(current.getBoolean(2));
                System.out.println(current.getBoolean(3));
                System.out.println(current.getDate(4));
                System.out.println(current.getDate(5));
                System.out.println(current.getLong(6));
                System.out.println(current.getString(7));
                System.out.println(current.getString(8));
                xResultSet1.next();
            }
            
            open2Arg.Mode = OpenMode.DOCUMENT;
            CMISActiveDataSink activeSink = new CMISActiveDataSink(xContext);
            open2Arg.Sink = activeSink;
            Command open2 = new Command();
            open2.Name = "open";
            open2.Handle = -1;
            open2.Argument = open2Arg;
            xcp2.execute(open2, -1, null);
            XInputStream xInputStream = activeSink.getInputStream();
            if(xInputStream==null)
                System.out.println("NUll");            
            else
            {
                System.out.println(xInputStream.available());
                byte b[][] = new byte[1][1];
                
                int nRead;
                nRead = xInputStream.readSomeBytes(b,xInputStream.available());                                
                String s = new String(b[0]);
                System.out.println(s);
                
            }
            
            /* 
            OpenCommandArgument2 oc = new OpenCommandArgument2();
            oc.Mode = OpenMode.DOCUMENT;
            XActiveDataSink xADS = new CMISActiveDataSink(xContext);
            oc.Sink = xADS;
         //   Logger.getLogger(CMISContentProviderTest.class.getName()).fine(oc.Properties[0].Name);
            Command cm = new Command();
            cm.Name = "open";
            cm.Argument = oc;
            
            
            xcp.execute(cm, -1, null);
            
            XInputStream xIS = xADS.getInputStream();
            if(xIS==null)
                System.out.println("NUll");            
            else
            {
                System.out.println(xIS.available());
                byte b[][] = new byte[1][1];
                
                int nRead;
                nRead = xIS.readSomeBytes(b,xIS.available());                                
                    String s = new String(b[0]);
                    System.out.println(s);
                
            }
            
//            Command creatablecommand = new Command();
//            creatablecommand.Name = "CreatableContentsInfo";
//            creatablecommand.Handle = -1;
//            creatablecommand.Argument = null;
            
    //        ContentInfo cifo[] = (ContentInfo[]) AnyConverter.toArray(xcp.execute(creatablecommand, -1, null));
//            ContentInfo createInfo = new ContentInfo();
//            createInfo.Type = "application/cmis-folder";
//            createInfo.Attributes = 0;
            
//            creatablecommand.Name = "createNewContent";
 //           creatablecommand.Argument = createInfo;
  //          creatablecommand.Handle = -1;
            
//            XContent newContent = (XContent) AnyConverter.toObject(XContent.class,xcp.execute(creatablecommand, -1, null));
//            XCommandProcessor newCP = UnoRuntime.queryInterface(XCommandProcessor.class, newContent);
           
//            System.out.println(newContent.getContentType());
            
   //         PropertyValue newPV[] = new PropertyValue[1];
    //        newPV[0] = new PropertyValue("Title", -1, AnyConverter.toObject(Type.STRING, "new-folder"), PropertyState.DIRECT_VALUE);
     //       Command newsettitle = new Command();
       //     newsettitle.Name = "setPropertyValues";
      //      newsettitle.Argument = newPV;
       //     newsettitle.Handle = -1;
//            newCP.execute(newsettitle, -1, null);
            
//            System.out.println(newContent.getIdentifier().getContentIdentifier());
            
            System.out.println();
            //xr = (XRow) AnyConverter.toObject(XRow.class,newCP.execute(cmd,  0, null)); 
            //System.out.println(xr.getString(1));            
/*            Command transfer = new Command();
            transfer.Name="transfer";
            TransferInfo info = new TransferInfo();
            info.SourceURL = "cmis://localhost:8080/inmemory/atom/A1/path?path=/My_Folder-0-1";
            info.NewTitle = "new-folder";
            info.MoveData = false;
            info.NameClash = 0;            
            transfer.Argument = info;
            transfer.Handle = -1;
            xcp.execute(transfer, -1, null);
  */          
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            //            com.sun.star.sdbc.XResultSet xRS;
            
            
            
            
            //xRS.next();
            
            //while(!xRS.isAfterLast())
            //{
             //   XRow xR = UnoRuntime.queryInterface(XRow.class, xRS);
             //   System.out.println(xR.getString(1));
             //   System.out.println(xR.getLong(2));
//                System.out.println(xR.getString(3));
              //  System.out.println(xR.getString(4));
               // xRS.next();
           // }
            
            //PropertyValue pva[] = new PropertyValue[1];
            //PropertyValue pz = new PropertyValue();
            //pz.Handle = -1;
            //pz.Name = "Title";
            //pz.Value = "Changed";
            //pva[0] = pz;
            //Command setC = new Command();
            //setC.Name = "setPropertyValues";
            //setC.Argument = pva;
            
        //    Any anyans[];
            
            //anyans = (Any[]) AnyConverter.toArray(xcp.execute(setC,-1 , null));*/
    }
        catch (java.lang.Exception e){
            e.printStackTrace();
}
        //finally {
        //    System.exit( 0 );
       // }
    // }
    
}}
