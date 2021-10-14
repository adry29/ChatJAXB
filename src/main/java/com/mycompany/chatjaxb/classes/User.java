/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatjaxb.classes;

/**
 *
 * @author adryc
 */
public class User {
    private String nickname;

    public User() {
    }
    
    
    
    public User(String nickname){
        this.nickname=nickname;
    }
    

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    
    }
