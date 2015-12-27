/*
 * Copyright (c) 2011, 2012 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package business;
 


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import dataaccess.Auth;
 
public class loginController {
    @FXML private Text actiontarget;
    @FXML private TextField UserNameField;
    @FXML private TextField passwordField;
    
    @FXML protected void handleSubmitButtonAction(ActionEvent event)  {
        SystemController sc=new SystemController();
    try{
        Auth author= sc.login(UserNameField.getText(), passwordField.getText());
    	//actiontarget.setText("Sign in button pressed");
       if(author==Auth.ADMIN){
    	   Parent adminPanel = FXMLLoader.load(getClass().getResource("..\\View\\Admin.fxml"));
    	   SystemController.ParentScreen=adminPanel;
    	   
    	   SystemController.stageArea.setScene(new Scene(adminPanel,700,450));
    	   SystemController.stageArea.show();
       }
       else if(author==Auth.LIBRARIAN){
    	   Parent librarian = FXMLLoader.load(getClass().getResource("..\\View\\Librarian.fxml"));
    	   SystemController.ParentScreen=librarian;
    	   SystemController.stageArea.setScene(new Scene(librarian,700,450));
    	   SystemController.stageArea.show();
       }
       else if(author==Auth.BOTH){
    	   Parent both = FXMLLoader.load(getClass().getResource("..\\View\\AdminLibrarian.fxml"));
    	   SystemController.ParentScreen=both;
    	   
	       SystemController.stageArea.setScene(new Scene(both,700,450));
    	   SystemController.stageArea.show();
       }
      }
        catch(Exception e){
        	   actiontarget.setText(e.getMessage());
        	   e.printStackTrace();
        }
    }

}
