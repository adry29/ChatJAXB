/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatjaxb.classes;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.security.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author adryc
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Message {
    
    private String author;
    private String time;
    private String text;

    public Message() {
    }
    
    
    

    public Message(User author, String text) {
        this.author = author.getNickname();
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        time = dtf.format(LocalDateTime.now());
        
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public String getTime() {
        return time;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTime(String time) {
        this.time = time;
    }
    
    public String toString(){
        return (this.time +" " + this.author+" escribió: "+this.text);
    }
    
    
}
