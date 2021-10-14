/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import com.mycompany.chatjaxb.classes.Message;
import com.mycompany.chatjaxb.classes.Room;
import com.mycompany.chatjaxb.classes.User;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author adryc
 */
public class Controller {
    private String uri;
    private JAXBContext ctx;
    private Marshaller ms;
    private Unmarshaller ums;
    
    public Controller(String uri){
        this.uri = uri;
    }
    
    //Leemos los datos del archivo xml y los devolvemos en forma de Room
    public Room read() throws JAXBException{
        ctx = JAXBContext.newInstance(Room.class);
        ums = ctx.createUnmarshaller();
        Room room = (Room) ums.unmarshal(new File(uri));
        return room;
    }
    
    //Actualizamos el Room del archivo xml por el Room actualizado
    public void save(Room room) throws JAXBException{
        ctx = JAXBContext.newInstance(Room.class);
        ms = ctx.createMarshaller();
        ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        ms.marshal(room, new File(uri));
    }
    
    //Nos devuelve todos los usuarios alojados en el Room del xml
    public ArrayList<User> getUsers(Room room) throws JAXBException{
        room = read();
        ArrayList<User> result = new ArrayList<>();
        for(User u : room.getUsers()){
            result.add(u);
        }
        
        return result;
    }
    
    //Nos devuelve los mensajes alojados en el Room del xml
    public ArrayList<Message> getMessages(Room room) throws JAXBException{
        room = read();
        ArrayList<Message> result = new ArrayList<>();
        for(Message m : room.getMessages()){
            result.add(m);
        }
        return result;
    }
    
    //Añade un usuario al ArrayList del room, recuperando dicho ArrayList
    //actualizándolo y sobreescribiéndolo en el archivo
    public void addUser(User u) throws JAXBException{
        Room room = read();
        ArrayList<User> users = getUsers(room);
        users.add(u);
        room.setUsers(users);
        save(room);   
    }
    
    //Añade un nuevo mensaje, de la misma manera que la función addUser()
    public void addMessage(Message m) throws JAXBException{
        Room room = read();
        ArrayList<Message> messages = getMessages(room);
        messages.add(m);
        room.setMessages(messages);
        save(room);   
    }
}

