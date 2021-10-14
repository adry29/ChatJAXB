/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatjaxb.classes;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;


/**
 *
 * @author adryc
 */
@XmlRootElement(name = "Room")
@XmlAccessorType(XmlAccessType.FIELD)
public class Room {
    @XmlElement(name = "usuarios", type = User.class)
    private ArrayList<User> users;
    @XmlElement(name = "mensajes", type = Message.class)
    private ArrayList<Message> messages;

    public Room() {
        users = new ArrayList<>();
        messages = new ArrayList<>();
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    
    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
    
    public ArrayList<User> deleteUser(String nickname){
        int i = 0;
        while(i < users.size()){
            if(users.get(i).getNickname().equals(nickname)){
                users.remove(i);
                i++;
            }
        }
        return users;
    }
    
    
}
